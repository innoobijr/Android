package com.bitrider.survey.SignIn;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bitrider.survey.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class SignStepNameFragment extends Fragment implements BlockingStep {

    boolean check = false;
    RadioGroup group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.sign_in_one, container, false);

        //check = (RadioButton) v.findViewById(R.id.check);
        //check.setId((int)9);
        ImageButton b = (ImageButton) v.findViewById(R.id.entryButton);
        EditText name = (EditText) v.findViewById(R.id.input_email);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                name.clearFocus();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                b.callOnClick();
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        callback.getStepperLayout().showProgress("Operation in progress, please wait...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToNextStep();
                callback.getStepperLayout().hideProgress();
            }}, 2000L);

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
