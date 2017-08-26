package com.zoazh.le.ComPract.controller.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.sub.CreateQuestionActivity;
import com.zoazh.le.ComPract.model.BaseActivity;

public class PracticeActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private ProgressDialog cProgress;


    private ConstraintLayout cBottomBar;
    private ConstraintLayout cLayoutPractice;
    private ImageView cImageViewPractice;
    private ConstraintLayout cLayoutAdvise;
    private ConstraintLayout cLayoutSearch;
    private ConstraintLayout cLayoutProfile;
    private TextView cTextPractice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);


        //BTM BAR
        cBottomBar = (ConstraintLayout) findViewById(R.id.BottomBar);
        cLayoutPractice = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutPractice);
        cImageViewPractice = (ImageView) cBottomBar.findViewById(R.id.ImageViewPractice);
        cLayoutAdvise = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutAdvise);
        cLayoutSearch = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutSearch);
        cLayoutProfile = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutProfile);
        cTextPractice = (TextView) cBottomBar.findViewById(R.id.TextPractice);

        cTextPractice.setTextColor(getResources().getInteger(R.color.secondary));
        cImageViewPractice.setColorFilter(getResources().getInteger(R.color.secondary));

        //OnClick
        cLayoutAdvise.setOnClickListener(clickListener);
        cLayoutSearch.setOnClickListener(clickListener);
        cLayoutProfile.setOnClickListener(clickListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        OnlineTimer(true);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        OnlineTimer(false);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (getCurrentFocus() != null) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            }
            switch (v.getId()) {
                case R.id.LayoutAdvise:
                    startActivity(new Intent(PracticeActivity.this, AdviseActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.LayoutSearch:
                    startActivity(new Intent(PracticeActivity.this, SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.LayoutProfile:
                    startActivity(new Intent(PracticeActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
            }
        }
    };


    private void Loading() {
        cProgress = new ProgressDialog(this);
        cProgress.setTitle("Logging in");
        cProgress.setMessage("Please wait...");
        cProgress.setCancelable(false);
        cProgress.show();
    }

}
