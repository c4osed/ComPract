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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AnswerActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private ProgressDialog cProgress;

    private ImageView cImageButtonChat;
    private ImageButton cImageButtonCreateQuestion;

    private ConstraintLayout cBottomBar;
    private ConstraintLayout cLayoutPractice;
    private ConstraintLayout cLayoutAdvise;
    private ImageView cImageViewAdvise;
    private ConstraintLayout cLayoutSearch;
    private ConstraintLayout cLayoutProfile;
    private TextView cTextAdvise;

    ListAnswer adapter;
    private ListView cListView;
    private List<HashMap<String, String>> cListAnswer = new ArrayList<HashMap<String, String>>();

    HashMap<String, String> map;

    String cQuestionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        cImageButtonChat = (ImageView) findViewById(R.id.ImageButtonChat);


        //OnClick
        cImageButtonChat.setOnClickListener(clickListener);


        cListView = (ListView) findViewById(R.id.ListViewAnswer);

        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        cQuestionID = map.get("QuestionID");
        ListAnswer();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ListAnswer();
    }

    private void ListAnswer() {

        cDatabaseRef.child("answer").child(cAuth.getCurrentUser().getUid()).child(cQuestionID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                cListAnswer.clear();
                adapter = new ListAnswer(getApplicationContext(), cListAnswer);
                cListView.setAdapter(adapter);

                for (final DataSnapshot answerID : dataSnapshot.getChildren()) {
                    final Answer answer = answerID.getValue(Answer.class);
                    cDatabaseRef.child("user").child(answerID.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            User user = dataSnapshot2.getValue(User.class);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("QuestionID", dataSnapshot.getKey());
                            map.put("AnswerUID", answerID.getKey());
                            map.put("AnswerPicture", user.profilePicture);
                            map.put("AnswerName", user.fullName);
                            map.put("Answer", answer.Answer);
                            map.put("AnswerTime", answer.ASCAnswerTime + "");
                            map.put("Score", answer.Score + "");
                            map.put("Comment", answer.Comment);


                            cListAnswer.add(map);

                            adapter = new ListAnswer(getApplicationContext(), cListAnswer);
                            cListView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

//
        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(AnswerActivity.this, CommentActivity.class).putExtra("map", cListAnswer.get(position)));

            }
        });
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
//                case R.id.ImageButtonChat:
//                    startActivity(new Intent(AdviseActivity.this, ChatList.class));
//                    break;
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

class ListAnswer extends ArrayAdapter {

    List<HashMap<String, String>> cListAnswer;

    public ListAnswer(Context context, List<HashMap<String, String>> listUser) {
        super(context, R.layout.listview_answer, R.id.TextAnswerUser, listUser);
        this.cListAnswer = listUser;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HashMap<String, String> map = cListAnswer.get(position);

        String vAnswerUID = map.get("AnswerUID");
        String vAnswerPicture = map.get("AnswerPicture");
        String vAnswerName = map.get("AnswerName");
        String vAnswer = map.get("Answer");
        String vAnswerTime = map.get("AnswerTime");
        String vAnswerScore = map.get("AnswerScore");

//        final String vUID = map.get("UID");
//        final String vEmail = map.get("email");
//        String vCountry = map.get("country");
//        String vNative = map.get("native");
//        String vLearn = map.get("learnAbbreviation");


        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_answer, parent, false);

        ImageView ImageViewPicture = (ImageView) row.findViewById(R.id.ImageViewPicture);
        TextView TextAnswerUser = (TextView) row.findViewById(R.id.TextAnswerUser);
        TextView TextAnswerTime = (TextView) row.findViewById(R.id.TextAnswerTime);
        TextView TextAnswer = (TextView) row.findViewById(R.id.TextViewAnswer);


        MyClass mc = new MyClass();
        mc.SetImage(getContext(), ImageViewPicture, vAnswerPicture, vAnswerUID);
        TextAnswerUser.setText(vAnswerName);
        TextAnswerTime.setText(new SimpleDateFormat("HH:mm", Locale.US).format(Long.parseLong(vAnswerTime)));
        TextAnswer.setText(vAnswer);
        ImageViewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ss", Toast.LENGTH_LONG).show();
            }
        });
//        TextQuestion.setText("\t\t\t\t" + vQuestion + "\n");
//        TextName.setText(vName);
//        TextNative.setText("Native: " + vNative);
//        TextLearn.setText("Learning: " + vLearn.replaceAll(",", ", "));

        return row;
    }
}