package com.bitrider.survey.Recyclers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.bitrider.survey.R;
import com.bitrider.survey.Survey;
import com.bitrider.survey.SurveyListAdapter;
import com.bitrider.survey.SurveyListFragment;
import com.bitrider.survey.SurveyViewModel;
import com.bitrider.survey.listen.Report;
import com.bitrider.survey.listen.ReportViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.dift.ui.SwipeToAction;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ReportListFragment extends Fragment {

    private RecyclerView mSurveyRecyclerView;
    private ReportListAdapter adapter;
    SwipeToAction swipeToAction;
    private static final int mMaxFlingPages = 2;
    private ReportViewModel reportViewModel;
    private static final String TAG = "ReportListFragment";

    public static ReportListFragment newInstance() {
        ReportListFragment fragment = new ReportListFragment();
        return fragment;
    }

    public static ReportListFragment newRecyclerInstance(Context context) {
        ReportListFragment fragment = new ReportListFragment();
        fragment.mSurveyRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_list, container, false);

        mSurveyRecyclerView = (RecyclerView) view
                .findViewById(R.id.survey_recycler_view);
        SlideInRightAnimator animator = new SlideInRightAnimator(new OvershootInterpolator(1f));
        mSurveyRecyclerView.setItemAnimator(animator);
        mSurveyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        SnapToBlock snapToBlock = new SnapToBlock(mMaxFlingPages);
        snapToBlock.setmSnapBlockCallback(new SnapToBlock.SnapBlockCallback() {
            @Override
            public void onBlockSnap(int snapPosition) {

            }

            @Override
            public void onBlockSnapped(int snapPosition) {

            }
        });
        snapToBlock.attachToRecyclerView(mSurveyRecyclerView);

        //updateUI();

        adapter = new ReportListAdapter(getActivity());
        //mSurveyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        //alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        mSurveyRecyclerView.setAdapter(adapter);

        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        reportViewModel.getReports().observe(ReportListFragment.this, new Observer<List<Report>>() {
            @Override
            public void onChanged(@Nullable List<Report> reports) {
                adapter.setReports(reports);
            }
        });

        /**try {
            reportViewModel.getReportLiveData().observe(this, new Observer<List<Report>>() {
                @Override
                public void onChanged(@Nullable final List<Report> reports) {
                    // Update the cached copy of the words in the adapter.
                    Log.i(TAG, "I sense a change in Reports");// Log
                    reportViewModel.getReports().observe(ReportListFragment.this, new Observer<List<Report>>() {
                        @Override
                        public void onChanged(@Nullable List<Report> reports) {
                            adapter.setReports(reports);
                        }
                    });
                    //This is where we process what is displayed in the front recycler
                    //Currently tied to teh view model and not the D
                    //adapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {

        }**/
        return view;

    }


}

