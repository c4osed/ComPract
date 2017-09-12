package com.zoazh.le.ComPract.controller;

import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CallScreenActivity extends BaseActivity {

    static final String TAG = CallScreenActivity.class.getSimpleName();

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;

    private String mCallId;

    private TextView mCallerName;
    private ImageView cImageViewCallingProfilePicture;
    private TextView mCallState;
    private TextView mCallDuration;
    private ImageButton cImageButtonEndCall;
    private ImageButton cImageButtonSpeaker;

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CallScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        mAudioPlayer = new AudioPlayer(this);
        mCallerName = (TextView) findViewById(R.id.TextCallingUser);
        cImageViewCallingProfilePicture = (ImageView) findViewById(R.id.ImageViewCallingProfilePicture);
        mCallState = (TextView) findViewById(R.id.TextCallingState);
        mCallDuration = (TextView) findViewById(R.id.TextCallingDuration);
        cImageButtonEndCall = (ImageButton) findViewById(R.id.ImageButtonCallingEnd);
        cImageButtonSpeaker = (ImageButton) findViewById(R.id.ImageButtonSpeaker);

        cImageButtonEndCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall();
            }
        });

        cImageButtonSpeaker.setOnClickListener(clickListener);
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        //mCallId = getIntent().getStringExtra();

    }

    private View.OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ImageButtonSpeaker:
                    SpeakerMode();
                    break;
            }
        }
    };

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            final MyClass mc = new MyClass();
            cDatabaseRef.child("user").child(call.getRemoteUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    mc.SetImage(CallScreenActivity.this, cImageViewCallingProfilePicture, user.profilePicture, dataSnapshot.getKey());
                    mCallerName.setText(user.fullName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //mCallerName.setText(call.getRemoteUserId());
            mCallState.setText(call.getState().toString());
        } else {

            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }

//        Call call = getSinchServiceInterface().getCall(mCallId);
//        if (call != null) {
//            call.addCallListener(new SinchCallListener());
//            mCallerName.setText(call.getRemoteUserId());
//            mCallState.setText(call.getState().toString());
//        } else {
//            Log.e(TAG, "Started with invalid callId, aborting.");
//            finish();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDurationTask.cancel();
        mTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
    }

    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    private void endCall() {
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private String formatTimespan(int totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void updateCallDuration() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            mCallDuration.setText(formatTimespan(call.getDetails().getDuration()));
        }
    }


    public void SpeakerMode() {
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        if (audioManager.isSpeakerphoneOn()) {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(false);
            cImageButtonSpeaker.setBackgroundColor(getResources().getInteger(R.color.transparent));
        } else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(true);
            cImageButtonSpeaker.setBackgroundColor(getResources().getInteger(R.color.silver));
        }
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String endCause = call.getDetails().getEndCause().toString();
            switch (endCause) {
                case "CANCELED":
                    endCause = "Canceled";
                    break;
                case "DENIED":
                    endCause = "Denied";
                    break;
                case "TIMEOUT":
                    endCause = "Timeout";
                    break;
                case "HUNG_UP":
                    endCause = "Hang Up";
                    break;
            }
//            if(){
//
//                timeput
//                        denied
//            }else{
            Toast.makeText(CallScreenActivity.this, endCause, Toast.LENGTH_LONG).show();
//            }
            endCall();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            mAudioPlayer.stopProgressTone();
            mCallState.setText(call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(false);
        }


        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // GCM
        }

    }
}
