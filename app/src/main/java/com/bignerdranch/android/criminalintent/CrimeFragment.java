package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 1;
    private static final int REQUEST_TIME = 2;

    private Crime mCrime;
    //private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private EditText mDescriptionText;
    private EditText mSolvedText;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        if (crimeId != null) {
            mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        final Spinner mTitleSpinner = (Spinner) v.findViewById(R.id.crime_title);
        String compareValue = mCrime.getTitle();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.crimes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTitleSpinner.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            mTitleSpinner.setSelection(spinnerPosition);
        }
        mTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int crime, long l) {
                if(!(adapterView.getItemAtPosition(crime).equals("Select a crime:"))) {
                    updateCrime();
                    mCrime.setTitle(mTitleSpinner.getSelectedItem().toString());
                    Toast.makeText(adapterView.getContext(),"Selected: "+mCrime.getTitle(), Toast.LENGTH_SHORT).show();
                    updateCrime();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(adapterView.getContext(),"Selected: "+mCrime.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        mDescriptionText = (EditText)v.findViewById(R.id.crime_description);
        mDescriptionText.setText(mCrime.getDescription());
        mDescriptionText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence description, int start, int before, int count) {
                mCrime.setDescription(description.toString());
                updateCrime();
            }

            @Override
            public void beforeTextChanged(CharSequence c, int start, int count,
                                          int after) {
                // left blank
            }

            @Override
            public void afterTextChanged(Editable c) {
                // left blank
            }
        });
        mSolvedText = (EditText)v.findViewById(R.id.solved_description);
        mSolvedText.setText(mCrime.getSolvedDescription());
        mSolvedText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence solvedDescription, int start, int before, int count) {
                mCrime.setSolvedDescription(solvedDescription.toString());
                updateCrime();
            }

            @Override
            public void beforeTextChanged(CharSequence  solvedDescription, int start, int count,
                                          int after) {
                // left blank
            }

            @Override
            public void afterTextChanged(Editable  solvedDescription) {
                // left blank
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        updateCrime();
        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mCrime.getTime());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });
        updateCrime();

        CheckBox mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }


    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private void updateCrime() {
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        if (requestCode == REQUEST_TIME) {
            Date time = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setTime(time);
            updateDate();
        }
    }

    private void updateDate() {
        String dateFormat = "EEE, MMM dd";
        String timeFormat = "h:mm a";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String timeString = DateFormat.format(timeFormat, mCrime.getTime()).toString();
        mDateButton.setText(dateString);
        mTimeButton.setText(timeString);
    }

    /*private void updateDate() {
        Date date = mCrime.getDate();
        Date time = mCrime.getTime();
        CharSequence dateFormat = DateFormat.format("EEEE, MMM, dd, yyyy", date);
        CharSequence timeFormat = DateFormat.format("h:mm a", time);
        mDateButton.setText(dateFormat);
        mTimeButton.setText(timeFormat);
    }*/
    private String getCrimeReport(){
        String solvedStatus = null;
        if (mCrime.isSolved()){
            solvedStatus = getString(R.string.crime_report_solved);
        }
        else{
            solvedStatus = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEEE, MMM dd";
        String date = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else{
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,  mCrime.getTitle(), date, solvedStatus, suspect);


        return report;
    }

}