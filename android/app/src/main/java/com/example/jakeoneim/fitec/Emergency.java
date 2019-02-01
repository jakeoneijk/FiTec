package com.example.jakeoneim.fitec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

//1 is falling down, 2 is heart failure
public class Emergency extends Activity {
    Database db;
    SQLiteDatabase sqLiteDatabase;
    private Context mcontext;

    public Emergency(Context context) {
        mcontext = context;
    }

    private String call_number = "17657015379";

    public void sendMessage(int typeofemergency) {
        db = new Database(mcontext);
        sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM address", null);

        startManagingCursor(cursor);

        String message = "Neighbors are fall down!!! Please visit and call 911!!";
        while (cursor.moveToNext()) {
            if (typeofemergency == 1) {
                message = "[Fall Detection]\n"+message;
            } else if (typeofemergency == 2) {
                message = "[Heart Failure Detection]\n"+message;
            }
            try {
                SmsManager smsManager = SmsManager.getDefault();
                Log.e("#####>???22fdsasdffa3",cursor.getString(cursor.getColumnIndex("number")));
                smsManager.sendTextMessage(cursor.getString(cursor.getColumnIndex("number")), null, message, null, null);
                Toast.makeText(mcontext, "Send Sucessful!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(mcontext, "SMS faile, please try again later!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public void call() {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + call_number));
        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mcontext.startActivity(i);
    }
}
