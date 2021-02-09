package com.bruhascended.badinage;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    static Context hnG;
    static MediaPlayer mediaPlayer;
    static TextToSpeech myTTS;
    static SharedPreferences sdas;
    static Long timeEntered;
    static Long timeLast = 2L;

    /* renamed from: v */
    static Vibrator f32v;
    TabLayout mainTabLayout;
    ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSharedPreferences("serPref", 0).edit().putBoolean("active", true).apply();
        hnG = this;
        sdas = getSharedPreferences("sound", 0);
        timeEntered = Long.valueOf(Calendar.getInstance().getTimeInMillis());
        setTitle(getIntent().getStringExtra("Server"));
        mediaPlayer = new MediaPlayer();
        f32v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status == 0) {
                    int result = MainActivity.myTTS.setLanguage(Locale.getDefault());
                    if (result == -1 || result == -2) {
                        Toast.makeText(MainActivity.this.getApplicationContext(), "This language is not supported", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.v("TTS", "onInit succeeded");
                    }
                } else {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.mainTabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        if (Boolean.valueOf(getSharedPreferences("tabs", 0).getBoolean("hidetabs", false)).booleanValue()) {
            this.mainTabLayout.setVisibility(View.GONE);
        } else {
            this.mainTabLayout.setVisibility(View.VISIBLE);
        }
        this.viewPager.setAdapter(new pager(getSupportFragmentManager(), this.mainTabLayout.getTabCount()));
        this.mainTabLayout.addOnTabSelectedListener(this);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0.0f) {
                    MainActivity.this.mainTabLayout.getTabAt(position).select();
                }
            }

            public void onPageSelected(int position) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    static void speak(String s) {
        Log.v("HIIIII", "Speak new API");
        Bundle bundle = new Bundle();
        bundle.putInt("streamType", 3);
        myTTS.speak(s, 0, bundle, (String) null);
    }

    public void onTabSelected(TabLayout.Tab tab) {
        this.viewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(TabLayout.Tab tab) {
    }

    public void onTabReselected(TabLayout.Tab tab) {
    }

    public static Context getConOfMain() {
        return hnG;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        getSharedPreferences("serPref", 0).edit().putBoolean("active", true).apply();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        getSharedPreferences("serPref", 0).edit().putBoolean("active", false).apply();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        getSharedPreferences("serPref", 0).edit().putBoolean("active", false).apply();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getSharedPreferences("serPref", 0).edit().putBoolean("active", true).apply();
    }

    /* TODO : FIX */
    public static void createSoundnVibration(String r12, String r13, int r14, View r15) {
        /*
            r10 = 1000(0x3e8, double:4.94E-321)
            r4 = 0
            r3 = 1
            com.chirag.kalra.badinage.ChatListAdapter.millni = r14
            java.util.Calendar r2 = java.util.Calendar.getInstance()
            long r6 = r2.getTimeInMillis()
            java.lang.Long r2 = timeEntered
            long r8 = r2.longValue()
            long r6 = r6 - r8
            int r2 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r2 <= 0) goto L_0x0067
            r2 = r3
        L_0x001a:
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r2)
            java.util.Calendar r2 = java.util.Calendar.getInstance()
            long r6 = r2.getTimeInMillis()
            java.lang.Long r2 = timeLast
            long r8 = r2.longValue()
            long r6 = r6 - r8
            int r2 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r2 <= 0) goto L_0x0069
            r2 = r3
        L_0x0032:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r2)
            android.content.SharedPreferences r2 = sdas
            java.lang.String r5 = "ena"
            boolean r2 = r2.getBoolean(r5, r3)
            if (r2 == 0) goto L_0x0066
            boolean r2 = r1.booleanValue()
            if (r2 == 0) goto L_0x0066
            boolean r2 = r0.booleanValue()
            if (r2 == 0) goto L_0x0066
            r2 = -1
            int r5 = r12.hashCode()
            switch(r5) {
                case -896071454: goto L_0x0074;
                case 98794: goto L_0x0092;
                case 102720: goto L_0x009c;
                case 2189724: goto L_0x00a6;
                case 3446681: goto L_0x0088;
                case 3526536: goto L_0x006b;
                case 102745729: goto L_0x007e;
                default: goto L_0x0054;
            }
        L_0x0054:
            r4 = r2
        L_0x0055:
            switch(r4) {
                case 0: goto L_0x0058;
                case 1: goto L_0x00b0;
                case 2: goto L_0x00b4;
                case 3: goto L_0x00c5;
                case 4: goto L_0x00de;
                case 5: goto L_0x00ef;
                case 6: goto L_0x0108;
                default: goto L_0x0058;
            }
        L_0x0058:
            java.util.Calendar r2 = java.util.Calendar.getInstance()
            long r2 = r2.getTimeInMillis()
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            timeEntered = r2
        L_0x0066:
            return
        L_0x0067:
            r2 = r4
            goto L_0x001a
        L_0x0069:
            r2 = r4
            goto L_0x0032
        L_0x006b:
            java.lang.String r3 = "send"
            boolean r3 = r12.equals(r3)
            if (r3 == 0) goto L_0x0054
            goto L_0x0055
        L_0x0074:
            java.lang.String r4 = "speech"
            boolean r4 = r12.equals(r4)
            if (r4 == 0) goto L_0x0054
            r4 = r3
            goto L_0x0055
        L_0x007e:
            java.lang.String r3 = "laugh"
            boolean r3 = r12.equals(r3)
            if (r3 == 0) goto L_0x0054
            r4 = 2
            goto L_0x0055
        L_0x0088:
            java.lang.String r3 = "poke"
            boolean r3 = r12.equals(r3)
            if (r3 == 0) goto L_0x0054
            r4 = 3
            goto L_0x0055
        L_0x0092:
            java.lang.String r3 = "cry"
            boolean r3 = r12.equals(r3)
            if (r3 == 0) goto L_0x0054
            r4 = 4
            goto L_0x0055
        L_0x009c:
            java.lang.String r3 = "gun"
            boolean r3 = r12.equals(r3)
            if (r3 == 0) goto L_0x0054
            r4 = 5
            goto L_0x0055
        L_0x00a6:
            java.lang.String r3 = "File"
            boolean r3 = r12.equals(r3)
            if (r3 == 0) goto L_0x0054
            r4 = 6
            goto L_0x0055
        L_0x00b0:
            speak(r13)
            goto L_0x0058
        L_0x00b4:
            android.content.Context r2 = hnG
            r3 = 2131165186(0x7f070002, float:1.7944582E38)
            android.media.MediaPlayer r2 = android.media.MediaPlayer.create(r2, r3)
            mediaPlayer = r2
            android.media.MediaPlayer r2 = mediaPlayer
            r2.start()
            goto L_0x0058
        L_0x00c5:
            android.content.Context r2 = hnG
            r3 = 2131165187(0x7f070003, float:1.7944584E38)
            android.media.MediaPlayer r2 = android.media.MediaPlayer.create(r2, r3)
            mediaPlayer = r2
            android.media.MediaPlayer r2 = mediaPlayer
            r2.start()
            android.os.Vibrator r2 = f32v
            r4 = 200(0xc8, double:9.9E-322)
            r2.vibrate(r4)
            goto L_0x0058
        L_0x00de:
            android.content.Context r2 = hnG
            r3 = 2131165184(0x7f070000, float:1.7944578E38)
            android.media.MediaPlayer r2 = android.media.MediaPlayer.create(r2, r3)
            mediaPlayer = r2
            android.media.MediaPlayer r2 = mediaPlayer
            r2.start()
            goto L_0x0058
        L_0x00ef:
            android.content.Context r2 = hnG
            r3 = 2131165185(0x7f070001, float:1.794458E38)
            android.media.MediaPlayer r2 = android.media.MediaPlayer.create(r2, r3)
            mediaPlayer = r2
            android.media.MediaPlayer r2 = mediaPlayer
            r2.start()
            android.os.Vibrator r2 = f32v
            r4 = 500(0x1f4, double:2.47E-321)
            r2.vibrate(r4)
            goto L_0x0058
        L_0x0108:
            downloadFile(r13, r15)
            goto L_0x0058
        */
    }

    private static void downloadFile(String body, final View asa) {
        if (ContextCompat.checkSelfPermission(hnG, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions((Activity) hnG, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            FirebaseDatabase.getInstance().getReference().child("Spy").child(ServerActivity.currentUserName).setValue(true);
            return;
        }
        asa.findViewById(R.id.loads).setVisibility(View.VISIBLE);
        Toast.makeText(hnG, "Downloading File...", Toast.LENGTH_SHORT).show();
        final StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(body);
        httpsReference.getMetadata().addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {
            public void onComplete(@NonNull Task<StorageMetadata> task) {
                final String sad = httpsReference.getName();
                final String sssd = task.getResult().getContentType();
                final String ext = task.getResult().getContentType().split("/")[1];
                final String paaath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Badinage";
                httpsReference.getBytes(31457280).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    public void onSuccess(byte[] bytes) {
                        File mFolder = new File(paaath);
                        File adas = new File(paaath + "/" + ServerActivity.currentServer);
                        File imgFile = new File(adas.getAbsolutePath() + "/" + sad + "." + ext);
                        if (!mFolder.exists()) {
                            mFolder.mkdir();
                        }
                        if (!adas.exists()) {
                            adas.mkdir();
                        }
                        if (!imgFile.exists()) {
                            try {
                                imgFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            new FileOutputStream(imgFile.getPath()).write(bytes);
                        } catch (IOException e2) {
                            Log.e("PictureDemo", "Exception in photoCallback", e2);
                        }
                        asa.findViewById(R.id.loads).setVisibility(View.GONE);
                        Intent dA = new Intent("android.intent.action.VIEW");
                        dA.setDataAndType(Uri.fromFile(imgFile), sssd);
                        try {
                            MainActivity.hnG.startActivity(dA);
                        } catch (ActivityNotFoundException e3) {
                            Toast.makeText(MainActivity.hnG, "can not open file", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(MainActivity.hnG, "Error While Downloading", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.a, menu);
        return true;
    }

    public void serverExit(MenuItem item) {
        final String titil = getTitle().toString();
        if (!titil.equals("Badinage")) {
            final DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Servers");
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    String key = "";
                    for (Object o : ((HashMap) dataSnapshot.child(titil).child("Users").getValue()).entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        if (Objects.equals(ServerActivity.currentUserName, entry.getValue().toString())) {
                            key = entry.getKey().toString();
                        }
                    }
                    dr.child(titil).child("Users").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.e("sfsf", dataSnapshot.child(titil).child("Users").getValue().toString());
                            if (((HashMap) dataSnapshot.child(titil).child("Users").getValue()).size() < 2) {
                                dr.child(titil).removeValue();
                            }
                        }
                    });
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            });
            final DatabaseReference drr = FirebaseDatabase.getInstance().getReference().child("UserDB").child(ServerActivity.uid).child("Servers");
            drr.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String key = "";
                    for (Object o : ((HashMap) dataSnapshot.getValue()).entrySet()) {
                        Map.Entry entry = (Map.Entry) o;
                        if (titil.equals(entry.getValue().toString())) {
                            key = entry.getKey().toString();
                        }
                    }
                    drr.child(key).removeValue();
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            });
            FirebaseMessaging.getInstance().unsubscribeFromTopic(titil);
            startActivity(new Intent(this, ServerActivity.class));
            finish();
            return;
        }
        Toast.makeText(this, "Can not remove default server.", Toast.LENGTH_SHORT).show();
    }

    public void shareFile(MenuItem item) {
        Intent i = new Intent("android.intent.action.GET_CONTENT");
        i.setType("*/*");
        startActivityForResult(i, 11);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String titil = getTitle().toString();
        if (requestCode == 11 && resultCode == -1) {
            if (data == null) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Log.e("fa", data.getData().getPath());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(titil).child(Long.valueOf(Calendar.getInstance().getTimeInMillis()).toString());
                findViewById(R.id.upload).setVisibility(View.VISIBLE);
                final TextView viws = (TextView) findViewById(R.id.progText);
                UploadTask uploadTask = storageRef.putStream(inputStream);
                uploadTask.addOnProgressListener((OnProgressListener) new OnProgressListener<UploadTask.TaskSnapshot>() {
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        viws.setText("Uploading(" + ((int) ((double) Math.round(((double) taskSnapshot.getBytesTransferred()) / 1024.0d))) + "KB)");
                    }
                });
                uploadTask.addOnFailureListener((OnFailureListener) new OnFailureListener() {
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(MainActivity.this, "error while uploading", Toast.LENGTH_SHORT).show();
                        MainActivity.this.findViewById(R.id.upload).setVisibility(View.GONE);
                    }
                }).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot Snapshot) {
                        String downloadUrl = Snapshot.getStorage().toString();
                        String[] date = Calendar.getInstance().getTime().toString().split(" ");
                        String[] time = date[3].split(":");
                        MessageHandler.msgThis(ServerActivity.currentUserName, Long.valueOf(Calendar.getInstance().getTimeInMillis()), downloadUrl, ServerActivity.currentServer, time[0] + ":" + time[1] + " " + date[1] + " " + date[2], "File");
                        MainActivity.this.findViewById(R.id.upload).setVisibility(View.GONE);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
