package com.dhyan.mystudyloginrecjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.dhyan.mystudyloginrecjava.api.MainRetrofitClient;
import com.dhyan.mystudyloginrecjava.databinding.ActivityMainBinding;
import com.dhyan.mystudyloginrecjava.db.SharedPreferenceManager;
import com.dhyan.mystudyloginrecjava.models.MainBody;
import com.dhyan.mystudyloginrecjava.models.MainResponseValues;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextInputEditText passwordEdt,userNameEdt;
    TextInputLayout passwordLay, emailLay;
    Button loginBtn;
    ContentLoadingProgressBar progressBar;
    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPreferenceManager.getInstence(this).isLoggedIn()) {
            Intent i = new Intent(MainActivity.this, NavigationHostActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      ActivityMainBinding  binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        passwordEdt=binding.IdLoginPassword;
        userNameEdt=binding.IdLoginEmail;
        loginBtn=binding.IdLoginBtn;
        passwordLay=binding.IdLoginPasswordLay;
        emailLay =binding.IdLoginEmailLay;
        progressBar=binding.IdLoginProBar;
        progressBar.hide();
       loginBtn.setOnClickListener(v -> {
           closeKeyboard();
           //loginMethod();
           Intent i = new Intent(MainActivity.this, NavigationHostActivity.class);
           i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           startActivity(i);
       });

    }

    private void loginMethod() {
        if (userNameEdt.getText() != null && passwordEdt.getText() != null) {
        String emailString = userNameEdt.getText().toString();
        String passwordString = passwordEdt.getText().toString();
        System.out.println("ddf  " + emailString);
        if (emailString.isEmpty()) {
            emailLay.setErrorEnabled(true);
            emailLay.setError("Field can't be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            emailLay.setErrorEnabled(true);
            emailLay.setError("Invalid email address");
        } else {
            emailLay.setErrorEnabled(false);
            emailLay.setError("");
        }
        if (passwordString.isEmpty()) {
            passwordLay.setErrorEnabled(true);
            passwordLay.setError("Field can't be empty");
        } else {
            passwordLay.setErrorEnabled(false);
            passwordLay.setError("");
        }

        if (!emailString.isEmpty() && !passwordString.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            progressBar.show();
            MainBody mainBody = new MainBody();
            mainBody.setEmail(emailString);
            mainBody.setPassword(passwordString);
            Call<MainResponseValues> call= MainRetrofitClient.getMinstence().getApi().signIn(mainBody);
            call.enqueue(new Callback<MainResponseValues>() {
                @Override
                public void onResponse(@NonNull Call<MainResponseValues> call, @NonNull Response<MainResponseValues> response) {
                    progressBar.hide();
                    MainResponseValues values = response.body();
                    if(values !=null){
                        String status = values.getMessage();
                        if (status.equals("Success")){
                            SharedPreferenceManager.getInstence(MainActivity.this)
                                    .saveUser(values);
                            Intent i = new Intent(MainActivity.this, NavigationHostActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                        else {
                            getNotification();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MainResponseValues> call, @NonNull Throwable t) {
                    progressBar.hide();
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    }

    private void closeKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void getNotification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Username and Password mismatch !");
        builder.setPositiveButton("Ok", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}