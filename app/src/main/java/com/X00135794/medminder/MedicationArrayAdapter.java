package com.X00135794.medminder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MedicationArrayAdapter extends ArrayAdapter<Medication> {

    private Context context;
    private List<Medication> medicationList;

    public MedicationArrayAdapter(Context context, int resource, ArrayList<Medication> medObjs){
        super(context, resource, medObjs);
        this.context = context;
        this.medicationList = medObjs;

    }

    public View getView(int position, View convertView, ViewGroup parent){

        // getting the medication
        Medication medication = medicationList.get(position);

        //getting inflater and inflate it
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view  = inflater.inflate(R.layout.medication_row_layout, null);
        //set text for activity
        TextView medName = view.findViewById(R.id.tvMedName);
        medName.setText("Name: " + medication.getMedName());
        return view;





    }
}
