package com.X00135794.medminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private TextView userDetails;
    private Button userLogout;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore  db;
    private TextView medicationList;
    private EditText medName;
    private EditText medDose;
    private EditText medFrq;
    private DocumentReference docRef;
    private User user;
    private Button addMedBtn;
    private Button addMedPageBtn;

    private Button cancelAlarm;

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
        //userDetails.setText(firebaseUser.getEmail());
        docRef = db.document("users/"+ firebaseUser.getUid());

        listView = findViewById(R.id.medListView);

        context = UserAccountActivity.this;
        cancelAlarm = findViewById(R.id.btnCancelAlarm);

        /*
        if(!medication.isEmpty()){
            m.setText("Meds isnt empty");
            adapter = new MedicationArrayAdapter(this, medication);
            listView.setAdapter(adapter);
        }
        else{
            m.setText("Meds is empty");
            medication = user.getMedList();
            adapter = new MedicationArrayAdapter(this, medication);
        }
*/
        /*
        userDetails = findViewById(R.id.tvUserDetails);
        userLogout = findViewById(R.id.btnLogout);
        medicationList = findViewById(R.id.tvMedicationList);

        medName = findViewById(R.id.etMedName);
        medDose = findViewById(R.id.etMedDosage);
        medFrq = findViewById(R.id.etMedFrq);

        addMedBtn = findViewById(R.id.btnAddMed);*/
        addMedPageBtn = findViewById(R.id.btnGoToAddPage);

        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent i = new Intent(UserAccountActivity.this, AlarmActivity.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(UserAccountActivity.this,0,i,0);
                alarmMgr.cancel(pendingIntent);
                Toast.makeText(UserAccountActivity.this, "Alarm is cancled", Toast.LENGTH_SHORT).show();

            }
        });

        /*
        userLogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent( UserAccountActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        addMedBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String mName = medName.getText().toString();
                double mDose = Double.parseDouble(medDose.getText().toString());
                double mFrq = Double.parseDouble(medFrq.getText().toString());

                Medication m = new Medication(mName,mDose,mFrq);
                user.addMed(m);

                docRef.update("medList", user.getMedList())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "med added");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });

        */
        addMedPageBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(UserAccountActivity.this, AddMedActivity.class));

            }

        });

        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                 Medication med = medication.get(position);

                 Intent intent = new Intent(UserAccountActivity.this, MedItemActivity.class);
                 intent.putExtra("medName", med.getMedName());
                 intent.putExtra("medDose", med.getDosageString());
                 startActivity(intent);
            }
        };

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

                    //String fName = user.getFirstName();
                    //ArrayList<Medication> meds = user.getMedList();
                    /*String content = fName + "\nMedication List\n";
                    if(!meds.isEmpty()){
                        for(Medication m : meds ){
                            content += m.getMedName() + "\n";
                        }
                    }else{
                        content += "your medication list is empty";
                    }
                    medicationList.setText(content );
                    */

                }
            }
        });


    }

}
