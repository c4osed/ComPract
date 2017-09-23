package com.zoazh.le.ComPract.controller.sub;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
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
import com.zoazh.le.ComPract.model.database.User;

public class PrivacyActivity extends BaseActivity {
    Switch switchPrivacy;
    ConstraintLayout cLayoutPrivacy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        cLayoutPrivacy = (ConstraintLayout) findViewById(R.id.LayoutPrivacy);
        switchPrivacy = (Switch) findViewById(R.id.switchPrivacy);
        switchPrivacy.setOnClickListener(clickListener);
        cLayoutPrivacy.setOnClickListener(clickListener);
        
        FirebaseAuth cAuth = FirebaseAuth.getInstance();
        DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseAuth cAuth = FirebaseAuth.getInstance();
                DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
                User user = dataSnapshot.getValue(User.class);
                if(dataSnapshot.hasChild("search")){
                    String searchStatus = user.search.toString();
                    if(searchStatus.matches("yes")){
                        switchPrivacy.setChecked(true);
                    }else {
                        switchPrivacy.setChecked(false);
                    }
                }else {
                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("search").setValue("yes");
                    switchPrivacy.setChecked(true);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           switch (v.getId()){
               case R.id.switchPrivacy:

                   FirebaseAuth cAuth = FirebaseAuth.getInstance();
                   DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
                   if(switchPrivacy.isChecked()){
                       cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("search").setValue("yes");
                   }else {
                       cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("search").setValue("no");
                   }
                   break;
               case R.id.LayoutPrivacy:
                   break;
           }
        }
    };
}
