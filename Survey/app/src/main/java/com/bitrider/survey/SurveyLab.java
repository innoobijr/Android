package com.bitrider.survey;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SurveyLab {
    private static SurveyLab sSurveyLab;

    private static List<Survey> mSurveys;

    public static SurveyLab get(Context context){
        if (sSurveyLab == null) {
            sSurveyLab = new SurveyLab(context);
        }
        return sSurveyLab;
    }

    public List<Survey> getmSurveys(){
        return mSurveys;
    }

    public Survey getSurvey(UUID mID) {
        for (Survey survey : mSurveys) {
            if (survey.getmID().equals(mID)) {
                return survey;
            }
        }
        return null;
    }

    public SurveyLab(Context context){
        mSurveys = new ArrayList<>();
        Survey temp = new Survey();
        mSurveys.add(temp);
        temp = new Survey();
        temp.setmQuestion("Are you a good driver?");
        mSurveys.add(temp);

    }
}
