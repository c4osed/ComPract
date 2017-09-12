package com.zoazh.le.ComPract.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.zoazh.le.ComPract.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MyClass {

    StorageReference cStorageRef = FirebaseStorage.getInstance().getReference();

    public String GetAge(String DOB) {

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        try {
            dob.setTime(sdf.parse(DOB));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public void SetImage(final Context ctx, final ImageView img, String pic, final String path) {
        if (pic == null) {
            img.setImageResource(R.drawable.ic_profile_picture);
        } else {
            final File file = new File(ctx.getCacheDir() + "/" + pic.substring(pic.length() - 36));
            if (file.exists() && !file.isDirectory()) {
                Picasso.with(ctx).load(file).transform(new CircleTransform()).into(img);
            } else {
                cStorageRef.child("user/" + path + "/profilePicture").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Picasso.with(ctx).load(file).transform(new CircleTransform()).into(img);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
//                        Toast.makeText(ctx, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public boolean CheckStatus(String onlineTime) {
        if (!onlineTime.isEmpty()) {
            String nowTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(new Date());
            String date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(new Date(System.currentTimeMillis() + 1 * 60 * 1000));
            String date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(new Date(System.currentTimeMillis() + 2 * 60 * 1000));
            String dateArray[] = {nowTime, date1, date2};
            if (Arrays.asList(dateArray).contains(onlineTime)) {
                return true;
            }
            return false;
        } else {
            return false;
        }

    }


}
