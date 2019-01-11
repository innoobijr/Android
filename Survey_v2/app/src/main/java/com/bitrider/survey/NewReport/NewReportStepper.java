package com.bitrider.survey.NewReport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bitrider.survey.R;
import com.stepstone.stepper.StepperLayout;

public class NewReportStepper extends AppCompatActivity {

    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report_stepper);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new NewReportAdapter(getSupportFragmentManager(), this));
    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        Intent replyIntent = new Intent(Intent.ACTION_MAIN);
        setResult( 200, replyIntent);
        finish();
        //super.onDestroy();

    }
}
