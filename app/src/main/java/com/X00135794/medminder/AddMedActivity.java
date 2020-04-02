package com.X00135794.medminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddMedActivity extends AppCompatActivity {


    private DocumentReference docRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore  db;
    private User user;
    EditText imageText;
    ImageView imgPrev;
    EditText medCheckText;
    Button addBtn;
    private TextInputEditText medName;

    private TextInputEditText dosageAmount;
    private TextInputEditText dosageType;
    private EditText medDesc;
    private TextInputEditText medFrq;
    private TextInputEditText medFrqRate;
    private TimePicker timePicker;

    private Date startDate;
    private Medication med;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMG_PICK_GALLERY_CODE = 1000;
    private static final int IMG_PICK_CAMERA_CODE = 1001;


    private static final String TAG = "AddMedActivity";

    String[] cameraPermission;
    String[] storagePermission;

    Uri img_uri;

    String[] medListCheck = new String[]{"Paracetamol", "Neomercazole", "Insulin", "Antihistamine", "Telfast", "Valtrex","Omeprazole" };

    List<Medication> medListCheck2 = new ArrayList<Medication>(Arrays.asList(
            new Medication("Omeprazole", "pill", 3, "Daily", 1),
        new Medication("Valtrex", "pill", 1, "Daily", 1),
        new Medication("Telfast", "pill", 2, "Daily", 1),
        new Medication("Antihistamine", "pill", 1, "Daily", 4),
        new Medication("Insulin", "Liquid", 10, "Daily", 1),
        new Medication("Neomercazole", "pill", 3, "Daily", 1),
        new Medication("Paracetamol", "pill", 2, "Daily", 4)
    ));

    Object [][] wordToNums = {{"One",1},{"two",2},{"three",3},{"four",4},{"five",5},{"six",6},{"seven",7},{"eight",8},{"nine",9},{"ten",10}};

    String[] excludeList = new String[]{"the", "and", "of", "in", "if", "for", "taken", "one", "twice", "two", "three", "four", "times", "daily", "mg", "ml", "keep", "out", "reach", "from", "children"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Medication");
        actionBar.setSubtitle("Click Image button to insert Image");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.document("users/"+ firebaseUser.getUid());

        imageText = findViewById(R.id.resultEt);
        imgPrev = findViewById(R.id.imageIv);
        medCheckText = findViewById(R.id.checkMed);

        medName = findViewById(R.id.medName);
        dosageType = findViewById(R.id.dosageType);
        dosageAmount = findViewById(R.id.dosageAmount);
        medDesc = findViewById(R.id.medDesc);
        medFrq = findViewById(R.id.medFreq);
        medFrqRate = findViewById(R.id.medFreqRate);

        timePicker = findViewById(R.id.timePicker);



        addBtn = findViewById(R.id.btnAddMedPage);
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        addBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String mName = medName.getText().toString();
                double mDose = Double.parseDouble(dosageAmount.getText().toString());
                String mDoseType = dosageType.getText().toString();
                String medDes = medDesc.getText().toString();
                double mFrq = Double.parseDouble(medFrq.getText().toString());
                String mFrqRate = medFrqRate.getText().toString();

                Calendar c = Calendar.getInstance();
                startDate = c.getTime();
                // using day instead of hours and mins as we would assume they will not restart medication in the same day
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(startDate);
                String medID = mName+date;

                final int requestCode = user.getMedList().size();
                ArrayList<Integer>alarmRequestcodes = new ArrayList<Integer>();
                alarmRequestcodes.add(requestCode);

                med = new Medication(medID,mName,mDoseType,mDose,mFrqRate,mFrq,startDate, medDes, alarmRequestcodes);
                user.addMed(med);

                docRef.update("medList", user.getMedList())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "med added");
                                Calendar calendar = Calendar.getInstance();
                                if(android.os.Build.VERSION.SDK_INT >=23){
                                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                            timePicker.getHour(), timePicker.getMinute(), 0);
                                }else{
                                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                                }

                                setAlarm(calendar.getTimeInMillis(), requestCode);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });



                startActivity(new Intent(AddMedActivity.this, UserAccountActivity.class));
            }
        });

    }

    private void setAlarm(long timeInMillis, int requestCode) {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(this, AlarmActivity.class);

        i.putExtra("medName",medName.getText().toString().trim());
        i.putExtra("medDosage",dosageAmount.getText().toString().trim());
        i.putExtra("medDesc",medDesc.getText().toString().trim());
        i.putExtra("medStartDate",startDate);
        i.putExtra("rCode",requestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,requestCode,i,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.addImg){
            showImageImportDialog();
        }
        if(id == R.id.settings){

            Toast.makeText(this,"Setting", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ForgottenPasswordActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog() {
        // whats going in the dialog box
        String[] items = {"Camera","Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //title
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //for camera option
                if(which == 0){
                    //getting permissions
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        pickCamera();
                    }
                }
                // for camera gallery option
                if(which == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickGallery();
                    }
                }
            }
        });
        dialog.create().show();
    }

    private void pickGallery() {
        Intent intent = new Intent (Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMG_PICK_GALLERY_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(AddMedActivity.this, storagePermission, STORAGE_REQUEST_CODE);
    }


    private boolean checkStoragePermission() {
        boolean StorageResult = ContextCompat.checkSelfPermission(AddMedActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return StorageResult;

    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image To text");
        img_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, img_uri);

        startActivityForResult(cameraIntent, IMG_PICK_CAMERA_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean CamResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        //for better quality image saving to storage first;
        boolean StorageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return CamResult && StorageResult;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickCamera();
                    }
                    else{
                        Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickGallery();
                    }
                    else{
                        Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //handling image result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMG_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
            if (requestCode == IMG_PICK_CAMERA_CODE) {
                CropImage.activity(img_uri)
                        .setGuidelines(CropImageView.Guidelines.ON).start(this);

            }
        }
        //getting image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imgPrev.setImageURI(resultUri);

                // getting a drawable bitmap for text recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imgPrev.getDrawable();
                Bitmap myBitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");

                        for(Text textLine : myItem.getComponents()) {
                            for (Text currentWord : textLine.getComponents()) {
                                for(int c = 0; c < excludeList.length; c++) {
                                    if (!excludeList[c].toUpperCase().contains(currentWord.getValue().toUpperCase())) {
                                        for (Medication med : medListCheck2) {
                                            if (currentWord.getValue().toUpperCase().equals(med.getMedName().toUpperCase())) {

                                                medName.setText(med.getMedName());
                                                dosageType.setText(med.getDosageType());
                                                dosageAmount.setText(med.getDosageString());
                                                medFrqRate.setText(med.getFreqRate());
                                                medFrq.setText(med.getFrequencyString());

                                                medName.setTextColor(Color.parseColor("#1ac6ff"));
                                                dosageType.setTextColor(Color.parseColor("#1ac6ff"));
                                                dosageAmount.setTextColor(Color.parseColor("#1ac6ff"));
                                                medFrq.setTextColor(Color.parseColor("#1ac6ff"));
                                                medFrqRate.setTextColor(Color.parseColor("#1ac6ff"));
                                            }
                                        }
                                    }

                                }
                            }
                        }


                        for(Text textLine : myItem.getComponents()) {

                            if(textLine.getValue().toUpperCase().contains("TO BE TAKEN")){
                                medDesc.setText(textLine.getValue());

                                String dosage = textLine.getComponents().get(0).getValue();
                                medDesc.setText(dosage);
                                for(int n = 0; n < wordToNums.length; n++){
                                    if(dosage.toUpperCase().equals(wordToNums[n][0].toString().toUpperCase())){

                                        dosageAmount.setText(wordToNums[n][1].toString());

                                    }
                                }

                            }
                        }

                        imageText.setText(sb.toString());
                    }
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception err = result.getError();
                Toast.makeText(this, "" + err, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(AddMedActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }

                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);

                }
            }
        });
    }
}
