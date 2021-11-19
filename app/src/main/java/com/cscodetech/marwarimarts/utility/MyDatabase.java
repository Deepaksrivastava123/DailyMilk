package com.cscodetech.marwarimarts.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cscodetech.marwarimarts.model.ProductdataItem;


import java.util.ArrayList;
import java.util.List;


public class MyDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cscodetect.db";
    public static final String TABLE_NAME = "items";
    public static final String TABLE_NAMES = "subscribitems";
    public static final String ICOL_1 = "ID";
    public static final String ICOL_2 = "product_id";
    public static final String ICOL_3 = "cityid";
    public static final String ICOL_4 = "catid";
    public static final String ICOL_5 = "subcatid";
    public static final String ICOL_6 = "product_discount";
    public static final String ICOL_7 = "product_variation";
    public static final String ICOL_8 = "product_regularprice";
    public static final String ICOL_9 = "product_subscribeprice";
    public static final String ICOL_10 = "product_title";
    public static final String ICOL_11 = "product_img";
    public static final String ICOL_12 = "service_qty";
    public static final String ICOL_13 = "day";
    public static final String ICOL_14 = "tdelivery";
    public static final String ICOL_15 = "sdate";
    public static final String ICOL_16 = "type";
    public static final String ICOL_17 = "tdeliverydigit";
    public static final String ICOL_18 = "stime";

    SessionManager sessionManager;
    Context mContext;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        sessionManager = new SessionManager(context);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, product_id TEXT , cityid TEXT ,catid TEXT , subcatid TEXT , product_discount Double, product_variation TEXT , product_regularprice Double, product_subscribeprice Double , product_title TEXT , product_img TEXT , service_qty int )");
        db.execSQL("create table " + TABLE_NAMES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, product_id TEXT , cityid TEXT ,catid TEXT , subcatid TEXT , product_discount Double, product_variation TEXT , product_regularprice Double, product_subscribeprice Double , product_title TEXT , product_img TEXT , service_qty int , day TEXT , tdelivery TEXT , sdate TEXT , type TEXT, tdeliverydigit TEXT , stime TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES);
        onCreate(db);
    }

    public boolean insertData(ProductdataItem rModel) {
        SQLiteDatabase database = this.getWritableDatabase();

        if (getID(rModel.getProductId()) == -1) {
            Cursor res = database.rawQuery("select * from " + TABLE_NAMES, null);
            if (res.getCount() == 0) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(ICOL_2, rModel.getProductId());
                contentValues.put(ICOL_3, rModel.getCityid());
                contentValues.put(ICOL_4, rModel.getCatid());
                contentValues.put(ICOL_5, rModel.getSubcatid());
                contentValues.put(ICOL_6, rModel.getProductDiscount());
                contentValues.put(ICOL_7, rModel.getProductVariation());
                contentValues.put(ICOL_8, rModel.getProductRegularprice());
                contentValues.put(ICOL_9, rModel.getProductSubscribeprice());
                contentValues.put(ICOL_10, rModel.getProductTitle());
                contentValues.put(ICOL_11, rModel.getProductImg());
                contentValues.put(ICOL_12, rModel.getProductQty());
                long result = db.insert(TABLE_NAME, null, contentValues);
                if (result == -1) {
                    return false;
                } else {
                    return true;
                }
            } else {
                new ProductDetail().bottonCardClear(mContext, "sub");
                return false;
            }


        } else {
            return updateData(rModel.getProductId(), rModel.getProductQty());
        }

    }

    public boolean insertSubcription(ProductdataItem rModel) {
        SQLiteDatabase database = this.getWritableDatabase();

        if (getIDS(rModel.getProductId()) == -1) {
            Cursor res = database.rawQuery("select * from " + TABLE_NAME, null);
            if (res.getCount() == 0) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(ICOL_2, rModel.getProductId());
                contentValues.put(ICOL_3, rModel.getCityid());
                contentValues.put(ICOL_4, rModel.getCatid());
                contentValues.put(ICOL_5, rModel.getSubcatid());
                contentValues.put(ICOL_6, rModel.getProductDiscount());
                contentValues.put(ICOL_7, rModel.getProductVariation());
                contentValues.put(ICOL_8, rModel.getProductRegularprice());
                contentValues.put(ICOL_9, rModel.getProductSubscribeprice());
                contentValues.put(ICOL_10, rModel.getProductTitle());
                contentValues.put(ICOL_11, rModel.getProductImg());
                contentValues.put(ICOL_12, rModel.getProductQty());
                contentValues.put(ICOL_13, rModel.getDay());
                contentValues.put(ICOL_14, rModel.getTdelivery());
                contentValues.put(ICOL_15, rModel.getSdate());
                contentValues.put(ICOL_16, rModel.getType());
                contentValues.put(ICOL_17, rModel.getTdeliverydigit());
                contentValues.put(ICOL_18, rModel.getStime());
                long result = db.insert(TABLE_NAMES, null, contentValues);
                if (result == -1) {
                    return false;
                } else {
                    return true;
                }
            } else {
                new ProductDetail().bottonCardClear(mContext, "nor");
                return false;
            }

        } else {
            return updateDataS(rModel);
        }


    }

    public List<ProductdataItem> getCData() {
        List<ProductdataItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor c = db.rawQuery("select * from items ", null);
            if (c.getCount() != -1) { //if the row exist then return the id
                while (c.moveToNext()) {
                    ProductdataItem item = new ProductdataItem();
                    item.setProductId(c.getString(1));
                    item.setCityid(c.getString(2));
                    item.setCatid(c.getString(3));
                    item.setSubcatid(c.getString(4));
                    item.setProductDiscount("" + c.getDouble(5));
                    item.setProductVariation(c.getString(6));
                    item.setProductRegularprice("" + c.getDouble(7));
                    item.setProductSubscribeprice("" + c.getDouble(8));
                    item.setProductTitle(c.getString(9));
                    item.setProductImg(c.getString(10));
                    item.setProductQty(c.getString(11));
                    list.add(item);
                }

            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());

        }
        return list;
    }

    public List<ProductdataItem> getCDataS() {
        List<ProductdataItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor c = db.rawQuery("select * from subscribitems ", null);
            if (c.getCount() != -1) { //if the row exist then return the id
                while (c.moveToNext()) {
                    ProductdataItem item = new ProductdataItem();
                    item.setProductId(c.getString(1));
                    item.setCityid(c.getString(2));
                    item.setCatid(c.getString(3));
                    item.setSubcatid(c.getString(4));
                    item.setProductDiscount("0");//c.getDouble(5)
                    item.setProductVariation(c.getString(6));
                    item.setProductRegularprice("" + c.getDouble(7));
                    item.setProductSubscribeprice("" + c.getDouble(8));
                    item.setProductTitle(c.getString(9));
                    item.setProductImg(c.getString(10));
                    item.setProductQty(c.getString(11));
                    item.setDay(c.getString(12));
                    item.setTdelivery(c.getString(13));
                    item.setSdate(c.getString(14));
                    item.setType(c.getString(15));
                    item.setTdeliverydigit(c.getString(16));
                    item.setStime(c.getString(17));
                    list.add(item);
                }

            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());

        }
        return list;
    }


    private int getID(String pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"product_id"}, "product_id =? ", new String[]{pid}, null, null, null, null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("product_id"));
        return -1;
    }

    private int getIDS(String pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAMES, new String[]{"product_id"}, "product_id =? ", new String[]{pid}, null, null, null, null);
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("product_id"));
        return -1;
    }

    public ProductdataItem getItems(String pid) {
        ProductdataItem u = new ProductdataItem();
        SQLiteDatabase db = this.getWritableDatabase(); //get the database that was created in this instance
        Cursor c = db.rawQuery("select * from " + TABLE_NAMES + " where product_id =?", new String[]{pid});
        if (c.moveToLast()) {
            ProductdataItem item = new ProductdataItem();
            item.setProductId(c.getString(1));
            item.setCityid(c.getString(2));
            item.setCatid(c.getString(3));
            item.setSubcatid(c.getString(4));
            item.setProductDiscount("" + c.getDouble(5));
            item.setProductVariation(c.getString(6));
            item.setProductRegularprice("" + c.getDouble(7));
            item.setProductSubscribeprice("" + c.getDouble(8));
            item.setProductTitle(c.getString(9));
            item.setProductImg(c.getString(10));
            item.setProductQty(c.getString(11));
            item.setDay(c.getString(12));
            item.setTdelivery(c.getString(13));
            item.setSdate(c.getString(14));
            item.setType(c.getString(15));
            item.setTdeliverydigit(c.getString(16));
            item.setStime(c.getString(17));

            return item;

        } else {
            Log.e("error not found", "user can't be found or database empty");
            return u;
        }
    }

    public int getCard(String pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{"service_qty"}, "product_id =? ", new String[]{pid}, null, null, null, null);
        if (c.moveToFirst()) { //if the row exist then return the id
            return c.getInt(c.getColumnIndex("service_qty"));
        } else {
            return -1;
        }
    }

    public int getCardS(String pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAMES, new String[]{"service_qty"}, "product_id =? ", new String[]{pid}, null, null, null, null);
        if (c.moveToFirst()) { //if the row exist then return the id
            return c.getInt(c.getColumnIndex("service_qty"));
        } else {
            return -1;
        }
    }

    public int getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        Cursor res1 = db.rawQuery("select * from " + TABLE_NAMES, null);
        int t = res.getCount() + res1.getCount();
        return t;
    }

    public boolean updateData(String pid, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ICOL_12, qty);
        db.update(TABLE_NAME, contentValues, "product_id = ? ", new String[]{pid});

        return true;
    }

    public boolean updateDataS(ProductdataItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ICOL_12, item.getProductQty());
        contentValues.put(ICOL_13, item.getDay());
        contentValues.put(ICOL_14, item.getTdelivery());
        contentValues.put(ICOL_15, item.getSdate());
        contentValues.put(ICOL_16, "subcription");
        contentValues.put(ICOL_17, item.getTdeliverydigit());
        contentValues.put(ICOL_18, item.getStime());
        db.update(TABLE_NAMES, contentValues, "product_id = ? ", new String[]{item.getProductId()});

        return true;
    }

    public void deleteCard(String cid) {
        SQLiteDatabase db = this.getWritableDatabase();
         db.delete(TABLE_NAME, "product_id = ? ", new String[]{cid});
    }

    public Integer deleteRData(String pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer a = db.delete(TABLE_NAME, "product_id = ? ", new String[]{pid});

        return a;
    }

    public Integer deleteRDataS(String pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Integer a = db.delete(TABLE_NAMES, "product_id = ? ", new String[]{pid});

        return a;
    }

    public void deleteCard() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);

    }

    public void deleteCardS() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAMES);

    }
}