package com.zoazh.le.ComPract.controller.sub;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.main.ProfileActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ViewProfileActivity extends BaseActivity {

    FirebaseAuth cAuth = FirebaseAuth.getInstance();
    DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();

    private Button cButtonMessage;
    private Button cButtonFollow;

    private ConstraintLayout cLayoutLearn;

    private TextView cTextName;
    private ImageView cImageProfilePicture;
    private TextView cTextStudyLevel;
    private TextView cTextAdviseLevel;
    private TextView cTextFollowing;
    private TextView cTextFollower;
    private TextView cTextAbout;
    private TextView cTextAge;
    private TextView cTextGender;
    private TextView cTextCountry;
    private TextView cTextNative;
    private TextView cTextLearn;

    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");

        cButtonMessage = (Button) findViewById(R.id.ButtonMessage);
        cButtonFollow = (Button) findViewById(R.id.ButtonFollow);
        cLayoutLearn = (ConstraintLayout) findViewById(R.id.LayoutLearn);

        cTextName = (TextView) findViewById(R.id.TextProfile);
        cImageProfilePicture = (ImageView) findViewById(R.id.ImageViewProfilePicture);
        cTextStudyLevel = (TextView) findViewById(R.id.TextStudentLevelResult);
        cTextAdviseLevel = (TextView) findViewById(R.id.TextAdvisorLevelResult);
        cTextFollowing = (TextView) findViewById(R.id.TextFollowingResult);
        cTextFollower = (TextView) findViewById(R.id.TextFollowerResult);
        cTextAbout = (TextView) findViewById(R.id.TextAboutResult);
        cTextAge = (TextView) findViewById(R.id.TextAgeResult);
        cTextGender = (TextView) findViewById(R.id.TextGenderResult);
        cTextCountry = (TextView) findViewById(R.id.TextCountryResult);
        cTextNative = (TextView) findViewById(R.id.TextNativeResult);
        cTextLearn = (TextView) findViewById(R.id.TextLearnResult);

        MyClass mc = new MyClass();

        mc.SetImage(getApplicationContext(), cImageProfilePicture, map.get("profilePicture"), map.get("email"));

        cTextName.setText(map.get("name"));
        cTextStudyLevel.setText(map.get("studentLevel"));
        cTextAdviseLevel.setText(map.get("advisorLevel"));
        cTextFollowing.setText(map.get("followingCount"));
        cTextFollower.setText(map.get("followerCount"));
        cTextAbout.setText(map.get("about"));
        cTextAge.setText(map.get("age"));
        cTextGender.setText(map.get("gender"));
        cTextCountry.setText(map.get("country"));
        cTextNative.setText(map.get("native"));
        cTextLearn.setText(map.get("learnAbbreviation").replace(",", ", "));

        cButtonMessage.setOnClickListener(clickListener);
        cButtonFollow.setOnClickListener(clickListener);
        cLayoutLearn.setOnClickListener(clickListener);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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
                case R.id.ButtonMessage:
                    startActivity(new Intent(ViewProfileActivity.this, ChatActivity.class).putExtra("map", map));
                    break;
                case R.id.ButtonFollow:
                    follow();
                    break;
                case R.id.LayoutLearn:
                    listLearn();
                    break;
            }
        }
    };

    private void listLearn() {
        AlertDialog.Builder listLearn = new AlertDialog.Builder(ViewProfileActivity.this);
        listLearn.setItems(map.get("learnFull").split(","), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        listLearn.show();
    }

    private void follow() {
        if (cButtonFollow.getText().equals("Follow")) {
            cDatabaseRef.child("follow").child(cAuth.getCurrentUser().getUid()).child(map.get("UID")).setValue(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
//            cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("followingCount").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("followingCount").setValue(Integer.parseInt(dataSnapshot.getValue().toString()) + 1);
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

            cButtonFollow.setText("Unfollow");
            Toast.makeText(getApplicationContext(), "Followed.", Toast.LENGTH_LONG).show();
        } else {

        }
    }
}
