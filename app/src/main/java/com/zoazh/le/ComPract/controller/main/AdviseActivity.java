package com.zoazh.le.ComPract.controller.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.zoazh.le.ComPract.controller.sub.AnswerActivity;
import com.zoazh.le.ComPract.controller.sub.ChatActivity;
import com.zoazh.le.ComPract.controller.sub.ChatList;
import com.zoazh.le.ComPract.controller.sub.CreateQuestionActivity;
import com.zoazh.le.ComPract.controller.sub.QuestionActivity;
import com.zoazh.le.ComPract.controller.sub.ViewProfileActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.database.Question;
import com.zoazh.le.ComPract.model.database.User;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AdviseActivity extends BaseActivity {

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

    ListAdvise adapter;
    private ListView cListView;
    private List<HashMap<String, String>> cListQuestion = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advise);

        cImageButtonChat = (ImageView) findViewById(R.id.ImageButtonChat);
        cImageButtonCreateQuestion = (ImageButton) findViewById(R.id.ImageButtonCreateQuestion);

        //BTM BAR
        cBottomBar = (ConstraintLayout) findViewById(R.id.BottomBar);
        cLayoutPractice = (ConstraintLayout) findViewById(R.id.LayoutPractice);
        cLayoutAdvise = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutAdvise);
        cImageViewAdvise = (ImageView) cBottomBar.findViewById(R.id.ImageViewAdvise);
        cLayoutSearch = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutSearch);
        cLayoutProfile = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutProfile);
        cTextAdvise = (TextView) cBottomBar.findViewById(R.id.TextAdvise);

        cTextAdvise.setTextColor(getResources().getInteger(R.color.secondary));
        cImageViewAdvise.setColorFilter(getResources().getInteger(R.color.secondary));

        //OnClick
        cImageButtonChat.setOnClickListener(clickListener);
        cImageButtonCreateQuestion.setOnClickListener(clickListener);
        cLayoutPractice.setOnClickListener(clickListener);
        cLayoutSearch.setOnClickListener(clickListener);
        cLayoutProfile.setOnClickListener(clickListener);


        cListView = (ListView) findViewById(R.id.ListViewAdvise);

        ListPractice();
    }

    private void ListPractice() {

        cDatabaseRef.child("question").orderByChild("QuestionAuthor").equalTo(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cListQuestion.clear();
                adapter = new ListAdvise(getApplicationContext(), cListQuestion);
                cListView.setAdapter(adapter);

                for (final DataSnapshot questionID : dataSnapshot.getChildren()) {
                    final Question question = questionID.getValue(Question.class);

                    cDatabaseRef.child("user").child(question.QuestionAuthor).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("QuestionID", questionID.getKey());
                            map.put("AuthorID", question.QuestionAuthor);
                            map.put("AuthorPicture", user.profilePicture);
                            map.put("AuthorName", user.fullName);
                            map.put("QuestionLanguage", question.QuestionLanguage);
                            map.put("QuestionType", question.QuestionType);
                            map.put("Question", question.Question);
                            map.put("QuestionPicture", question.QuestionPicture);
                            map.put("AnswerCount", question.AnswerCount+"");

                            cListQuestion.add(map);

                            adapter = new ListAdvise(getApplicationContext(), cListQuestion);
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


        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(AdviseActivity.this, AnswerActivity.class).putExtra("map", cListQuestion.get(position)));
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
                case R.id.ImageButtonChat:
                    startActivity(new Intent(AdviseActivity.this, ChatList.class));
                    break;
                case R.id.ImageButtonCreateQuestion:
                    startActivity(new Intent(AdviseActivity.this, CreateQuestionActivity.class));
                    break;
                case R.id.LayoutPractice:
                    startActivity(new Intent(AdviseActivity.this, PracticeActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.LayoutSearch:
                    startActivity(new Intent(AdviseActivity.this, SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.LayoutProfile:
                    startActivity(new Intent(AdviseActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
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

class ListAdvise extends ArrayAdapter {

    List<HashMap<String, String>> cListQuestion;

    public ListAdvise(Context context, List<HashMap<String, String>> listUser) {
        super(context, R.layout.listview_practice, R.id.TextQuestion, listUser);
        this.cListQuestion = listUser;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HashMap<String, String> map = cListQuestion.get(position);

        String vAuthorID = map.get("AuthorID");
        String vAuthorPicture = map.get("AuthorPicture");
        String vQuestionAuthorName = map.get("AuthorName");
        String vQuestionLanguage = map.get("QuestionLanguage");
        String vQuestionType = map.get("QuestionType");
        String vQuestion = map.get("Question");
        String vImage = map.get("QuestionPicture");
        String vCount = map.get("AnswerCount");
//        final String vUID = map.get("UID");
//        final String vEmail = map.get("email");
//        String vCountry = map.get("country");
//        String vNative = map.get("native");
//        String vLearn = map.get("learnAbbreviation");


        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_practice_advise, parent, false);

        ImageView ImgCount = (ImageView) row.findViewById(R.id.imageView9);
        ImageView ImageViewPicture = (ImageView) row.findViewById(R.id.ImageViewPicture);
        TextView TextQuestionAuthor = (TextView) row.findViewById(R.id.TextQuestionAuthor);
        TextView TextQuestionType = (TextView) row.findViewById(R.id.QuestionType);
        TextView TextQuestion = (TextView) row.findViewById(R.id.TextViewQuestion);
        TextView TextCount = (TextView) row.findViewById(R.id.TextCountAns);
        final ImageView ImageViewQuestion = (ImageView) row.findViewById(R.id.ImageViewQuestion);


        MyClass mc = new MyClass();
        ImgCount.setImageResource(R.drawable.ic_comments);
        mc.SetImage(getContext(), ImageViewPicture, vAuthorPicture, vAuthorID);
        TextQuestionAuthor.setText(vQuestionAuthorName);
        TextCount.setText(vCount);
        TextQuestion.setText("\t\t\t\t" + vQuestion + "\n");
        TextQuestionType.setText(vQuestionLanguage + " (" + vQuestionType + ")");
        Picasso.with(getContext()).load(vImage).into(ImageViewQuestion);
//        TextName.setText(vName);
//        TextNative.setText("Native: " + vNative);
//        TextLearn.setText("Learning: " + vLearn.replaceAll(",", ", "));

        return row;
    }
}