package com.zoazh.le.ComPract.controller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.calling.Call;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.start.LoginActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.OnSwipeTouchListener;
import com.zoazh.le.ComPract.model.SinchService;

public class PlaceCallActivity extends BaseActivity {

    private FirebaseAuth.AuthStateListener cAuthListener;
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();

    private Button mCallButton;
    private EditText mCallName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCallName = (EditText) findViewById(R.id.InputEmail);
        mCallButton = (Button) findViewById(R.id.ButtonCall);
        mCallButton.setEnabled(false);
        mCallButton.setOnTouchListener(new OnSwipeTouchListener(PlaceCallActivity.this) {
            public void onSwipeRight() {
                Toast.makeText(PlaceCallActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(PlaceCallActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

        });
        mCallButton.setOnClickListener(buttonClickListener);

        Button stopButton = (Button) findViewById(R.id.ButtonEnd);
        stopButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onServiceConnected() {
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(cAuth.getCurrentUser().getEmail());

        }
        TextView userName = (TextView) findViewById(R.id.TextUserEmail);
        userName.setText(getSinchServiceInterface().getUserName());
        mCallButton.setEnabled(true);
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        cAuth.signOut();
        startActivity(new Intent(PlaceCallActivity.this,LoginActivity.class));
        finish();
    }

    private void callButtonClicked() {
        String userName = mCallName.getText().toString();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Call call = getSinchServiceInterface().callUser(userName);
            if (call == null) {
                // Service failed for some reason, show a Toast and abort
                Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
                        + "placing a call.", Toast.LENGTH_LONG).show();
                return;
            }
            String callId = call.getCallId();
            Intent callScreen = new Intent(this, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            startActivity(callScreen);
        } catch (MissingPermissionException e) {
            ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now place a call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ButtonCall:
                    callButtonClicked();
                    break;

                case R.id.ButtonEnd:
                    stopButtonClicked();
                    break;

            }
        }
    };
}
