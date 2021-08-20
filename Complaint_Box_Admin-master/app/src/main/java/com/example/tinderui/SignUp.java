package com.example.tinderui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tinderui.internetcheck.InternetCheck;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    TextView signin;
    Button signup,location;
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText email,password,address,name,company;
    ImageView imageView_icon;
    ProgressDialog pd;
    String url="https://complainbox2000.000webhostapp.com/sign_up_admin.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signin=findViewById(R.id.login);

        InternetCheck internetCheck=new InternetCheck();
        boolean b=internetCheck.checkConnection(this);

        imageView_icon = findViewById(R.id.show_pass_btn);

        email=findViewById(R.id.edtEmail);
        password=findViewById(R.id.edtPassword);
        company=findViewById(R.id.edtCompany);
        name=findViewById(R.id.edtName);
        address=findViewById(R.id.edtAddress);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);


        imageView_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    imageView_icon.setImageResource(R.drawable.hide_password);

                    //Show Password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    imageView_icon.setImageResource(R.drawable.show_password);

                    //Hide Password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
        location=findViewById(R.id.location);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(SignUp.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location=task.getResult();
                            if(location!=null)
                            {
                                try {
                                    Geocoder geocoder=new Geocoder(SignUp.this, Locale.getDefault());
                                    List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    address.setText(""+addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });;
                }
                else {
                    ActivityCompat.requestPermissions(SignUp.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        signup=findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetCheck internetCheck=new InternetCheck();
                boolean b=internetCheck.checkConnection(SignUp.this);
                if(name.getText().toString().trim().equals("")){
                    name.setError("Name is missing");
                }
                else if(email.getText().toString().trim().equals("")){
                    email.setError("Email is missing");
                }
                else if(company.getText().toString().trim().equals("")){
                    company.setError("Name of the company is missing");
                }
                else if(address.getText().toString().trim().equals("")){
                    address.setError("Address of the company is missing");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password is missing");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    email.setError("Please Enter Valid Email.");
                }
                else if (password.length() < 6) {
                    password.setError("Please Enter Minimum 6 Char.");
                }
                else if(!b){
                    Toast.makeText(SignUp.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    pd = new ProgressDialog(SignUp.this, R.style.MyAlertDialogStyle);
                    pd.setTitle("Connecting Server");
                    pd.setMessage("loading...");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            if(response.trim().equals("Registration Successful")){
                                Toast.makeText(SignUp.this, "" + response, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else if(response.trim().equals("Company already registered with us")){
                                Toast.makeText(SignUp.this, "" + response, Toast.LENGTH_SHORT).show();
                            }
                            else if(response.trim().equals("Email already registered with us")){
                                Toast.makeText(SignUp.this, "" + response, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("email", email.getText().toString().trim());
                            map.put("password", password.getText().toString().trim());
                            map.put("address", address.getText().toString().trim());
                            map.put("name", name.getText().toString().trim());
                            map.put("company", company.getText().toString().trim());
                            return map;
                        }
                    };
                    RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
                    mque.add(stringRequest);
                    // Intent intent=new Intent(MainActivity.this,Pending_Complaint.class);
                    //startActivity(intent);
                }
            }
        });
    }


}