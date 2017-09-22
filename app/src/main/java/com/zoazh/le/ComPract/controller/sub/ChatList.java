package com.zoazh.le.ComPract.controller.sub;

import android.content.Context;
import android.content.Intent;
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

public class ChatList extends AppCompatActivity {

    private DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();
    ListChat adapter;
    private ListView cListViewChat;
    private List<HashMap<String, String>> cListChat = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        cListViewChat = (ListView) findViewById(R.id.ListViewChat);

        ListChat();
    }

    private void ListChat() {

        cDatabaseRef.child("message").child(cAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                cListChat.clear();
                adapter = new ListChat(getApplicationContext(), cListChat);
                cListViewChat.setAdapter(adapter);

                for (final DataSnapshot chatID : dataSnapshot.getChildren()) {
                    cDatabaseRef.child("message").child(cAuth.getCurrentUser().getUid()).child(chatID.getKey()).orderByChild("messageTimeDESC").limitToFirst(1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {
                            for (DataSnapshot data2 : dataSnapshot2.getChildren()) {
                                final Message chat = data2.getValue(Message.class);
//                                Toast.makeText(getApplicationContext(), chat.messageSender, Toast.LENGTH_LONG).show();
                                cDatabaseRef.child("user").child(chatID.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot3) {
                                        User user = dataSnapshot3.getValue(User.class);
                                        if (user != null) {
                                            HashMap<String, String> map = new HashMap<String, String>();
                                            map.put("UID", dataSnapshot3.getKey());
                                            map.put("name", user.fullName);
                                            map.put("profilePicture", user.profilePicture);
                                            map.put("lastMessage", chat.messageText);
                                            map.put("lastTime", chat.messageTimeASC + "");
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


        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_chat, parent, false);

        ImageView ImageViewPicture = (ImageView) row.findViewById(R.id.ImageViewPicture);
        TextView TextName = (TextView) row.findViewById(R.id.TextViewNameChatList);
        TextView TextLastMessage = (TextView) row.findViewById(R.id.TextMessage);
        TextView TextTime = (TextView) row.findViewById(R.id.TextViewTimeChat);


        MyClass mc = new MyClass();
        TextName.setText(vName);
        mc.SetImage(getContext(), ImageViewPicture, vProfilePicture, vUID);
        TextLastMessage.setText(vLastMessage);
        TextTime.setText(new SimpleDateFormat("HH:mm", Locale.US).format(Long.parseLong(vLastTime)));
//        TextQuestionAuthor.setText(vName);

        return row;
    }
}
