package com.zoazh.le.ComPract.controller.sub;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.main.ProfileActivity;

public class SettingActivity extends AppCompatActivity {
    ConstraintLayout layoutEditprofile;
    ConstraintLayout layoutAccount;
    ConstraintLayout layoutPrivacy;
    ConstraintLayout layoutNotification;

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.LayoutEditProfile:
                    startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                    break;
                case R.id.LayoutAccount:
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                    break;
                case R.id.LayoutPrivacy:
                    startActivity(new Intent(getApplicationContext(), PrivacyActivity.class));
                    break;
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        layoutEditprofile = (ConstraintLayout) findViewById(R.id.LayoutEditProfile);
        layoutAccount = (ConstraintLayout) findViewById(R.id.LayoutAccount);
        layoutPrivacy = (ConstraintLayout) findViewById(R.id.LayoutPrivacy);
        layoutNotification = (ConstraintLayout) findViewById(R.id.LayoutNotification);

        layoutEditprofile.setOnClickListener(onClick);
        layoutAccount.setOnClickListener(onClick);
        layoutPrivacy.setOnClickListener(onClick);
        layoutNotification.setOnClickListener(onClick);
    }
}
