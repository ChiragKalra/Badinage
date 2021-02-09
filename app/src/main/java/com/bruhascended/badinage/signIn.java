package com.bruhascended.badinage;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signIn extends AppCompatActivity {
    DatabaseReference UsersData;
    Context abcs;
    FirebaseAuth mAuth;
    TextInputEditText password;
    Boolean show;
    String userPass;
    TextInputEditText username;
    String usernamee;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.sign_in);
        this.abcs = this;
        this.mAuth = FirebaseAuth.getInstance();
        this.username = (TextInputEditText) findViewById(R.id.name);
        this.password = (TextInputEditText) findViewById(R.id.Password);
        this.UsersData = FirebaseDatabase.getInstance().getReference().child("UserDB");
        this.show = false;
        final float den = getResources().getDisplayMetrics().density;
        final LinearLayout aniMa = (LinearLayout) findViewById(R.id.amim);
        final View activityRootView = findViewById(R.id.lol);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                if (activityRootView.getHeight() - (r.bottom - r.top) > 100) {
                    RelativeLayout.LayoutParams ss = (RelativeLayout.LayoutParams) aniMa.getLayoutParams();
                    ss.addRule(10);
                    ss.removeRule(12);
                    ss.height = -1;
                    aniMa.setLayoutParams(ss);
                    return;
                }
                RelativeLayout.LayoutParams ss2 = (RelativeLayout.LayoutParams) aniMa.getLayoutParams();
                ss2.addRule(12);
                ss2.removeRule(10);
                ss2.height = Math.round(320.0f * den);
                aniMa.setLayoutParams(ss2);
            }
        });
        ObjectAnimator varl = ObjectAnimator.ofFloat(aniMa, "translationY", new float[]{0.0f});
        varl.setDuration(700);
        aniMa.setVisibility(View.VISIBLE);
        varl.start();
        ObjectAnimator var = ObjectAnimator.ofFloat(findViewById(R.id.appico), "translationY", new float[]{-155.0f * den});
        var.setDuration(700);
        var.start();
    }

    @SuppressLint({"SetTextI18n"})
    public void UserSignIn(View view) {
        this.usernamee = this.username.getText().toString().trim();
        this.userPass = this.password.getText().toString().trim();
        if (!this.usernamee.isEmpty() && !this.userPass.isEmpty()) {
            this.mAuth.signInWithEmailAndPassword(this.usernamee, this.userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    signIn.this.correctPass();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void correctPass() {
        if (this.mAuth.getCurrentUser() == null) {
            animateWrong();
            Toast.makeText(this, "wrong password/e-mail", Toast.LENGTH_SHORT).show();
        } else if (this.mAuth.getCurrentUser().isEmailVerified()) {
            getSharedPreferences("101", 0).edit().putString("Email", this.usernamee).putString("Password", this.userPass).apply();
            getSharedPreferences("sign", 0).edit().putBoolean("in", true).apply();
            startActivity(new Intent(this, ServerActivity.class));
            finish();
        } else {
            animateWrong();
            Toast.makeText(this, "e-mail is not verified", Toast.LENGTH_SHORT).show();
        }
    }

    private void animateWrong() {
        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(225);
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

    public void openSignUp(View view) {
        startActivity(new Intent(this, SignUP.class));
    }

    public void forgotPassword(View view) {
        String email = this.username.getText().toString().trim();
        if (email.equals("")) {
            Toast.makeText(this, "Enter account email", Toast.LENGTH_SHORT).show();
        } else {
            this.mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(signIn.this, "Password reset link sent", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
