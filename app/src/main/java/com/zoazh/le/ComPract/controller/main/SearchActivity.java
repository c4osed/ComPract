package com.zoazh.le.ComPract.controller.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.sub.ViewProfileActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.database.User;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private ProgressDialog cProgress;
    MyAdapter adapter;

    private EditText cInputSearch;
    private ImageButton cButtonSearch;
    private ImageButton cButtonFilter;

    private ConstraintLayout cLayoutFilter;
    private ConstraintLayout cLayoutAgeRange;
    private EditText cInputAgeRangeX;
    private EditText cInputAgeRangeY;
    private ConstraintLayout cLayoutCountry;
    private TextView cTextCountryResult;
    private ConstraintLayout cLayoutNative;
    private TextView cTextNativeResult;
    private ConstraintLayout cLayoutLearn;
    private TextView cTextLearnResult;
    private ConstraintLayout cLayoutStatus;
    private TextView cTextStatusResult;


    private ListView cListView;

    private ConstraintLayout cLayoutAdvise;
    private ConstraintLayout cBottomBar;
    private ConstraintLayout cLayoutSearch;
    private ImageView cImageViewSearch;
    private ConstraintLayout cLayoutProfile;
    private TextView cTextSearch;

    private List<String> cListCountry = new ArrayList<String>();
    private List<String> cListLanguage = new ArrayList<String>();
    private List<HashMap<String, String>> cListUser = new ArrayList<HashMap<String, String>>();

    private int cCountrySelected = 0;
    private int cNativeSelected = 0;
    private int cLearnSelected = 0;
    private int cStatusSelected = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cInputSearch = (EditText) findViewById(R.id.InputSearch);
        cButtonFilter = (ImageButton) findViewById(R.id.ImageButtonFilter);
        cButtonSearch = (ImageButton) findViewById(R.id.ImageViewSearch);
        cListView = (ListView) findViewById(R.id.ListViewSearch);

        //Filter
        cLayoutFilter = (ConstraintLayout) findViewById(R.id.LayoutFilter);
        cLayoutAgeRange = (ConstraintLayout) cLayoutFilter.findViewById(R.id.LayoutNormalQuestion2);
        cInputAgeRangeX = (EditText) cLayoutFilter.findViewById(R.id.InputAgeRangeX);
        cInputAgeRangeY = (EditText) cLayoutFilter.findViewById(R.id.InputAgeRangeY);
        cLayoutCountry = (ConstraintLayout) cLayoutFilter.findViewById(R.id.LayoutChoiceQuestion);
        cTextCountryResult = (TextView) cLayoutFilter.findViewById(R.id.TextCountryResult);
        cLayoutNative = (ConstraintLayout) cLayoutFilter.findViewById(R.id.LayoutQuestion);
        cTextNativeResult = (TextView) cLayoutFilter.findViewById(R.id.TextNativeResult);
        cLayoutLearn = (ConstraintLayout) cLayoutFilter.findViewById(R.id.LayoutLearn);
        cTextLearnResult = (TextView) cLayoutFilter.findViewById(R.id.TextLearnResult);
        cLayoutStatus = (ConstraintLayout) cLayoutFilter.findViewById(R.id.LayoutStatus);
        cTextStatusResult = (TextView) cLayoutFilter.findViewById(R.id.TextStatusResult);


        //BTM BAR
        cBottomBar = (ConstraintLayout) findViewById(R.id.BottomBar);

        cLayoutAdvise = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutAdvise);
        cLayoutSearch = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutSearch);
        cImageViewSearch = (ImageView) cBottomBar.findViewById(R.id.ImageViewSearch);
        cLayoutProfile = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutProfile);
        cTextSearch = (TextView) cBottomBar.findViewById(R.id.TextSearch);

        cTextSearch.setTextColor(getResources().getInteger(R.color.secondary));
        cImageViewSearch.setColorFilter(getResources().getInteger(R.color.secondary));

        //OnClick
        cLayoutAdvise.setOnClickListener(clickListener);
        cButtonSearch.setOnClickListener(clickListener);
        cLayoutProfile.setOnClickListener(clickListener);
        cButtonFilter.setOnClickListener(clickListener);
        cLayoutAgeRange.setOnClickListener(clickListener);
        cLayoutCountry.setOnClickListener(clickListener);
        cLayoutNative.setOnClickListener(clickListener);
        cLayoutLearn.setOnClickListener(clickListener);
        cLayoutStatus.setOnClickListener(clickListener);

        cInputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Search();
                    return true;
                }
                return false;
            }
        });


        cDatabaseRef.child("country").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    cListCountry.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        cDatabaseRef.child("language").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //cMapLearn.put(WordUtils.capitalizeFully(data.getKey()), data.getValue().toString());
                    cListLanguage.add(data.getKey().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

//        cListView.setOnTouchListener(new OnSwipeTouchListener(SearchActivity.this) {
//            public void onSwipeLeft() {
//                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
//                overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
//            }
//
//            public void onSwipeRight() {
//                Toast.makeText(SearchActivity.this, "right", Toast.LENGTH_SHORT).show();
//            }
//
//        });


        Search();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OnlineTimer(true);
        overridePendingTransition(0,0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        OnlineTimer(false);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (getCurrentFocus() != null) {
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            }
            switch (v.getId()) {
                case R.id.ImageViewSearch:
                    Search();
                    break;
                case R.id.ImageButtonFilter:
                    ShowFilter();
                    break;
                case R.id.LayoutChoiceQuestion:
                    CountryFilter();
                    break;
                case R.id.LayoutQuestion:
                    NativeFilter();
                    break;
                case R.id.LayoutLearn:
                    LearnFilter();
                    break;
                case R.id.LayoutStatus:
                    StatusFilter();
                    break;
                case R.id.LayoutAdvise:
                    startActivity(new Intent(SearchActivity.this, AdviseActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
                case R.id.LayoutProfile:
                    startActivity(new Intent(SearchActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    //.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    //overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                    break;
            }
        }
    };

    private void ShowFilter() {
        if (cLayoutFilter.getVisibility() == View.VISIBLE) {
            cLayoutFilter.setVisibility(View.INVISIBLE);
            cButtonFilter.setImageResource(R.drawable.ic_dropdown);
        } else {
            cLayoutFilter.setVisibility(View.VISIBLE);
            cButtonFilter.setImageResource(R.drawable.ic_dropup);

        }
    }


    private void CountryFilter() {
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
                cTextCountryResult.setText(cCountry[cCountrySelected]);
            }
        });
        setCountry.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cTextCountryResult.getText().equals("Any")) {
                    cCountrySelected = 0;
                }
            }
        });
        setCountry.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cTextCountryResult.setText("Any");
                cCountrySelected = 0;
            }
        });
        setCountry.show();
    }


    private void NativeFilter() {
        AlertDialog.Builder setNative = new AlertDialog.Builder(this);
        setNative.setTitle("Select Native");
        final String[] cNative = new String[cListLanguage.size()];
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
                cTextNativeResult.setText(cNative[cNativeSelected]);
            }
        });
        setNative.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cTextNativeResult.getText().equals("Any")) {
                    cNativeSelected = 0;
                }
            }
        });
        setNative.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cTextNativeResult.setText("Any");
                cNativeSelected = 0;
            }
        });
        setNative.show();
    }

    private void LearnFilter() {
        AlertDialog.Builder setLearn = new AlertDialog.Builder(this);
        setLearn.setTitle("Select Learn");
        final String[] cLearn = new String[cListLanguage.size()];
        cListLanguage.toArray(cLearn);
        setLearn.setSingleChoiceItems(cLearn, cLearnSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cLearnSelected = which;
            }
        });

        setLearn.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cTextLearnResult.setText(cLearn[cLearnSelected]);
            }
        });
        setLearn.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cTextNativeResult.getText().equals("Any")) {
                    cNativeSelected = 0;
                }
            }
        });
        setLearn.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cTextLearnResult.setText("Any");
                cLearnSelected = 0;
            }
        });
        setLearn.show();
    }

    private void StatusFilter() {
        AlertDialog.Builder setStatus = new AlertDialog.Builder(this);
        setStatus.setTitle("Select Status");
        final String[] cStatus = {"Online", "Offline"};
        setStatus.setSingleChoiceItems(cStatus, cStatusSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cStatusSelected = which;
            }
        });

        setStatus.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cTextStatusResult.setText(cStatus[cStatusSelected]);
            }
        });
        setStatus.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cTextStatusResult.getText().equals("Any")) {
                    cStatusSelected = 0;
                }
            }
        });
        setStatus.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cTextStatusResult.setText("Any");
                cStatusSelected = 0;
            }
        });
        setStatus.show();
    }


    private void Search() {
        cLayoutFilter.setVisibility(View.INVISIBLE);
        cButtonFilter.setImageResource(R.drawable.ic_dropdown);
//        Loading();
        String vSearch = WordUtils.capitalizeFully(cInputSearch.getText().toString().trim());
        final String vCountry = cTextCountryResult.getText().toString();
        final String vNative = cTextNativeResult.getText().toString();
        final String vLearn = cTextLearnResult.getText().toString();
        final String vStatus = cTextStatusResult.getText().toString();

        cDatabaseRef.child("user").orderByChild("fullName").startAt(vSearch).endAt(vSearch + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //final ArrayList<HashMap<String, String>> ListUserName = new ArrayList<HashMap<String, String>>();
                cListUser.clear();
                adapter = new MyAdapter(getApplicationContext(), cListUser);
                cListView.setAdapter(adapter);
                for (DataSnapshot userID : dataSnapshot.getChildren()) {
                    User user = userID.getValue(User.class);
                    if (!cAuth.getCurrentUser().getUid().equals(userID.getKey())) {

                        MyClass mc = new MyClass();
                        int vAge = Integer.parseInt(mc.GetAge(user.DOB));

                        if (!vCountry.equalsIgnoreCase("any") && !vCountry.equals(user.country)) {
                            continue;
                        }

                        if (!vNative.equalsIgnoreCase("any") && !vNative.equals(user.nativeLanguage)) {
                            continue;
                        }


                        if (!vLearn.equalsIgnoreCase("any") && !user.learnFull.contains(vLearn)) {
                            continue;
                        }
                        if (!vStatus.equalsIgnoreCase("any")) {
                            if (vStatus.equalsIgnoreCase("online")) {
                                if (!mc.CheckStatus(user.onlineTime)) {
                                    continue;
                                }
                            } else if (vStatus.equalsIgnoreCase("offline")) {
                                if(mc.CheckStatus(user.onlineTime)){
                                    continue;
                                }
                            }
                        }

                        if (cInputAgeRangeX.getText().toString().isEmpty()) {
                            cInputAgeRangeX.setText("1");
                        }

                        if (cInputAgeRangeY.getText().toString().isEmpty()) {
                            cInputAgeRangeY.setText("100");
                        }

                        int vAgeX = Integer.parseInt(cInputAgeRangeX.getText().toString());
                        int vAgeY = Integer.parseInt(cInputAgeRangeY.getText().toString());

                        if (vAgeX > vAgeY) {
                            cInputAgeRangeY.setText(vAgeX + "");
                            vAgeY = vAgeX;
                        }

                        if (vAge < vAgeX || vAge > vAgeY) {
                            continue;
                        }


                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("UID", userID.getKey());
                        map.put("name", user.fullName);
                        map.put("email", user.email);
                        map.put("studentLevel", user.studentLevel + "");
                        map.put("advisorLevel", user.advisorLevel + "");
                        map.put("followingCount", user.followingCount + "");
                        map.put("followerCount", user.followerCount + "");
                        map.put("about", user.about);
                        map.put("DOB", user.DOB);
                        map.put("gender", user.gender);
                        map.put("country", user.country);
                        map.put("native", user.nativeLanguage);
                        map.put("profilePicture", user.profilePicture);
                        map.put("age", mc.GetAge(user.DOB));
                        map.put("learnAbbreviation", user.learnAbbreviation);
                        map.put("learnFull", user.learnFull);


//                        cDatabaseRef.child("learn").child(userID.getKey()).orderByKey().addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                    map.put("learn", (map.get("learn") != null ? map.get("learn") + ", " : "") + data.getValue().toString());
//                                    adapter = new MyAdapter(getApplicationContext(), cListUser);
//                                    cListView.setAdapter(adapter);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });
                        cListUser.add(map);
                        adapter = new MyAdapter(getApplicationContext(), cListUser);
                        cListView.setAdapter(adapter);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(SearchActivity.this, ViewProfileActivity.class).putExtra("map", cListUser.get(position)));
            }
        });
    }

    private void Loading() {
        cProgress = new ProgressDialog(this);
        cProgress.setTitle("Logging in");
        cProgress.setMessage("Please wait...");
        cProgress.setCancelable(false);
        cProgress.show();
    }

}


class MyAdapter extends ArrayAdapter {
    private StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();
    List<HashMap<String, String>> cListUser;

    public MyAdapter(Context context, List<HashMap<String, String>> listUser) {
        super(context, R.layout.listview_search, R.id.TextProfile, listUser);
        this.cListUser = listUser;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HashMap<String, String> map = cListUser.get(position);

        String vName = map.get("name");
        final String vImage = map.get("profilePicture");
        final String vUID = map.get("UID");
        final String vEmail = map.get("email");
        String vCountry = map.get("country");
        String vNative = map.get("native");
        String vLearn = map.get("learnAbbreviation");

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_search, parent, false);

        ImageView ImageViewPicture = (ImageView) row.findViewById(R.id.ImageViewPicture);
        TextView TextName = (TextView) row.findViewById(R.id.TextProfile);
        TextView TextNative = (TextView) row.findViewById(R.id.TextNatuve);
        TextView TextLearn = (TextView) row.findViewById(R.id.TextLearn);

//        if (vImage == null) {
//            ImageViewPicture.setImageResource(R.drawable.ic_profile_picture);
//            //Picasso.with(getContext()).load(R.drawable.ic_profile_picture).into(ImageViewPicture);
//        } else {
//            //Toast.makeText(getContext(), image.substring(image.length() - 36), Toast.LENGTH_LONG).show();
//            final File file = new File(getContext().getCacheDir() + "/" + vImage.substring(vImage.length() - 36));
//            if (file.exists() && !file.isDirectory()) {
//                //ImageViewPicture.setImageURI(Uri.fromFile(file));
//                Picasso.with(getContext()).load(file).transform(new CircleTransform()).into(ImageViewPicture);
//            } else {
//                cStorageRef.child("user/" + vEmail + "/profilePicture").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        Picasso.with(getContext()).load(file).transform(new CircleTransform()).into(ImageViewPicture);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        }

        MyClass mc = new MyClass();
        mc.SetImage(getContext(), ImageViewPicture, vImage, vEmail);

        TextName.setText(vName);
        TextNative.setText("Native: " + vNative);
        TextLearn.setText("Learning: " + vLearn.replaceAll(",", ", "));

        return row;
    }
}