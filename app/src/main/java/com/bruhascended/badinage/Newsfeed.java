package com.bruhascended.badinage;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Newsfeed extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newsfeed = inflater.inflate(R.layout.news_feed, container, false);
        final Switch anonymousTrue = (Switch) newsfeed.findViewById(R.id.anony);
        final ImageButton newPostButton = (ImageButton) newsfeed.findViewById(R.id.new_post);
        final RelativeLayout postLayout = (RelativeLayout) newsfeed.findViewById(R.id.post_layout);
        final Button postThis = (Button) newsfeed.findViewById(R.id.postThisButton);
        final EditText newPostBody = (EditText) newsfeed.findViewById(R.id.newsCon);
        final Button cancel = (Button) newsfeed.findViewById(R.id.canc);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                postLayout.setVisibility(View.GONE);
                newPostButton.setVisibility(View.VISIBLE);
                newPostBody.setText("");
                ((InputMethodManager) Newsfeed.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(cancel.getWindowToken(), 0);
            }
        });
        postLayout.setVisibility(View.GONE);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Servers").child(ServerActivity.currentServer).child("Newsfeed");
        final ListView listView = (ListView) newsfeed.findViewById(R.id.newsfeedList);
        mRef.addValueEventListener(new ValueEventListener() {
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
                    for (int i = 0; i < lmao.length / 2; i++) {
                        String temp = lmao[i];
                        lmao[i] = lmao[(lmao.length - i) - 1];
                        lmao[(lmao.length - i) - 1] = temp;
                    }
                    listView.setAdapter(new NewsFeedListAdapter(lmao));
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        newPostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                postLayout.setVisibility(View.VISIBLE);
                newPostButton.setVisibility(View.GONE);
            }
        });
        final EditText editText = newPostBody;
        final RelativeLayout relativeLayout = postLayout;
        final ImageButton imageButton = newPostButton;
        postThis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((InputMethodManager) Newsfeed.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(postThis.getWindowToken(), 0);
                if (!editText.getText().toString().trim().equals("")) {
                    relativeLayout.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);
                    Calendar cal = Calendar.getInstance();
                    cal.add(5, 1);
                    String[] date = cal.getTime().toString().split(" ");
                    String[] time = date[3].split(":");
                    String nDate = time[0] + ":" + time[1] + " " + date[1] + " " + (Integer.valueOf(date[2]).intValue() - 1);
                    String body = String.valueOf(editText.getText());
                    imageButton.setVisibility(View.VISIBLE);
                    Log.e("Anonymous", anonymousTrue.isChecked() + "");
                    NewPost.postThis(anonymousTrue.isChecked() ? "Anonymous" : ServerActivity.currentUserName, Long.valueOf(Calendar.getInstance().getTimeInMillis()), body, ServerActivity.currentServer, nDate, ServerActivity.currentUserName);
                    editText.setText("");
                    anonymousTrue.setChecked(false);
                }
            }
        });
        ImageView BG = (ImageView) newsfeed.findViewById(R.id.bg);
        Bitmap dsf = loadImageFromStorage();
        Boolean bg = Boolean.valueOf(ServerActivity.thisCon.getSharedPreferences("bg", 0).getBoolean("bg", false));
        if (dsf != null && bg.booleanValue()) {
            BG.setImageBitmap(dsf);
        }
        return newsfeed;
    }

    private Bitmap loadImageFromStorage() {
        try {
            return BitmapFactory.decodeStream(new FileInputStream(new File(new ContextWrapper(ServerActivity.thisCon.getApplicationContext()).getDir("imageDir", 0), "profile.jpg")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
