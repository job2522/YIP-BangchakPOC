package com.yibintsoi.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        Button logInBtn = findViewById(R.id.logInBtn);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(userName.getText().toString(),password.getText().toString());
            }
        });
    }
    private void login(String userName, String password){
        if(userName.equals("") && password.equals("")){
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Log in fail.", Toast.LENGTH_SHORT).show();
        }
    }
}
