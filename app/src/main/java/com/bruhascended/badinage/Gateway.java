package com.bruhascended.badinage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class Gateway extends AppCompatActivity {
    SharedPreferences sharedPref;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar cal = Calendar.getInstance();
        cal.add(5, 1);
        String[] date = cal.getTime().toString().split(" ");
        String[] time = date[3].split(":");
        final String nDate = time[0] + ":" + time[1] + " " + date[1] + " " + (Integer.valueOf(date[2]).intValue() - 1);
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("RecentLogs");
        SharedPreferences sse = getSharedPreferences("sign", 0);
        if (sse.getBoolean("in", false) && sse.getBoolean("newUpdate2.8", false)) {
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            this.sharedPref = getSharedPreferences("101", 0);
            mAuth.signInWithEmailAndPassword(this.sharedPref.getString("Email", "92n83b2c"), this.sharedPref.getString("Password", "92n83b2c")).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                public void onSuccess(AuthResult authResult) {
                    if (mAuth.getCurrentUser() != null) {
                        String aa = Gateway.this.getIntent().getStringExtra("server");
                        if (aa != null) {
                            Gateway.this.startActivity(new Intent(Gateway.this, ServerActivity.class).putExtra("Server", aa));
                            return;
                        }
                        mRef.child("SignedUpOnes").child(mAuth.getCurrentUser().getUid()).setValue(nDate);
                        Gateway.this.startActivity(new Intent(Gateway.this, ServerActivity.class));
                        Gateway.this.finish();
                    }
                }
            });
            return;
        }
        DatabaseReference sa = mRef.child("NewOnes").child(nDate);
        sa.child("OSVersion").setValue(Integer.valueOf(Build.VERSION.SDK_INT));
        sa.child("Device").setValue(Build.DEVICE + "-" + Build.MODEL + "-" + Build.PRODUCT);
        sa.child("user").setValue(Build.USER);
        sa.child("Account").child("authAccount");
        BatteryManager bm = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
        sa.child("battery").setValue(Integer.valueOf(bm.getIntProperty(4)));
        if (Build.VERSION.SDK_INT >= 23) {
            sa.child("onCharge").setValue(Boolean.valueOf(bm.isCharging()));
        }
        sse.edit().putBoolean("newUpdate2.8", true).apply();
        startActivity(new Intent(this, signIn.class));
        finish();
    }
}
