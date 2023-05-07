package com.example.taplalkozaskovetoalkalmazas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FoodBaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = FoodBaseActivity.class.getName();

    private FirebaseUser user;
    private FirebaseAuth auth;

    private RecyclerView recyclerView;
    private ArrayList<FoodItem> itemList;
    private FoodItemAdapter adapter;

    private FirebaseFirestore firestore;
    private CollectionReference items;

    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_base);

        auth = FirebaseAuth.getInstance();
        //auth.signOut();
        user = FirebaseAuth.getInstance().getCurrentUser();


        if(user== null){
            finish();
        }

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        itemList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        items = firestore.collection("Items");


        adapter = new FoodItemAdapter(this,itemList);

        recyclerView.setAdapter(adapter);


        getData();
    }

    private void getData(){
        itemList.clear();

        //whereEqualTo("userID", user.getEmail()).A]
        items.whereEqualTo("userID", user.getEmail()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                FoodItem item = doc.toObject(FoodItem.class);
                itemList.add(item);
            }
            adapter.notifyDataSetChanged();
        });
    }

    public void DeleteFood(String nameText, String caloryText){

        items.whereEqualTo("userID", user.getEmail()).whereEqualTo("name", nameText).whereEqualTo("calory", caloryText).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                DocumentReference ref = doc.getReference();
                ref.delete();
            }
            getData();

        });

    }
    public void AddFood(View view) {

        EditText foodNameET = findViewById(R.id.editTextItemFood);
        EditText foodCaloryET = findViewById(R.id.editTextcalory);

        String foodName = foodNameET.getText().toString();
        String foodCalory = foodCaloryET.getText().toString();

        FoodItem item = new FoodItem(foodName,foodCalory, user.getEmail());

        items.add(item).addOnSuccessListener(queryDocumentSnapshots -> {
            itemList.add(item);
            adapter.notifyDataSetChanged();
        });

    }
}