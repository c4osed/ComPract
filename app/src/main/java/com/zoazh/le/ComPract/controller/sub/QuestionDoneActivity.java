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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.main.PracticeActivity;
import com.zoazh.le.ComPract.controller.main.ProfileActivity;
import com.zoazh.le.ComPract.controller.main.SearchActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;

import java.util.Date;
import java.util.HashMap;

public class QuestionDoneActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private ProgressDialog cProgress;
    private ImageButton cImageButtonSendAnswerChoice;


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

    private TextView cTextViewAnswer;
    private TextView cTextViewComment;
    private  TextView cTextViewScore;


    private RadioButton cRadioChoiceA;
    private RadioButton cRadioChoiceB;
    private RadioButton cRadioChoiceC;
    private RadioButton cRadioChoiceD;


    HashMap<String, String> map;

    String cQuestionID;
    String cQuestionAuthorID;
    private int cScore;
    private String cRadioAnswer;
    private String cRadioCorrect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_done);


        cImageViewAuthor = (ImageView) findViewById(R.id.ImageViewAuthor);
        cTextAuthor = (TextView) findViewById(R.id.TextViewAuthor);
        cTextQuestionType = (TextView) findViewById(R.id.TextViewQuestionType);
        cTextQuestion = (TextView) findViewById(R.id.TextViewQuestion);
        cImageViewQuestion = (ImageView) findViewById(R.id.ImageViewQuestion);
        cLayoutNormalQuestion = (ConstraintLayout) findViewById(R.id.LayoutNormalQuestion);
        cLayoutChoiceQuestion = (ConstraintLayout) findViewById(R.id.LayoutChoiceQuestion);
        cImageButtonSendAnswerChoice = (ImageButton) findViewById(R.id.ImageButtonSendAnswerChoice);
        cImageButtonSendAnswerChoice.setVisibility(View.INVISIBLE);

        cTextViewAnswer = (TextView) findViewById(R.id.TextViewAnswerUser);
        cTextViewComment = (TextView) findViewById(R.id.TextViewComment);
        cTextViewScore = (TextView) findViewById(R.id.TextViewScore);


        cRadioChoiceA = (RadioButton) findViewById(R.id.RadioChoiceA);
        cRadioChoiceB = (RadioButton) findViewById(R.id.RadioChoiceB);
        cRadioChoiceC = (RadioButton) findViewById(R.id.RadioChoiceC);
        cRadioChoiceD = (RadioButton) findViewById(R.id.RadioChoiceD);


        cRadioChoiceA.setOnClickListener(clickListener);
        cRadioChoiceB.setOnClickListener(clickListener);
        cRadioChoiceC.setOnClickListener(clickListener);
        cRadioChoiceD.setOnClickListener(clickListener);


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
            cRadioChoiceA.setClickable(false);
            cRadioChoiceB.setClickable(false);
            cRadioChoiceC.setClickable(false);
            cRadioChoiceD.setClickable(false);
            cRadioChoiceA.setText("  a )  " + map.get("ChoiceA"));
            cRadioChoiceB.setText("  b )  " + map.get("ChoiceB"));
            cRadioChoiceC.setText("  c )  " + map.get("ChoiceC"));
            cRadioChoiceD.setText("  d )  " + map.get("ChoiceD"));
            cRadioAnswer = map.get("Answer");
            cRadioCorrect = map.get("AnswerCorrect");

            if (cRadioAnswer.equals("a")) {
                cRadioChoiceA.setChecked(true);
            } else if (cRadioAnswer.equals("b")) {
                cRadioChoiceB.setChecked(true);
            } else if (cRadioAnswer.equals("c")) {
                cRadioChoiceC.setChecked(true);
            } else if (cRadioAnswer.equals("d")) {
                cRadioChoiceD.setChecked(true);
            }

            if (cRadioCorrect.equals("a")) {
                cRadioChoiceA.setTextColor(getResources().getInteger(R.color.green));
            } else if (cRadioCorrect.equals("b")) {
                cRadioChoiceB.setTextColor(getResources().getInteger(R.color.green));
            } else if (cRadioCorrect.equals("c")) {
                cRadioChoiceC.setTextColor(getResources().getInteger(R.color.green));
            } else if (cRadioCorrect.equals("d")) {
                cRadioChoiceD.setTextColor(getResources().getInteger(R.color.green));
            }
        } else {
//            cLayoutChoiceQuestion.removeAllViews();
            cLayoutNormalQuestion.setVisibility(View.VISIBLE);
            if(map.get("Comment")==null){
                cTextViewComment.setText("Comment :    -");
                cTextViewScore.setText("Score :    -");
            }else {
                cTextViewComment.setText("Comment :   "+map.get("Comment"));
            }
            cTextViewAnswer.setText("Your answer :    "+map.get("Answer"));
            cScore = Integer.parseInt(map.get("Score"));
            if (cScore == 1) {
                cTextViewScore.setText("Score :    Bad");
            } else if (cScore == 2) {
                cTextViewScore.setText("Score :    Good");
            } else if (cScore == 3) {
                cTextViewScore.setText("Score :    Perfect");
            }
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
                    startActivity(new Intent(QuestionDoneActivity.this, CreateQuestionActivity.class));
                    break;
                case R.id.LayoutPractice:
                    startActivity(new Intent(QuestionDoneActivity.this, PracticeActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.LayoutSearch:
                    startActivity(new Intent(QuestionDoneActivity.this, SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.animห.move_out_left);
                    break;
                case R.id.LayoutProfile:
                    startActivity(new Intent(QuestionDoneActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
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
//                    sendAnswer();
                    break;
                case R.id.ImageButtonSendAnswerChoice:
//                    sendAnswer();
                    break;

            }


        }
    };

//    private void sendAnswer() {
//        if (cInputAnswerQuestion.getText() != null) {
//            cDatabaseRef.child("answer").child(cQuestionAuthorID).child(cQuestionID).child(cAuth.getCurrentUser().getUid()).child("ASCAnswerTime").setValue(new Date().getTime());
//            cDatabaseRef.child("answer").child(cQuestionAuthorID).child(cQuestionID).child(cAuth.getCurrentUser().getUid()).child("DESCAnswerTime").setValue(new Date().getTime()* -1);
//
//            if ((""+map.get("QuestionType")).equals("Multiple Choice"))
//                cDatabaseRef.child("answer").child(cQuestionAuthorID).child(cQuestionID).child(cAuth.getCurrentUser().getUid()).child("Answer").setValue(cRadioAnswer);
//            else
//                cDatabaseRef.child("answer").child(cQuestionAuthorID).child(cQuestionID).child(cAuth.getCurrentUser().getUid()).child("Answer").setValue(cInputAnswerQuestion.getText()+"");
//
//        } else
//            Toast.makeText(QuestionDoneActivity.this, "Please Answer The Question First", Toast.LENGTH_LONG).show();
//
//        Toast.makeText(QuestionDoneActivity.this, "The Answer has been sent!", Toast.LENGTH_LONG).show();
//        startActivity(new Intent(QuestionDoneActivity.this, PracticeActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//
//    }


    private void Loading() {
        cProgress = new ProgressDialog(this);
        cProgress.setTitle("Logging in");
        cProgress.setMessage("Please wait...");
        cProgress.setCancelable(false);
        cProgress.show();
    }


}
