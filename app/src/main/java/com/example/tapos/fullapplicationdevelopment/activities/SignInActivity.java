package com.example.tapos.fullapplicationdevelopment.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tapos.fullapplicationdevelopment.R;
import com.example.tapos.fullapplicationdevelopment.api.APIService;
import com.example.tapos.fullapplicationdevelopment.api.APIUrl;
import com.example.tapos.fullapplicationdevelopment.model.CreatorInfo;
import com.example.tapos.fullapplicationdevelopment.model.LoginInformation;
import com.example.tapos.fullapplicationdevelopment.model.SimpleError;
import com.example.tapos.fullapplicationdevelopment.model.User;
import com.example.tapos.fullapplicationdevelopment.model.ValidationError;
import com.example.tapos.fullapplicationdevelopment.utils.SharedPrefManager;
import com.example.tapos.fullapplicationdevelopment.viewModel.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail,editTextPassword;
    Button buttonSignIn;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        progressDialog = new ProgressDialog(this);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);

        buttonSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();
        if(id== R.id.buttonSignIn){
            login();
        }
    }



    private void login() {

        //defining a progress dialog to show while signing up

        progressDialog.setMessage("Signing Up...");
        progressDialog.show();



        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        UserViewModel userViewModel = new UserViewModel(email,password);

        //defining the call
        Call<LoginInformation> call = service.UserLogin(
                userViewModel.getEmail(),
                userViewModel.getPassword()

        );

        call.enqueue(new Callback<LoginInformation>() {
            @Override
            public void onResponse(Call<LoginInformation> call, Response<LoginInformation> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){

                    LoginInformation aInformation = response.body();
                    SharedPrefManager.getInstance(getApplicationContext()).userLoginDataUpdate(aInformation);
                    setLoggedInUserInformation();
                    Toast.makeText(getApplicationContext(),aInformation.getToken_type(),Toast.LENGTH_SHORT).show();
                   // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }else if (response.code()==422){
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = null;
                    try {
                        mJson = parser.parse(response.errorBody().string());
                        Gson gson = new Gson();
                        ValidationError errorResponse = gson.fromJson(mJson, ValidationError.class);
                        Toast.makeText(getApplicationContext(),errorResponse.getError().getEmail().toString(),Toast.LENGTH_SHORT).show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = null;
                    try {
                        mJson = parser.parse(response.errorBody().string());
                        Gson gson = new Gson();
                        SimpleError simpleerrorRes = gson.fromJson(mJson, SimpleError.class);
                        Toast.makeText(getApplicationContext(),simpleerrorRes.getError().toString(),Toast.LENGTH_SHORT).show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<LoginInformation> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setLoggedInUserInformation() {


        progressDialog.setMessage("Get User Information...");
        progressDialog.show();

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);



        //defining the call
        Call<CreatorInfo> call = service.GetLoggedInUserData(
                SharedPrefManager.getInstance(this).getAuthToken()

        );

        call.enqueue(new Callback<CreatorInfo>() {
            @Override
            public void onResponse(Call<CreatorInfo> call, Response<CreatorInfo> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){

                    CreatorInfo others = response.body();
                    SharedPrefManager.getInstance(getApplicationContext()).userOwnDataUpdate(others);
                    finish();
                     startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }else{
                    JsonParser parser = new JsonParser();
                    JsonElement mJson = null;
                    try {
                        mJson = parser.parse(response.errorBody().string());
                        Gson gson = new Gson();
                        SimpleError simpleerrorRes = gson.fromJson(mJson, SimpleError.class);
                        Toast.makeText(getApplicationContext(),simpleerrorRes.getError().toString(),Toast.LENGTH_SHORT).show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<CreatorInfo> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
