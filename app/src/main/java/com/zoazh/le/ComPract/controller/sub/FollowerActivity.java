package com.zoazh.le.ComPract.controller.sub;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.database.User;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FollowerActivity extends AppCompatActivity {
    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private List<HashMap<String, String>> cListUser = new ArrayList<HashMap<String, String>>();
    ListView listFollower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        Search();
    }
    private void Search() {
        String vSearch = WordUtils.capitalizeFully(" ".toString().trim());
        cDatabaseRef.child("user").orderByChild("fullName").startAt(vSearch).endAt(vSearch + "\uf8ff").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //final ArrayList<HashMap<String, String>> ListUserName = new ArrayList<HashMap<String, String>>();
                listFollower = (ListView) findViewById(R.id.listFollower);
                ListAdappter adppter = new ListAdappter(getApplicationContext(), cListUser);
                cListUser.clear();
                listFollower.setAdapter(adppter);

                for (DataSnapshot userID : dataSnapshot.getChildren()) {
                    User user = userID.getValue(User.class);
                    if (!cAuth.getCurrentUser().getUid().equals(userID.getKey())) {
                        MyClass mc = new MyClass();
                        int vAge = Integer.parseInt(mc.GetAge(user.DOB));

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
                        adppter = new ListAdappter(getApplicationContext(), cListUser);
                        listFollower.setAdapter(adppter);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        //listFollower.setOnItemClickListener(new AdapterView.OnItemClickListener()
        //{
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        startActivity(new Intent(FollowingActivity.this, ViewProfileActivity.class).putExtra("map", cListUser.get(position)));
        //    }
        //});
    }
}






class ListFollowerAdappter extends ArrayAdapter {
    private StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();
    List<HashMap<String, String>> cListUser;

    public ListFollowerAdappter(Context context, List<HashMap<String, String>> listUser) {
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
        TextView TextNative = (TextView) row.findViewById(R.id.TextNative);
        TextView TextLearn = (TextView) row.findViewById(R.id.TextLearn);


        MyClass mc = new MyClass();
        mc.SetImage(getContext(), ImageViewPicture, vImage, vEmail);

        TextName.setText(vName);
        TextNative.setText("Native: " + vNative);
        TextLearn.setText("Learning: " + vLearn.replaceAll(",", ", "));

        return row;
    }
}