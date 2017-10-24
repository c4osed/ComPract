package com.zoazh.le.ComPract.controller.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.database.Mission;
import com.zoazh.le.ComPract.model.database.User;

import java.util.HashMap;

public class MissionActivity extends BaseActivity {

    FirebaseAuth cAuth = FirebaseAuth.getInstance();
    DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();

    TextView cTextMissionAnswerStatus;
    TextView cTextMissionQuestionStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        cTextMissionAnswerStatus = (TextView) findViewById(R.id.TextMissionAnswerStatus);
        cTextMissionQuestionStatus = (TextView) findViewById(R.id.TextMissionQuestionStatus);

        cDatabaseRef.child("mission").child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Mission mission = dataSnapshot.getValue(Mission.class);
                try {

                    if (mission.missionAnswer) {
                        cTextMissionAnswerStatus.setText("Done");
                    }
                    if (mission.missionQuestion) {
                        cTextMissionQuestionStatus.setText("Done");
                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

            }
        }
    };

}
