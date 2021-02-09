package com.bruhascended.badinage;

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chat extends Fragment {
    static String[] msgList;
    static HashMap userColor = new HashMap();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chatView = inflater.inflate(R.layout.chat, container, false);
        final SharedPreferences sharedp = getActivity().getPreferences(0);
        final ImageView theView = (ImageView) chatView.findViewById(R.id.anyehi);
        final TextView theView2 = (TextView) chatView.findViewById(R.id.dial);
        final ImageView theViewn = (ImageView) chatView.findViewById(R.id.anyehi2);
        final TextView theView2n = (TextView) chatView.findViewById(R.id.dial2);
        final Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        final Animation fadeout = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);
        ImageButton sendBut = (ImageButton) chatView.findViewById(R.id.sendBut);
        final ImageButton speechBut = (ImageButton) chatView.findViewById(R.id.speechBut);
        final ImageButton laughBut = (ImageButton) chatView.findViewById(R.id.laughBut);
        final ImageButton cryBut = (ImageButton) chatView.findViewById(R.id.cryBut);
        final ImageButton pokeBut = (ImageButton) chatView.findViewById(R.id.pokeBut);
        final ImageButton gunBut = (ImageButton) chatView.findViewById(R.id.gunBut);
        final EditText userMessage = (EditText) chatView.findViewById(R.id.text_message);
        ListView chatList = (ListView) chatView.findViewById(R.id.message_list);
        View.OnClickListener buttonListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (sharedp.getBoolean("hasopenbef", true)) {
                    theView.setVisibility(View.VISIBLE);
                    theView2.setVisibility(View.VISIBLE);
                    theView.startAnimation(fadeIn);
                    theView2.startAnimation(fadeIn);
                }
                if (sharedp.getBoolean("hasopenbefn", true)) {
                    theViewn.setVisibility(View.GONE);
                    theView2n.setVisibility(View.GONE);
                    theViewn.startAnimation(fadeout);
                    theView2n.startAnimation(fadeout);
                    if (v.getTransitionName().equals("speech")) {
                        sharedp.edit().putBoolean("hasopenbefn", false).apply();
                    }
                }
                if ((!v.getTransitionName().equals("send") && !v.getTransitionName().equals("speech")) || !userMessage.getText().toString().trim().equals("")) {
                    String[] date = Calendar.getInstance().getTime().toString().split(" ");
                    String[] time = date[3].split(":");
                    String nDate = time[0] + ":" + time[1] + " " + date[1] + " " + date[2];
                    FirebaseDatabase.getInstance().getReference().child("Servers").child(ServerActivity.currentServer).child("date").setValue(nDate);
                    MessageHandler.msgThis(ServerActivity.currentUserName, Long.valueOf(Calendar.getInstance().getTimeInMillis()), userMessage.getText().toString(), ServerActivity.currentServer, nDate, v.getTransitionName());
                    chat.this.animateIBI(cryBut, 0, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                    chat.this.animateIBI(gunBut, 0, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                    chat.this.animateIBI(laughBut, 0, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                    chat.this.animateIBI(pokeBut, 0, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                    chat.this.animateIBI(speechBut, 0, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
                    if (v.getTransitionName().equals("send")) {
                        chat.this.sendNotificationToUser(ServerActivity.currentServer, userMessage.getText().toString(), ServerActivity.currentUserName);
                    }
                    userMessage.setText("");
                }
            }
        };
        ImageView BG = (ImageView) chatView.findViewById(R.id.bg);
        Bitmap dsf = loadImageFromStorage();
        Boolean bg = Boolean.valueOf(ServerActivity.thisCon.getSharedPreferences("bg", 0).getBoolean("bg", false));
        if (dsf != null && bg.booleanValue()) {
            BG.setImageBitmap(dsf);
        }
        sendBut.setTransitionName("send");
        sendBut.setOnClickListener(buttonListener);
        speechBut.setTransitionName("speech");
        speechBut.setOnClickListener(buttonListener);
        laughBut.setTransitionName("laugh");
        laughBut.setOnClickListener(buttonListener);
        pokeBut.setTransitionName("poke");
        pokeBut.setOnClickListener(buttonListener);
        gunBut.setTransitionName("gun");
        gunBut.setOnClickListener(buttonListener);
        cryBut.setTransitionName("cry");
        cryBut.setOnClickListener(buttonListener);
        final ImageButton imageButton3 = laughBut;
        final ImageButton imageButton4 = pokeBut;
        final ImageButton imageButton5 = speechBut;
        final SharedPreferences sharedPreferences = sharedp;
        final ImageView imageView = theView;
        final Animation animation = fadeout;
        final TextView textView = theView2;
        final ImageView imageView2 = theViewn;
        final TextView textView2 = theView2n;
        final Animation animation2 = fadeIn;
        sendBut.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                chat.this.animateIBI(cryBut, 180, 400);
                chat.this.animateIBI(gunBut, 300, 400);
                chat.this.animateIBI(imageButton3, 120, 400);
                chat.this.animateIBI(imageButton4, 240, 400);
                chat.this.animateIBI(imageButton5, 60, 400);
                if (sharedPreferences.getBoolean("hasopenbef", true)) {
                    imageView.startAnimation(animation);
                    textView.startAnimation(animation);
                    imageView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    sharedPreferences.edit().putBoolean("hasopenbef", false).apply();
                }
                if (sharedPreferences.getBoolean("hasopenbefn", true)) {
                    imageView2.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    imageView2.startAnimation(animation2);
                    textView2.startAnimation(animation2);
                }
                return true;
            }
        });
        final ListView listView = chatList;
        FirebaseDatabase.getInstance().getReference().child("Servers").child(ServerActivity.currentServer).child("Messages").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Map<String, String> xd = (HashMap) dataSnapshot.getValue();
                    List<String> listOfDates = new ArrayList<>(xd.keySet());
                    List<Long> longList = new ArrayList<>();
                    for (String j : listOfDates) {
                        longList.add(Long.valueOf(j));
                    }
                    Collections.sort(longList);
                    String[] lmao = new String[longList.size()];
                    int rofl = 0;
                    for (Long ikr : longList) {
                        lmao[rofl] = xd.get(ikr.toString());
                        rofl++;
                    }
                    chat.msgList = lmao;
                    listView.setAdapter(new ChatListAdapter(lmao));
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String[] msgInfo = new String[3];
                if (chat.msgList[position] != null) {
                    msgInfo = chat.msgList[position].split("");
                }
                String body = msgInfo[1];
                String type = msgInfo[2];
                SharedPreferences sadas = MainActivity.getConOfMain().getSharedPreferences("Files", 0);
                if (!type.equals("File")) {
                    MainActivity.createSoundnVibration(type, body, position, view);
                } else if (sadas.getBoolean(body, true)) {
                    MainActivity.createSoundnVibration(type, body, position, view);
                    sadas.edit().putBoolean(body, false).apply();
                } else {
                    StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(body);
                    final String sad = httpsReference.getName();
                    httpsReference.getMetadata().addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {
                        public void onComplete(@NonNull Task<StorageMetadata> task) {
                            String sssd = task.getResult().getContentType();
                            File imgFile = new File(new File((Environment.getExternalStorageDirectory().getAbsolutePath() + "/Badinage") + "/" + ServerActivity.currentServer).getAbsolutePath() + "/" + sad + "." + task.getResult().getContentType().split("/")[1]);
                            Intent dA = new Intent("android.intent.action.VIEW");
                            dA.setDataAndType(Uri.fromFile(imgFile), sssd);
                            try {
                                MainActivity.getConOfMain().startActivity(dA);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(MainActivity.getConOfMain(), "can not open file", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return chatView;
    }

    /* access modifiers changed from: private */
    public void animateIBI(ImageButton but, int i, int dur) {
        ObjectAnimator varl = ObjectAnimator.ofFloat(but, "translationY", new float[]{((float) (-i)) * getActivity().getResources().getDisplayMetrics().density});
        varl.setDuration((long) dur);
        varl.start();
    }

    /* access modifiers changed from: package-private */
    public void sendNotificationToUser(String server, String message, String sender) {
        FirebaseDatabase.getInstance().getReference().child("notificationRequests").child(server).child(sender).setValue(message);
    }

    private Bitmap loadImageFromStorage() {
        try {
            return BitmapFactory.decodeStream(new FileInputStream(new File(new ContextWrapper(MainActivity.getConOfMain().getApplicationContext()).getDir("imageDir", 0), "profile.jpg")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
