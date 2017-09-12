package com.zoazh.le.ComPract.controller.start;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.CaseFormat;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.CircleTransform;
import com.zoazh.le.ComPract.model.database.User;

import org.apache.commons.lang3.text.WordUtils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    FirebaseAuth cAuth = FirebaseAuth.getInstance();
    DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();

    Calendar myCalendar;
    ProgressDialog cProgress;


    EditText cInputRegisterEmail;
    EditText cInputRegisterPassword;
    EditText cInputRegisterConfirmPassword;
    EditText cInputRegisterFirstName;
    EditText cInputRegisterLastName;
    Button cButtonRegisterDOB;
    Button cButtonRegisterGender;
    Button cButtonRegisterCountry;
    Button cButtonRegisterNative;
    Button cButtonRegisterLearn;
    ImageButton cButtonRegisterProfilePicture;
    Button cButtonRegisterSignUp;

    List<String> cListLanguage = new ArrayList<String>();
    List<String> cListCountry = new ArrayList<String>();
    List<String> cListLearn = new ArrayList<String>();
    HashMap<String, String> cMapLearn = new HashMap<String, String>();

    int cGenderSelected = 0;
    int cCountrySelected = 0;
    int cNativeSelected = 0;
    boolean cLearnStart = true;
    boolean cLearnChecked[];
    String cLearnFull = null;
    String cLearnAbbreviation = null;

    private static final int SELECT_PROFILE_PICTURE = 1;
    Uri cProfilePicture = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cProgress = new ProgressDialog(this);

        myCalendar = Calendar.getInstance();

        cInputRegisterEmail = (EditText) findViewById(R.id.InputRegisterEmail);
        cInputRegisterPassword = (EditText) findViewById(R.id.InputRegisterPassword);
        cInputRegisterConfirmPassword = (EditText) findViewById(R.id.InputRegisterConfirmPassword);
        cInputRegisterFirstName = (EditText) findViewById(R.id.InputRegisterFirstName);
        cInputRegisterLastName = (EditText) findViewById(R.id.InputRegisterLastName);
        cButtonRegisterDOB = (Button) findViewById(R.id.ButtonRegisterDOB);
        cButtonRegisterGender = (Button) findViewById(R.id.ButtonRegisterGender);
        cButtonRegisterCountry = (Button) findViewById(R.id.ButtonRegisterCountry);
        cButtonRegisterNative = (Button) findViewById(R.id.ButtonRegisterNative);
        cButtonRegisterLearn = (Button) findViewById(R.id.ButtonRegisterLearn);
        cButtonRegisterProfilePicture = (ImageButton) findViewById(R.id.ButtonRegisterProfilePicture);
        cButtonRegisterSignUp = (Button) findViewById(R.id.ButtonRegisterSignUp);

        cButtonRegisterDOB.setOnClickListener(clickListener);
        cButtonRegisterGender.setOnClickListener(clickListener);
        cButtonRegisterCountry.setOnClickListener(clickListener);
        cButtonRegisterNative.setOnClickListener(clickListener);
        cButtonRegisterLearn.setOnClickListener(clickListener);
        cButtonRegisterProfilePicture.setOnClickListener(clickListener);
        cButtonRegisterSignUp.setOnClickListener(clickListener);

        cDatabaseRef.child("country").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    cListCountry.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        cDatabaseRef.child("language").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cLearnChecked = new boolean[(int) dataSnapshot.getChildrenCount()];
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    cMapLearn.put(data.getKey(), data.getValue().toString());
                    cListLanguage.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            switch (v.getId()) {
                case R.id.ButtonRegisterDOB:
                    setDOB();
                    break;
                case R.id.ButtonRegisterGender:
                    setGender();
                    break;
                case R.id.ButtonRegisterCountry:
                    setCountry();
                    break;
                case R.id.ButtonRegisterNative:
                    setNative();
                    break;
                case R.id.ButtonRegisterLearn:
                    setLearn();
                    break;
                case R.id.ButtonRegisterProfilePicture:
                    setProfilePicture();
                    break;
                case R.id.ButtonRegisterSignUp:
                    signUp();
                    break;
            }
        }
    };


    private void setDOB() {
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog setDOB = new DatePickerDialog(SignUpActivity.this, SignUpActivity.this, year, month, day);
        setDOB.show();
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        if (new Date().before(myCalendar.getTime())) {
            Toast.makeText(SignUpActivity.this, "Date of birth is incorrect!!!", Toast.LENGTH_LONG).show();
            cButtonRegisterDOB.setText("");
        } else {
            cButtonRegisterDOB.setText(sdf.format(myCalendar.getTime()));
        }

    }

    private void setGender() {
        AlertDialog.Builder setGender = new AlertDialog.Builder(this);
        setGender.setTitle("Select Gender");

        setGender.setSingleChoiceItems(getResources().getStringArray(R.array.gender), cGenderSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cGenderSelected = which;
            }
        });

        setGender.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gender = "Male";
                if (cGenderSelected == 1) {
                    gender = "Female";
                }
                cButtonRegisterGender.setText(gender);

//                if (cButtonRegisterCountry.getText().equals("Country")) {
//                    setCountry();
//                }
            }
        });

        setGender.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        setGender.show();
    }

    private void setCountry() {
        AlertDialog.Builder setCountry = new AlertDialog.Builder(this);
        setCountry.setTitle("Select Country");
        final String[] cCountry = new String[cListCountry.size()];
        cListCountry.toArray(cCountry);
        setCountry.setSingleChoiceItems(cCountry, cCountrySelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cCountrySelected = which;
            }
        });

        setCountry.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cButtonRegisterCountry.setText(cCountry[cCountrySelected]);
//                if (cButtonRegisterNative.getText().equals("Native")) {
//                    setNative();
//                }
            }
        });
        setCountry.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        setCountry.show();
    }

    private void setNative() {
        AlertDialog.Builder setNative = new AlertDialog.Builder(this);

        final String[] cNative = new String[cListLanguage.size()];
        setNative.setTitle("Select Native");
        cListLanguage.toArray(cNative);
        setNative.setSingleChoiceItems(cNative, cNativeSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cNativeSelected = which;
            }
        });

        setNative.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cButtonRegisterNative.setText(cNative[cNativeSelected]);
//                if (cButtonRegisterLearn.getText().equals("Learn")) {
//                    setLearn();
//                }
            }
        });
        setNative.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        setNative.show();
    }

    private void setLearn() {
        cListLearn.clear();
        AlertDialog.Builder setLearn = new AlertDialog.Builder(this);

        final String[] cLearn = new String[cListLanguage.size()];
        cListLanguage.toArray(cLearn);
//        if (cLearnStart) {
//            cLearnChecked = new boolean[cLearn.length];
//            for (int i = 0; i < cLearn.length; i++) {
//                cLearnChecked[i] = false;
//            }
//            cLearnStart = false;
//        }

        setLearn.setTitle("Select Learn");
        setLearn.setMultiChoiceItems(cLearn, cLearnChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                cLearnChecked[which] = isChecked;
            }
        });
        setLearn.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cButtonRegisterLearn.setText("");
                cLearnFull = null;
                for (int i = 0; i < cLearnChecked.length; i++) {
                    boolean checked = cLearnChecked[i];
                    if (checked) {
                        cListLearn.add(cLearn[i]);
                        cButtonRegisterLearn.setText("Learn (" + cListLearn.size() + ")");
                    }
                }
                if (cListLearn.size() == 0) {
                    cButtonRegisterLearn.setText("Learn");
                }
            }
        });

        setLearn.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        setLearn.show();
    }

    private void setProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PROFILE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PROFILE_PICTURE && resultCode == RESULT_OK) {
            cProfilePicture = data.getData();
            Picasso.with(getApplicationContext()).load(cProfilePicture).transform(new CircleTransform()).into(cButtonRegisterProfilePicture);
        }
    }

    private void signUp() {

        final String vEmail = cInputRegisterEmail.getText().toString().trim().toLowerCase();
        final String vPassword = cInputRegisterPassword.getText().toString().trim();
        String vConfirmPassword = cInputRegisterConfirmPassword.getText().toString().trim();
        final String vFirstName = WordUtils.capitalizeFully(cInputRegisterFirstName.getText().toString().trim());
        final String vLastName = WordUtils.capitalizeFully(cInputRegisterLastName.getText().toString().trim());
        final String vDOB = cButtonRegisterDOB.getText().toString().trim();
        final String vGender = cButtonRegisterGender.getText().toString().trim();
        final String vCountry = cButtonRegisterCountry.getText().toString().trim();
        final String vNative = cButtonRegisterNative.getText().toString().trim();
        String vLearn = cButtonRegisterLearn.getText().toString().trim();

        if (!TextUtils.isEmpty(vEmail)) {
            if (vPassword.length() > 5) {
                if (vPassword.equals(vConfirmPassword)) {
                    if (!TextUtils.isEmpty(vPassword)) {
                        if (!TextUtils.isEmpty(vFirstName)) {
                            if (!TextUtils.isEmpty(vLastName)) {
                                if (!TextUtils.isEmpty(vDOB)) {
                                    if (!vGender.equals("Gender")) {
                                        if (!vCountry.equals("Country")) {
                                            if (!vNative.equals("Native")) {
                                                if (!vLearn.equals("Learn")) {
                                                    cProgress.setMessage("Signing Up...");
                                                    cProgress.show();
                                                    if (cAuth.getCurrentUser() != null) {
                                                        String vLearnFull = null;
                                                        String vLearnAbbreviation = null;
                                                        for (String learn : cListLearn) {
                                                            vLearnFull = (vLearnFull == null ? "" : vLearnFull + ",") + learn;
                                                            vLearnAbbreviation = (vLearnAbbreviation == null ? "" : vLearnAbbreviation + ",") + cMapLearn.get(learn);
                                                        }
                                                        User user = new User(vFirstName + " " + vLastName, vFirstName, vLastName, vEmail, vDOB, vGender, vCountry, vNative, vLearnFull, vLearnAbbreviation, null, "", "", 0, 0, 0, 0, 1, 0, 1, 0);
                                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).setValue(user);
                                                        if (cProfilePicture != null) {
                                                            cButtonRegisterProfilePicture.setDrawingCacheEnabled(true);
                                                            cButtonRegisterProfilePicture.buildDrawingCache();
                                                            Bitmap bitmap = cButtonRegisterProfilePicture.getDrawingCache();
                                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                            byte[] data = baos.toByteArray();

                                                            UploadTask uploadTask = cStorageRef.child("user").child(cAuth.getCurrentUser().getUid()).child("profilePicture").putBytes(data);
                                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception exception) {
                                                                    Toast.makeText(SignUpActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("profilePicture").setValue(taskSnapshot.getDownloadUrl().toString());

                                                                }
                                                            });
//                                    cStorageRef.child("user").child(vEmail).child("profilePicture").putFile(cProfilePicture).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                        @Override
//                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                            cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("profilePicture").setValue(taskSnapshot.getDownloadUrl().toString());
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                                        }
//                                    });
                                                        }

                                                        for (String Learn : cListLearn) {
                                                            cDatabaseRef.child("learn").child(cAuth.getCurrentUser().getUid()).child(Learn.toLowerCase()).setValue(cMapLearn.get(Learn).toLowerCase());
                                                        }
                                                    } else {
                                                        cAuth.createUserWithEmailAndPassword(vEmail, vPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    String vLearnFull = null;
                                                                    String vLearnAbbreviation = null;
                                                                    for (String learn : cListLearn) {
                                                                        vLearnFull = (vLearnFull == null ? "" : vLearnFull + ",") + learn;
                                                                        vLearnAbbreviation = (vLearnAbbreviation == null ? "" : vLearnAbbreviation + ",") + cMapLearn.get(learn);
                                                                    }
//                                for (String learn : cListLearn) {
//                                    vLearnFull = (vLearnFull == null ? "" : vLearnFull + ",") + learn;
//                                    cDatabaseRef.child("language").child(learn).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                                vLearnAbbreviation = (vLearnAbbreviation == null ? "" : vLearnAbbreviation + ",") + data.getValue();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
                                                                    User user = new User(vFirstName + " " + vLastName, vFirstName, vLastName, vEmail, vDOB, vGender, vCountry, vNative, vLearnFull, vLearnAbbreviation, null, "", "", 0, 0, 0, 0, 1, 0, 1, 0);
                                                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).setValue(user);
                                                                    if (cProfilePicture != null) {
                                                                        cButtonRegisterProfilePicture.setDrawingCacheEnabled(true);
                                                                        cButtonRegisterProfilePicture.buildDrawingCache();
                                                                        Bitmap bitmap = cButtonRegisterProfilePicture.getDrawingCache();
                                                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                                        byte[] data = baos.toByteArray();

                                                                        UploadTask uploadTask = cStorageRef.child("user").child(cAuth.getCurrentUser().getUid()).child("profilePicture").putBytes(data);
                                                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception exception) {
                                                                                Toast.makeText(SignUpActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("profilePicture").setValue(taskSnapshot.getDownloadUrl().toString());

                                                                            }
                                                                        });
//                                    cStorageRef.child("user").child(vEmail).child("profilePicture").putFile(cProfilePicture).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                        @Override
//                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                            cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("profilePicture").setValue(taskSnapshot.getDownloadUrl().toString());
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                                        }
//                                    });
                                                                    }

                                                                    for (String Learn : cListLearn) {
                                                                        cDatabaseRef.child("learn").child(cAuth.getCurrentUser().getUid()).child(Learn.toLowerCase()).setValue(cMapLearn.get(Learn).toLowerCase());
                                                                    }

                                                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                                                    Toast.makeText(SignUpActivity.this, "Successfully signed up!", Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage() + "!!!", Toast.LENGTH_LONG).show();
                                                                }

                                                                cProgress.dismiss();
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, "Learn field is required!!!", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Native field is required!!!", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Country field is required!!!", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Gender field is required!!!", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Date of birth field is required!!!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, "Last Name field is required!!!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "First Name field is required!!!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Password field is required!!!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match!!!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Password must contain at least 6 characters!!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(SignUpActivity.this, "Email field is required!!!", Toast.LENGTH_LONG).show();
        }
    }
}
