package com.bruhascended.badinage;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;

class ChatListAdapter extends ArrayAdapter<String> {
    static int millni = -10;
    private int killbill;

    ChatListAdapter(String[] values) {
        super(MainActivity.getConOfMain(), R.layout.message_layout_item, values);
        this.killbill = values.length;
    }

    @SuppressLint({"SetTextI18n"})
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View theView = LayoutInflater.from(getContext()).inflate(R.layout.message_layout_item, parent, false);
        String[] msgInfo = new String[4];
        if (getItem(position) != null) {
            msgInfo = ((String) getItem(position)).split("");
        }
        String username = msgInfo[0];
        final String body = msgInfo[1];
        String type = msgInfo[2];
        String date = msgInfo[3];
        TextView usernameView = (TextView) theView.findViewById(R.id.person_name);
        final int[] colors = {R.color.aa, R.color.ab, R.color.ac, R.color.ad, R.color.ae, R.color.af};
        if (chat.userColor != null && chat.userColor.containsKey(username)) {
            usernameView.setTextColor(MainActivity.getConOfMain().getResources().getColor(colors[((Integer) chat.userColor.get(username)).intValue()]));
        }
        final String str = username;
        final TextView textView = usernameView;
        FirebaseDatabase.getInstance().getReference().child("ColorDB").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    int js = Integer.valueOf(dataSnapshot.getValue().toString()).intValue();
                    chat.userColor.put(str, Integer.valueOf(js));
                    textView.setTextColor(MainActivity.getConOfMain().getResources().getColor(colors[js]));
                } catch (Exception e) {
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ImageView msgType = (ImageView) theView.findViewById(R.id.MsgType);
        final TextView bodyView = (TextView) theView.findViewById(R.id.message);
        usernameView.setText(username);
        ((TextView) theView.findViewById(R.id.date_posted)).setText(TimeCoolifier.coolifyThis(date));
        RelativeLayout lay = (RelativeLayout) theView.findViewById(R.id.smthLay);
        LinearLayout layLin = (LinearLayout) theView.findViewById(R.id.laySmth);
        if (!(!username.equals(ServerActivity.currentUserName) || lay == null || layLin == null)) {
            lay.setBackgroundResource(R.drawable.message_background);
            usernameView.setText("You");
            layLin.setGravity(GravityCompat.END);
        }
        char c = 65535;
        switch (type.hashCode()) {
            case -896071454:
                if (type.equals("speech")) {
                    c = 1;
                    break;
                }
                break;
            case 98794:
                if (type.equals("cry")) {
                    c = 4;
                    break;
                }
                break;
            case 102720:
                if (type.equals("gun")) {
                    c = 5;
                    break;
                }
                break;
            case 2189724:
                if (type.equals("File")) {
                    c = 6;
                    break;
                }
                break;
            case 3446681:
                if (type.equals("poke")) {
                    c = 3;
                    break;
                }
                break;
            case 3526536:
                if (type.equals("send")) {
                    c = 0;
                    break;
                }
                break;
            case 102745729:
                if (type.equals("laugh")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                bodyView.setText(body);
                break;
            case 1:
                bodyView.setVisibility(View.GONE);
                msgType.setVisibility(View.VISIBLE);
                msgType.setImageResource(R.drawable.ic_record_voice_over_black);
                break;
            case 2:
                bodyView.setVisibility(View.GONE);
                msgType.setVisibility(View.VISIBLE);
                msgType.setImageResource(R.drawable.ic_smily);
                break;
            case 3:
                bodyView.setVisibility(View.GONE);
                msgType.setVisibility(View.VISIBLE);
                msgType.setImageResource(R.drawable.ic_push_button);
                break;
            case 4:
                bodyView.setVisibility(View.GONE);
                msgType.setVisibility(View.VISIBLE);
                msgType.setImageResource(R.drawable.ic_cry);
                break;
            case 5:
                bodyView.setVisibility(View.GONE);
                msgType.setVisibility(View.VISIBLE);
                msgType.setImageResource(R.drawable.ic_gun);
                break;
            case 6:
                msgType.setVisibility(View.VISIBLE);
                msgType.setImageResource(R.drawable.ic_file);
                final SharedPreferences fda = MainActivity.getConOfMain().getSharedPreferences("bcmc", 0);
                bodyView.setText(fda.getString(body, ".."));
                bodyView.setTextSize(2, 12.0f);
                FirebaseStorage.getInstance().getReferenceFromUrl(body).getMetadata().addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {
                    public void onComplete(@NonNull Task<StorageMetadata> task) {
                        bodyView.setVisibility(View.VISIBLE);
                        String ext = task.getResult().getContentType().toUpperCase();
                        bodyView.setText(ext);
                        fda.edit().putString(body, ext).apply();
                    }
                });
                break;
        }
        boolean ldi = this.killbill == position + 1;
        boolean ahkd = millni != position;
        if (ldi && ahkd && !type.equals("File")) {
            MainActivity.createSoundnVibration(type, body, position, theView);
        }
        return theView;
    }
}
