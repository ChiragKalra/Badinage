package com.bruhascended.badinage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class PplViewListAdapter extends ArrayAdapter<String> {
    PplViewListAdapter(String[] values, Context thisC) {
        super(thisC, R.layout.news_list_item, values);
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View theView = LayoutInflater.from(getContext()).inflate(R.layout.ppl_view_item, parent, false);
        String userId = (String) getItem(position);
        String usernAM = ServerActivity.userDB.child(userId).child("UserName").getValue().toString();
        String lastL = ServerActivity.userDB.child(userId).child("Time").getValue().toString();
        String emaisl = ServerActivity.userDB.child(userId).child("email").getValue().toString();
        ((TextView) theView.findViewById(R.id.pName)).setText(usernAM);
        ((TextView) theView.findViewById(R.id.lastLogin)).setText(lastL);
        ((TextView) theView.findViewById(R.id.emailID)).setText(emaisl);
        return theView;
    }
}
