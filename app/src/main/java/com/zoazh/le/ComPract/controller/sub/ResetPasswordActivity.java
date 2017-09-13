package com.zoazh.le.ComPract.controller.sub;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.zoazh.le.ComPract.R;

import static android.widget.Toast.LENGTH_SHORT;


public class ResetPasswordActivity extends AppCompatActivity {
    public static final String TAG = ResetPasswordActivity.class.getSimpleName();
    Button btn;
    EditText edt;
    ImageView img;
    ImageView loadingImg;
    Animation animation;
    Animation animationRotate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        img = (ImageView) findViewById(R.id.Logo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        img.startAnimation(animation);
        loadingImg = (ImageView) findViewById(R.id.loadingImage);
        loadingImg.setVisibility(View.INVISIBLE);
        //animationRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        //loadingImg.startAnimation(animationRotate);
        //loadingImg.setVisibility(View.VISIBLE);

        edt = (EditText) findViewById(R.id.editTextEmail);
        btn = (Button) findViewById(R.id.SendButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = edt.getText().toString();
                if (emailAddress.matches("")) {
                    Toast.makeText(ResetPasswordActivity.this, "You did not enter a email.", Toast.LENGTH_SHORT).show();
                    return;
                }
                animationRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                loadingImg.startAnimation(animationRotate);
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingImg.clearAnimation();
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "We couldn't find your account with that email!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

    }
}
