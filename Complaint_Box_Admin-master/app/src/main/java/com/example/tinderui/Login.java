package com.example.tinderui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
TextView signup;
Button login;
EditText company,password;
String url="https://complainbox2000.000webhostapp.com/login_admin.php";
ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        company=findViewById(R.id.edtCompany);
        password=findViewById(R.id.editTextPassword);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String companyname = sh.getString("company", "");

        if(companyname.trim().equals("")){

        }
        else {
            Intent intent=new Intent(Login.this, Pending_Complaints.class);
            startActivity(intent);
            finish();
        }

        signup=findViewById(R.id.signup);
        login=findViewById(R.id.btnlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InternetCheck internetCheck=new InternetCheck();
                boolean b=internetCheck.checkConnection(Login.this);

                if(company.getText().toString().trim().equals("")){
                    company.setError("Email is missing");
                }
                else if(password.getText().toString().trim().equals("")){
                    password.setError("Password is missing");
                }
                else if (password.length() < 6) {
                    password.setError("Please Enter Minimum 6 Char.");
                } else if(!b){
                    Toast.makeText(Login.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    pd = new ProgressDialog(Login.this, R.style.MyAlertDialogStyle);
                    pd.setTitle("Connecting Server");
                    pd.setMessage("loading...");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            if(response.trim().equals("Login Successful")){
                                Intent intent=new Intent(Login.this, Pending_Complaints.class);
                                startActivity(intent);
                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("company", company.getText().toString());
                                myEdit.commit();
                                finish();
                            }
                            else  if(response.trim().equals("Invalid email or password")){
                                Toast.makeText(Login.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Login.this, "Something went wrong try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(Login.this, "Something went wrong try again later", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("company", company.getText().toString().trim());
                            map.put("password", password.getText().toString().trim());
                            return map;
                        }
                    };
                    RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
                    mque.add(stringRequest);

                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });
    }
}