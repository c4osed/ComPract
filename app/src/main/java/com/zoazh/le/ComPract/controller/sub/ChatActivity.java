package com.zoazh.le.ComPract.controller.sub;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.calling.Call;
import com.zoazh.le.ComPract.R;
import com.zoazh.le.ComPract.controller.CallScreenActivity;
import com.zoazh.le.ComPract.model.BaseActivity;
import com.zoazh.le.ComPract.model.MyClass;
import com.zoazh.le.ComPract.model.SinchService;
import com.zoazh.le.ComPract.model.database.Message;
import com.zoazh.le.ComPract.model.database.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends BaseActivity {

    private FirebaseAuth.AuthStateListener cAuthListener;
    private FirebaseAuth cAuth = FirebaseAuth.getInstance();

    private ValueEventListener cListenerStatus;
    private ValueEventListener cListenerRead;
    private ValueEventListener cListenerMessage;

    private ImageView cImageViewStatus;
    private TextView cTextName;
    private ImageButton cImageButtonCall;

    private ListView cListViewMessage;

    private EditText cInputMessage;
    private ImageButton cImageButtonSend;

    private String cUID;
    private String cName;
    private String cProfilePicture;
    private String cEmail;

    HashMap<String, String> map;
    List<HashMap<String, String>> cListMessage = new ArrayList<HashMap<String, String>>();
    MessageAdapter messageAdapter;

    final Handler h = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            final MyClass mc = new MyClass();
            cListenerStatus = cDatabaseRef.child("user").child(cUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (mc.CheckStatus(user.onlineTime)) {
                        cImageViewStatus.setColorFilter(getResources().getInteger(R.color.green));
                    } else {
                        cImageViewStatus.setColorFilter(getResources().getInteger(R.color.grey));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            h.postDelayed(r,60000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        cImageViewStatus = (ImageView) findViewById(R.id.ImageViewStatus);
        cTextName = (TextView) findViewById(R.id.TextProfile);
        cImageButtonCall = (ImageButton) findViewById(R.id.ImageButtonCall);
        cImageButtonCall.setEnabled(false);

        cListViewMessage = (ListView) findViewById(R.id.ListViewMessage);

        cInputMessage = (EditText) findViewById(R.id.InputMessage);
        cImageButtonSend = (ImageButton) findViewById(R.id.ImageButtonSend);

        cImageButtonCall.setOnClickListener(clickListener);
        cImageButtonSend.setOnClickListener(clickListener);

        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");

        cUID = map.get("UID");
        cName = map.get("name");
        cEmail = map.get("email");
        cProfilePicture = map.get("profilePicture");

        cTextName.setText(cName);

        //registerForContextMenu(cListViewMessage);

        cTextName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }


//    private void scrollMyListViewToBottom() {
//        cListViewMessage.post(new Runnable() {
//            @Override
//            public void run() {
//                if (messageAdapter != null) {
//                    cListViewMessage.setSelection(messageAdapter.getCount() - 1);
//                }
//            }
//        });
//    }

    @Override
    protected void onResume() {
        super.onResume();

        OnlineTimer(true);

        cListenerRead = cDatabaseRef.child("message").child(cUID).child(cAuth.getCurrentUser().getUid()).orderByChild("messageSender").equalTo(cUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data2 : dataSnapshot.getChildren()) {
                    Message message2 = data2.getValue(Message.class);
                    if (message2.messageRead.equals("")) {
                        cDatabaseRef.child("message").child(cUID).child(cAuth.getCurrentUser().getUid()).child(data2.getKey()).child("messageRead").setValue("Read");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cListenerMessage = cDatabaseRef.child("message").child(cAuth.getCurrentUser().getUid()).child(cUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cListMessage.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);

                    HashMap<String, String> map2 = new HashMap<String, String>();
                    map2.put("messageSender", message.messageSender);
                    map2.put("messageText", message.messageText);
                    map2.put("messageTime", message.messageTime);
                    map2.put("messageRead", message.messageRead);
                    cListMessage.add(map2);
                    messageAdapter = new MessageAdapter(getApplicationContext(), cListMessage, cAuth.getCurrentUser().getUid(), cName.substring(0,cName.indexOf(" ")), cEmail, cProfilePicture);
                    cListViewMessage.setAdapter(messageAdapter);
                }
                //scrollMyListViewToBottom();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        r.run();
    }
    @Override
    protected void onStop() {
        super.onStop();
        OnlineTimer(false);
        cDatabaseRef.child("message").child(cUID).child(cAuth.getCurrentUser().getUid()).orderByChild("messageSender").equalTo(cUID).removeEventListener(cListenerRead);
        cDatabaseRef.child("message").child(cAuth.getCurrentUser().getUid()).child(cUID).removeEventListener(cListenerMessage);

        h.removeCallbacks(r);
    }


    @Override
    protected void onServiceConnected() {
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(cAuth.getCurrentUser().getUid());
        }
        cImageButtonCall.setEnabled(true);
    }


    private void callButtonClicked() {
        try {
            Call call = getSinchServiceInterface().callUser(cUID);
            if (call == null) {
                Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
                        + "placing a call.", Toast.LENGTH_LONG).show();
                return;
            }
            String callId = call.getCallId();
            Intent callScreen = new Intent(this, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            //Toast.makeText(getApplicationContext(),callId+"+"+cUID,Toast.LENGTH_LONG).show();
            callScreen.putExtra("name", cName);
            startActivity(callScreen);
        } catch (MissingPermissionException e) {
            ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now place a call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ImageButtonCall:
                    callButtonClicked();
                    break;
                case R.id.ImageButtonSend:
                    sendMessage();
                    break;

            }
        }
    };


    private void sendMessage() {
        String messageText = cInputMessage.getText().toString();
        if (!messageText.isEmpty()) {
            String messageTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(new Date());

            Message message = new Message(cAuth.getCurrentUser().getUid(), messageText, messageTime, "");

            cDatabaseRef.child("message").child(cAuth.getCurrentUser().getUid()).child(map.get("UID")).push().setValue(message);
            cDatabaseRef.child("message").child(map.get("UID")).child(cAuth.getCurrentUser().getUid()).push().setValue(message);

            cInputMessage.setText("");
        }
        //scrollMyListViewToBottom();
    }
}

class MessageAdapter extends ArrayAdapter {
    private StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();

    List<HashMap<String, String>> cListMessage;
    String cUID;
    String cName;
    String cEmail;
    String cProfilePicture;
    TextView cTextMessageText;

    private String cDate = null;

    public MessageAdapter(Context context, List<HashMap<String, String>> listMessage, String UID, String name, String email, String profilePicture) {
        super(context, R.layout.listview_chat_sender, R.id.TextMessageText, listMessage);
        this.cListMessage = listMessage;
        this.cUID = UID;
        this.cName = name;
        this.cEmail = email;
        this.cProfilePicture = profilePicture;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

        HashMap<String, String> map = cListMessage.get(position);

        String vSender = map.get("messageSender");
        final String vText = map.get("messageText");
        final String vTime = map.get("messageTime");
        String vRead = map.get("messageRead");

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row;


        if (position == 0) {
            cDate = vTime.substring(0, 10);
        } else {
            String messageTimePre = cListMessage.get(position - 1).get("messageTime");
            if (!messageTimePre.substring(0, 10).equals(vTime.substring(0, 10))) {
                cDate = vTime.substring(0, 10);
            } else {
                cDate = "";

            }
        }

        if (vSender.equals(cUID)) {
            if (cDate.isEmpty()) {
                row = inflater.inflate(R.layout.listview_chat_sender, parent, false);
            } else {
                row = inflater.inflate(R.layout.listview_chat_date_sender, parent, false);
                TextView TextMessageDate = (TextView) row.findViewById(R.id.TextMessageDate);
                TextMessageDate.setText(cDate);
            }
        } else {
            if (cDate.isEmpty()) {
                row = inflater.inflate(R.layout.listview_chat_receiver, parent, false);

                ImageView ImageViewProfilePicture = (ImageView) row.findViewById(R.id.ImageViewProfilePicture);
                TextView TextName = (TextView) row.findViewById(R.id.TextNameReceiver);

                TextName.setText(cName);
                MyClass mc = new MyClass();
                mc.SetImage(getContext(), ImageViewProfilePicture, cProfilePicture, cUID);
            } else {
                row = inflater.inflate(R.layout.listview_chat_date_receiver, parent, false);

                TextView TextMessageDate = (TextView) row.findViewById(R.id.TextMessageDate);
                ImageView ImageViewProfilePicture = (ImageView) row.findViewById(R.id.ImageViewProfilePicture);
                TextView TextName = (TextView) row.findViewById(R.id.TextNameReceiver);

                TextMessageDate.setText(cDate);
                MyClass mc = new MyClass();
                mc.SetImage(getContext(), ImageViewProfilePicture, cProfilePicture, cUID);
                TextName.setText(cName);
            }
        }


        cTextMessageText = (TextView) row.findViewById(R.id.TextMessageText);
        TextView TextMessageTime = (TextView) row.findViewById(R.id.TextMessageTime);
        TextView TextMessageRead = (TextView) row.findViewById(R.id.TextMessageRead);

        cTextMessageText.setText(vText);
        TextMessageTime.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date(vTime)));
        TextMessageRead.setText(vRead);

        cTextMessageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), vTime, Toast.LENGTH_LONG).show();
            }
        });


        cTextMessageText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", vText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied to clipboard.", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        return row;
    }

}
