package com.example.tinderui;

import androidx.annotation.Nullable;
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
import com.example.tinderui.adapters.ArrayAdapterResolvedComplaints;
import com.example.tinderui.internetcheck.InternetCheck;
import com.example.tinderui.model.Complaint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Resolved_Complaints extends AppCompatActivity {
    RecyclerView rcv;
    ArrayAdapterResolvedComplaints adapter;
    RecyclerView.LayoutManager mgr;
    ArrayList<String> complaint;
    ArrayList<String> subject;
    ArrayList<String> emp_name;
    ArrayList<String> complaint_id;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolved__complaints);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcv = findViewById(R.id.rcv);
        complaint = new ArrayList<>();
        subject = new ArrayList<>();

        emp_name = new ArrayList<>();
        complaint_id = new ArrayList<>();


        InternetCheck internetCheck=new InternetCheck();
        boolean b=internetCheck.checkConnection(Resolved_Complaints.this);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final String company = sh.getString("company", "");

        if(b) {
            String url = "https://complainbox2000.000webhostapp.com/resolved_complaints.php";
            pd = new ProgressDialog(this, R.style.MyAlertDialogStyle);
            pd.setTitle("Connecting Server");
            pd.setMessage("loading...");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    if (response.trim().equals("")) {
                        Toast.makeText(Resolved_Complaints.this, "There are no resolved complaints to display", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Toast.makeText(Resolved_Complaints.this, "hii" + response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Complaint complaint_object=new Complaint();

                                complaint_object.setSubject(jsonObject.getString("subject"));
                                complaint_object.setEmp_name(jsonObject.getString("name"));
                                complaint_object.setDescription(jsonObject.getString("description"));
                                complaint_object.setComplaint_id(jsonObject.getString("complaint_id"));

                                complaint.add(complaint_object.getDescription());
                                subject.add(complaint_object.getSubject());
                                complaint_id.add(complaint_object.getComplaint_id());
                                emp_name.add("Raised by " + complaint_object.getEmp_name());
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("company", company);
                    return map;
                }
            };
            RequestQueue mque = Volley.newRequestQueue(getApplicationContext());
            mque.add(stringRequest);
            mgr = new LinearLayoutManager(this);

            rcv.setLayoutManager(mgr);
            adapter = new ArrayAdapterResolvedComplaints(this, complaint, emp_name, subject, complaint_id);
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