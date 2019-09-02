package com.yibintsoi.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.Edits;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

//import java.util.Collections;
//import java.util.HashMap;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class PopSummary extends AppCompatActivity {

    public ArrayList<String> valveList = new ArrayList<>();
    public ArrayList<String> valveStatusList = new ArrayList<>();

//    private ArrayList<Boolean> valveStatusList = new ArrayList<>();
//    TextView textViewValveID;
    ListView listViewSummary;
    private Button closeBtn;
    private static final String TAG = PopValveList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_summary);

        //        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.);


        listViewSummary = (ListView)findViewById(R.id.summaryList);
        closeBtn = findViewById(R.id.close_btn);


        createPopUp();
        createSummaryView();
    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<String> valveNameAdapter;
        ArrayList<String> valveStatusAdapter;

        MyAdapter(Context c , ArrayList<String> valveNameAdapter, ArrayList<String> valveStatusAdapter){
            super(c, R.layout.summary_list_row, R.id.valveIdSummary, valveNameAdapter);
            this.context = c;
            this.valveNameAdapter = valveNameAdapter;
            this.valveStatusAdapter = valveStatusAdapter;
        }

        public View getView(int position, @Nullable View convertView,@NonNull ViewGroup parent){
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.summary_list_row, parent, false);
            TextView myValveName = row.findViewById(R.id.valveIdSummary);
            TextView myValveStatus = row.findViewById(R.id.valveStatus);

            myValveName.setText("Valve : " + valveNameAdapter.get(position));

            if (valveStatusAdapter.get(position).equals("true")){
                myValveStatus.setText(": ON");
            }else{
                myValveStatus.setText(": OFF");
            }


            return row;
        }

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
        SharedPreferences sharedPreferences = getSharedPreferences(PopTankList.SHARED_PREFS, MODE_PRIVATE);
        String tankSummary = sharedPreferences.getString("tank_name","100");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef =  db.collection("Test").document(tankSummary);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();
                Object testSorting = document.get("valve_id");
                Log.d(TAG,"onEvent document : " + testSorting);

                Map<Integer,Boolean> valveIdMap = (Map<Integer,Boolean>) document.get("valve_id");

                TreeMap<Integer,Boolean> sortValveIdMap = new TreeMap<Integer,Boolean>();
                sortValveIdMap.putAll(valveIdMap);

                for (Map.Entry<Integer,Boolean> entry : sortValveIdMap.entrySet()){
                    valveList.add(String.valueOf(entry.getKey()));
                    valveStatusList.add(String.valueOf(entry.getValue()));
                }
//                Log.d(TAG,"onEvent Key: " + valveList + " Valve: " + valveStatusList);
                MyAdapter adapter = new MyAdapter(PopSummary.this, valveList, valveStatusList);
                listViewSummary.setAdapter(adapter);
//                CustomAdapter customAdapter = new CustomAdapter() ;
//                customAdapter.notifyDataSetChanged();
//                listViewSummary.setAdapter(customAdapter);
            }
        });
    }
}
