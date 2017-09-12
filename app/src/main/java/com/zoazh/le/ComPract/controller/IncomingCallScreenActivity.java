package com.zoazh.le.ComPract.controller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.model.AudioPlayer;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.SinchService;
import com.zoazh.le.ComPract.model.database.User;

import java.util.List;

public class IncomingCallScreenActivity extends BaseActivity {

    DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();

    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private String mCallId;
    private AudioPlayer mAudioPlayer;

    private ImageView cImageViewIncomeProfilePicture;
    private ImageButton cImageButtonIncomeAnswer;
    private ImageButton cImageButtonIncomeDecline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming);

        cImageViewIncomeProfilePicture = (ImageView) findViewById(R.id.ImageViewIncomeProfilePicture);
        cImageButtonIncomeAnswer = (ImageButton) findViewById(R.id.ImageButtonIncomeAnswer);
        cImageButtonIncomeDecline = (ImageButton) findViewById(R.id.ImageButtonIncomeDecline);

        cImageButtonIncomeAnswer.setOnClickListener(mClickListener);
        cImageButtonIncomeDecline.setOnClickListener(mClickListener);

        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            final TextView remoteUser = (TextView) findViewById(R.id.TextIncomeUser);
            final MyClass mc = new MyClass();
            cDatabaseRef.child("user").child(call.getRemoteUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    remoteUser.setText(user.fullName);
                    mc.SetImage(IncomingCallScreenActivity.this,cImageViewIncomeProfilePicture,user.profilePicture,dataSnapshot.getKey());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            try {
                call.answer();
                Intent intent = new Intent(this, CallScreenActivity.class);
                intent.putExtra(SinchService.CALL_ID, mCallId);
                startActivity(intent);
            } catch (MissingPermissionException e) {
                ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
            }
        } else {
            finish();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now answer the call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended, cause: " + cause.toString());
            mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ImageButtonIncomeAnswer:
                    answerClicked();
                    break;
                case R.id.ImageButtonIncomeDecline:
                    declineClicked();
                    break;
            }
        }
    };
}
