package com.zoazh.le.ComPract.controller.start;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.sinch.android.rtc.SinchError;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.main.SearchActivity;
import com.zoazh.le.ComPract.controller.sub.EditProfileActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.SinchService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends BaseActivity implements SinchService.StartFailedListener {

    private FirebaseAuth.AuthStateListener cAuthListener;
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    public static String emailTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        // setting tag for current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        emailTag = user.getEmail();
        OneSignal.sendTag("User_ID", emailTag);

        cAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                if (!getSinchServiceInterface().isStarted()) {
                                    //Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_LONG).show();
                                    getSinchServiceInterface().startClient(cAuth.getCurrentUser().getUid());
                                    startActivity(new Intent(MainActivity.this, SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                } else {
                                    //Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this, SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                }


                            } else {
                                //Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
                                //startActivity(new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                startActivity(new Intent(MainActivity.this, EditProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    //Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }
        };
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
        if (cAuth.getCurrentUser() != null) {
            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(cAuth.getCurrentUser().getUid());
            } else {
                finish();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        cAuth.addAuthStateListener(cAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (cAuthListener != null) {
            cAuth.removeAuthStateListener(cAuthListener);
        }
    }

    @Override
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {
        //startActivity(new Intent(MainActivity.this,SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
