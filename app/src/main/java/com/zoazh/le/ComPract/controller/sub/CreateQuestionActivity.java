package com.zoazh.le.ComPract.controller.sub;

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

public class CreateQuestionActivity extends BaseActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    private ProgressDialog cProgress;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);




        //BTM BAR
//        cBottomBar = (ConstraintLayout) findViewById(R.id.BottomBar);
//        cLayoutAdvise = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutAdvise);
//        cImageViewAdvise = (ImageView) cBottomBar.findViewById(R.id.ImageViewAdvise);
//        cLayoutSearch = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutSearch);
//        cLayoutProfile = (ConstraintLayout) cBottomBar.findViewById(R.id.LayoutProfile);
//        cTextAdvise = (TextView) cBottomBar.findViewById(R.id.TextAdvise);
//
//
//        //OnClick
//        cLayoutSearch.setOnClickListener(clickListener);
//        cLayoutProfile.setOnClickListener(clickListener);





    }

    @Override
    protected void onResume() {
        super.onResume();
        OnlineTimer(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        OnlineTimer(false);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.LayoutSearch:
                    break;
                case R.id.LayoutProfile:
                    break;
            }
        }
    };



}
