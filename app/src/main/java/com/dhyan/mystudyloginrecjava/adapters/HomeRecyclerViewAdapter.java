package com.dhyan.mystudyloginrecjava.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhyan.mystudyloginrecjava.R;
import com.dhyan.mystudyloginrecjava.interfaces.NavToCustomerDetails;
import com.dhyan.mystudyloginrecjava.models.CustomerResponseRows;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    private NavToCustomerDetails navToCustomerDetails;
    private ArrayList<CustomerResponseRows> searchRows=new ArrayList<>();

    public static HomeRecyclerViewAdapter newInstance(NavToCustomerDetails navToCustomerDetails) {
        HomeRecyclerViewAdapter fragment = new HomeRecyclerViewAdapter();
        fragment.navToCustomerDetails = navToCustomerDetails;
        return fragment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_customer_data, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CustomerResponseRows customerResponseRows = searchRows.get(position);
        holder.nameTxt.setText(customerResponseRows.getName());

    }

    @Override
    public int getItemCount() {
        return searchRows.size();
    }

    private void addCustomer(CustomerResponseRows customersSearchRows) {
        searchRows.add(customersSearchRows);
    }

    public void addCustomers(ArrayList<CustomerResponseRows> customersSearchRows) {
        for (CustomerResponseRows customersSearchRow : customersSearchRows) {
            addCustomer(customersSearchRow);
        }
    }

    public List<CustomerResponseRows> getSearchRows() {
        return searchRows;
    }

    public void clear() {
        searchRows.clear();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView nameTxt;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.Id_home_recycler_customer_name);

        }
    }
}
