package com.yibintsoi.navigation;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class HomePage extends Activity {
    private TextView textTankName;
    private TextView textValveName;

//    private static final String TAG = PopValveList.class.getSimpleName();
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String VALVE_NAME = "valve_name";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        textTankName = findViewById(R.id.textTankName);
        textValveName = findViewById(R.id.textValveName);

        TextView selectTank = findViewById(R.id.tankSelect);
        TextView selectValve = findViewById(R.id.tankValve);
        TextView selectSummary = findViewById(R.id.tankSummary);

        selectTank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PopTankList.class);
                startActivityForResult(i,100);
            }
        });
        selectValve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),PopValveList.class);
                startActivityForResult(i,200);
            }
        });
        selectSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PopSummary.class);
                startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences(HomePage.SHARED_PREFS, MODE_PRIVATE);
        String tankName = sharedPreferences.getString("tank_name","No Tank");
        String valveName = sharedPreferences.getString("valve_name","No Valve");

        textTankName.setText("Tank number " + tankName);
        textValveName.setText("Valve number " + valveName);

    }
    @SuppressLint("SetTextI18n")
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 100){
            if(resultCode == Activity.RESULT_OK){
                SharedPreferences sharedPreferences = getSharedPreferences(PopTankList.SHARED_PREFS, MODE_PRIVATE);
                String tankName = sharedPreferences.getString("tank_name","No Tank");
                textTankName.setText("Tank number " + tankName);


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(VALVE_NAME, " ");
                editor.apply();
                textValveName.setText("Please select valve");
            }else {
                Toast.makeText(this,"Please select tank",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == 200){
            if(resultCode == Activity.RESULT_OK){
                SharedPreferences sharedPreferences = getSharedPreferences(PopTankList.SHARED_PREFS, MODE_PRIVATE);
                String valveName = sharedPreferences.getString("valve_name","No Valve");
                textValveName.setText("Valve number " + valveName);
            }else {
                Toast.makeText(this,"Please select valve",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
