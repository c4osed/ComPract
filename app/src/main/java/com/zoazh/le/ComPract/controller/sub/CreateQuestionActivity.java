package com.zoazh.le.ComPract.controller.sub;

import android.app.ProgressDialog;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.model.BaseActivity;

public class CreateQuestionActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private ProgressDialog cProgress;

    private ConstraintLayout cLayoutQuestionType;
    private ConstraintLayout cLayoutQuestionType2;
    private TextView cTextQuestionTypeResult;
    private ImageButton cButtonDropdown;

    private ConstraintLayout cLayoutNormalQuestion;
    private ConstraintLayout cLayoutChoiceQuestion;

    private ConstraintLayout cLayoutNormalQuestion2;
    private ConstraintLayout cLayoutChoiceQuestion2;

    private int cRadioSelected = 1;
    private RadioButton cRadioChoiceA;
    private RadioButton cRadioChoiceB;
    private RadioButton cRadioChoiceC;
    private RadioButton cRadioChoiceD;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        cLayoutQuestionType = (ConstraintLayout) findViewById(R.id.LayoutQuestionType);
        cLayoutQuestionType2 = (ConstraintLayout) findViewById(R.id.LayoutQuestionType2);
        cTextQuestionTypeResult = (TextView) findViewById(R.id.TextQuestionTypeResult) ;
        cButtonDropdown = (ImageButton) findViewById(R.id.ImageButtonQuestionType);
        cLayoutNormalQuestion = (ConstraintLayout) findViewById(R.id.LayoutNormalQuestion);
        cLayoutChoiceQuestion = (ConstraintLayout) findViewById(R.id.LayoutChoiceQuestion);

        cLayoutNormalQuestion2 = (ConstraintLayout) findViewById(R.id.LayoutNormalQuestion2);
        cLayoutChoiceQuestion2 = (ConstraintLayout) findViewById(R.id.LayoutChoiceQuestion2);

        cRadioChoiceA = (RadioButton) findViewById(R.id.RadioChoiceA);
        cRadioChoiceB = (RadioButton) findViewById(R.id.RadioChoiceB);
        cRadioChoiceC = (RadioButton) findViewById(R.id.RadioChoiceC);
        cRadioChoiceD = (RadioButton) findViewById(R.id.RadioChoiceD);

        //cRadioGroupChoiceQuestion = (RadioGroup) findViewById(R.id.RadioGroupChoiceQuestion);

        //BTM BAR
//        cBottomBar = (ConstraintLayout) findViewById(R.id.BottomBar);
//        cLayoutAdvise = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutAdvise);
//        cImageViewAdvise = (ImageView) cBottomBar.findViewById(R.id.ImageViewAdvise);
//        cLayoutSearch = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutSearch);
//        cLayoutProfile = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutProfile);
//        cTextAdvise = (TextView) cBottomBar.findViewById(R.id.TextAdvise);
//
//
//        //OnClick
        cLayoutQuestionType.setOnClickListener(clickListener);
        cLayoutNormalQuestion.setOnClickListener(clickListener);
        cLayoutChoiceQuestion.setOnClickListener(clickListener);
        cRadioChoiceA.setOnClickListener(clickListener);
        cRadioChoiceB.setOnClickListener(clickListener);
        cRadioChoiceC.setOnClickListener(clickListener);
        cRadioChoiceD.setOnClickListener(clickListener);
//        cLayoutProfile.setOnClickListener(clickListener);





    }


    @Override
    protected void onResume() {
        super.onResume();
        OnlineTimer(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        OnlineTimer(false);
    }



    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.LayoutQuestionType:
                    ShowQuestionType();
                    break;
                case R.id.LayoutNormalQuestion:
                    cTextQuestionTypeResult.setText("Normal");
                    cLayoutQuestionType2.setVisibility(View.INVISIBLE);
                    cButtonDropdown.setImageResource(R.drawable.ic_dropdown);
                    cLayoutNormalQuestion2.setVisibility(View.VISIBLE);
                    break;
                case R.id.LayoutChoiceQuestion:
                    cLayoutQuestionType2.setVisibility(View.INVISIBLE);
                    cButtonDropdown.setImageResource(R.drawable.ic_dropdown);
                    cTextQuestionTypeResult.setText("Multiple Choice");
                    cLayoutChoiceQuestion2.setVisibility(View.VISIBLE);
                    break;
                case R.id.RadioChoiceA:
                    cRadioSelected = 1;
                    cRadioChoiceB.setChecked(false);
                    cRadioChoiceC.setChecked(false);
                    cRadioChoiceD.setChecked(false);
                    break;
                case R.id.RadioChoiceB:
                    cRadioSelected = 2;
                    cRadioChoiceA.setChecked(false);
                    cRadioChoiceC.setChecked(false);
                    cRadioChoiceD.setChecked(false);
                    break;
                case R.id.RadioChoiceC:
                    cRadioSelected = 3;
                    cRadioChoiceA.setChecked(false);
                    cRadioChoiceB.setChecked(false);
                    cRadioChoiceD.setChecked(false);
                    break;
                case R.id.RadioChoiceD:
                    cRadioSelected = 4;
                    cRadioChoiceA.setChecked(false);
                    cRadioChoiceB.setChecked(false);
                    cRadioChoiceC.setChecked(false);
                    break;


            }
        }
    };


    private void ShowQuestionType() {
        if (cLayoutQuestionType2.getVisibility() == View.VISIBLE) {
            cLayoutQuestionType2.setVisibility(View.INVISIBLE);
            cButtonDropdown.setImageResource(R.drawable.ic_dropdown);
            cTextQuestionTypeResult.setText("");
        } else {
            cLayoutQuestionType2.setVisibility(View.VISIBLE);
            cLayoutNormalQuestion2.setVisibility(View.INVISIBLE);
            cLayoutChoiceQuestion2.setVisibility(View.INVISIBLE);
            cButtonDropdown.setImageResource(R.drawable.ic_dropup);

        }
    }

}
