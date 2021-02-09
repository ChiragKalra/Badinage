package com.bruhascended.badinage;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class SettingActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.settings_activity);
        Switch volume = (Switch) findViewById(R.id.volume);
        final Boolean SoundEn = Boolean.valueOf(getSharedPreferences("sound", 0).getBoolean("ena", true));
        volume.setChecked(SoundEn.booleanValue());
        volume.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean z = false;
                SharedPreferences.Editor edit = SettingActivity.this.getSharedPreferences("sound", 0).edit();
                if (!SoundEn.booleanValue()) {
                    z = true;
                }
                edit.putBoolean("ena", z).apply();
            }
        });
        final Switch alert = (Switch) findViewById(R.id.notifySound);
        Switch notify = (Switch) findViewById(R.id.notify);
        Boolean aaz = Boolean.valueOf(getSharedPreferences("sound", 0).getBoolean("notify", true));
        if (!aaz.booleanValue()) {
            alert.setEnabled(false);
            alert.setChecked(false);
        } else {
            alert.setEnabled(true);
        }
        notify.setChecked(aaz.booleanValue());
        notify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean z;
                boolean z2 = true;
                Boolean aaz = Boolean.valueOf(SettingActivity.this.getSharedPreferences("sound", 0).getBoolean("notify", true));
                if (aaz.booleanValue()) {
                    alert.setEnabled(false);
                    alert.setChecked(false);
                } else {
                    alert.setEnabled(true);
                }
                SharedPreferences.Editor edit = SettingActivity.this.getSharedPreferences("sound", 0).edit();
                if (!aaz.booleanValue()) {
                    z = true;
                } else {
                    z = false;
                }
                edit.putBoolean("alert", z).apply();
                SharedPreferences.Editor edit2 = SettingActivity.this.getSharedPreferences("sound", 0).edit();
                if (aaz.booleanValue()) {
                    z2 = false;
                }
                edit2.putBoolean("notify", z2).apply();
            }
        });
        alert.setChecked(Boolean.valueOf(getSharedPreferences("sound", 0).getBoolean("alert", true)).booleanValue());
        alert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean z = true;
                Boolean ale = Boolean.valueOf(SettingActivity.this.getSharedPreferences("sound", 0).getBoolean("alert", true));
                SharedPreferences.Editor edit = SettingActivity.this.getSharedPreferences("sound", 0).edit();
                if (ale.booleanValue()) {
                    z = false;
                }
                edit.putBoolean("alert", z).apply();
            }
        });
        Switch tabs = (Switch) findViewById(R.id.showTabs);
        tabs.setChecked(Boolean.valueOf(getSharedPreferences("tabs", 0).getBoolean("hidetabs", false)).booleanValue());
        tabs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean z = false;
                Boolean tabHide = Boolean.valueOf(SettingActivity.this.getSharedPreferences("tabs", 0).getBoolean("hidetabs", false));
                SharedPreferences.Editor edit = SettingActivity.this.getSharedPreferences("tabs", 0).edit();
                if (!tabHide.booleanValue()) {
                    z = true;
                }
                edit.putBoolean("hidetabs", z).apply();
            }
        });
        Switch BgSw = (Switch) findViewById(R.id.bg);
        BgSw.setChecked(Boolean.valueOf(getSharedPreferences("bg", 0).getBoolean("bg", false)).booleanValue());
        BgSw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean z = false;
                Boolean bg = Boolean.valueOf(SettingActivity.this.getSharedPreferences("bg", 0).getBoolean("bg", false));
                SharedPreferences.Editor edit = SettingActivity.this.getSharedPreferences("bg", 0).edit();
                if (!bg.booleanValue()) {
                    z = true;
                }
                edit.putBoolean("bg", z).apply();
                if (!bg.booleanValue()) {
                    SettingActivity.this.pickImage();
                }
            }
        });
    }

    public void pickImage() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 2412);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 2412 || resultCode != -1) {
            return;
        }
        if (data == null) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            InputStream inputStream = getContentResolver().openInputStream(data.getData());
            Toast.makeText(this, "Saving background, please wait", Toast.LENGTH_LONG).show();
            saveToInternalStorage(BitmapFactory.decodeStream(inputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(new ContextWrapper(getApplicationContext()).getDir("imageDir", 0), "profile.png"));
            try {
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Toast.makeText(this, "saved!", Toast.LENGTH_SHORT).show();
                FileOutputStream fileOutputStream = fos;
            } catch (Exception e) {
                e = e;
                FileOutputStream fileOutputStream2 = fos;
                e.printStackTrace();
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    public void signOut(View item) {
        FirebaseAuth.getInstance().signOut();
        getSharedPreferences("sign", 0).edit().putBoolean("in", false).apply();
        finish();
        startActivity(new Intent(this, Gateway.class));
    }
}
