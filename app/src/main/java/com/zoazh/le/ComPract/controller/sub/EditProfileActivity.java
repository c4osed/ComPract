package com.zoazh.le.ComPract.controller.sub;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.main.ProfileActivity;
import com.zoazh.le.ComPract.controller.start.MainActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.CircleTransform;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.database.User;

import org.apache.commons.lang3.text.WordUtils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    FirebaseAuth cAuth = FirebaseAuth.getInstance();
    DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();

    Calendar myCalendar;
    ProgressDialog cProgress;


    EditText cInputEditProfileFirstName;
    EditText cInputEditProfileLastName;
    EditText cInputEditProfileAbout;
    Button cButtonEditProfileDOB;
    Button cButtonEditProfileGender;
    Button cButtonEditProfileCountry;
    Button cButtonEditProfileNative;
    Button cButtonEditProfileLearn;
    ImageButton cButtonEditProfileProfilePicture;
    Button cButtonEditProfile;

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
    String cLearnUser = null;

    boolean setup = true;

    private static final int SELECT_PROFILE_PICTURE = 1;
    Uri cProfilePicture = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        cProgress = new ProgressDialog(this);

        myCalendar = Calendar.getInstance();

        cInputEditProfileFirstName = (EditText) findViewById(R.id.InputEditProfileFirstName);
        cInputEditProfileLastName = (EditText) findViewById(R.id.InputEditProfileLastName);
        cInputEditProfileAbout = (EditText) findViewById(R.id.InputEditProfileAbout);
        cButtonEditProfileDOB = (Button) findViewById(R.id.ButtonEditProfileDOB);
        cButtonEditProfileGender = (Button) findViewById(R.id.ButtonEditProfileGender);
        cButtonEditProfileCountry = (Button) findViewById(R.id.ButtonEditProfileCountry);
        cButtonEditProfileNative = (Button) findViewById(R.id.ButtonEditProfileNative);
        cButtonEditProfileLearn = (Button) findViewById(R.id.ButtonEditProfileLearn);
        cButtonEditProfileProfilePicture = (ImageButton) findViewById(R.id.ButtonEditProfileProfilePicture);
        cButtonEditProfile = (Button) findViewById(R.id.ButtonEditProfile);

        cButtonEditProfileDOB.setOnClickListener(clickListener);
        cButtonEditProfileGender.setOnClickListener(clickListener);
        cButtonEditProfileCountry.setOnClickListener(clickListener);
        cButtonEditProfileNative.setOnClickListener(clickListener);
        cButtonEditProfileLearn.setOnClickListener(clickListener);
        cButtonEditProfileProfilePicture.setOnClickListener(clickListener);
        cButtonEditProfile.setOnClickListener(clickListener);

        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);
                    MyClass mc = new MyClass();
                    mc.SetImage(EditProfileActivity.this, cButtonEditProfileProfilePicture, user.profilePicture, user.email);
                    cInputEditProfileFirstName.setText(user.firstName);
                    cInputEditProfileLastName.setText(user.lastName);
                    cInputEditProfileAbout.setText(user.about);
                    cButtonEditProfileDOB.setText(user.DOB);
                    cButtonEditProfileGender.setText(user.gender);
                    cButtonEditProfileCountry.setText(user.country);
                    cButtonEditProfileNative.setText(user.nativeLanguage);
                    cLearnUser = user.learnFull;

                    if (cButtonEditProfileGender.getText().equals("Female")) {
                        cGenderSelected = 1;
                    }
                } else {
                    setup = false;
                    String fullName = cAuth.getCurrentUser().getDisplayName();
                    String firstName = fullName.substring(0, fullName.indexOf(" "));
                    String lastName = fullName.replace(firstName + " ", "");
                    cProfilePicture = cAuth.getCurrentUser().getPhotoUrl();
                    Picasso.with(getApplicationContext()).load(cProfilePicture).transform(new CircleTransform()).into(cButtonEditProfileProfilePicture);
                    cInputEditProfileFirstName.setText(firstName);
                    cInputEditProfileLastName.setText(lastName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cDatabaseRef.child("country").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    cListCountry.add(data.getKey().toString());
                    if (data.getKey().toString().equals(cButtonEditProfileCountry.getText())) {
                        cCountrySelected = i;
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        cDatabaseRef.child("language").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                int x = 0;
                cLearnChecked = new boolean[(int) dataSnapshot.getChildrenCount()];
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    cMapLearn.put(data.getKey(), data.getValue().toString());
                    cListLanguage.add(data.getKey().toString());
                    if (setup) {
                        if (data.getKey().toString().equals(cButtonEditProfileNative.getText())) {
                            cNativeSelected = i;
                        }
                        if (cLearnUser.contains(data.getKey().toString())) {
                            x++;
                            cLearnChecked[i] = true;
                            cListLearn.add(data.getKey().toString());
                        }
                        i++;
                    }
                }
                cButtonEditProfileLearn.setText("Learn (" + x + ")");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
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
                case R.id.ButtonEditProfileDOB:
                    setDOB();
                    break;
                case R.id.ButtonEditProfileGender:
                    setGender();
                    break;
                case R.id.ButtonEditProfileCountry:
                    setCountry();
                    break;
                case R.id.ButtonEditProfileNative:
                    setNative();
                    break;
                case R.id.ButtonEditProfileLearn:
                    setLearn();
                    break;
                case R.id.ButtonEditProfileProfilePicture:
                    setProfilePicture();
                    break;
                case R.id.ButtonEditProfile:
                    editProfile();
                    break;
            }
        }
    };


    private void setDOB() {
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog setDOB = new DatePickerDialog(EditProfileActivity.this, EditProfileActivity.this, year, month, day);
        setDOB.show();
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        cButtonEditProfileDOB.setText(sdf.format(myCalendar.getTime()));

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
                cButtonEditProfileGender.setText(gender);

//                if (cButtonEditProfileCountry.getText().equals("Country")) {
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
                cButtonEditProfileCountry.setText(cCountry[cCountrySelected]);
//                if (cButtonEditProfileNative.getText().equals("Native")) {
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
        final AlertDialog.Builder setNative = new AlertDialog.Builder(this);

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
                cButtonEditProfileNative.setText(cNative[cNativeSelected]);
//                if (cButtonEditProfileLearn.getText().equals("Learn")) {
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
        final AlertDialog.Builder setLearn = new AlertDialog.Builder(this);

        final String[] cLearn = new String[cListLanguage.size()];
        cListLanguage.toArray(cLearn);
//        if (cLearnStart) {
//            //cLearnChecked = new boolean[cLearn.length];
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
                cButtonEditProfileLearn.setText("");
                cLearnFull = null;
                for (int i = 0; i < cLearnChecked.length; i++) {
                    boolean checked = cLearnChecked[i];
                    if (checked) {
                        cListLearn.add(cLearn[i]);
                        cButtonEditProfileLearn.setText("Learn (" + cListLearn.size() + ")");
                    }
                }
                if (cListLearn.size() == 0) {
                    cButtonEditProfileLearn.setText("Learn");
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
            Picasso.with(getApplicationContext()).load(cProfilePicture).transform(new CircleTransform()).into(cButtonEditProfileProfilePicture);
        }
    }

    private void editProfile() {
        String vFirstName = WordUtils.capitalizeFully(cInputEditProfileFirstName.getText().toString());
        String vLastName = WordUtils.capitalizeFully(cInputEditProfileLastName.getText().toString());
        String vAbout = cInputEditProfileAbout.getText().toString();
        final String vDOB = cButtonEditProfileDOB.getText().toString().trim();
        final String vGender = cButtonEditProfileGender.getText().toString().trim();
        final String vCountry = cButtonEditProfileCountry.getText().toString().trim();
        final String vNative = cButtonEditProfileNative.getText().toString().trim();
        final String vLearn = cButtonEditProfileLearn.getText().toString().trim();

        String vLearnFull = null;
        String vLearnAbbreviation = null;

        for (String learn : cListLearn) {
            vLearnFull = (vLearnFull == null ? "" : vLearnFull + ",") + learn;
            vLearnAbbreviation = (vLearnAbbreviation == null ? "" : vLearnAbbreviation + ",") + cMapLearn.get(learn);
        }
        if (!TextUtils.isEmpty(vFirstName)) {
            if (!TextUtils.isEmpty(vLastName)) {
                if (!TextUtils.isEmpty(vDOB)) {
                    if (!vGender.equals("Gender")) {
                        if (!vCountry.equals("Country")) {
                            if (!vNative.equals("Native")) {
                                if (!vLearn.equals("Learn")) {
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("firstName").setValue(vFirstName);
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("lastName").setValue(vLastName);
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("about").setValue(vAbout);
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("fullName").setValue(vFirstName + " " + vLastName);
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("DOB").setValue(vDOB);
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("gender").setValue(vGender);
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("country").setValue(vCountry);
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("nativeLanguage").setValue(vNative);
//            Toast.makeText(getApplicationContext(), vLearnFull, Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), vLearnAbbreviation, Toast.LENGTH_LONG).show();
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("learnFull").setValue(vLearnFull);
                                    cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("learnAbbreviation").setValue(vLearnAbbreviation);

                                    if (!setup) {
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("about").setValue("");
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("advisorEXP").setValue(0);
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("advisorLevel").setValue(1);
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("answerCount").setValue(0);
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("email").setValue(cAuth.getCurrentUser().getEmail());
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("followerCount").setValue(0);
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("followingCount").setValue(0);
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("onlineTime").setValue("");
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("questionCount").setValue(0);
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("studentEXP").setValue(0);
                                        cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("studentLevel").setValue(1);
                                    }

                                    for (String Learn : cListLearn) {
                                        cDatabaseRef.child("learn").child(cAuth.getCurrentUser().getUid()).child(Learn.toLowerCase()).setValue(cMapLearn.get(Learn).toLowerCase());
                                    }
                                    if (cProfilePicture != null) {
                                        cButtonEditProfileProfilePicture.setDrawingCacheEnabled(true);
                                        cButtonEditProfileProfilePicture.buildDrawingCache();
                                        Bitmap bitmap = cButtonEditProfileProfilePicture.getDrawingCache();
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        byte[] data = baos.toByteArray();

                                        UploadTask uploadTask = cStorageRef.child("user").child(cAuth.getCurrentUser().getEmail()).child("profilePicture").putBytes(data);
                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                Toast.makeText(EditProfileActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("profilePicture").setValue(taskSnapshot.getDownloadUrl().toString());
                                            }
                                        });
                                    }
                                    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                                } else {

                                    Toast.makeText(EditProfileActivity.this, "Learn field is required!!!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Native field is required!!!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Country field is required!!!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Gender field is required!!!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Date of birth field is required!!!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(EditProfileActivity.this, "Last Name field is required!!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(EditProfileActivity.this, "First Name field is required!!!", Toast.LENGTH_LONG).show();
        }
    }


}
