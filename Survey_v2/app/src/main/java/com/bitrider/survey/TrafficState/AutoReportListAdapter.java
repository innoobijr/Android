package com.bitrider.survey.TrafficState;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bitrider.survey.R;
import com.bitrider.survey.Recyclers.ReportType;
import com.bitrider.survey.listen.Report;
import com.bitrider.survey.timeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import org.w3c.dom.Text;

import java.util.List;

public class AutoReportListAdapter extends RecyclerView.Adapter<AutoReportListAdapter.AutoReportViewHolder> {
    TrafficReportViewModel reportViewModel;
    private DatabaseReference ref;

    private static final String TAG = "AutoReportListAdapter";

    public ReportRankingAdapterListener onCheckListener;


    class AutoReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private RadioGroup mButtonGroup;
        private RadioButton mButtonTwo;
        private RadioButton mButtonThree;
        private TextView mIncidentText;
        private TextView mTimeView;
        ConstraintLayout layout;



        public AutoReportViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTextView = (TextView) itemView.findViewById(R.id.textView18);
            mButtonGroup = (RadioGroup) itemView.findViewById(R.id.trafficButtonGroup);
            mButtonTwo = (RadioButton) itemView.findViewById(R.id.radioButton2);
            mButtonThree = (RadioButton) itemView.findViewById(R.id.radioButton3);
            layout = (ConstraintLayout) itemView.findViewById(R.id.wrapper);
            mTimeView = (TextView) itemView.findViewById(R.id.textView20);
            mIncidentText = (TextView) itemView.findViewById(R.id.textView9);

            /**mButtonGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.buttonGroupViewOnClick(v, getAdapterPosition(), "position");

                }
            });**/

        }

        @Override
        public void onClick(View v) {

        }

    }

    private final LayoutInflater mInflater;
    private List<AutoReport> autoReports;
    GradientDrawable shape;
    private timeFormat convertTime;
    private ReportType autoReportTypes = new ReportType();
    private TrafficReportViewModel viewModel;


    AutoReportListAdapter(Context context, ReportRankingAdapterListener listener, TrafficReportViewModel viewModel ) {
        mInflater = LayoutInflater.from(context);
        this.onCheckListener = listener;
        this.viewModel = viewModel;
    }

    @Override
    public AutoReportListAdapter.AutoReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.autoreports, parent, false);
        return new AutoReportListAdapter.AutoReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AutoReportListAdapter.AutoReportViewHolder holder, final int position) {
        //autoReportViewHolder.mTextView.setText(survey.getQuestion());
        if (autoReports!= null) {
            AutoReport auto = autoReports.get(position);
            String y = auto.getFrom().toUpperCase() + " > " + auto.getTo().toUpperCase();
            holder.mTimeView.setText(convertTime.getTimeAgo(auto.getTime(), holder.mTimeView.getContext()));
            holder.mTextView.setText(y);

            int tag = auto.getTag();
            String key;

            shape = (GradientDrawable) ResourcesCompat.getDrawable(holder.mTextView.getResources(), R.drawable.rounded_corner_red, null);
            switch (tag) {
                case 1:
                    shape.setColor(ContextCompat.getColor(holder.mTextView.getContext(), R.color.red));
                    holder.layout.setBackground(shape);
                    holder.mTextView.setTextColor(ContextCompat.getColor(holder.mTextView.getContext(),
                            R.color.white));

                    //layout.setBackgroundResource(R.color.alert_green);
                    break;
                case 2:
                    shape.setColor(ContextCompat.getColor(holder.mTextView.getContext(), R.color.coloYellow));
                    holder.layout.setBackground(shape);
                    holder.mTextView.setTextColor(ContextCompat.getColor(holder.mTextView.getContext(),
                            R.color.colorPrimary));
                    //layout.setBackgroundResource(R.color.coloYellow);
                    break;
                case 3:
                    shape.setColor(ContextCompat.getColor(holder.mTextView.getContext(), R.color.alert_green));
                    holder.layout.setBackground(shape);
                    holder.mTextView.setTextColor(ContextCompat.getColor(holder.mTextView.getContext(),
                            R.color.colorPrimary));
                    //layout.setBackgroundResource(R.color.coloYellow);
                    break;
                default:
                    shape.setColor(ContextCompat.getColor(holder.mTextView.getContext(), R.color.colorPrimaryDarkTwo));
                    holder.layout.setBackground(shape);
                    holder.mTextView.setTextColor(ContextCompat.getColor(holder.mTextView.getContext(),
                            R.color.colorPrimaryDark));

            }

            if(auto.getChecked() == 1){
                holder.mButtonThree.setChecked(true);
            }
            if(auto.getChecked() == 2){
                holder.mButtonTwo.setChecked(true);
            }

            key = auto.getId();
            Log.i(TAG, "AutoReports " + auto.getId() + auto.getChecked());


            holder.mButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(group.getCheckedRadioButtonId() == checkedId) {
                        int adPosition = holder.getAdapterPosition();
                        Log.i(TAG, "Button changed: " + checkedId + "key: " + key);
                        ref = FirebaseDatabase.getInstance().getReference("trafficreports/" + key);
                        onRadioChange(ref, checkedId, key);
                    }
                    //int realP = group.getCheckedRadioButtonId();
                    //onCheckListener.buttonGroupViewOnClick(group, checkedId, key, adPosition);

                }
            });

        } else {
            holder.mTextView.setText("No Question");
        }
        }

    void setAutoReports(List<AutoReport> mAutoReports) {
        autoReports = mAutoReports;
        //notifyItemRemoved(0);
        //notifyDataSetChanged();
    }

    void removeAutoReports(int i) {
        autoReports.remove(i);

        notifyItemRemoved(i);
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (autoReports != null)
            return autoReports.size();
        else return 0;

    }

    public interface ReportRankingAdapterListener {
        void buttonGroupViewOnClick(View v, int position, String key, int adapterPosition);
        void likeViewOnClick(View v, int position, String key);
        void unlikeViewOnClick(View v, int position, String key);
    }

    private void onRadioChange(DatabaseReference ref, int checkId, String key){
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {


                if (mutableData != null) {
                    AutoReport p = mutableData.getValue(AutoReport.class);

                    int i, j;
                    String k;
                    Log.i(TAG, "Check id: " + checkId +
                            "radio2: " + R.id.radioButton2 + " --- Radio3: " + R.id.radioButton3);

                    if (p == null) {
                        return Transaction.success(mutableData);
                    }

                    if (checkId == R.id.radioButton3) {

                        int new_tag = p.getLikes() + 1;
                        p.setLikes(new_tag);
                        i = 1;
                        j = new_tag;
                        k = key;
                        viewModel.update(i, j, k);

                    } else if (checkId == R.id.radioButton2) {
                        if (p.getLikes() != 0) {
                            int new_tag = p.getLikes() - 1;
                            p.setLikes(new_tag);
                            i = 2;
                            j = new_tag;
                            k = key;
                            viewModel.update(i, j, k);
                        } else {
                            i = 2;
                            j = 0;
                            k = key;
                            viewModel.update(i, j, k);

                        }
                    }
                    mutableData.setValue(p);
                }

                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete (DatabaseError databaseError,boolean b, DataSnapshot
                dataSnapshot){
                    //Transaction completed
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);

                }
        });

    }


}
