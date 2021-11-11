package com.cscodetech.dailymilk.retrofit;


import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST(APIClient.APPEND_URL + "d_reg_user.php")
    Call<JsonObject> createUser(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_user_login.php")
    Call<JsonObject> loginUser(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_forget_password.php")
    Call<JsonObject> getForgot(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "d_mobile_check.php")
    Call<JsonObject> getMobileCheck(@Body RequestBody object);

    @POST(APIClient.APPEND_URL + "d_home_data.php")
    Call<JsonObject> getHome(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_subcat_product.php")
    Call<JsonObject> sSubList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_search_product.php")
    Call<JsonObject> search(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_deliverylist.php")
    Call<JsonObject> deliverylist(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_couponlist.php")
    Call<JsonObject> getCouponList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_check_coupon.php")
    Call<JsonObject> checkCoupon(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_wallet_up.php")
    Call<JsonObject> walletUp(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_wallet_report.php")
    Call<JsonObject> getHistry(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_city.php")
    Call<JsonObject> dCity(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_country_code.php")
    Call<JsonObject> getCodelist(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_address_user.php")
    Call<JsonObject> setAddress(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_address_list.php")
    Call<JsonObject> getAddress(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_paymentgateway.php")
    Call<JsonObject> getPaymentList(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_order_now.php")
    Call<JsonObject> getOrderNow(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_subscribe_order_history.php")
    Call<JsonObject> getSubcription(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_sub_order_product_list.php")
    Call<JsonObject> getSubcriptionItem(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_order_history.php")
    Call<JsonObject> getOrder(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_order_product_list.php")
    Call<JsonObject> getOrderItme(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_getdata.php")
    Call<JsonObject> getRefercode(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_order_cancel.php")
    Call<JsonObject> getOrdercancle(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_notification_list.php")
    Call<JsonObject> getNote(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "skip_extend.php")
    Call<JsonObject> setSkipday(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "d_timeslot.php")
    Call<JsonObject> getTimeslot(@Body JsonObject object);




}
