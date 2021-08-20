package com.example.tinderui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tinderui.adapters.ArrayAdapterPendingRequests;
import com.example.tinderui.internetcheck.InternetCheck;
import com.example.tinderui.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pending_Requests extends AppCompatActivity {
    RecyclerView rcv;
    ArrayAdapterPendingRequests adapter;
    RecyclerView.LayoutManager mgr;
    ArrayList<String> emp_id;
    ArrayList<String> emp_name;
    ArrayList<String> emp_email;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending__requests);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcv=findViewById(R.id.rcv);

        emp_id=new ArrayList<>();
        emp_email=new ArrayList<>();
        emp_name=new ArrayList<>();

        InternetCheck internetCheck=new InternetCheck();
        boolean b=internetCheck.checkConnection(Pending_Requests.this);

        if(b) {
            String url="https://complainbox2000.000webhostapp.com/pending_request.php";
            pd = new ProgressDialog(this, R.style.MyAlertDialogStyle);
            pd.setTitle("Connecting Server");
            pd.setMessage("loading...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    //Toast.makeText(Pending_Requests.this, ""+response, Toast.LENGTH_SHORT).show();
                    if (response.trim().equals("")) {
                        Toast.makeText(Pending_Requests.this, "There are no pending requests to display", Toast.LENGTH_SHORT).show();
                    }
                    else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                           Employee employee=new Employee();
                            employee.setEmp_name(jsonObject.getString("name"));
                            employee.setEmp_email(jsonObject.getString("email"));
                            employee.setEmp_id(jsonObject.getString("emp_id"));
                            emp_email.add(employee.getEmp_email());
                            emp_id.add(employee.getEmp_id());
                            emp_name.add(employee.getEmp_name());

                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Pending_Requests.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(Pending_Requests.this, "something went wrong", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    String s1 = sh.getString("company", "");
                    map.put("company", s1.trim());
                    return map;
                }
            };
            RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
            mque.add(stringRequest);
            mgr = new LinearLayoutManager(this);
            rcv.setLayoutManager(mgr);
            adapter = new ArrayAdapterPendingRequests(Pending_Requests.this, emp_id, emp_name, emp_email);
            rcv.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}