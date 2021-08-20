package com.example.tinderui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tinderui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArrayAdapterPendingComplaints extends RecyclerView.Adapter<ArrayAdapterPendingComplaints.Myholder> {
    Context context;
    ArrayList<String> arrayList_upvotes;
    ArrayList<String> arrayList_emp_name;
    ArrayList<String> arrayList_complaint;
    ArrayList<String> arrayList_subject;
    ArrayList<String> arrayList_complaint_id;

    public ArrayAdapterPendingComplaints(Context context, ArrayList<String> complaint, ArrayList<String> emp_name, ArrayList<String> upvotes,ArrayList<String> subject,ArrayList<String> complaint_id) {
        this.context=context;
        this.arrayList_complaint=complaint;
        this.arrayList_upvotes=upvotes;
        this.arrayList_emp_name=emp_name;
        this.arrayList_subject=subject;
        this.arrayList_complaint_id=complaint_id;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.pending_complaint_view,parent,false);
        ArrayAdapterPendingComplaints.Myholder holder = new ArrayAdapterPendingComplaints.Myholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.complaint.setText(arrayList_complaint.get(position));
        holder.emp_name.setText(arrayList_emp_name.get(position));
        holder.upvotes.setText(arrayList_upvotes.get(position));
        holder.subject.setText(arrayList_subject.get(position));
        holder.complaint_id.setText(arrayList_complaint_id.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList_complaint.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView complaint;TextView upvotes;TextView emp_name;TextView subject;TextView complaint_id;
        Button accept,reject;
        String url="https://complainbox2000.000webhostapp.com/accept_or_reject_pending_complaints.php";
        public Myholder(@NonNull View itemView) {
            super(itemView);
            complaint=itemView.findViewById(R.id.complaint);
            complaint_id=itemView.findViewById(R.id.complaint_id);
            upvotes=itemView.findViewById(R.id.upvote);
            subject=itemView.findViewById(R.id.subject);
            emp_name=itemView.findViewById(R.id.raised_by);
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reject.setEnabled(false);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.trim().equals("ok")){
                                reject.setVisibility(View.INVISIBLE);
                                accept.setText("Approved");
                                accept.setLayoutParams(new LinearLayout.LayoutParams(accept.getWidth()+accept.getWidth()+6, accept.getHeight()));
                                accept.setEnabled(false);
                            }
                            else{
                                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                                reject.setEnabled(true);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                            reject.setEnabled(true);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("complaint_id", complaint_id.getText().toString().trim());
                            map.put("status","1");
                            return map;
                        }
                    };
                    RequestQueue mque = Volley.newRequestQueue(context.getApplicationContext());
                    mque.add(stringRequest);
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accept.setEnabled(false);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.trim().equals("ok")){
                                accept.setVisibility(View.INVISIBLE);
                                reject.setText("Rejected");
                                reject.setLayoutParams(new LinearLayout.LayoutParams(reject.getWidth()+reject.getWidth()+6, reject.getHeight()));
                                reject.setEnabled(false);
                            }
                            else {
                                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                                accept.setEnabled(true);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                            accept.setEnabled(true);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("complaint_id", complaint_id.getText().toString().trim());
                            map.put("status","2");
                            return map;
                        }
                    };
                    RequestQueue mque = Volley.newRequestQueue(context.getApplicationContext());
                    mque.add(stringRequest);
                }
            });
        }
    }
}
