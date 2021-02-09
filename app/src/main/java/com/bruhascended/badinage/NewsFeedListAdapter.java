package com.bruhascended.badinage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

class NewsFeedListAdapter extends ArrayAdapter<String> {
    NewsFeedListAdapter(String[] values) {
        super(MainActivity.getConOfMain(), R.layout.news_list_item, values);
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View theView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        String[] postInfo = new String[4];
        if (!Objects.equals(getItem(position), (Object) null)) {
            postInfo = ((String) getItem(position)).split("");
        }
        String username = postInfo[0];
        String body = postInfo[1];
        String date = postInfo[2];
        ((TextView) theView.findViewById(R.id.person_name)).setText(username);
        ((TextView) theView.findViewById(R.id.news)).setText(body);
        ((TextView) theView.findViewById(R.id.date_poss)).setText(TimeCoolifier.coolifyThis(date));
        return theView;
    }
}
