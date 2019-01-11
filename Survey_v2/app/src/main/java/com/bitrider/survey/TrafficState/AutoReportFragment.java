package com.bitrider.survey.TrafficState;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bitrider.survey.ActivityListener.Networking.LoginToFirebase;
import com.bitrider.survey.ActivityListener.Networking.LoginToFirebaseWToken;
import com.bitrider.survey.ActivityListener.Views.BoostedRecyclerView;
import com.bitrider.survey.FeedView;
import com.bitrider.survey.QueryPreferences;
import com.bitrider.survey.R;
import com.bitrider.survey.Recyclers.SnapToBlock;
import com.bitrider.survey.StartFragment;
import com.bitrider.survey.Survey;
import com.bitrider.survey.SurveyListAdapter;
import com.bitrider.survey.SurveyListFragment;
import com.bitrider.survey.SurveyViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.security.AuthProvider;
import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static android.os.SystemClock.sleep;

public class AutoReportFragment extends Fragment {
    private BoostedRecyclerView mReportRecyclerView;
    private final static String TAG = AutoReportFragment.class.getSimpleName();
    private AutoReportListAdapter adapter;
    FirebaseAuth mAuth;
    private TrafficReportViewModel autoReportViewModel;
    private DatabaseReference ref;
    List mReports;
    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;
    private static final int mMaxFlingPages = 5;
    //LoginToFirebase lg;
    LoginToFirebaseWToken lg;
    ConnectivityManager cm;
    private ShimmerFrameLayout shimmerViewContainer;
    RecyclerView.LayoutManager lm;


    static View view;
    View view2;
    ViewGroup container;
    private int progressStatus = 0;
    ProgressBar P;
    Handler handler;


    public static AutoReportFragment newInstance(){
       AutoReportFragment fragment = new AutoReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mReports = new ArrayList();
        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        lg = new LoginToFirebaseWToken();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup c, Bundle savedInstanceState){
        view =  inflater.inflate(R.layout.fragment_survey_list2, c, false);
        this.container = c;
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Snackbar.make(view, "I see you", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        /**handler = new Handler();

        P = view.findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            P.setProgress(progressStatus);
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();**/
        return view;

    }
    public void setNormalRecycler(){
        shimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        View v2 = getLayoutInflater().inflate(R.layout.fragment_survey_list2, null, true);

        mReportRecyclerView = (BoostedRecyclerView) v2
                .findViewById(R.id.survey_recycler_view);
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        mReportRecyclerView.setItemAnimator(animator);

        SnapToBlock snapToBlock = new SnapToBlock(mMaxFlingPages);
        snapToBlock.setmSnapBlockCallback(new SnapToBlock.SnapBlockCallback() {
            @Override
            public void onBlockSnap(int snapPosition) {

            }

            @Override
            public void onBlockSnapped(int snapPosition) {

            }
        });
        //mSurveyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ImageButton refresh = (ImageButton) v2.findViewById(R.id.refreshButton);


        //updateUI();


        //Adapted code for adapter to include reference to listener to the button group
        AutoReportListAdapter.ReportRankingAdapterListener listener= new AutoReportListAdapter.ReportRankingAdapterListener() {
            @Override
            public void buttonGroupViewOnClick(View v, int position, String key, int adPosition) {
                Log.e(TAG, "Position: " + position + "Key: " + key + "Adapter: " + adPosition);

                /**RadioButton b = (RadioButton) v.findViewById(position);
                 Toast.makeText(getContext(), "Position: " + position, Toast.LENGTH_SHORT).show();
                 ref = FirebaseDatabase.getInstance().getReference("trafficreports/" + key);
                 onRadioChange(ref, position, adPosition, key);**/
            }

            @Override
            public void likeViewOnClick(View v, int position, String key) {
            }

            @Override
            public void unlikeViewOnClick(View v, int position, String key) {

            }
        };

        mReportRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        snapToBlock.attachToRecyclerView(mReportRecyclerView);
        v2.findViewById(R.id.banner2).bringToFront();
        view2 = v2.findViewById(R.id.placer2);
        view2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDarkTwo));
        View emptyView = v2.findViewById(R.id.placer);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                Snackbar.make(v2, "On Move", Snackbar.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT){
                    Snackbar.make(v2, "Swiped Left", Snackbar.LENGTH_SHORT).show();

                } else {
                    Snackbar.make(v2, "Swiped Right", Snackbar.LENGTH_SHORT).show();
                }
                int i = viewHolder.getAdapterPosition();
                adapter.removeAutoReports(i);
                //adapter.notifyItemChanged(i);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mReportRecyclerView);

        autoReportViewModel = ViewModelProviders.of(AutoReportFragment.this).get(TrafficReportViewModel.class);


        LiveData<List<AutoReport>> reportLiveDataOne = autoReportViewModel.getReportLiveData();

        reportLiveDataOne.observe(AutoReportFragment.this, new Observer<List<AutoReport>>() {
            @Override
            public void onChanged(@Nullable List<AutoReport> autoReports) {
                Log.e(TAG, "Is this working?");


            }
        });

        adapter = new AutoReportListAdapter(getActivity(),listener, autoReportViewModel);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setAutoReports(mReports);
                mReportRecyclerView.swapAdapter(adapter, true);
                mReportRecyclerView.setAdapter(adapter);

            }
        });

        try {
            autoReportViewModel.query(P, getContext()).observe(AutoReportFragment.this, new Observer<List<AutoReport>>() {
                @Override
                public void onChanged(@Nullable final List<AutoReport> autoReports) {
                    // Update the cached copy of the words in the adapter.
                    mReports = autoReports;
                    if (adapter.getItemCount() == 0){
                        adapter.setAutoReports(mReports);
                        mReportRecyclerView.setAdapter(adapter);
                    }
                    Log.e(TAG, "mreports is: " + mReports.size());
                    TabLayout feedTabsLayout = (TabLayout) getActivity().findViewById(R.id.shimmy);
                    FeedView feed1 = (FeedView) feedTabsLayout.getTabAt(0).getCustomView();
                    TextView notif = (TextView) feed1.findViewById(R.id.notif_num);
                    notif.setText(""+mReports.size());
                    //adapter.setAutoReports(mReports);
                    //adapter.setAutoReports(autoReports);
                    int count = adapter.getItemCount();
                    if (count == 0) {
                        @SuppressWarnings("deprecation")
                        boolean isNetworkAvailable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;
                        //container.removeAllViews();
                        if (!isNetworkAvailable) {
                            //View views = inflater.inflate(R.layout.no_list_available, container, false);
                            //container.addView(views);
                            //container.bringChildToFront(views);
                        }
                    } else {
                        container.bringChildToFront(view);
                    }
                    //if (count == 0){ adapter.getItemViewType(1);};

                    Toast.makeText(getContext(),"The count is: " + count, Toast.LENGTH_SHORT).show();
                    //adapter.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "This isnt working");

        }

        adapter.setAutoReports(mReports);
        Log.e(TAG, "The size of mReports is: " + mReports.size());
        mReportRecyclerView.setAdapter(adapter);
        mReportRecyclerView.setEmptyView(view2);

        container.removeAllViews();
        container.addView(v2, 0);
        view = container.getChildAt(0);






        //mReportRecyclerView.setAdapter(adapter);
        //mReportRecyclerView.setEmptyView(view2);



    }

    public void startFragment(){
        if(getUserVisibleHint()){
            Log.e(TAG, "It is visible");

        } else {
            Log.e(TAG, "It is invisible");

        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.e(TAG, "IS mee the user: " + currentUser);
        String token = QueryPreferences.getToken(getContext());
        Log.e(TAG, "IS mee again: "+(boolean)lg.loginToFirebaseWToken(token, currentUser).get(0));
        if (currentUser == null ){
            if((boolean)lg.loginToFirebaseWToken(token, currentUser).get(0)){
                Toast.makeText(getContext(), "User is now logged-in", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "User is not logged-in", Toast.LENGTH_SHORT).show();
                logInUser();
            };
            Toast.makeText(getContext(), "User is null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "User is not null", Toast.LENGTH_SHORT).show();
            boolean isNetworkAvailable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;
             if (isNetworkAvailable){
                 Toast.makeText(getContext(), "Network is avail", Toast.LENGTH_SHORT).show();
                 setNormalRecycler();
             } else {
                 Toast.makeText(getContext(), "Network is not avail", Toast.LENGTH_SHORT).show();
                 setNetworkError();

             }

        }
    }

    public void logInUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = getString(R.string.firebase_email);
        String password = getString(R.string.firebase_password);

        View vi = getLayoutInflater().inflate(R.layout.no_login, null, true);
        Log.e(TAG, "Child Count: " + container.getChildCount());
        int count = (int) container.getChildCount();
        //v.removeAllViews();
        //v.removeView(getView());
        container.removeAllViews();
        container.addView(vi, 0);
        view = container.getChildAt(0);
        Button b = (Button) view.findViewById(R.id.login_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = QueryPreferences.getToken(getContext());
                if((boolean)lg.loginToFirebaseWToken(token, currentUser).get(0)){
                    setNormalRecycler();
                } else {
                    Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void setNetworkError(){
        View vi = getLayoutInflater().inflate(R.layout.error_layout, null, true);
        Log.e(TAG, "Child Count: " + container.getChildCount());
        int count = (int) container.getChildCount();
        //v.removeAllViews();
        //v.removeView(getView());
        container.removeAllViews();
        container.addView(vi, 0);
        view = container.getChildAt(0);

        Button try_again = view.findViewById(R.id.doit);
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(view, "This is the try again button", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        startFragment();

       /** Log.e(TAG, "Child Count: " + container.getChildCount());
        container.getChildCount();

        boolean isNetworkAvailable = cm.getBackgroundDataSetting() && cm.getActiveNetworkInfo() != null;


        if (!isNetworkAvailable) {
            View vi = getLayoutInflater().inflate(R.layout.no_list_available, null, true);
            Log.e(TAG, "Child Count: " + container.getChildCount());
            int count = (int) container.getChildCount();
            //v.removeAllViews();
            //v.removeView(getView());
            container.addView(vi, 0);
            if (count >= 1) {
                container.removeViewAt(1);
            }
        } else {
                if (getView().getId() != R.id.no_content) {
                    View v2 = getLayoutInflater().inflate(R.layout.fragment_survey_list2, null, true);
                    Log.e(TAG, "Child Count: " + container.getChildCount());
                    int count2 = (int) container.getChildCount();
                    //v.removeAllViews();
                    //v.removeView(getView());
                    container.addView(v2, 0);
                    if (count2 >= 1) {
                        container.removeViewAt(1);

                    }
                }

            }

        //v.setVisibility(View.GONE);**/
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // User is viewing the fragment,
            // or fragment is inside the screen
            Log.e(TAG, "It is visible");
        }
        else {
            // User is not viewing the fragment,
            // or fragment is our of the screen
            Log.e(TAG, "It is not visible");

        }
    }

    public static void wordFromPapa(boolean visible){
        Log.e(TAG, "it is vi: EEEEEEEEEE");
        Snackbar.make(view, "It is visible", Snackbar.LENGTH_SHORT).show();

    }

    private void onRadioChange(DatabaseReference ref, int checkId, int adapterPosition, String key){
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                AutoReport p = mutableData.getValue(AutoReport.class);
                int i, j;
                String k;
                Log.i(TAG, "Check id: " + checkId +
                "radio2: " + R.id.radioButton2 + " --- Radio3: " + R.id.radioButton3);

                if(p == null){
                    return Transaction.success(mutableData);
                }

                if(checkId == R.id.radioButton3) {

                    int new_tag = p.getTag()+1;
                    p.setTag(new_tag);
                    i = 1;
                    j = new_tag;
                    k = key;
                    autoReportViewModel.update(i, j, k);

                } else if (checkId == R.id.radioButton2){
                    if(p.getTag() != 0){
                        int new_tag = p.getTag() - 1;
                        p.setTag(new_tag);
                        i = 2;
                        j = new_tag;
                        k = key;
                        autoReportViewModel.update(i, j, k);
                    } else {
                        i = 2;
                        j = 0;
                        k = key;
                        autoReportViewModel.update(i, j, k);

                    }
                }

                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                //Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);

            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        //shimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause(){
        //shimmerViewContainer.stopShimmer();
        super.onPause();
    }
}
