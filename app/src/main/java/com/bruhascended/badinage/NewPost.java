package com.bruhascended.badinage;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class NewPost {
    NewPost() {
    }

    static void postThis(String UserName, Long dateInMS, String postBody, String Server, String date, String afk) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Servers").child(Server).child("Newsfeed");
        mRef.child(dateInMS.toString()).setValue(UserName + 137 + postBody + 137 + date + 137 + afk);
    }
}
