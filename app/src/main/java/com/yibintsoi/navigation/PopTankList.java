package com.yibintsoi.navigation;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.SharedPreferences;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.SharedPreferences.*;

public class PopTankList extends AppCompatActivity {

    private ArrayList<Integer> tankList = new ArrayList<>();
    private Button closeBtn;
    public ListView listView;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TANK_ID = "tank_id";
    public static final String TANK_NAME = "tank_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_tank_list);
        closeBtn = findViewById(R.id.close_btn);
        listView = findViewById(R.id.tankList);

        createPopUp();
        createListView();
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


    public void createListView(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Test").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                tankList.clear();
                assert queryDocumentSnapshots != null;
                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                    tankList.add(Integer.valueOf(snapshot.get("name").toString()));
                }
                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_selectable_list_item,tankList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int index, long length) {
                        saveTankID(tankList.get(index));
                    }
                });
            }
        });
    }
    public void saveTankID(Integer tankID){
//        Toast.makeText(PopTankList.this,"Click tank ID " + tankID,Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();

        editor.putInt(TANK_ID, tankID);
        editor.putString(TANK_NAME, tankID.toString());
        editor.apply();

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK,intent);
        finish();


    }
}
