package com.zoazh.le.ComPract.controller.sub;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.main.ProfileActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.controller.start.MainActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.SinchService;
import com.zoazh.le.ComPract.model.database.User;

public class AccountActivity extends BaseActivity  {



    TextView textEmail;
    ConstraintLayout layoutEmail;
    ConstraintLayout layoutPassword;
    ConstraintLayout layoutLogout;
    Button btnDeleteAcc;


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.LayoutLogout:
                    Logout();
                    break;
                case R.id.LayoutEmail:
                    break;
                case R.id.LayoutPassword:
                    startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                    break;
                case R.id.buttonDeleteAccount:
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(AccountActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(AccountActivity.this);
                    }
                    builder.setTitle("Delete account")
                            .setMessage("Are you sure you want to delete this account?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
//                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                    FirebaseAuth cAuth = FirebaseAuth.getInstance();
//                                    DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
//                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).removeValue();
//                                    user.delete()
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//                                                        Logout();
//                                                        Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
//                                                    }else {
//                                                        Toast.makeText(getApplicationContext(),"Fail to delete this account",Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            });
                                    //do delete

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing

                                }
                            })
                            .setIcon(R.drawable.alert_black)
                            .show();
                    break;
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        textEmail = (TextView) findViewById(R.id.textEmail);
        layoutLogout = (ConstraintLayout) findViewById(R.id.LayoutLogout);
        layoutPassword = (ConstraintLayout) findViewById(R.id.LayoutPassword);
        layoutEmail = (ConstraintLayout) findViewById(R.id.LayoutEmail);
        btnDeleteAcc = (Button) findViewById(R.id.buttonDeleteAccount);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        textEmail.setText(email);

        layoutLogout.setOnClickListener(onClick);
        layoutPassword.setOnClickListener(onClick);
        layoutEmail.setOnClickListener(onClick);
        btnDeleteAcc.setOnClickListener(onClick);



    }


}
