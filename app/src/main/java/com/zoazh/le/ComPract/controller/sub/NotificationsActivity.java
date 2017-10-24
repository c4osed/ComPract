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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class NotificationsActivity extends BaseActivity {

    FirebaseAuth cAuth = FirebaseAuth.getInstance();
    DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();


    private ConstraintLayout cLayoutMission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        cLayoutMission = (ConstraintLayout) findViewById(R.id.LayoutMission);


        cLayoutMission.setOnClickListener(clickListener);


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
                case R.id.LayoutMission:
                    startActivity(new Intent(NotificationsActivity.this, MissionActivity.class));
                    break;
            }
        }
    };

}
