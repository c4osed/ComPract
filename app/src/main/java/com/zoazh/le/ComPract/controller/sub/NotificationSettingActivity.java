package com.zoazh.le.ComPract.controller.sub;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import static com.zoazh.le.ComPract.controller.main.SearchActivity.ringtone;

import com.zoazh.le.ComPract.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.R.attr.path;

public class NotificationSettingActivity extends AppCompatActivity {
    private ConstraintLayout cLayoutRingtone;
    private TextView ctextRing;
    private int ringSelect;
    private MediaPlayer ringCurrent ;
    private MediaPlayer ring01;
    private MediaPlayer ring02;
    private MediaPlayer ring03;
    private MediaPlayer ring04;
    private MediaPlayer ring05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        cLayoutRingtone = (ConstraintLayout) findViewById(R.id.LayoutRingtone);
        ctextRing = (TextView) findViewById(R.id.textRing);

        cLayoutRingtone.setOnClickListener(clickListener);

        ringCurrent = MediaPlayer.create(NotificationSettingActivity.this,R.raw.ring2);

        readFile();

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            switch (v.getId()) {
                case R.id.LayoutRingtone:
                    setRingtone();
                    break;
            }
        }
    };

    public void readFile(){
        BufferedReader reader=null;
        try {
            FileInputStream fis = openFileInput("setting.txt");
            reader = new BufferedReader(new InputStreamReader(fis));
            StringBuffer stringBuffer=new StringBuffer();
            String tempStr="";
            while ((tempStr=reader.readLine())!=null){
                stringBuffer.append(tempStr);
            }
            this.ctextRing.setText(stringBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveFile(String text){
        BufferedWriter write= null;
        try {
            FileOutputStream fos=openFileOutput("setting.txt", Context.MODE_PRIVATE);
            String data = text;
            write=new BufferedWriter((new OutputStreamWriter(fos)));
            write.write(data);
            write.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(write!=null){
                try {
                    write.close();
                } catch (IOException e) {
                    System.err.println("Error close write");
                    e.printStackTrace();
                }
            }
        }
    }


    private void playSound(int x){
        switch (x){
            case 1:
                ring01 = MediaPlayer.create(NotificationSettingActivity.this,R.raw.ring2);
                ring01.start();
                ringCurrent = ring01;
                break;
            case 2:
                ring02 = MediaPlayer.create(NotificationSettingActivity.this,R.raw.blizzard);
                ring02.start();
                ringCurrent = ring02;
                break;
            case 3:
                ring03 = MediaPlayer.create(NotificationSettingActivity.this,R.raw.electronic);
                ring03.start();
                ringCurrent = ring03;
                break;
            case 4:
                ring04 = MediaPlayer.create(NotificationSettingActivity.this,R.raw.drumbeat);
                ring04.start();
                ringCurrent = ring04;
                break;
            case 5:
                ring05 = MediaPlayer.create(NotificationSettingActivity.this,R.raw.dance);
                ring05.start();
                ringCurrent = ring05;
                break;
        }
    }

    private  void stopSound(MediaPlayer x){
        x.stop();
    }

    private void setRingtone() {
        AlertDialog.Builder setRingtone = new AlertDialog.Builder(this);

        setRingtone.setTitle("Select Ringtone");

        setRingtone.setCancelable(false);

        setRingtone.setSingleChoiceItems(getResources().getStringArray(R.array.ringtone), ringSelect, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ringSelect = which;
            }
        });

        setRingtone.setSingleChoiceItems(R.array.ringtone, -1, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file = new File(path + "/setting.txt");
                switch (which){
                    case 0:
                        stopSound(ringCurrent);
                        playSound(1);
                        ctextRing.setText("Ring");
                        break;
                    case 1:
                        stopSound(ringCurrent);
                        playSound(2);
                        ctextRing.setText("Blizzard");
                        break;
                    case 2:
                        stopSound(ringCurrent);
                        playSound(3);
                        ctextRing.setText("Electronic");
                        break;
                    case 3:
                        stopSound(ringCurrent);
                        playSound(4);
                        ctextRing.setText("Drum");
                        break;
                    case 4:
                        stopSound(ringCurrent);
                        playSound(5);
                        ctextRing.setText("Dance");
                        break;
                }
            }
        });

        setRingtone.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopSound(ringCurrent);
                saveFile(ctextRing.getText().toString());
                ringtone = ctextRing.getText().toString();
            }
        });

        setRingtone.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopSound(ringCurrent);
                readFile();
            }
        });

        setRingtone.show();
    }
}
