package com.yibintsoi.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//import java.util.Collections;
//import java.util.HashMap;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PopSummary extends AppCompatActivity {

//    String [] valveList = {"101","102","103"};
    public ArrayList<String> valveList = new ArrayList<>();
//    private ArrayList<Boolean> valveStatusList = new ArrayList<>();
//    TextView textViewValveID;
    private Button closeBtn;
    private static final String TAG = PopValveList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_summary);

//        ListView listViewSummary = (ListView)findViewById(R.id.summaryList);
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.);
        closeBtn = findViewById(R.id.close_btn);

//        listViewSummary = findViewById(R.id.summaryList);


        createPopUp();
        createSummaryView();
    }


    public void createPopUp() {
        DisplayMetrics listPopUp = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(listPopUp);

        int width = listPopUp.widthPixels;
        int height = listPopUp.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.7));

        WindowManager.LayoutParams params =  getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -100;

        getWindow().setAttributes(params);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void createSummaryView(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences(PopTankList.SHARED_PREFS, MODE_PRIVATE);
        String tankSummary = sharedPreferences.getString("tank_name","100");

        db.collection("Test").document(tankSummary).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Map<Integer,Boolean> valveIdMap = (Map<Integer,Boolean>) document.get("valve_id");
                assert valveIdMap != null;
                for (Map.Entry<Integer,Boolean> entry : valveIdMap.entrySet()){
                    valveList.add(String.valueOf(entry.getKey()));
//                    valveStatusList.add(entry.getValue());
                }
                Log.d(TAG,"onEvent Key: " + valveList + " Valve: ");
//                CustomAdapter customAdapter = new CustomAdapter() ;
//                customAdapter.notifyDataSetChanged();
//                listViewSummary.setAdapter(customAdapter);
            }
        });
    }
}
