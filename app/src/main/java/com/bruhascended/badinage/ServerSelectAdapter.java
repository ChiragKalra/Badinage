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

class ServerSelectAdapter extends ArrayAdapter<String> {

    /* renamed from: sw */
    private Context f45sw;

    ServerSelectAdapter(String[] values, Context thisC) {
        super(thisC, R.layout.news_list_item, values);
        this.f45sw = thisC;
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View theView = LayoutInflater.from(getContext()).inflate(R.layout.server_list_item, parent, false);
        theView.findViewById(R.id.loda).setVisibility(View.GONE);
        String Server = (String) getItem(position);
        TextView server = (TextView) theView.findViewById(R.id.serverName);
        final ImageView lockImage = (ImageView) theView.findViewById(R.id.locked);
        server.setText(Server);
        server.setTextColor(this.f45sw.getResources().getColor(R.color.colorPrimaryDark));
        final TextView date = (TextView) theView.findViewById(R.id.time);
        final TextView peopleView = (TextView) theView.findViewById((R.id.serverPeople));
        if (Server != null) {
            FirebaseDatabase.getInstance().getReference().child("Servers").child(Server).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, String> xd;
                    if (dataSnapshot.getValue() != null && (xd = (HashMap) dataSnapshot.child("Users").getValue()) != null) {
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
                        ((TextView) theView.findViewById(R.id.eas)).setText(String.valueOf(locked));
                        lockImage.setImageResource(locked ? R.drawable.ic_locked : R.drawable.ic_lock_open);
                        date.setText(TimeCoolifier.coolifyThis(dataSnapshot.child("date").getValue().toString()));
                        peopleView.setText(serverPeople);
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        return theView;
    }
}
