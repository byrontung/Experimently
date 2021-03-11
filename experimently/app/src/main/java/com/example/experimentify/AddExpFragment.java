package com.example.experimentify;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class AddExpFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private EditText experimentName;
    private EditText region;
    private EditText date;
    private EditText descriptionBox;
    private EditText minTrialsBox;
    private AppCompatSpinner expType;
    private DatePickerDialog.OnDateSetListener selectDate;
    private OnFragmentInteractionListener listener;
    private CheckBox locationRequiredBox;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String txt = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), txt, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment newExp);
        void onDeletePressed(Experiment current);
        void editItem(Experiment ogItem, Experiment editedItem );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This method creates a new experiment and passes it to the onOkPressed listener.
     */
    private void createExperiment() {
        String name = experimentName.getText().toString();
        String date1 = date.getText().toString();
        String region1 = region.getText().toString();
        String desc = descriptionBox.getText().toString();
        String minTrialsStr = minTrialsBox.getText().toString();
        boolean locationRequired = locationRequiredBox.isChecked();
        int minTrials;

        //If no number is entered, minTrials is set to 0
        if (!minTrialsStr.isEmpty()) {
            /*
                Author: Steve Pierce
                Date published: Aug 4 '15 at 19:43
                License: Attribution-ShareAlike 3.0 Unported
                Link: https://stackoverflow.com/a/3518505

                Used this source to learn how to convert a String to int.
            */
            minTrials = Integer.parseInt(minTrialsBox.getText().toString());
        }
        else {
            minTrials = 0;
        }
        listener.onOkPressed(new Experiment(desc, name, region1, minTrials, date1, locationRequired));
    }

    //TODO fix calendar implementation
    /**
     * This method creates a new calendar fragment for picking a date.
     */
    private void createCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog calendarDialog = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) selectDate, year, month, day);
        calendarDialog.show();
    }

    private void setDate() {
        //String dat = year + "  " + (month + 1) + "  " + dayOfMonth;
        //date.setText(dat);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_exp, null);
        experimentName = view.findViewById(R.id.expName);
        descriptionBox = view.findViewById(R.id.expDescription);
        minTrialsBox = view.findViewById(R.id.minTrials);
        date = view.findViewById(R.id.date);
        region = view.findViewById(R.id.region);
        expType = view.findViewById(R.id.expType);
        locationRequiredBox = view.findViewById(R.id.locationRequiredBox);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.experiments, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expType.setAdapter(adapter);
        expType.setOnItemSelectedListener(this);

        //creating a date picker dialog using DatePickerDialog class
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendar();
            }
        });
        selectDate = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dat = year + "  " + (month + 1) + "  " + dayOfMonth;
                date.setText(dat);
            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Experiment")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createExperiment();
                    }}).create();
    }


}