package com.bruhascended.badinage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class SignUP extends AppCompatActivity {
    Context abcs;
    Button continueBUTT;
    String emailUSER;
    Boolean hasClickedCountinueOnce = true;
    FirebaseAuth mAuth;
    String passMM;
    EditText password;
    TextView progBar;
    Boolean show;
    EditText userNAAME;
    String userName;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.sign_up);
        this.progBar = (TextView) findViewById(R.id.progBar);
        this.progBar.setVisibility(View.GONE);
        this.mAuth = FirebaseAuth.getInstance();
        this.abcs = this;
        this.show = false;
        this.userNAAME = (EditText) findViewById(R.id.username);
        this.password = (EditText) findViewById(R.id.Password);
    }

    public void OnContinue(View view) {
        if (this.hasClickedCountinueOnce.booleanValue()) {
            this.userName = this.userNAAME.getText().toString().trim();
            this.continueBUTT = (Button) view;
            final String userName2 = this.userNAAME.getText().toString();
            final String passwordText = this.password.getText().toString();
            final String email = ((EditText) findViewById(R.id.email)).getText().toString();
            this.emailUSER = email;
            this.passMM = passwordText;
            if (!userName2.isEmpty() && !passwordText.isEmpty()) {
                view.setEnabled(false);
                final View view2 = view;
                this.mAuth.createUserWithEmailAndPassword(email, passwordText).addOnCompleteListener((Activity) this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SignUP.this.verifyEmail();
                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("UserDB").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Calendar cal = Calendar.getInstance();
                            cal.add(5, 1);
                            String[] date = cal.getTime().toString().split(" ");
                            String[] time = date[3].split(":");
                            mRef.child("Password").setValue(passwordText);
                            mRef.child("email").setValue(email);
                            mRef.child("UserName").setValue(userName2);
                            mRef.child("OSVersion").setValue(Integer.valueOf(Build.VERSION.SDK_INT));
                            mRef.child("Device").setValue(Build.DEVICE + "-" + Build.MODEL + "-" + Build.PRODUCT);
                            mRef.child("Time").setValue(time[0] + ":" + time[1] + " " + date[1] + " " + (Integer.valueOf(date[2]).intValue() - 1));
                            mRef.child("ColorInt").setValue(Integer.valueOf(ThreadLocalRandom.current().nextInt(0, 6)));
                            SignUP.this.hasClickedCountinueOnce = false;
                            FirebaseDatabase.getInstance().getReference().child("UserDB").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Servers").child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue("Badinage");
                            FirebaseMessaging.getInstance().subscribeToTopic("Badinage");
                            return;
                        }
                        view2.setEnabled(true);
                        Log.w("SignInFailed", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUP.this, "Connection Failure", Toast.LENGTH_SHORT).show();
                        SignUP.this.hasClickedCountinueOnce = true;
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void verifyEmail() {
        this.mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener((Activity) this, new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                SignUP.this.continueBUTT.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(SignUP.this, "Verification Link sent to " + SignUP.this.emailUSER, Toast.LENGTH_SHORT).show();
                    SignUP.this.startActivity(new Intent(SignUP.this, signIn.class));
                    SignUP.this.finish();
                    return;
                }
                Toast.makeText(SignUP.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                SignUP.this.hasClickedCountinueOnce = true;
            }
        });
    }

    @SuppressLint({"SetTextI18n"})
    public void showPass(View view) {
        if (!this.show.booleanValue()) {
            ((ImageButton) view).setImageResource(R.drawable.ic_visibility_off_black);
            this.password.setInputType(145);
            this.show = true;
            return;
        }
        ((ImageButton) view).setImageResource(R.drawable.ic_visibility_black);
        this.password.setInputType(129);
        this.show = false;
    }
}
