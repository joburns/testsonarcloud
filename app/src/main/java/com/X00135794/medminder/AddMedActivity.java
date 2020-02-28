package com.X00135794.medminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.in;

public class AddMedActivity extends AppCompatActivity {

    EditText imageText;
    ImageView imgPrev;
    EditText medCheckText;

    EditText medName;
    EditText dosageAmount;
    EditText dosageType;
    EditText medDesc;
    EditText medFrq;
    EditText medFrqRate;


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMG_PICK_GALLERY_CODE = 1000;
    private static final int IMG_PICK_CAMERA_CODE = 1001;

    String[] cameraPermission;
    String[] storagePermission;

    Uri img_uri;

    String[] medListCheck = new String[]{"Paracetamol", "Neomercazole", "Insulin", "Antihistamine", "Telfast", "Valtrex","Omeprazole" };

    List<Medication> medListCheck2 = new ArrayList<Medication>(Arrays.asList(
            new Medication("Omeprazole", "pill", 1, "Daily", 1),
        new Medication("Valtrex", "pill", 1, "Daily", 1),
        new Medication("Telfast", "pill", 2, "Daily", 1),
        new Medication("Antihistamine", "pill", 1, "Daily", 4),
        new Medication("Insulin", "Liquid", 10, "Daily", 1),
        new Medication("Neomercazole", "pill", 3, "Daily", 1),
        new Medication("Paracetamol", "pill", 2, "Daily", 4)
    ));


    String[] excludeList = new String[]{"the", "and", "of", "in", "if", "for", "taken", "one", "twice", "two", "three", "four", "times", "daily", "mg", "ml", "keep", "out", "reach", "from", "children"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Click Image button to insert Image");
        imageText = findViewById(R.id.resultEt);
        imgPrev = findViewById(R.id.imageIv);
        medCheckText = findViewById(R.id.checkMed);

        medName = findViewById(R.id.medName);
        dosageType = findViewById(R.id.dosageType);
        dosageAmount = findViewById(R.id.dosageAmount);
        medDesc = findViewById(R.id.medDesc);
        medFrq = findViewById(R.id.medFreq);
        medFrqRate = findViewById(R.id.medFreqRate);

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
                        /*
                        String value = myItem.getValue();
                        for(int c = 0; c < excludeList.length; c++) {

                            medDesc.setText(value);
                            //if (!value.toUpperCase().contains(excludeList[c].toUpperCase())) {
                            if(excludeList[c].toUpperCase().contains(value.toUpperCase())){
                                medDesc.setText(value);
                                medCheckText.setText("here");
                                for (Medication med : medListCheck2) {

                                    medCheckText.setText("here in med loop");
                                    if (value.toUpperCase().equals(med.getMedName().toUpperCase())) {

                                        medCheckText.setText("here in if statement");
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
                      /*
                            if(!excludeList.toString().toUpperCase().contains(value.toUpperCase())){
                            medDesc.setText(value);
                            for( Medication med : medListCheck2){
                                if(value.toUpperCase().equals(med.getMedName().toUpperCase())){
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


                            for(int j=0;j<medListCheck2.size();j++){
                                if(value.toUpperCase().contains(medListCheck[j].toUpperCase())){
                                    medCheckText.setText(medListCheck[j]);
                                    medCheckText.setTextColor(Color.parseColor("#1ac6ff"));
                                }
                            }
                        }


                        }*/

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
    /*
    Toolbar toolbar;
    ProgressBar progressBar;
    Button back;
    SurfaceView camera;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case RequestCameraPermissionID:
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    try {
                        cameraSource.start(camera.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);

        toolbar = findViewById(R.id.toolbarAddMed);
        progressBar= findViewById(R.id.progressBar);
        back = findViewById(R.id.btnBack);

        camera = findViewById(R.id.surfaceView);
        textView = findViewById(R.id.textView1);
        toolbar.setTitle("Add Med");

        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMedActivity.this, UserAccountActivity.class));
            }
        });
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational()){
            Log.w("AddMed","Detector dependencies are not yet available");
        }
        else{
            cameraSource = new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(cameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(640,480)
                    . setRequestedFps(2)
                    .setAutoFocusEnabled(true)
                    .build();

            camera.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try{
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(AddMedActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(camera.getHolder());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0; i<items.size(); i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }

                                textView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    }
     */
}
