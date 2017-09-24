package com.zoazh.le.ComPract.controller.sub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.CircleTransform;
import com.zoazh.le.ComPract.model.database.Question;
import com.zoazh.le.ComPract.model.database.User;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreateQuestionActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();
    private ProgressDialog cProgress;

    private ConstraintLayout cLayoutQuestionLanguage;
    private TextView cTextQuestionLangaugeResult;
    private ConstraintLayout cLayoutQuestionType;
    private ConstraintLayout cLayoutQuestionType2;
    private TextView cTextQuestionTypeResult;
    private ImageButton cButtonDropdown;

    private ConstraintLayout cLayoutNormalQuestion;
    private ConstraintLayout cLayoutChoiceQuestion;

    private ConstraintLayout cLayoutNormalQuestion2;
    private ConstraintLayout cLayoutChoiceQuestion2;

    private ImageView cImageViewNormalQuestion;
    private ImageView cImageViewChoiceQuestion;
    private EditText cInputNormalQuestion;
    private EditText cInputChoiceQuestion;
    private String cRadioAnswer = "a";
    private RadioButton cRadioChoiceA;
    private RadioButton cRadioChoiceB;
    private RadioButton cRadioChoiceC;
    private RadioButton cRadioChoiceD;
    private EditText cInputChoiceA;
    private EditText cInputChoiceB;
    private EditText cInputChoiceC;
    private EditText cInputChoiceD;

    private ImageButton cImageButtonUploadImageChoice;
    private ImageButton cImageButtonUploadImageNormal;
    private ImageButton cImageButtonSendNormalQuestion;
    private ImageButton cImageButtonSendChoiceQuestion;

    List<String> cListLanguage = new ArrayList<String>();
    private int cQuestionLanguageSelected = 0;

    private static final int SELECT_QUESTION_PICTURE = 1;
    private Uri cQuestionImage = null;
    private String[] listLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        cLayoutQuestionLanguage = (ConstraintLayout) findViewById(R.id.LayoutQuestionLanguage);
        cTextQuestionLangaugeResult = (TextView) findViewById(R.id.TextQuestionLanguageResult);
        cLayoutQuestionType = (ConstraintLayout) findViewById(R.id.LayoutQuestionType);
        cLayoutQuestionType2 = (ConstraintLayout) findViewById(R.id.LayoutQuestionType2);
        cTextQuestionTypeResult = (TextView) findViewById(R.id.TextQuestionTypeResult);
        cButtonDropdown = (ImageButton) findViewById(R.id.ImageButtonQuestionType);
        cLayoutNormalQuestion = (ConstraintLayout) findViewById(R.id.LayoutNormalQuestion);
        cLayoutChoiceQuestion = (ConstraintLayout) findViewById(R.id.LayoutChoiceQuestion);

        cLayoutNormalQuestion2 = (ConstraintLayout) findViewById(R.id.LayoutNormalQuestion2);
        cLayoutChoiceQuestion2 = (ConstraintLayout) findViewById(R.id.LayoutChoiceQuestion2);

        cImageViewNormalQuestion = (ImageView) findViewById(R.id.ImageViewNormalQuestion);
        cImageViewChoiceQuestion = (ImageView) findViewById(R.id.ImageViewChoiceQuestion);
        cInputNormalQuestion = (EditText) findViewById(R.id.InputNormalQuestion);
        cInputChoiceQuestion = (EditText) findViewById(R.id.InputChoiceQuestion);
        cRadioChoiceA = (RadioButton) findViewById(R.id.RadioChoiceA);
        cRadioChoiceB = (RadioButton) findViewById(R.id.RadioChoiceB);
        cRadioChoiceC = (RadioButton) findViewById(R.id.RadioChoiceC);
        cRadioChoiceD = (RadioButton) findViewById(R.id.RadioChoiceD);
        cInputChoiceA = (EditText) findViewById(R.id.InputChoiceA);
        cInputChoiceB = (EditText) findViewById(R.id.InputChoiceB);
        cInputChoiceC = (EditText) findViewById(R.id.InputChoiceC);
        cInputChoiceD = (EditText) findViewById(R.id.InputChoiceD);

        cImageButtonUploadImageNormal = (ImageButton) findViewById(R.id.ImageButtonUploadImageNormal);
        cImageButtonUploadImageChoice = (ImageButton) findViewById(R.id.ImageButtonUploadImageChoice);
        cImageButtonSendNormalQuestion = (ImageButton) findViewById(R.id.ImageButtonSendNormalQuestion);
        cImageButtonSendChoiceQuestion = (ImageButton) findViewById(R.id.ImageButtonSendChoiceQuestion);

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
        cLayoutQuestionLanguage.setOnClickListener(clickListener);
        cLayoutQuestionType.setOnClickListener(clickListener);
        cLayoutNormalQuestion.setOnClickListener(clickListener);
        cLayoutChoiceQuestion.setOnClickListener(clickListener);
        cRadioChoiceA.setOnClickListener(clickListener);
        cRadioChoiceB.setOnClickListener(clickListener);
        cRadioChoiceC.setOnClickListener(clickListener);
        cRadioChoiceD.setOnClickListener(clickListener);
        cImageButtonUploadImageNormal.setOnClickListener(clickListener);
        cImageButtonUploadImageChoice.setOnClickListener(clickListener);
        cImageButtonSendNormalQuestion.setOnClickListener(clickListener);
        cImageButtonSendChoiceQuestion.setOnClickListener(clickListener);
//        cLayoutProfile.setOnClickListener(clickListener);

//        cDatabaseRef.child("language").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    cListLanguage.add(data.getKey().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseAuth cAuth = FirebaseAuth.getInstance();
                DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
                User user = dataSnapshot.getValue(User.class);
                listLanguage = user.learnFull.toString().split(",");
                cListLanguage = Arrays.asList(listLanguage);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
                case R.id.LayoutQuestionLanguage:
                    SetQuestionLangauge();
                    break;
                case R.id.LayoutQuestionType:
                    ShowQuestionType();
                    break;
                case R.id.LayoutNormalQuestion:
                    cTextQuestionTypeResult.setText("Open-Ended");
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
                case R.id.ImageButtonUploadImageNormal:
                    UploadImageQuestion();
                    break;
                case R.id.ImageButtonUploadImageChoice:
                    UploadImageQuestion();
                    break;
                case R.id.ImageButtonSendNormalQuestion:
                    SendQuestion();
                    break;
                case R.id.ImageButtonSendChoiceQuestion:
                    SendQuestion();
                    break;


            }
        }
    };

    private void UploadImageQuestion() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_QUESTION_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_QUESTION_PICTURE && resultCode == RESULT_OK) {
            cQuestionImage = data.getData();
            Picasso.with(getApplicationContext()).load(cQuestionImage).resize(500, 500).centerCrop().into(cImageViewNormalQuestion);
            Picasso.with(getApplicationContext()).load(cQuestionImage).resize(500, 500).centerCrop().into(cImageViewChoiceQuestion);
        }
    }

    private void SetQuestionLangauge() {
        final AlertDialog.Builder setQuestionLangauge = new AlertDialog.Builder(this);

        final String[] cQuestionLanguage = new String[cListLanguage.size()];
        setQuestionLangauge.setTitle("Select Language");
        cListLanguage.toArray(cQuestionLanguage);
        setQuestionLangauge.setSingleChoiceItems(cQuestionLanguage, cQuestionLanguageSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cQuestionLanguageSelected = which;
            }
        });

        setQuestionLangauge.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cLayoutQuestionType.setVisibility(View.VISIBLE);
                cTextQuestionLangaugeResult.setText(cQuestionLanguage[cQuestionLanguageSelected]);
//                if (cButtonEditProfileLearn.getText().equals("Learn")) {
//                    setLearn();
//                }
            }
        });
        setQuestionLangauge.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        setQuestionLangauge.show();
    }


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

    private void SendQuestion() {

        String vTextQuestionLanguage = cTextQuestionLangaugeResult.getText().toString();
        String vTextQuestionType = cTextQuestionTypeResult.getText().toString();
        String vQuestionPicture = null;
        String vInputQuestion = null;
        String vInputChoiceA = null;
        String vInputChoiceB = null;
        String vInputChoiceC = null;
        String vInputChoiceD = null;
        String vAnswer = null;

        boolean pass = false;

        if (vTextQuestionType.equals("Multiple Choice")) {
            vInputQuestion = cInputChoiceQuestion.getText().toString();
            vInputChoiceA = cInputChoiceA.getText().toString();
            vInputChoiceB = cInputChoiceB.getText().toString();
            vInputChoiceC = cInputChoiceC.getText().toString();
            vInputChoiceD = cInputChoiceD.getText().toString();
            if (vInputChoiceA.isEmpty() || vInputChoiceB.isEmpty() || vInputChoiceC.isEmpty() || vInputChoiceD.isEmpty()) {
                pass = true;
            }
        } else {
            vInputQuestion = cInputNormalQuestion.getText().toString();
        }


        if (vInputQuestion.isEmpty() || pass) {
            Toast.makeText(getApplicationContext(), "All field are required!!!", Toast.LENGTH_LONG).show();
        } else {
            final String vKey = cDatabaseRef.child("question").push().getKey();

            if (vTextQuestionType.equals("Multiple Choice")) {
                vAnswer = cRadioAnswer;
                if (cQuestionImage != null) {
                    cImageViewChoiceQuestion.setDrawingCacheEnabled(true);
                    cImageViewChoiceQuestion.buildDrawingCache();
                    Bitmap bitmap = cImageViewChoiceQuestion.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = cStorageRef.child("user").child(cAuth.getCurrentUser().getUid()).child("QuestionPicture").child(vKey).putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            cDatabaseRef.child("question").child(vKey).child("QuestionPicture").setValue(taskSnapshot.getDownloadUrl().toString());
                        }
                    });
                }
            } else {

                if (cQuestionImage != null) {
                    cImageViewNormalQuestion.setDrawingCacheEnabled(true);
                    cImageViewNormalQuestion.buildDrawingCache();
                    Bitmap bitmap = cImageViewNormalQuestion.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = cStorageRef.child("user").child(cAuth.getCurrentUser().getUid()).child("QuestionPicture").child(vKey).putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            cDatabaseRef.child("question").child(vKey).child("QuestionPicture").setValue(taskSnapshot.getDownloadUrl().toString());
                        }
                    });
                }
            }

            Question question = new Question(cAuth.getCurrentUser().getUid(), vTextQuestionLanguage, vTextQuestionType, vQuestionPicture, vInputQuestion, vInputChoiceA, vInputChoiceB, vInputChoiceC, vInputChoiceD, vAnswer, new Date().getTime(), new Date().getTime() * -1, "Normal");

            cDatabaseRef.child("question").child(vKey).setValue(question);

            Toast.makeText(getApplicationContext(), "Success!!!", Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());
        }


    }
}
