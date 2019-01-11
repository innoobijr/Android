package com.bitrider.survey.TrafficState;

import android.arch.lifecycle.LiveData;


import java.util.List;

public interface AsyncResponseTraffic {
    void processFinish(LiveData<List<AutoReport>> output);

}

