package com.yibintsoi.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PopValveList extends AppCompatActivity {
    private ArrayList<Object> valveList = new ArrayList<>();
//    private static final String TAG = PopValveList.class.getSimpleName();
    private Button closeBtn;
    private ListView listView;

    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String VALVE_ID = "valve_id";
    public static final String VALVE_NAME = "valve_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_valve_list);
        closeBtn = findViewById(R.id.close_btn);
        listView = findViewById(R.id.valveList);


        createPopUp();
        createListView();

    }
    public void createPopUp() {
        DisplayMetrics listPopUp = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(listPopUp);

        int width = listPopUp.widthPixels;
        int height = listPopUp.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.65));

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

    public void createListView(){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences(PopTankList.SHARED_PREFS, MODE_PRIVATE);
        String tankName = sharedPreferences.getString("tank_name","100");

        DocumentReference docRef = db.collection("Test").document(tankName);

//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot document = task.getResult();
//                Map<Integer,Boolean> valveIdMap = (Map<Integer,Boolean>) document.get("valve_id");
//                Log.d(TAG,"onEvent5555:" + valveIdMap);
//                assert valveIdMap != null;
//                for (Map.Entry<Integer,Boolean> entry : valveIdMap.entrySet()){
//                    valveList.add(entry.getKey());
//                }

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                valveList.clear();
                DocumentSnapshot document = task.getResult();
                Map<Integer,Boolean> valveIdMap = (Map<Integer,Boolean>) document.get("valve_id");

                assert valveIdMap != null;
                TreeMap<Integer, Boolean> sortValveIdMap = new TreeMap<>(valveIdMap);

                for (Map.Entry<Integer,Boolean> entry : sortValveIdMap.entrySet()){
                    valveList.add(entry.getKey());
                }

//                Collections.reverse(valveList);
                ArrayAdapter<Object> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_selectable_list_item,valveList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int index, long length) {
                        saveValveID(Integer.valueOf(valveList.get(index).toString()));
                        finish();
                    }
                });
            }
        });
    }
    public void saveValveID(Integer valveID){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        editor.putInt(VALVE_ID, valveID);
        editor.putString(VALVE_NAME, valveID.toString());
        editor.apply();

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK,intent);
        finish();


    }
}
