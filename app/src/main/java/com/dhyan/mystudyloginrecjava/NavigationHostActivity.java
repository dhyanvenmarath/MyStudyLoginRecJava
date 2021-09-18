package com.dhyan.mystudyloginrecjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dhyan.mystudyloginrecjava.databinding.ActivityMainBinding;
import com.dhyan.mystudyloginrecjava.databinding.ActivityNavigationHostBinding;

public class NavigationHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNavigationHostBinding binding = ActivityNavigationHostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);
    }
}