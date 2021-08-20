package com.example.tinderui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinderui.R;

import java.util.ArrayList;

public class ArrayAdapterResolvedComplaints  extends RecyclerView.Adapter<ArrayAdapterResolvedComplaints.Myholder> {
    Context context;
    ArrayList<String> arrayList_emp_name;
    ArrayList<String> arrayList_complaint;
    ArrayList<String> arrayList_subject;
    ArrayList<String> arrayList_complaint_id;

    public ArrayAdapterResolvedComplaints(Context context, ArrayList<String> complaint, ArrayList<String> emp_name,ArrayList<String> subject,ArrayList<String> complaint_id) {
        this.context=context;
        this.arrayList_complaint=complaint;
        this.arrayList_emp_name=emp_name;
        this.arrayList_subject=subject;
        this.arrayList_complaint_id=complaint_id;
    }

    @NonNull
    @Override
    public ArrayAdapterResolvedComplaints.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.resolved_complaint_view,parent,false);
        ArrayAdapterResolvedComplaints.Myholder holder = new ArrayAdapterResolvedComplaints.Myholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArrayAdapterResolvedComplaints.Myholder holder, int position) {
        holder.complaint.setText(arrayList_complaint.get(position));
        holder.emp_name.setText(arrayList_emp_name.get(position));
        holder.subject.setText(arrayList_subject.get(position));
        holder.complaint_id.setText(arrayList_complaint_id.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList_complaint.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView complaint;TextView emp_name;TextView subject;TextView complaint_id;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            complaint = itemView.findViewById(R.id.complaint);
            complaint_id = itemView.findViewById(R.id.complaint_id);
            subject = itemView.findViewById(R.id.subject);
            emp_name = itemView.findViewById(R.id.raised_by);
        }
    }
}
