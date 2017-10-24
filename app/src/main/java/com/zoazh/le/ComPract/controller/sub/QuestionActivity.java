package com.zoazh.le.ComPract.controller.sub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.main.PracticeActivity;
import com.zoazh.le.ComPract.controller.main.ProfileActivity;
import com.zoazh.le.ComPract.controller.main.SearchActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.database.Mission;
import com.zoazh.le.ComPract.model.database.User;

import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;

public class QuestionActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private ProgressDialog cProgress;


    private ImageButton cImageButtonCreateQuestion;

    private ConstraintLayout cBottomBar;
    private ConstraintLayout cLayoutPractice;
    private ConstraintLayout cLayoutAdvise;
    private ImageView cImageViewAdvise;
    private ConstraintLayout cLayoutSearch;
    private ConstraintLayout cLayoutProfile;
    private TextView cTextAdvise;

    private ImageView cImageViewAuthor;
    private TextView cTextAuthor;
    private TextView cTextQuestionType;
    private TextView cTextQuestion;

    private ImageView cImageViewQuestion;

    private ConstraintLayout cLayoutNormalQuestion;
    private ConstraintLayout cLayoutChoiceQuestion;

    private TextView cInputAnswerQuestion;
    private String cRadioAnswer;
    private RadioButton cRadioChoiceA;
    private RadioButton cRadioChoiceB;
    private RadioButton cRadioChoiceC;
    private RadioButton cRadioChoiceD;

    private ImageView cImageButtonSendAnswerNormal;
    private ImageView cImageButtonSendAnswerChoice;

    HashMap<String, String> map;

    String cQuestionID;
    String cQuestionAuthorID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        cImageViewAuthor = (ImageView) findViewById(R.id.ImageViewAuthor);
        cTextAuthor = (TextView) findViewById(R.id.TextViewAuthor);
        cTextQuestionType = (TextView) findViewById(R.id.TextViewQuestionType);
        cTextQuestion = (TextView) findViewById(R.id.TextViewQuestion);
        cImageViewQuestion = (ImageView) findViewById(R.id.ImageViewQuestion);
        cLayoutNormalQuestion = (ConstraintLayout) findViewById(R.id.LayoutNormalQuestion);
        cLayoutChoiceQuestion = (ConstraintLayout) findViewById(R.id.LayoutChoiceQuestion);
        cInputAnswerQuestion = (EditText) findViewById(R.id.InputAnswerQuestion);
        cRadioChoiceA = (RadioButton) findViewById(R.id.RadioChoiceA);
        cRadioChoiceB = (RadioButton) findViewById(R.id.RadioChoiceB);
        cRadioChoiceC = (RadioButton) findViewById(R.id.RadioChoiceC);
        cRadioChoiceD = (RadioButton) findViewById(R.id.RadioChoiceD);

        cImageButtonSendAnswerNormal = (ImageView) findViewById(R.id.ImageButtonSendAnswerNormal);
        cImageButtonSendAnswerChoice = (ImageView) findViewById(R.id.ImageButtonSendAnswerChoice);

        cRadioChoiceA.setOnClickListener(clickListener);
        cRadioChoiceB.setOnClickListener(clickListener);
        cRadioChoiceC.setOnClickListener(clickListener);
        cRadioChoiceD.setOnClickListener(clickListener);

        cImageButtonSendAnswerNormal.setOnClickListener(clickListener);
        cImageButtonSendAnswerChoice.setOnClickListener(clickListener);

        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        MyClass mc = new MyClass();
        mc.SetImage(getApplicationContext(), cImageViewAuthor, map.get("AuthorPicture"), map.get("AuthorID"));
        cTextAuthor.setText(map.get("AuthorName"));
        cTextQuestionType.setText(map.get("QuestionLanguage") + " (" + map.get("QuestionType") + ")");
        cTextQuestion.setText("\t\t\t\t" + map.get("Question"));
        cQuestionID = map.get("QuestionID");
        cQuestionAuthorID = map.get("AuthorID");
        Picasso.with(getApplicationContext()).load(map.get("QuestionPicture")).into(cImageViewQuestion);

        if (map.get("QuestionType").equals("Multiple Choice")) {
            cLayoutChoiceQuestion.setVisibility(View.VISIBLE);
            cRadioChoiceA.setText("  a )  " + map.get("ChoiceA"));
            cRadioChoiceB.setText("  b )  " + map.get("ChoiceB"));
            cRadioChoiceC.setText("  c )  " + map.get("ChoiceC"));
            cRadioChoiceD.setText("  d )  " + map.get("ChoiceD"));
        } else {
            cLayoutNormalQuestion.setVisibility(View.VISIBLE);
        }
        cRadioChoiceA.setChecked(false);


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
                case R.id.ImageButtonCreateQuestion:
                    startActivity(new Intent(QuestionActivity.this, CreateQuestionActivity.class));
                    break;
                case R.id.LayoutPractice:
                    startActivity(new Intent(QuestionActivity.this, PracticeActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.LayoutSearch:
                    startActivity(new Intent(QuestionActivity.this, SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.animห.move_out_left);
                    break;
                case R.id.LayoutProfile:
                    startActivity(new Intent(QuestionActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.RadioChoiceA:
                    cRadioAnswer = "a";
                    cRadioChoiceB.setChecked(false);
                    cRadioChoiceC.setChecked(false);
                    cRadioChoiceD.setChecked(false);
                    break;
                case R.id.RadioChoiceB:
                    cRadioAnswer = "b";
                    cRadioChoiceA.setChecked(false);
                    cRadioChoiceC.setChecked(false);
                    cRadioChoiceD.setChecked(false);
                    break;
                case R.id.RadioChoiceC:
                    cRadioAnswer = "c";
                    cRadioChoiceA.setChecked(false);
                    cRadioChoiceB.setChecked(false);
                    cRadioChoiceD.setChecked(false);
                    break;
                case R.id.RadioChoiceD:
                    cRadioAnswer = "d";
                    cRadioChoiceA.setChecked(false);
                    cRadioChoiceB.setChecked(false);
                    cRadioChoiceC.setChecked(false);
                    break;
                case R.id.ImageButtonSendAnswerNormal:
                    sendAnswer();
                    break;
                case R.id.ImageButtonSendAnswerChoice:
                    sendAnswer();
                    break;

            }


        }
    };

    private void sendAnswer() {
        if (cInputAnswerQuestion.getText() != null) {
            cDatabaseRef.child("answer").child(cQuestionAuthorID).child(cQuestionID).child(cAuth.getCurrentUser().getUid()).child("ASCAnswerTime").setValue(new Date().getTime());
            cDatabaseRef.child("answer").child(cQuestionAuthorID).child(cQuestionID).child(cAuth.getCurrentUser().getUid()).child("DESCAnswerTime").setValue(new Date().getTime() * -1);

            if (("" + map.get("QuestionType")).equals("Multiple Choice")) {
                cDatabaseRef.child("answer").child(cQuestionAuthorID).child(cQuestionID).child(cAuth.getCurrentUser().getUid()).child("Answer").setValue(cRadioAnswer);
                cDatabaseRef.child("answer").child(cQuestionAuthorID).child(cQuestionID).child(cAuth.getCurrentUser().getUid()).child("Correct").setValue(map.get("Answer"));
            } else {
                cDatabaseRef.child("answer").child(cQuestionAuthorID).child(cQuestionID).child(cAuth.getCurrentUser().getUid()).child("Answer").setValue(cInputAnswerQuestion.getText() + "");

            }
        } else {
            Toast.makeText(QuestionActivity.this, "Please Answer The Question First", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(QuestionActivity.this, "The Answer has been sent!", Toast.LENGTH_LONG).show();
        cDatabaseRef.child("mission").child(cAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Mission mission = dataSnapshot.getValue(Mission.class);
                try {
                    if (!mission.missionAnswer) {
                        DoneMission();
                    }
                } catch (Exception ex) {
                    DoneMission();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        startActivity(new Intent(QuestionActivity.this, PracticeActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        finish();
    }

    private void DoneMission() {

//        ValueEventListener listen = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                int studentEXP = user.studentEXP + 20;
//
//                Toast.makeText(getApplicationContext(),"+20 EXP - You have done mission!!!",Toast.LENGTH_LONG).show();
//
//                if (studentEXP >= 100) {
//                    studentEXP -= 100;
//                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("studentLevel").setValue(user.studentLevel + 1);
//                    Toast.makeText(getApplicationContext(),"Level Up !!!",Toast.LENGTH_LONG).show();
//                }
//
//                cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("studentEXP").setValue(studentEXP);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//
//        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addValueEventListener(listen);
//        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).removeEventListener(listen);

        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                int studentEXP = user.studentEXP + 20;

                Toast.makeText(getApplicationContext(), "+20 EXP - You have done mission!!!", Toast.LENGTH_LONG).show();

                if (studentEXP >= 100) {
                    studentEXP -= 100;
                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("studentLevel").setValue(user.studentLevel + 1);
                    Toast.makeText(getApplicationContext(), "Level Up !!!", Toast.LENGTH_LONG).show();
                }

                cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("studentEXP").setValue(studentEXP);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cDatabaseRef.child("mission").child(cAuth.getCurrentUser().getUid()).child("missionAnswer").setValue(true);
        cDatabaseRef.child("mission").child(cAuth.getCurrentUser().getUid()).child("missionTime").setValue(ServerValue.TIMESTAMP);
    }

    private void Loading() {
        cProgress = new ProgressDialog(this);
        cProgress.setTitle("Logging in");
        cProgress.setMessage("Please wait...");
        cProgress.setCancelable(false);
        cProgress.show();
    }


}
