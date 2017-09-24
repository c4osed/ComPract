package com.zoazh.le.ComPract.controller.sub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.sub.ChatActivity;
import com.zoazh.le.ComPract.controller.sub.ChatList;
import com.zoazh.le.ComPract.controller.sub.CreateQuestionActivity;
import com.zoazh.le.ComPract.controller.sub.QuestionActivity;
import com.zoazh.le.ComPract.controller.sub.ViewProfileActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.database.Answer;
import com.zoazh.le.ComPract.model.database.Question;
import com.zoazh.le.ComPract.model.database.User;

import org.apache.commons.lang3.text.WordUtils;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CommentActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private ProgressDialog cProgress;

    private ImageView cImageButtonChat;

    private TextView cTextViewName;
    private TextView cTextViewTime;
    private TextView cTextViewComment;
    private EditText cInputComment;
    private RadioButton cRadioBad;
    private RadioButton cRadioGood;
    private RadioButton cRadioPerfect;
    private ImageView cImageViewSendComment;

    HashMap<String, String> map;

    String cQuestionID;
    String cAnswerID;
    String cComment;
    int cScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        cImageButtonChat = (ImageView) findViewById(R.id.ImageButtonChat);

        cTextViewComment = (TextView) findViewById(R.id.TextViewComment);
        cInputComment = (EditText) findViewById(R.id.EditTextComment);
        cRadioBad = (RadioButton) findViewById(R.id.RadioBad);
        cRadioGood = (RadioButton) findViewById(R.id.RadioGood);
        cRadioPerfect = (RadioButton) findViewById(R.id.RadioPerfect);
        cImageViewSendComment = (ImageView) findViewById(R.id.ImageButtonSendComment);

        //OnClick
        cImageButtonChat.setOnClickListener(clickListener);
        cRadioBad.setOnClickListener(clickListener);
        cRadioGood.setOnClickListener(clickListener);
        cRadioPerfect.setOnClickListener(clickListener);
        cImageViewSendComment.setOnClickListener(clickListener);


        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        cQuestionID = map.get("QuestionID");
        cAnswerID = map.get("AnswerUID");
        cComment = map.get("Comment");
        cScore = Integer.parseInt(map.get("Score"));

        if (cScore != 0) {
            cRadioBad.setClickable(false);
            cRadioGood.setClickable(false);
            cRadioPerfect.setClickable(false);
            if (cScore == 1) {
                cRadioBad.setChecked(true);
            } else if (cScore == 2) {
                cRadioGood.setChecked(true);
            } else if (cScore == 3) {
                cRadioPerfect.setChecked(true);
            }
        }

        if (cComment != null) {
            cTextViewComment.setText(cComment);
        }

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
                case R.id.ImageButtonChat:
                    startActivity(new Intent(CommentActivity.this, ChatList.class));
                    break;
                case R.id.ImageButtonSendComment:
                    SendComment();
                    break;
                case R.id.RadioBad:
                    Toast.makeText(getApplicationContext(), "ss", Toast.LENGTH_LONG).show();
                    cScore = 1;
                    cRadioGood.setChecked(false);
                    cRadioPerfect.setChecked(false);
                    break;
                case R.id.RadioGood:
                    cScore = 2;
                    cRadioBad.setChecked(false);
                    cRadioPerfect.setChecked(false);
                    break;
                case R.id.RadioPerfect:
                    cScore = 3;
                    cRadioBad.setChecked(false);
                    cRadioGood.setChecked(false);
                    break;
            }
        }
    };

    private void SendComment() {
        cDatabaseRef.child("answer").child(cAuth.getCurrentUser().getUid()).child(cQuestionID).child(cAnswerID).child("Comment").setValue(cInputComment.getText().toString());
        cDatabaseRef.child("answer").child(cAuth.getCurrentUser().getUid()).child(cQuestionID).child(cAnswerID).child("Score").setValue(cScore);
        cTextViewComment.setText(cInputComment.getText());
    }


    private void Loading() {
        cProgress = new ProgressDialog(this);
        cProgress.setTitle("Logging in");
        cProgress.setMessage("Please wait...");
        cProgress.setCancelable(false);
        cProgress.show();
    }

}
