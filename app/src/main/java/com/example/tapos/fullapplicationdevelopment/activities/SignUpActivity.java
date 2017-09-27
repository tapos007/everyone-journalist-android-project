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
import com.example.tapos.fullapplicationdevelopment.model.SimpleError;
import com.example.tapos.fullapplicationdevelopment.model.User;
import com.example.tapos.fullapplicationdevelopment.model.ValidationError;
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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nameEditText, emailEditText, passwordEditText, rePasswordEditText, phoneNumberEditText;
    Button singUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        nameEditText = (EditText) findViewById(R.id.editTextName);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        rePasswordEditText = (EditText) findViewById(R.id.reeditTextPassword);
        phoneNumberEditText = (EditText) findViewById(R.id.numbereditText);
        singUpButton = (Button) findViewById(R.id.buttonSignUp);
        singUpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSignUp) {
            userSignUp();
        }
    }

    private void userSignUp() {

        //defining a progress dialog to show while signing up
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();


        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String repassword = rePasswordEditText.getText().toString().trim();
        String phone_no = phoneNumberEditText.getText().toString().trim();


        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        UserViewModel userViewModel = new UserViewModel(name,email,password,repassword,phone_no);

        //defining the call
        Call<User> call = service.createUser(
                userViewModel.getName(),
                userViewModel.getEmail(),
                userViewModel.getPassword(),
                userViewModel.getRe_password(),
                userViewModel.getPhoneNumber()
        );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    User aUser = response.body();
                    Toast.makeText(getApplicationContext(),aUser.getName() + " user create succssfully",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),SignInActivity.class);

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
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
