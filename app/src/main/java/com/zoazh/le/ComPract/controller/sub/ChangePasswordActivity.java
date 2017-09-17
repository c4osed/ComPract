package com.zoazh.le.ComPract.controller.sub;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zoazh.le.ComPract.R;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText editCurrentPass;
    EditText editNewPass;
    EditText editConfirmPass;
    Button btnChangePass;
    TextView textForgotpass;
    ImageView imgLoading;
    Animation animationRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        editConfirmPass = (EditText) findViewById(R.id.editTextConfirmPassword);
        editCurrentPass = (EditText) findViewById(R.id.editTextCurrentPassword);
        editNewPass = (EditText) findViewById(R.id.editTextNewPassword);
        btnChangePass = (Button) findViewById(R.id.buttonChangPassword);
        textForgotpass = (TextView) findViewById(R.id.textViewForgotPass);
        imgLoading = (ImageView) findViewById(R.id.imageViewLoading);

        imgLoading.setVisibility(View.INVISIBLE);

        textForgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationRotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
                imgLoading.startAnimation(animationRotate);
                if (editNewPass.getText().toString().matches("")|editConfirmPass.getText().toString().matches("")|editCurrentPass.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();
                    imgLoading.clearAnimation();
                }else {
                    if(editNewPass.getText().toString().matches(editConfirmPass.getText().toString())&&editNewPass.getText().toString().length()>5){
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String email = user.getEmail().toString();
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(email,editCurrentPass.getText().toString());

                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(editNewPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        imgLoading.clearAnimation();
                                                        Toast.makeText(getApplicationContext(),"Password updated",Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        imgLoading.clearAnimation();
                                                        Toast.makeText(getApplicationContext(),"Error password not update",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            imgLoading.clearAnimation();
                                            Toast.makeText(getApplicationContext(),"Error password not update",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else if(editNewPass.getText().toString().matches(editConfirmPass.getText().toString())&&editNewPass.getText().toString().length()<6){
                        imgLoading.clearAnimation();
                        Toast.makeText(getApplicationContext(),"The new password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                    }else {
                        imgLoading.clearAnimation();
                        Toast.makeText(getApplicationContext(),"The new password does not match the confirm password",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
