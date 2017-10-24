package com.zoazh.le.ComPract.model;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zoazh.le.ComPract.controller.start.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseActivity extends AppCompatActivity implements ServiceConnection {

    private SinchService.SinchServiceInterface mSinchServiceInterface;

    FirebaseAuth cAuth = FirebaseAuth.getInstance();

    public DatabaseReference cDatabaseRef = FirebaseDatabase.getInstance().getReference();
    StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();


    private Handler mHandler = new Handler();
    private Runnable r = new Runnable() {
        @Override
        public void run() {
//            cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("onlineTime").setValue(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(new Date()));

            cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("onlineTime").setValue(ServerValue.TIMESTAMP);
            cDatabaseRef.child("user").child(cAuth.getCurrentUser().getUid()).child("onlineTime").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String currentTime = new SimpleDateFormat("dd/MM/yyyy").format(dataSnapshot.getValue());
                    cDatabaseRef.child("mission").child(cAuth.getCurrentUser().getUid()).child("missionTime").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (!new SimpleDateFormat("dd/MM/yyyy").format(dataSnapshot.getValue()).equals(currentTime)) {
                                    cDatabaseRef.child("mission").child(cAuth.getCurrentUser().getUid()).removeValue();
                                }
                            } catch (Exception ex) {
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mHandler.postDelayed(r,30000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().bindService(new Intent(this, SinchService.class), this,
                BIND_AUTO_CREATE);

    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchService.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchService.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected SinchService.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }


//    @Override
//    protected void onPause() {
//        Toast.makeText(getApplicationContext(),"ss",Toast.LENGTH_LONG).show();
//        mHandler.removeCallbacks(r);
//        super.onPause();
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void OnlineTimer(boolean time) {
        if(time){
            r.run();
        }else{
            mHandler.removeCallbacks(r);
        }

    }

    public void Logout() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        //FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        cAuth.signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

//    public class CircleTransform implements Transformation {
//        @Override
//        public Bitmap transform(Bitmap source) {
//            int size = Math.min(source.getWidth(), source.getHeight());
//
//            int x = (source.getWidth() - size) / 2;
//            int y = (source.getHeight() - size) / 2;
//
//            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
//            if (squaredBitmap != source) {
//                source.recycle();
//            }
//
//            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
//
//            Canvas canvas = new Canvas(bitmap);
//            Paint paint = new Paint();
//            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
//            paint.setShader(shader);
//            paint.setAntiAlias(true);
//
//            float r = size/2f;
//            canvas.drawCircle(r, r, r, paint);
//
//            squaredBitmap.recycle();
//            return bitmap;
//        }
//
//        @Override
//        public String key() {
//            return "circle";
//        }
//    }

}
