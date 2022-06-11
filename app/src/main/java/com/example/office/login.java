package com.example.office;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import java.time.LocalDateTime;

import javax.net.ssl.HttpsURLConnection;

public class login extends AppCompatActivity {
    Button enter;
    EditText vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        enter = (Button) findViewById(R.id.enter_btn);
        vehicle = (EditText) findViewById(R.id.vehicle);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ABC", "inside click");
                String veh = vehicle.getText().toString();
                HashMap<String, Object> v = new HashMap<>();
                v.put("Date", Timestamp.now());
                v.put("Name", "Vehicle towed");
                v.put("Transaction", false);
                v.put("location", "");
                FirebaseFirestore.getInstance().collection(veh).add(v).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                        Toast.makeText(getApplicationContext(), "INSERTION DONE", Toast.LENGTH_SHORT).show();
                    }

                });
                sendsms();
                startActivity(new Intent(login.this,MapsActivity.class).putExtra("vehicleNo",vehicle.getText().toString()));


            }
        });
    }

public void sendsms(){
    if(checkpermission(Manifest.permission.SEND_SMS)){
        SmsManager smsmanager= SmsManager.getDefault();
        smsmanager.sendTextMessage("9825725689",null,"Your vehicle is towed! Fine Charged :500 Kindly use app to track your vehicle",null,null);
        Toast.makeText(getApplicationContext(),"Message Sent!",Toast.LENGTH_SHORT).show();}
    else{
        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
    }
}
    public boolean checkpermission(String permission){
        int check= ContextCompat.checkSelfPermission(this,permission);
        return(check== PackageManager.PERMISSION_GRANTED);


    }
    }

