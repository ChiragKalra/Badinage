package com.bruhascended.badinage;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class MessageHandler {
    MessageHandler() {
    }

    static void msgThis(String UserName, Long dateInMS, String msgBody, String Server, String date, String type) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Servers").child(Server).child("Messages");
        mRef.child(dateInMS.toString()).setValue(UserName + 136 + msgBody + 136 + type + 136 + date);
    }
}
