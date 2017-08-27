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

public class FollowingActivity extends AppCompatActivity {
    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private List<HashMap<String, String>> cListUser = new ArrayList<HashMap<String, String>>();
    ListView listFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        cDatabaseRef.child("following").child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //final ArrayList<HashMap<String, String>> ListUserName = new ArrayList<HashMap<String, String>>();
                listFollowing = (ListView) findViewById(R.id.listFollowing);
                final ListFollowingAdappter[] adppter = {new ListFollowingAdappter(getApplicationContext(), cListUser)};
                cListUser.clear();
                listFollowing.setAdapter(adppter[0]);

                for (DataSnapshot followingUserID : dataSnapshot.getChildren()) {
//                    Toast.makeText(getApplicationContext(),followingUserID.getKey(),Toast.LENGTH_LONG).show();
                    cDatabaseRef.child("user").child(followingUserID.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot userID : dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            MyClass mc = new MyClass();

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("UID", dataSnapshot.getKey());
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

                            cListUser.add(map);
                            adppter[0] = new ListFollowingAdappter(getApplicationContext(), cListUser);
                            listFollowing.setAdapter(adppter[0]);
//                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        //listFollowing.setOnItemClickListener(new AdapterView.OnItemClickListener()
        //{
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        startActivity(new Intent(FollowingActivity.this, ViewProfileActivity.class).putExtra("map", cListUser.get(position)));
        //    }
        //});
    }

    private void Search() {


    }
}


class ListFollowingAdappter extends ArrayAdapter {
    private StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();
    List<HashMap<String, String>> cListUser;

    public ListFollowingAdappter(Context context, List<HashMap<String, String>> listUser) {
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