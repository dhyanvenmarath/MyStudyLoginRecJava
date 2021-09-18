package com.dhyan.mystudyloginrecjava;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dhyan.mystudyloginrecjava.adapters.HomeRecyclerViewAdapter;
import com.dhyan.mystudyloginrecjava.api.MainRetrofitClient;
import com.dhyan.mystudyloginrecjava.databinding.FragmentHomeBinding;
import com.dhyan.mystudyloginrecjava.interfaces.NavToCustomerDetails;
import com.dhyan.mystudyloginrecjava.models.CustomerMainRes;
import com.dhyan.mystudyloginrecjava.models.CustomerResponseRows;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements NavToCustomerDetails {


    private EditText searchEdt;
    private int count_fn = 1, temp_count = 0;
    private Boolean isScrolling = false;
    private int current_item, total_item, scrolledOut_item;
    private LinearLayoutManager manager;
    private HomeRecyclerViewAdapter adapter;
    private String responseTest;
    private ContentLoadingProgressBar loadingData;


    public HomeFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.dhyan.mystudyloginrecjava.databinding.FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        searchEdt = binding.IdHomeSearchEdt;
        RecyclerView recyclerView = binding.IdHomeSearchRecycler;
        Button searchBtn = binding.IdHomeSearchBtn;
        loadingData = binding.IdHomeSearchProBar;
        manager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(manager);
        adapter=HomeRecyclerViewAdapter.newInstance(this);
        recyclerView.setAdapter(adapter);
        loadingData.hide();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                current_item = manager.getChildCount();
                total_item = manager.getItemCount() - 3;
                scrolledOut_item = manager.findFirstVisibleItemPosition();

                if (isScrolling && (current_item + scrolledOut_item == total_item) && dy > 0) {
                    isScrolling = false;
                    temp_count = pageCount();
                  getListOfCustomers(false);
                    responseTest = "";
                    startTimer();

                }
            }
        });

        searchBtn.setOnClickListener(v -> {
            loadingData.show();
           // checkStatusDisable();
            temp_count = 0;
            count_fn = 1;
           getListOfCustomers(true);
        });



        return view;
    }

    private void getListOfCustomers(boolean isFirst) {
        loadingData.show();
        int page_nos = temp_count + 1;
        System.out.println("ggggg "+page_nos);
        if(searchEdt.getText()!=null){
            String searchValue=searchEdt.getText().toString();
            Call<CustomerMainRes> call= MainRetrofitClient.getMinstence().getApi().
                    getCustomerDetails(  "1",searchValue,page_nos,"12");
            call.enqueue(new Callback<CustomerMainRes>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<CustomerMainRes> call, @NonNull Response<CustomerMainRes> response) {

                    CustomerMainRes values=response.body();
                    responseTest="obtained";
                    loadingData.hide();
                    //checkStatusEnable();
                    if(values!=null){
                        ArrayList<CustomerResponseRows> rows = values.getSearchRows();
                        int row_size = rows.size();
                        if (row_size != 0) {
                            if (isFirst) {
                                adapter.clear();
                            }
                            adapter.addCustomers(rows);
                            adapter.notifyDataSetChanged();
                        } else {
                            if (isFirst) {
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Data not found", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "End of the list", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CustomerMainRes> call, @NonNull Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingData.hide();
                  //  checkStatusEnable();
                }
            });
        }




    }

    private int pageCount() {
        return count_fn++;
    }

/*
    private void checkStatusDisable() {
        if (getActivity() != null) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void checkStatusEnable() {
        if (getActivity() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
*/
    private void startTimer() {
        new Handler().postDelayed(() -> {
            if (responseTest.equals("")) {
                loadingData.show();
             //   checkStatusDisable();
            } else {
                loadingData.hide();
              //  checkStatusEnable();
            }
        }, 1000);
    }

    @Override
    public void navCallDetails(String name) {

    }
}