package com.bitrider.survey.NewReport;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bitrider.survey.R;
import com.bitrider.survey.Recyclers.ReportListFragment;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class NewReportFragment extends Fragment implements BlockingStep {

    boolean check = false;
    RadioGroup group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.innogroup, container, false);

        //check = (RadioButton) v.findViewById(R.id.check);
        //check.setId((int)9);
        group = (RadioGroup) v.findViewById(R.id.trafficButtonGroup);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton b = (RadioButton) group.getChildAt(checkedId);
                int size = group.getChildCount();
                Toast.makeText(getContext(), "Count: " + size + ",  CheckId: " + checkedId, Toast.LENGTH_SHORT).show();
                if (checkedId == R.id.trafficButton8){
                    check = true;
                    Snackbar.make(v, "This is the last button", Snackbar.LENGTH_SHORT).show();
                } else {
                    check = false;
                }
            }
        });
        return v;
    }

    @Override
    public VerificationError verifyStep() {
       return null;
    }

    @Override
    public void onSelected(){

    }

    @Override
    @UiThread
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {

        if (check == true){
            callback.getStepperLayout().showProgress("Operation in progress, please wait...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.goToNextStep();
                    callback.getStepperLayout().hideProgress();
                }
            }, 2000L);
        }

    }

    @Override
    public void onBackClicked(final StepperLayout.OnBackClickedCallback callback){
        Toast.makeText(this.getContext(), "Your custom back action. Here you should cancel currently running operations", Toast.LENGTH_SHORT).show();
        callback.goToPrevStep();
    }

    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback callback){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.complete();
            }
        }, 2000L);
    }

    @Override
    public void onError(@NonNull VerificationError error){

    }


}
