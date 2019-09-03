package com.yibintsoi.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PopSummary extends AppCompatActivity {

    public ArrayList<String> valveList = new ArrayList<>();
    public ArrayList<String> valveStatusList = new ArrayList<>();


    ListView listViewSummary;
    private Button closeBtn;
//    private static final String TAG = PopValveList.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_summary);

        listViewSummary = findViewById(R.id.summaryList);
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

        @SuppressLint("SetTextI18n")
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.summary_list_row, parent, false);
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

                Map<Integer,Boolean> valveIdMap = (Map<Integer,Boolean>) document.get("valve_id");
                assert valveIdMap != null;
                TreeMap<Integer, Boolean> sortValveIdMap = new TreeMap<>(valveIdMap);

                for (Map.Entry<Integer,Boolean> entry : sortValveIdMap.entrySet()){
                    valveList.add(String.valueOf(entry.getKey()));
                    valveStatusList.add(String.valueOf(entry.getValue()));
                }
                MyAdapter adapter = new MyAdapter(PopSummary.this, valveList, valveStatusList);
                listViewSummary.setAdapter(adapter);
            }
        });
    }
}
