package com.bruhascended.badinage;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

class HandleNewServer extends ServerActivity {
    HandleNewServer(String server, String password, Context thisCon, boolean old, boolean newS, EditText sPass, String uid, String currentUserName) {
        if (old) {
            final String str = password;
            final String str2 = server;
            final String str3 = uid;
            final String str4 = currentUserName;
            final Context context = thisCon;
            FirebaseDatabase.getInstance().getReference().child("Servers").child(server).child(EmailAuthProvider.PROVIDER_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue().toString().equals(str.trim().equals("") ? "" : str)) {
                        FirebaseMessaging.getInstance().subscribeToTopic(str2);
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                        mRef.child("UserDB").child(str3).child("Servers").child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(str2);
                        mRef.child("Servers").child(str2).child("Users").child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(str4);
                        return;
                    }
                    Toast.makeText(context, "incorrect password", Toast.LENGTH_SHORT).show();
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "Connection error", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (newS) {
            final String str5 = server;
            final String str6 = currentUserName;
            final String str7 = uid;
            final EditText editText = sPass;
            final Context context2 = thisCon;
            final String str8 = password;
            FirebaseDatabase.getInstance().getReference().child("Servers").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, String> xd = (HashMap) dataSnapshot.getValue();
                    int k = 0;
                    String[] servers = new String[xd.keySet().size()];
                    for (Object a : xd.keySet()) {
                        if (a != null) {
                            servers[k] = a.toString();
                            k++;
                        }
                    }
                    if (!Arrays.asList(servers).contains(str5)) {
                        try {
                            FirebaseDatabase.getInstance().getReference().child("Servers").child(str5).child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        if (((HashMap) dataSnapshot.getValue()).containsValue(str6)) {
                                            Toast.makeText(context2, "server already added", Toast.LENGTH_SHORT).show();
                                        } else if (str5.trim().split(" ").length == 1) {
                                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                                            mRef.child("UserDB").child(str7).child("Servers").child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(str5);
                                            mRef.child("Servers").child(str5).child("Users").child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(str6);
                                            Calendar cal = Calendar.getInstance();
                                            cal.add(5, 1);
                                            String[] date = cal.getTime().toString().split(" ");
                                            String[] time = date[3].split(":");
                                            mRef.child("UserDB").child(str7).child("Servers").child(str5).child("date").setValue(time[0] + ":" + time[1] + " " + date[1] + " " + (Integer.valueOf(date[2]).intValue() - 1));
                                            editText.setText("");
                                            FirebaseMessaging.getInstance().subscribeToTopic(str5);
                                        } else {
                                            Toast.makeText(context2, "servers can't contain spaces", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (str5.trim().split(" ").length == 1) {
                                        DatabaseReference mRef2 = FirebaseDatabase.getInstance().getReference();
                                        mRef2.child("UserDB").child(str7).child("Servers").child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(str5);
                                        mRef2.child("Servers").child(str5).child("Users").child(String.valueOf(Calendar.getInstance().getTimeInMillis())).setValue(str6);
                                        mRef2.child("Servers").child(str5).child(EmailAuthProvider.PROVIDER_ID).setValue(str8.equals("") ? "" : str8);
                                        Calendar cal2 = Calendar.getInstance();
                                        cal2.add(5, 1);
                                        String[] date2 = cal2.getTime().toString().split(" ");
                                        String[] time2 = date2[3].split(":");
                                        mRef2.child("Servers").child(str5).child("date").setValue(time2[0] + ":" + time2[1] + " " + date2[1] + " " + (Integer.valueOf(date2[2]).intValue() - 1));
                                        mRef2.child("Servers").child(str5).child("locked").setValue(Boolean.valueOf(!str8.equals("")));
                                        editText.setText("");
                                        FirebaseMessaging.getInstance().subscribeToTopic(str5);
                                    } else {
                                        Toast.makeText(context2, "servers can't contain spaces", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(context2, "Connection error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            Toast.makeText(context2, "servers can't contain '.'s ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context2, "server already exists", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context2, "Connection error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
