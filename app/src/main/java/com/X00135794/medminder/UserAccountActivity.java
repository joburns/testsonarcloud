package com.X00135794.medminder;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class UserAccountActivity extends AppCompatActivity {

    private ListView listView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore  db;
    private DocumentReference docRef;
    private User user;
    private FloatingActionButton addMedPageBtn;


    ArrayAdapter<Medication> adapter;
    private static final String TAG = "UserAccountActivity";

    private ArrayList<Medication> medication = new ArrayList<>();
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.document("users/"+ firebaseUser.getUid());

        listView = findViewById(R.id.medListView);

        context = UserAccountActivity.this;
        addMedPageBtn = findViewById(R.id.btnGoToAddPage);


        addMedPageBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(UserAccountActivity.this, AddMedActivity.class));

            }

        });

        // creating and AdapterView.OnItemClick listener. this will listen to your listview
        // and when an item is clicked it will do whatever yo have in the  onItemClick part
        // can be seperated out but I do it all in one
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                 Medication med = medication.get(position);

                 Intent intent = new Intent(UserAccountActivity.this, MedItemActivity.class);
                 intent.putExtra("medName", med.getMedName());
                 intent.putExtra("medDose", med.getDosageString());
                 intent.putExtra("medDesc", med.getDescription());
                 intent.putExtra("medStartDate", med.getStatDate());
                intent.putExtra("rCode", med.getAlarmRequestCodes().get(0));
                startActivity(intent);
            }
        };

        // now set your listviews on click listener to the adapterViewListener you made above
        listView.setOnItemClickListener(adapterViewListener);

    }
   @Override
    protected void onStart(){
        super.onStart();
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(UserAccountActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }

                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);

                    medication = user.getMedList();

                    adapter = new MedicationArrayAdapter(context,0, medication);
                    listView.setAdapter(adapter);

                }
            }
        });

    }

}
