package com.dhyan.mystudyloginrecjava.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CustomerMainRes {
    @SerializedName("data")
    private ArrayList<CustomerResponseRows> searchRows;

    public ArrayList<CustomerResponseRows> getSearchRows() {
        return searchRows;
    }
}
