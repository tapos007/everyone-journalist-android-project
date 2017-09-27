package com.example.tapos.fullapplicationdevelopment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tapos.fullapplicationdevelopment.R;
import com.example.tapos.fullapplicationdevelopment.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button SingInButton, SignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
        SingInButton = (Button) findViewById(R.id.buttonSignIn);
        SignUpButton = (Button) findViewById(R.id.buttonSignUp);
        SingInButton.setOnClickListener(this);
        SignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonSignIn) {
            Intent i = new Intent(getApplicationContext(),SignInActivity.class);
            startActivity(i);
        } else if (id == R.id.buttonSignUp) {
            Intent i = new Intent(getApplicationContext(),SignUpActivity.class);
            startActivity(i);
        }
    }
}
