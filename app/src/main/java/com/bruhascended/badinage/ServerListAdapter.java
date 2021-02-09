package com.bruhascended.badinage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ServerListAdapter extends ArrayAdapter<String> {
    /* access modifiers changed from: private */
    public Context asa;

    ServerListAdapter(String[] values, Context thisC) {
        super(thisC, R.layout.news_list_item, values);
        this.asa = thisC;
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View theView = LayoutInflater.from(getContext()).inflate(R.layout.server_list_item, parent, false);
        final String Server = (String) getItem(position);
        TextView server = (TextView) theView.findViewById(R.id.serverName);
        if (Server.equals("mitron$$")) {
            theView.findViewById(R.id.hid).setVisibility(View.GONE);
            server.setText("mitron$$");
        } else {
            final ImageView lockImage = (ImageView) theView.findViewById(R.id.locked);
            server.setText(Server);
            final TextView date = (TextView) theView.findViewById(R.id.time);
            final TextView peopleView = (TextView) theView.findViewById(R.id.serverPeople);
            boolean locked = this.asa.getSharedPreferences("lock", 0).getBoolean(Server, false);
            lockImage.setImageResource(locked ? R.drawable.ic_locked : R.drawable.ic_lock_open);
            lockImage.setTag(Boolean.valueOf(locked));
            String serverPeople = this.asa.getSharedPreferences("people", 0).getString(Server, "loading...");
            date.setText(this.asa.getSharedPreferences("date", 0).getString(Server, "loading..."));
            peopleView.setText(serverPeople);
            FirebaseDatabase.getInstance().getReference().child("Servers").child(Server).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Map<String, String> xd = (HashMap) dataSnapshot.child("Users").getValue();
                        String[] lmao = (String[]) Arrays.asList(xd.values().toArray()).toArray(new String[xd.size()]);
                        StringBuilder serverPeople = new StringBuilder();
                        serverPeople.append(lmao[0]);
                        lmao[0] = "";
                        for (String a : lmao) {
                            if (a != null && !a.equals("")) {
                                serverPeople.append(", ").append(a);
                            }
                        }
                        boolean locked = ((Boolean) dataSnapshot.child("locked").getValue()).booleanValue();
                        lockImage.setImageResource(locked ? R.drawable.ic_locked : R.drawable.ic_lock_open);
                        lockImage.setTag(Boolean.valueOf(locked));
                        date.setText(TimeCoolifier.coolifyThis(dataSnapshot.child("date").getValue().toString()));
                        peopleView.setText(serverPeople);
                        ServerListAdapter.this.asa.getSharedPreferences("date", 0).edit().putString(Server, TimeCoolifier.coolifyThis(dataSnapshot.child("date").getValue().toString())).apply();
                        ServerListAdapter.this.asa.getSharedPreferences("people", 0).edit().putString(Server, String.valueOf(serverPeople)).apply();
                        ServerListAdapter.this.asa.getSharedPreferences("lock", 0).edit().putBoolean(Server, locked).apply();
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        return theView;
    }
}
