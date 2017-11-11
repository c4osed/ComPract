package com.zoazh.le.ComPract.controller.sub;

import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.main.PracticeActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.database.Message;
import com.zoazh.le.ComPract.model.database.Question;
import com.zoazh.le.ComPract.model.database.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.zoazh.le.ComPract.model.BaseActivity;

import org.jetbrains.annotations.NotNull;

import static android.R.attr.x;

public class ChatList extends AppCompatActivity {


    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    ListChat adapter;
    private ListView cListViewChat;
    private List<HashMap<String, String>> cListChat = new ArrayList<HashMap<String, String>>();
    private ValueEventListener cListenerRead;

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        cListViewChat = (ListView) findViewById(R.id.ListViewChat);
        cListViewChat.setAdapter(null);
        ListChat();
    }

    private String checkRead(){
        final String[] x = new String[1];
        cDatabaseRef.child("message").child(cAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot chatID : dataSnapshot.getChildren()) {
                    cDatabaseRef.child("message").child(chatID.getKey()).child(cAuth.getCurrentUser().getUid()).orderByChild("messageSender").equalTo(chatID.getKey()).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data: dataSnapshot.getChildren()) {
                                if(data.child("messageRead").toString().contains("Read")){
                                    x[0] ="read";
                                }else {
                                    x[0] ="";
                                }
                            }
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
        Toast.makeText(this, "x: "+ x[0], Toast.LENGTH_SHORT).show();
        return x[0]="";
    }

    private void ListChat() {

        cDatabaseRef.child("message").child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cListViewChat.setAdapter(null);
                cListChat.clear();
                adapter = new ListChat(getApplicationContext(), cListChat);
                cListViewChat.setAdapter(adapter);

                for (final DataSnapshot chatID : dataSnapshot.getChildren()) {
                    cDatabaseRef.child("message").child(cAuth.getCurrentUser().getUid()).child(chatID.getKey()).orderByChild("messageTimeDESC").limitToFirst(1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            cListViewChat.setAdapter(null);
                            cListChat.clear();
                            for (DataSnapshot data2 : dataSnapshot2.getChildren()) {
                                final Message chat = data2.getValue(Message.class);
//                                Toast.makeText(getApplicationContext(), chat.messageSender, Toast.LENGTH_LONG).show();
                                cDatabaseRef.child("user").child(chatID.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot3) {
                                        User user = dataSnapshot3.getValue(User.class);
                                        if (user != null) {

                                            final HashMap<String, String> map = new HashMap<String, String>();
                                            final String[] x = {""};
                                            map.put("UID", dataSnapshot3.getKey());
                                            map.put("name", user.fullName);
                                            map.put("profilePicture", user.profilePicture);
                                            map.put("lastMessage", chat.messageText);
                                            map.put("lastTime", chat.messageTimeASC + "");
                                            map.put("readCheck",checkRead());



                                            Toast.makeText(ChatList.this, "map : "+map.get("readCheck"), Toast.LENGTH_SHORT).show();
//

//                            map.put("AuthorPicture", user.profilePicture);
//                            map.put("AuthorName", user.fullName);

                                            cListChat.add(map);

                                            adapter = new ListChat(getApplicationContext(), cListChat);
                                            cListViewChat.setAdapter(adapter);


                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

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


        cListViewChat.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ChatList.this, ChatActivity.class).putExtra("map", cListChat.get(position)));
//                startActivity(new Intent(getApplicationContext(), QuestionActivity.class).putExtra("map", cListQuestion.get(position)));
            }
        });
    }
}


class ListChat extends ArrayAdapter {



    List<HashMap<String, String>> cListChat;

    public ListChat(Context context, List<HashMap<String, String>> listUser) {
        super(context, R.layout.listview_chat, R.id.TextProfile, listUser);
        this.cListChat = listUser;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HashMap<String, String> map = cListChat.get(position);

        String vUID = map.get("UID");
        String vName = map.get("name");
        String vProfilePicture = map.get("profilePicture");
        String vLastMessage = map.get("lastMessage");
        String vLastTime = map.get("lastTime");
        String vReadCheck = map.get("readCheck");

        Toast.makeText(getContext(), "vReadCheck : "+vReadCheck, Toast.LENGTH_SHORT).show();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_chat, parent, false);

        ImageView ImageViewPicture = (ImageView) row.findViewById(R.id.ImageViewPicture);
        ImageView cImageNewMs =(ImageView) row.findViewById(R.id.imageNewMs);
        TextView TextName = (TextView) row.findViewById(R.id.TextViewNameChatList);
        TextView TextLastMessage = (TextView) row.findViewById(R.id.TextMessage);
        TextView TextTime = (TextView) row.findViewById(R.id.TextViewTimeChat);


        MyClass mc = new MyClass();
        TextName.setText(vName);
        mc.SetImage(getContext(), ImageViewPicture, vProfilePicture, vUID);
        cImageNewMs.setImageResource(R.drawable.ic_online);

//        if(vReadCheck=="read"){
//            cImageNewMs.setVisibility(View.INVISIBLE);
//        }
//
//        if(vReadCheck==""||vReadCheck==" "){
//            cImageNewMs.setColorFilter(Color.RED);
//            cImageNewMs.setVisibility(View.VISIBLE);
//            Toast.makeText(getContext(), "Check space ", Toast.LENGTH_SHORT).show();
//        }
//
//        if(vReadCheck==null){
//            cImageNewMs.setColorFilter(Color.RED);
//            cImageNewMs.setVisibility(View.VISIBLE);
//            Toast.makeText(getContext(), "Check null", Toast.LENGTH_SHORT).show();
//        }


        if(vLastMessage.replaceAll("[\n\r]", "").length()>25){
            TextLastMessage.setText(vLastMessage.replaceAll("[\n]", " ").substring(0,25)+"....");
        }else {
            TextLastMessage.setText(vLastMessage.replaceAll("[\n]", " "));
        }

        TextTime.setText(new SimpleDateFormat("HH:mm", Locale.US).format(Long.parseLong(vLastTime)));

//        TextQuestionAuthor.setText(vName);

        return row;
    }
}
