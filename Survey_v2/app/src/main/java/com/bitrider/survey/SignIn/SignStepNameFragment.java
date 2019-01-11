package com.bitrider.survey.SignIn;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bitrider.survey.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class SignStepNameFragment extends Fragment implements BlockingStep {

    boolean check = false;
    RadioGroup group;
    StepperLayout.OnNextClickedCallback mCallback;
    StepperLayout mStepperLayout;
    TextInputEditText name;
    private final static String TAG = SignStepNameFragment.class.getSimpleName();

    public static SignStepNameFragment newInstance() {
        SignStepNameFragment fragment = new SignStepNameFragment();
        return fragment;
    }

    DataManager dataManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DataManager) {
            dataManager = (DataManager) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DataManager");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dataManager = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.sign_in_one, container, false);
        mStepperLayout = (StepperLayout) getActivity().findViewById(R.id.stepperLayout);
        mStepperLayout.setNextButtonEnabled(false);
        //check = (RadioButton) v.findViewById(R.id.check);
        //check.setId((int)9);
        v.findViewById(R.id.main1).setVisibility(View.VISIBLE);
        ImageButton b = (ImageButton) v.findViewById(R.id.entry99);
        name = (TextInputEditText) v.findViewById(R.id.input_email);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                name.clearFocus();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                b.callOnClick();
                mStepperLayout.setNextButtonEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    @Override
    public VerificationError verifyStep() {
        if (TextUtils.isEmpty(name.getText())){
            return new VerificationError("Please enter a name.");
        }
        return null;
    }

    @Override
    public void onSelected(){

    }



    @Override
    @UiThread
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String i = name.getText().toString();
                dataManager.saveName(i);
                Log.i(TAG, "Name111:" + i);
                Log.i(TAG, "Name111: gurry");
                callback.goToNextStep();
                callback.getStepperLayout().hideProgress();
            }
            }, 500L);
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
        Snackbar.make(getView(), error.getErrorMessage(),Snackbar.LENGTH_SHORT).show();
    }



}
