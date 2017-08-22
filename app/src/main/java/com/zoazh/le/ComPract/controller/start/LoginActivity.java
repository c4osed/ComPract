package com.zoazh.le.ComPract.controller.start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sinch.android.rtc.SinchError;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.sub.EditProfileActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.SinchService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends BaseActivity implements SinchService.StartFailedListener {
    StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();

    FirebaseAuth cAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener cAuthListener;


    private EditText cInputEmail;
    private EditText cInputPassword;
    private Button cButtonLogin;
    private Button cButtonSignUp;
    private Button cButtonLoginFacebook;
    private TextView cTextForgotPassword;

    private CallbackManager mCallbackManager;


    private ProgressDialog mSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        cInputEmail = (EditText) findViewById(R.id.InputEmail);
        cInputPassword = (EditText) findViewById(R.id.InputPassword);
        cButtonLogin = (Button) findViewById(R.id.ButtonLogin);
        cButtonSignUp = (Button) findViewById(R.id.ButtonSignUp);
        //cButtonLoginFacebook = (Button) findViewById(R.id.ButtonLoginFacebook);
        cTextForgotPassword = (TextView) findViewById(R.id.TextForgotPassword);

        LoginButton cButtonLoginFacebook = (LoginButton) findViewById(R.id.ButtonLoginFacebook);

        cButtonLogin.setEnabled(false);

        cButtonLogin.setOnClickListener(clickListener);
        cButtonSignUp.setOnClickListener(clickListener);
        cButtonLoginFacebook.setOnClickListener(clickListener);
        cTextForgotPassword.setOnClickListener(clickListener);

        mCallbackManager = CallbackManager.Factory.create();
        cButtonLoginFacebook.setReadPermissions("email", "public_profile");
        cButtonLoginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),"Success!!!",Toast.LENGTH_LONG).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Canceled!!!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"A network error (such as timeout, interrupted connection on unreachable host) has occurred. 5",Toast.LENGTH_LONG).show();
            }
        });


//        File localFile = null;
//        try {
//            localFile = File.createTempFile("images", "jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        final File finalLocalFile = localFile;
//        cStorageRef.child("User/c@c.com/ProfilePicture").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                ImageView c = (ImageView) findViewById(R.id.ImageViewLogo);
//                Toast.makeText(LoginActivity.this, finalLocalFile.toString(),Toast.LENGTH_LONG).show();
//                Picasso.with(getApplicationContext()).load(finalLocalFile).into(c);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

//        cStorageRef.child("User/c@c.com/ProfilePicture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                ImageView c = (ImageView) findViewById(R.id.ImageViewLogo);
//                Picasso.with(getApplicationContext()).load(uri).into(c);
//                Toast.makeText(LoginActivity.this,uri.toString(),Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//
//                Toast.makeText(LoginActivity.this,exception.getMessage(),Toast.LENGTH_LONG).show();;
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        showSpinner();
        cAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            mSpinner.dismiss();
                        } else {
                            cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        if (!getSinchServiceInterface().isStarted()) {
                                            getSinchServiceInterface().startClient(cAuth.getCurrentUser().getUid());
                                        } else {
                                            openPlaceCallActivity();
                                            finish();
                                        }
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, EditProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ButtonLogin:
                    login();
                    break;
                case R.id.ButtonSignUp:
                    signUp();
                    break;
                case R.id.TextForgotPassword:
                    forgotPassword();
                    break;
            }
        }
    };

    private void login() {
        final String email = cInputEmail.getText().toString().trim();
        String password = cInputPassword.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty()) {
            showSpinner();
            cAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                mSpinner.dismiss();
                            } else {
                                if (!getSinchServiceInterface().isStarted()) {
                                    getSinchServiceInterface().startClient(cAuth.getCurrentUser().getUid());
                                } else {
                                    openPlaceCallActivity();
                                    finish();
                                }
                            }

                        }
                    });
        } else {
            Toast.makeText(this, getResources().getString(R.string.required), Toast.LENGTH_LONG).show();
        }

    }

    private void signUp() {
        Intent SignUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(SignUpIntent);
    }

    private void forgotPassword() {
    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    protected void onServiceConnected() {
        cButtonLogin.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
        openPlaceCallActivity();
    }


    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.setCancelable(false);
        mSpinner.show();
    }
}
