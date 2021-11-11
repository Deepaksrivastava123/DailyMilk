package com.cscodetech.dailymilk.locationpick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.model.RestResponse;
import com.cscodetech.dailymilk.retrofit.APIClient;
import com.cscodetech.dailymilk.retrofit.GetResult;
import com.cscodetech.dailymilk.utility.Utility;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class LocationGetActivity extends AppCompatActivity implements OnMapReadyCallback, GetResult.MyListener {


    @BindView(R.id.lvl_actionsearch)
    LinearLayout lvlActionsearch;
    @BindView(R.id.rmap)
    RelativeLayout rmap;

    @BindView(R.id.btn_save)
    TextView btnSave;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.img_curunt)
    ImageView imgCurunt;
    Bundle addressBundle;
    double mLatitude;
    double mLongitude;
    double bmLatitude;
    double bmlongitude;
    GoogleMap mMap;
    int requestIdMultiplePermissions = 2;

    int placeAutocompleteRequestCode = 1;
    boolean isZooming = false;

    FusedLocationProviderClient fusedLocationProviderClient;
    List<AsyncTask> filterTaskList = new ArrayList<>();
    String regex = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)$";
    Pattern latLongPattern = Pattern.compile(regex);

    double currentLatitude;
    double currentLongitude;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    String userAddress = "";
    String userState = "";
    String userCity = "";
    String userPostalCode = "";
    String userCountry = "";
    String userAddressline2 = "";
    String userid = "0";
    String newuser = "user";
    String aid = "0";
    String atype = "Home";
    String hno = "";
    String landmark = "";
    String mobile = "";
    String name = "";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Utility.hasGPSDevice(LocationGetActivity.this)) {
            Toast.makeText(LocationGetActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            Utility.enableLoc(LocationGetActivity.this);
        }

        addressBundle = new Bundle();
        fusedLocationProviderClient = getFusedLocationProviderClient(this);
        getLocationRequest();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }
                //Location fetched, update listener can be removed
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        if (i != null) {
            Bundle extras = i.getExtras();
            if (extras != null) {
                userAddress = extras.getString(MapUtility.address);

                hno = getIntent().getStringExtra("hno");
                landmark = getIntent().getStringExtra("landmark");
                mobile = getIntent().getStringExtra("mobile");
                name = getIntent().getStringExtra("name");

                userid = getIntent().getStringExtra("userid");
                newuser = getIntent().getStringExtra("newuser");
                aid = getIntent().getStringExtra("aid");
                atype = getIntent().getStringExtra("atype");
                atype = getIntent().getStringExtra("atype");


                mLatitude = getIntent().getDoubleExtra(MapUtility.latitude, 0);
                mLongitude = getIntent().getDoubleExtra(MapUtility.longitude, 0);

            }
        }
        if (savedInstanceState != null) {
            mLatitude = savedInstanceState.getDouble("latitude");
            mLongitude = savedInstanceState.getDouble("longitude");
            userAddress = savedInstanceState.getString("userAddress");
            currentLatitude = savedInstanceState.getDouble("currentLatitude");
            currentLongitude = savedInstanceState.getDouble("currentLongitude");
        }
        if (!MapUtility.isNetworkAvailable(this)) {
            MapUtility.showToast(this, "Please Connect to Internet");
        }


    }

    @OnClick({R.id.lvl_actionsearch, R.id.btn_save, R.id.img_curunt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvl_actionsearch:
                if (!Places.isInitialized()) {
                    Places.initialize(this.getApplicationContext(), MapUtility.apiKey);
                }
                // Set the fields to specify which types of place data to return.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .build(this);
                this.startActivityForResult(intent, placeAutocompleteRequestCode);
                break;

            case R.id.btn_save:
                bottonAddtoCard();

                break;
            case R.id.img_curunt:
                showCurrentLocationOnMap(false);
                break;
            default:
                break;
        }
    }

    private void getLocationRequest() {
        locationRequest = new LocationRequest().create().setInterval(10000)
        .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(3000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (mMap.isIndoorEnabled()) {
            mMap.setIndoorEnabled(false);
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {


                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();
                mLatitude = latLng.latitude;
                mLongitude = latLng.longitude;
                return null;

            }
        });
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Setting a click event handler for the map
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            mLatitude = latLng.latitude;
            mLongitude = latLng.longitude;
            Log.e("latlng", latLng + "");
            isZooming = true;
            LocationGetActivity.this.addMarker();
            if (!MapUtility.isNetworkAvailable(LocationGetActivity.this)) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                MapUtility.showToast(LocationGetActivity.this, "Please Connect to Internet");
            }
            LocationGetActivity.this.getAddressByGeoCodingLatLng();

        });
        if (newuser.equalsIgnoreCase("update")) {
            LocationGetActivity.this.getAddressByGeoCodingLatLng();
        } else if (checkAndRequestPermissions()) {
            startParsingAddressToShow();

        }


    }

    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermision = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarsePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), requestIdMultiplePermissions);
            return false;
        }

        return true;

    }

    private void showCurrentLocationOnMap(final boolean isDirectionClicked) {

        if (checkAndRequestPermissions()) {

            @SuppressLint("MissingPermission")
            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
            lastLocation.addOnSuccessListener(this, location -> {
                if (location != null) {
                    mMap.clear();
                    if (isDirectionClicked) {
                        currentLatitude = location.getLatitude();
                        currentLongitude = location.getLongitude();
                        //Go to Map for Directions
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "http://maps.google.com/maps?saddr=" + currentLatitude + ", " + currentLongitude + "&daddr=" + mLatitude + ", " + mLongitude + ""));
                        LocationGetActivity.this.startActivity(intent);
                    } else {
                        //Go to Current Location
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        LocationGetActivity.this.getAddressByGeoCodingLatLng();
                    }
                } else {
                    //Gps not enabled if loc is null
                    Utility.enableLoc(LocationGetActivity.this);
                    Toast.makeText(LocationGetActivity.this, "Location not Available", Toast.LENGTH_SHORT).show();

                }
            });
            lastLocation.addOnFailureListener(e -> {
                //If perm provided then gps not enabled
                Toast.makeText(LocationGetActivity.this, "Location Not Availabe", Toast.LENGTH_SHORT).show();

            });
        }

    }

    private void startParsingAddressToShow() {
        //get address from intent to show on map
        if (userAddress == null || userAddress.isEmpty()) {


            showCurrentLocationOnMap(false);

        } else if (latLongPattern.matcher(userAddress).matches()) {

            Pattern p = Pattern.compile("(-?\\d+(\\.\\d+)?)");   // the pattern to search for
            Matcher m = p.matcher(userAddress);

            // if we find a match, get the group
            int i = 0;
            while (m.find()) {
                // we're only looking for 2s group, so get it
                if (i == 0)
                    mLatitude = Double.parseDouble(m.group());
                if (i == 1)
                    mLongitude = Double.parseDouble(m.group());

                i++;

            }

            getAddressByGeoCodingLatLng();
            addMarker();

        } else {

            if (mLatitude == 0 && mLongitude == 0) {
                getLatLngByRevGeoCodeFromAdd();
            } else {

                addMarker();
            }
        }

    }

    private void getLatLngByRevGeoCodeFromAdd() {


        if (mLatitude == 0 && mLongitude == 0) {

            if (MapUtility.popupWindow != null && MapUtility.popupWindow.isShowing()) {
                MapUtility.hideProgress();
            }

            Log.d("TAG", "getLatLngByRevGeoCodeFromAdd: START");

            for (AsyncTask prevTask : filterTaskList) {
                prevTask.cancel(true);
            }

            filterTaskList.clear();
            GetLatLngFromAddress asyncTask = new GetLatLngFromAddress();
            filterTaskList.add(asyncTask);
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, userAddress);
        }
    }


    private class GetLatLngFromAddress extends AsyncTask<String, Void, LatLng> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MapUtility.showProgress(LocationGetActivity.this);
        }

        @Override
        protected LatLng doInBackground(String... userAddress) {
            LatLng latLng = new LatLng(0, 0);

            try {

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(LocationGetActivity.this, Locale.getDefault());


                addresses = geocoder.getFromLocationName(userAddress[0], 1);

                if (addresses != null && addresses.size() > 0) {
                    latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                }
            } catch (Exception ignored) {
                Log.e("ex", "eror");
            }
            return latLng;
        }


        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            LocationGetActivity.this.mLatitude = latLng.latitude;
            LocationGetActivity.this.mLongitude = latLng.longitude;
            MapUtility.hideProgress();
            addMarker();
        }
    }

    private void getAddressByGeoCodingLatLng() {

        //Get string address by geo coding from lat long
        if (mLatitude != 0 && mLongitude != 0) {

            if (MapUtility.popupWindow != null && MapUtility.popupWindow.isShowing()) {
                MapUtility.hideProgress();
            }

            Log.d("TAG", "getAddressByGeoCodingLatLng: START");
            //Cancel previous tasks and launch this one
            for (AsyncTask prevTask : filterTaskList) {
                prevTask.cancel(true);
            }

            filterTaskList.clear();
            GetAddressFromLatLng asyncTask = new GetAddressFromLatLng();
            filterTaskList.add(asyncTask);
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mLatitude, mLongitude);
        }
    }


    private class GetAddressFromLatLng extends AsyncTask<Double, Void, Bundle> {
        Double latitude;
        Double longitude;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MapUtility.showProgress(LocationGetActivity.this);
        }

        @Override
        protected Bundle doInBackground(Double... doubles) {
            try {

                latitude = doubles[0];
                longitude = doubles[1];
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(LocationGetActivity.this, Locale.getDefault());
                StringBuilder sb = new StringBuilder();

                //get location from lat long if address string is null
                addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {

                    String address = addresses.get(0).getAddressLine(0);
                    if (address != null)
                        addressBundle.putString("addressline2", address);
                    sb.append(address).append(" ");


                    String city = addresses.get(0).getLocality();
                    if (city != null)
                        addressBundle.putString("city", city);
                    sb.append(city).append(" ");


                    String state = addresses.get(0).getAdminArea();
                    if (state != null)
                        addressBundle.putString("state", state);
                    sb.append(state).append(" ");


                    String country = addresses.get(0).getCountryName();
                    if (country != null)
                        addressBundle.putString("country", country);
                    sb.append(country).append(" ");

                    String postalCode = addresses.get(0).getPostalCode();
                    if (postalCode != null)
                        addressBundle.putString("postalcode", postalCode);
                    sb.append(postalCode).append(" ");

                    addressBundle.putString("fulladdress", sb.toString());

                    return addressBundle;
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                addressBundle.putBoolean("error", true);
                return addressBundle;

            }

        }


        @Override
        // setting address into different components
        protected void onPostExecute(Bundle userAddress) {
            super.onPostExecute(userAddress);

            LocationGetActivity.this.userAddress = userAddress.getString("fulladdress");
            LocationGetActivity.this.userCity = userAddress.getString("city");
            LocationGetActivity.this.userState = userAddress.getString("state");
            LocationGetActivity.this.userPostalCode = userAddress.getString("postalcode");
            LocationGetActivity.this.userCountry = userAddress.getString("country");
            LocationGetActivity.this.userAddressline2 = userAddress.getString("addressline2");
            MapUtility.hideProgress();
            addMarker();
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private void addMarker() {
        CameraUpdate cameraUpdate;

        LatLng coordinate = new LatLng(mLatitude, mLongitude);
        if (mMap != null) {
            MarkerOptions markerOptions;
            try {
                mMap.clear();
                txtLocation.setText("" + userAddress);
                markerOptions = new MarkerOptions().position(coordinate).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_map_pin", 73, 104)));
                if (isZooming) {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, mMap.getCameraPosition().zoom);
                } else {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, 18);
                }

                mMap.animateCamera(cameraUpdate);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Marker marker = mMap.addMarker(markerOptions);
                marker.showInfoWindow();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            userAddressline2 = userAddressline2.substring(0, userAddressline2.indexOf(userCity));
        } catch (Exception ex) {
            Log.d("TAG", "address error " + ex);
        }
        try {

            txtLocation.setText(userAddress);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void tempMarker() {
        CameraUpdate cameraUpdate;

        LatLng coordinate = new LatLng(mLatitude, mLongitude);
        if (mMap != null) {
            MarkerOptions markerOptions;
            try {
                mMap.clear();

                markerOptions = new MarkerOptions().position(coordinate).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("ic_map_pin", 73, 104)));
                if (isZooming) {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, mMap.getCameraPosition().zoom);
                } else {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, 18);
                }

                mMap.animateCamera(cameraUpdate);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Marker marker = mMap.addMarker(markerOptions);
                marker.showInfoWindow();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            userAddressline2 = userAddressline2.substring(0, userAddressline2.indexOf(userCity));
        } catch (Exception ex) {
            Log.d("TAG", "address error " + ex);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //after a place is searched
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == placeAutocompleteRequestCode) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                userAddress = place.getAddress();

                txtLocation.setText("" + place.getAddress() + userAddressline2);
                mLatitude = place.getLatLng().latitude;
                mLongitude = place.getLatLng().longitude;


                tempMarker();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    private void addAddress() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", userid);
            jsonObject.put("address", userAddressline2);
            jsonObject.put("pincode", LocationGetActivity.this.userPostalCode);
            jsonObject.put("city", userCity);
            jsonObject.put("houseno", hno);
            jsonObject.put("landmark", landmark);
            jsonObject.put("type", atype);
            jsonObject.put("lat_map", LocationGetActivity.this.mLatitude);
            jsonObject.put("long_map", LocationGetActivity.this.mLongitude);
            jsonObject.put("aid", aid);
            jsonObject.put("name", name);
            jsonObject.put("mobile", mobile);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().setAddress(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(LocationGetActivity.this, response.getResponseMsg(), Toast.LENGTH_SHORT).show();
                if (response.getResult().equalsIgnoreCase("true")) {

                    finish();
                } else {
                    Toast.makeText(LocationGetActivity.this, response.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void bottonAddtoCard() {
        Activity activity = this;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        View rootView = activity.getLayoutInflater().inflate(R.layout.locationedit_layout, null);
        mBottomSheetDialog.setContentView(rootView);
        EditText edHoseno = rootView.findViewById(R.id.ed_hoseno);
        EditText edLandmark = rootView.findViewById(R.id.ed_landmark);
        EditText edName = rootView.findViewById(R.id.ed_name);
        EditText edMobile = rootView.findViewById(R.id.ed_mobile);

        TextView txtHome = rootView.findViewById(R.id.txt_home);
        TextView txtOffice = rootView.findViewById(R.id.txt_office);
        TextView txtAther = rootView.findViewById(R.id.txt_ather);

        TextView btnSave = rootView.findViewById(R.id.btn_save);
        if (hno != null)
            edHoseno.setText("" + hno);

        if (landmark != null)
            edLandmark.setText("" + landmark);

        if (name != null)
            edName.setText("" + name);

        if (mobile != null)
            edMobile.setText("" + mobile);

        if (atype.equalsIgnoreCase("Home")) {
            txtHome.performClick();
        } else if (atype.equalsIgnoreCase("Office")) {
            txtOffice.performClick();
        } else if (atype.equalsIgnoreCase("Other")) {
            txtOffice.performClick();
        }

        txtHome.setOnClickListener(v -> {
            txtHome.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            txtOffice.setBackground(getResources().getDrawable(R.drawable.rounded_border));
            txtAther.setBackground(getResources().getDrawable(R.drawable.rounded_border));
            txtAther.setTextColor(getResources().getColor(R.color.black));
            txtOffice.setTextColor(getResources().getColor(R.color.black));
            txtHome.setTextColor(getResources().getColor(R.color.white));
            atype = "Home";
        });
        txtOffice.setOnClickListener(v -> {
            txtOffice.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            txtAther.setBackground(getResources().getDrawable(R.drawable.rounded_border));
            txtHome.setBackground(getResources().getDrawable(R.drawable.rounded_border));
            txtAther.setTextColor(getResources().getColor(R.color.black));
            txtHome.setTextColor(getResources().getColor(R.color.black));
            txtOffice.setTextColor(getResources().getColor(R.color.white));
            atype = "Office";
        });

        txtAther.setOnClickListener(v -> {
            txtAther.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            txtHome.setBackground(getResources().getDrawable(R.drawable.rounded_border));
            txtOffice.setBackground(getResources().getDrawable(R.drawable.rounded_border));
            txtHome.setTextColor(getResources().getColor(R.color.black));
            txtOffice.setTextColor(getResources().getColor(R.color.black));
            txtAther.setTextColor(getResources().getColor(R.color.white));
            atype = "Other";
        });

        btnSave.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            if (edHoseno.getText().toString().isEmpty()) {
                edHoseno.setError("Enter House No");

            } else if (edLandmark.getText().toString().isEmpty()) {
                edLandmark.setError("Enter Landmark");

            } else if (edName.getText().toString().isEmpty()) {
                edName.setError("Enter Name");

            } else if (edMobile.getText().toString().isEmpty()) {
                edMobile.setError("Enter Mobile");

            } else {
                hno = edHoseno.getText().toString();
                landmark = edLandmark.getText().toString();
                mobile = edMobile.getText().toString();
                name = edName.getText().toString();
                addAddress();
            }


        });
        mBottomSheetDialog.show();


    }
}