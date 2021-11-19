package com.cscodetech.marwarimarts.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.model.Country;
import com.cscodetech.marwarimarts.model.CountryCodeItem;
import com.cscodetech.marwarimarts.model.LoginUser;
import com.cscodetech.marwarimarts.model.User;
import com.cscodetech.marwarimarts.retrofit.APIClient;
import com.cscodetech.marwarimarts.retrofit.GetResult;
import com.cscodetech.marwarimarts.utility.CustPrograssbar;
import com.cscodetech.marwarimarts.utility.SessionManager;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.cscodetech.marwarimarts.utility.Utility.isvarification;

public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.view_login)
    View viewLogin;
    @BindView(R.id.txt_singup)
    TextView txtSingup;
    @BindView(R.id.view_sing)
    View viewSing;
    @BindView(R.id.lvl_login)
    LinearLayout lvlLogin;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.img_showp)
    ImageView imgShowp;
    @BindView(R.id.img_showpr)
    ImageView imgShowpr;
    @BindView(R.id.txt_forgot)
    TextView txtForgot;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.lvl_sign)
    LinearLayout lvlSign;
    @BindView(R.id.ed_fname)
    EditText edFname;
    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.ed_passwordr)
    EditText edPasswordr;
    @BindView(R.id.ed_refercode)
    EditText edRefercode;
    @BindView(R.id.btn_signup)
    TextView btnSignup;

    @BindView(R.id.spinner)
    Spinner spinner;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    List<CountryCodeItem> cCodes = new ArrayList<>();
    String codeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);
        txtLogin.setTextColor(getResources().getColor(R.color.black));
        viewLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        txtSingup.setTextColor(getResources().getColor(R.color.colorgrey));
        viewSing.setBackgroundColor(getResources().getColor(R.color.colorgrey1));
        lvlLogin.setVisibility(View.VISIBLE);
        lvlSign.setVisibility(View.GONE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getCCode();

    }

    private void getCCode() {
        custPrograssbar.prograssCreate(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCodelist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void login() {
        custPrograssbar.prograssCreate(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mobile", edEmail.getText().toString());
            jsonObject.put("password", edPassword.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().loginUser(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    @OnClick({R.id.txt_login, R.id.txt_singup, R.id.btn_login, R.id.btn_signup, R.id.txt_forgot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_login:
                txtLogin.setTextColor(getResources().getColor(R.color.black));
                viewLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                txtSingup.setTextColor(getResources().getColor(R.color.colorgrey));
                viewSing.setBackgroundColor(getResources().getColor(R.color.colorgrey1));
                lvlLogin.setVisibility(View.VISIBLE);
                lvlSign.setVisibility(View.GONE);
                break;
            case R.id.txt_singup:
                txtLogin.setTextColor(getResources().getColor(R.color.colorgrey));
                viewLogin.setBackgroundColor(getResources().getColor(R.color.colorgrey1));
                txtSingup.setTextColor(getResources().getColor(R.color.black));
                viewSing.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                lvlLogin.setVisibility(View.GONE);
                lvlSign.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_login:
                if (validationCreate1()) {
                    login();
                }
                break;
            case R.id.btn_signup:
                if (validationCreate()) {
                    User user = new User();
                    user.setCcode(codeSelect);
                    user.setFname("" + edFname.getText().toString());
                    user.setMobile("" + edMobile.getText().toString());
                    user.setPassword("" + edPasswordr.getText().toString());
                    user.setRefercode("" + edRefercode.getText().toString());
                    sessionManager.setUserDetails("", user);
                    isvarification = 1;
                    startActivity(new Intent(LoginActivity.this, VerifyPhoneActivity.class));

                }

                break;
            case R.id.txt_forgot:
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));

                break;

            default:
                break;
        }
    }

    public boolean validationCreate() {
        if (TextUtils.isEmpty(edFname.getText().toString())) {
            edFname.setError("Enter Name");
            return false;
        }

        if (TextUtils.isEmpty(edMobile.getText().toString())) {
            edMobile.setError("Enter Mobile");
            return false;
        }
        if (TextUtils.isEmpty(edPasswordr.getText().toString())) {
            edPasswordr.setError("Enter Password");
            return false;
        }

        return true;
    }

    public boolean validationCreate1() {
        if (TextUtils.isEmpty(edEmail.getText().toString())) {
            edEmail.setError("Enter Mobile");
            return false;
        }


        if (TextUtils.isEmpty(edPassword.getText().toString())) {
            edPassword.setError("Enter Password");
            return false;
        }

        return true;
    }

    public void showHidePass(View view) {

        if (view.getId() == R.id.img_showp) {
            if (edPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eye_closed);
                //Show Password
                edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eye_open);
                //Hide Password
                edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } else if (view.getId() == R.id.img_showpr) {
            if (edPasswordr.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eye_closed);
                //Show Password
                edPasswordr.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.ic_eye_open);
                //Hide Password
                edPasswordr.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Country country = gson.fromJson(result.toString(), Country.class);
                if (country.getResult().equalsIgnoreCase("true")) {

                    cCodes = country.getCountryCode();
                    List<String> arrayList = new ArrayList<>();
                    for (int i = 0; i < cCodes.size(); i++) {
                        if (cCodes.get(i).getStatus().equalsIgnoreCase("1")) {
                            arrayList.add(cCodes.get(i).getCcode());
                        }
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinnercode_layout, arrayList);
                    dataAdapter.setDropDownViewResource(R.layout.spinnercode_layout);
                    spinner.setAdapter(dataAdapter);
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();

                LoginUser loginUser = gson.fromJson(result.toString(), LoginUser.class);
                Toast.makeText(this, loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    OneSignal.sendTag("userid", loginUser.getUser().getId());
                    sessionManager.setBooleanData(SessionManager.login, true);
                    sessionManager.setStringData(SessionManager.cityid, "0");
                    sessionManager.setUserDetails("", loginUser.getUser());
                    startActivity(new Intent(LoginActivity.this, CityActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                }
            }
        } catch (Exception e) {
            e.toString();
        }
    }
}