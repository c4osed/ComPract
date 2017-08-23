package com.zoazh.le.ComPract.controller.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.sub.EditProfileActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.database.User;

public class ProfileActivity extends BaseActivity {

    FirebaseAuth cAuth = FirebaseAuth.getInstance();

    ImageView cImageViewProfilePicture;

    ConstraintLayout cLayoutAbout;
    ConstraintLayout cLayoutNative;
    ConstraintLayout cLayoutLearn;

    TextView cStudyLevelResult;
    TextView cAdviseLevelResult;
    TextView cFollowingResult;
    TextView cFollowerResult;
    TextView cAboutResult;
    TextView cNaviveResult;
    TextView cLearnResult;
    TextView cAnswerResult;
    TextView cQuestionResult;

    Button cButtonAbout;
    Button cButtonSetting;

    ConstraintLayout cBottomBar;
    ConstraintLayout cLayoutAdvise;
    ConstraintLayout cLayoutSearch;
    ConstraintLayout cLayoutProfile;
    ImageView cImageViewProfile;
    TextView cTextProfile;

    String clistLearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cImageViewProfilePicture = (ImageView) findViewById(R.id.ImageViewProfilePicture);
        cStudyLevelResult = (TextView) findViewById(R.id.TextStudentLevelResult);
        cAdviseLevelResult = (TextView) findViewById(R.id.TextAdvisorLevelResult);
        cFollowingResult = (TextView) findViewById(R.id.TextFollowingResult);
        cFollowerResult = (TextView) findViewById(R.id.TextFollowerResult);
        cAboutResult = (TextView) findViewById(R.id.TextAboutResult);
        cNaviveResult = (TextView) findViewById(R.id.TextNativeResult);
        cLearnResult = (TextView) findViewById(R.id.TextLearnResult);
        cAnswerResult = (TextView) findViewById(R.id.TextAnswerResult);
        cQuestionResult = (TextView) findViewById(R.id.TextQuestionResult);
        cLayoutAbout = (ConstraintLayout) findViewById(R.id.LayoutAbout);
        cLayoutLearn = (ConstraintLayout) findViewById(R.id.LayoutLearn);

        cButtonAbout = (Button) findViewById(R.id.ButtonAbout);
        cButtonSetting = (Button) findViewById(R.id.ButtonSetting);

        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                MyClass mc = new MyClass();
                cStudyLevelResult.setText(user.studentLevel + "");
                cAdviseLevelResult.setText(user.advisorLevel + "");
                cFollowingResult.setText(user.followingCount + "");
                cFollowerResult.setText(user.followerCount + "");
                cAboutResult.setText(user.about);
                cNaviveResult.setText(user.nativeLanguage);
                clistLearn = user.learnFull;
                if (user.learnAbbreviation.length() > 8) {
                    cLearnResult.setText((user.learnAbbreviation.substring(0, 9) + "...").replace(",", ", "));
                } else if (user.learnAbbreviation.length() == 2) {
                    cLearnResult.setText(user.learnFull);
                } else {
                    cLearnResult.setText(user.learnAbbreviation);
                }
                cAnswerResult.setText(user.answerCount + "");
                cQuestionResult.setText(user.questionCount + "");
                mc.SetImage(ProfileActivity.this, cImageViewProfilePicture, user.profilePicture, user.email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        cBottomBar = (ConstraintLayout) findViewById(R.id.BottomBar);
        cLayoutAdvise = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutAdvise);
        cLayoutSearch = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutSearch);
        cLayoutProfile = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutProfile);
        cImageViewProfile = (ImageView) cBottomBar.findViewById(R.id.ImageViewProfile);
        cTextProfile = (TextView) cBottomBar.findViewById(R.id.TextProfile);

        cTextProfile.setTextColor(getResources().getInteger(R.color.secondary));
        cImageViewProfile.setColorFilter(getResources().getInteger(R.color.secondary));

        cImageViewProfile.setOnClickListener(clickListener);
        cLayoutAbout.setOnClickListener(clickListener);
        cLayoutLearn.setOnClickListener(clickListener);
        cButtonAbout.setOnClickListener(clickListener);
        cButtonSetting.setOnClickListener(clickListener);

        cLayoutAdvise.setOnClickListener(clickListener);
        cLayoutSearch.setOnClickListener(clickListener);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        OnlineTimer(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        OnlineTimer(false);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ImageViewProfile:
                    break;
                case R.id.LayoutAbout:
                    editAbout();
                    break;
                case R.id.LayoutLearn:
                    listLearn();
                    break;
                case R.id.ButtonAbout:
                    Logout();
                    break;
                case R.id.ButtonSetting:
                    startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                    break;

                case R.id.LayoutAdvise:
                    startActivity(new Intent(ProfileActivity.this, AdviseActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    break;
                case R.id.LayoutSearch:
                    startActivity(new Intent(ProfileActivity.this, SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    break;
            }
        }
    };

    private void editAbout(){

    }

    private void listLearn() {
        AlertDialog.Builder listLearn = new AlertDialog.Builder(ProfileActivity.this);
        listLearn.setItems(clistLearn.split(","), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        listLearn.show();
    }
}
