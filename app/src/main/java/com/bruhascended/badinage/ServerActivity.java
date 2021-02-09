package com.bruhascended.badinage;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ServerActivity extends AppCompatActivity {
    public static String currentServer;
    public static String currentUserName;
    static View hintDialoge;
    static SharedPreferences sharedp;
    static Context thisCon;
    static String uid;
    static DataSnapshot userDB;

    /* renamed from: aa */
    String f44aa;
    FirebaseUser currentUser;
    Dialog dialog;
    DatabaseReference mRef;
    ImageButton newServerButton;
    EditText sPass;
    EditText sPass2;
    Boolean show;
    Boolean show2 = false;
    Boolean showlow = true;
    String wdw = "";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = this.currentUser.getUid();
        this.mRef = FirebaseDatabase.getInstance().getReference();
        setUserName(this.mRef.child("UserDB").child(this.currentUser.getUid()).child("UserName"));
        setContentView((int) R.layout.servers_page);
        thisCon = this;
        this.f44aa = getIntent().getStringExtra("Server");
        setServerSelectList();
        setServerList();
        setOnClickServerList();
        hintDialoge = findViewById(R.id.hintDialoge);
        this.newServerButton = (ImageButton) findViewById(R.id.new_server);
        this.sPass2 = (EditText) findViewById(R.id.serverPass2);
        this.sPass = (EditText) findViewById(R.id.serverPass);
        sharedp = getPreferences(0);
        this.show = false;
    }

    private void setUserName(DatabaseReference userName) {
        userName.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                ServerActivity.currentUserName = dataSnapshot.getValue().toString();
                ServerActivity.this.mRef.child("ColorDB").child(ServerActivity.currentUserName).setValue(Integer.valueOf(ThreadLocalRandom.current().nextInt(0, 6)));
                if (ServerActivity.this.f44aa != null) {
                    ServerActivity.currentServer = ServerActivity.this.f44aa;
                    ServerActivity.this.startActivity(new Intent(ServerActivity.this, MainActivity.class).putExtra("Server", ServerActivity.this.f44aa));
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setOnClickServerList() {
        ((ListView) findViewById(R.id.serverList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ServerActivity.currentServer = ((TextView) view.findViewById(R.id.serverName)).getText().toString();
                        if (!ServerActivity.currentServer.equals("mitron$$")) {
                            ServerActivity.this.startActivity(new Intent(ServerActivity.thisCon, MainActivity.class).putExtra("Server", ServerActivity.currentServer));
                        }
                    }
                }, 150);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void setServerList() {
        Set<String> ssdw = getSharedPreferences("serPref", 0).getStringSet("serverSet", (Set) null);
        if (ssdw != null) {
            String[] hahxa = (String[]) Arrays.asList(ssdw.toArray()).toArray(new String[ssdw.size()]);
            Arrays.sort(hahxa, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    if (o1 == null && o2 == null) {
                        return 0;
                    }
                    if (o1 == null) {
                        return 1;
                    }
                    if (o2 == null) {
                        return -1;
                    }
                    return o1.compareTo(o2);
                }
            });
            for (String sda : hahxa) {
                if (sda != null && !sda.equals("mitron$$")) {
                    FirebaseMessaging.getInstance().subscribeToTopic(sda);
                }
            }
            hahxa[hahxa.length - 1] = "mitron$$";
            ((ListView) findViewById(R.id.serverList)).setAdapter(new ServerListAdapter(hahxa, this));
        }
        FirebaseDatabase.getInstance().getReference().child("UserDB").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Servers").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Map<String, String> xd = (HashMap) dataSnapshot.getValue();
                    String[] haha = (String[]) Arrays.asList(xd.values().toArray()).toArray(new String[(xd.size() + 1)]);
                    Arrays.sort(haha, new Comparator<String>() {
                        public int compare(String o1, String o2) {
                            if (o1 == null && o2 == null) {
                                return 0;
                            }
                            if (o1 == null) {
                                return 1;
                            }
                            if (o2 == null) {
                                return -1;
                            }
                            return o1.compareTo(o2);
                        }
                    });
                    ServerActivity.this.getSharedPreferences("serPref", 0).edit().putStringSet("serverSet", new HashSet(Arrays.asList(haha))).apply();
                    haha[haha.length - 1] = "mitron$$";
                    ((ListView) ServerActivity.this.findViewById(R.id.serverList)).setAdapter(new ServerListAdapter(haha, ServerActivity.this));
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void setServerSelectList() {
        Set<String> ssdw = getSharedPreferences("serPref", 0).getStringSet("serverSeSet", (Set) null);
        if (ssdw != null) {
            ((ListView) findViewById(R.id.selector)).setAdapter(new ServerSelectAdapter((String[]) Arrays.asList(ssdw.toArray()).toArray(new String[ssdw.size()]), this));
        }
        final DatabaseReference mRef2 = FirebaseDatabase.getInstance().getReference();
        mRef2.child("UserDB").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Servers").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Map<String, String> xd = (HashMap) dataSnapshot.getValue();
                    final String[] haha = (String[]) Arrays.asList(xd.values().toArray()).toArray(new String[xd.size()]);
                    mRef2.child("Servers").addValueEventListener(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Map<String, String> xd = (HashMap) dataSnapshot.getValue();
                                int k = 0;
                                Log.e("AA", xd.keySet().toString());
                                String[] servers = new String[xd.keySet().size()];
                                for (Object a : xd.keySet()) {
                                    if (a != null) {
                                        servers[k] = a.toString();
                                        k++;
                                    }
                                }
                                for (String d : haha) {
                                    for (int w = 0; w < servers.length; w++) {
                                        if (d.equals(servers[w])) {
                                            servers[w] = null;
                                        }
                                    }
                                }
                                Log.e("thsi", servers.length + "");
                                Log.e("thsi", haha.length + "");
                                String[] sss = new String[(servers.length - haha.length)];
                                int k2 = 0;
                                for (String ikr : servers) {
                                    if (ikr != null) {
                                        Log.e("thsi", ikr);
                                        try {
                                            sss[k2] = ikr;
                                        } catch (Exception e) {
                                        }
                                        k2++;
                                    }
                                }
                                ServerActivity.this.getSharedPreferences("serPref", 0).edit().putStringSet("serverSeSet", new HashSet(Arrays.asList(sss))).apply();
                                ((ListView) ServerActivity.this.findViewById(R.id.selector)).setAdapter(new ServerSelectAdapter(sss, ServerActivity.this));
                            }
                        }

                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ((ListView) findViewById(R.id.selector)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ((ListView) ServerActivity.this.findViewById(R.id.selector)).setSelection(position);
                ServerActivity.this.wdw = ((TextView) view.findViewById(R.id.serverName)).getText().toString();
                if (((TextView) view.findViewById(R.id.eas)).getText().toString().equals("true")) {
                    ServerActivity.this.changePassLay(true);
                    return;
                }
                new HandleNewServer(ServerActivity.this.wdw, ServerActivity.this.sPass2.getText().toString().trim(), ServerActivity.thisCon, !ServerActivity.this.wdw.equals(""), false, ServerActivity.this.sPass2, ServerActivity.uid, ServerActivity.currentUserName);
                ServerActivity.this.animateLowView(325);
                ServerActivity.this.showlow = true;
                ServerActivity.this.newServerButton.setVisibility(View.VISIBLE);
                ((ListView) ServerActivity.this.findViewById(R.id.selector)).setItemChecked(-1, true);
                ((InputMethodManager) ServerActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ServerActivity.this.findViewById(R.id.cancel).getWindowToken(), 0);
            }
        });
    }

    /* access modifiers changed from: private */
    public void changePassLay(boolean ts) {
        final float den = getResources().getDisplayMetrics().density;
        final View view = findViewById(R.id.pasLy);
        if (ts) {
            if (view.getHeight() == 0) {
                ValueAnimator va = ValueAnimator.ofInt(new int[]{0, 60});
                va.setDuration(200);
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float value = Float.valueOf(((float) ((Integer) animation.getAnimatedValue()).intValue()) * den);
                        view.getLayoutParams().height = value.intValue();
                        view.requestLayout();
                    }
                });
                va.start();
            }
        } else if (((float) view.getHeight()) == 60.0f * den) {
            ValueAnimator va2 = ValueAnimator.ofInt(new int[]{60, 0});
            va2.setDuration(200);
            va2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float value = Float.valueOf(((float) ((Integer) animation.getAnimatedValue()).intValue()) * den);
                    view.getLayoutParams().height = value.intValue();
                    view.requestLayout();
                }
            });
            va2.start();
        }
    }

    public void newServer(final View view) {
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        if (sharedp.getBoolean("hasopenbef", true)) {
            hintDialoge.startAnimation(fadeout);
            hintDialoge.setVisibility(View.GONE);
            sharedp.edit().putBoolean("hasopenbef", false).apply();
        }
        final LinearLayout newServerLayout = (LinearLayout) findViewById(R.id.newServerLayout);
        final EditText sName = (EditText) findViewById(R.id.serverh);
        final EditText sPass3 = (EditText) findViewById(R.id.serverPass);
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sName.setText("");
                view.setVisibility(View.VISIBLE);
                newServerLayout.setVisibility(View.GONE);
                ((InputMethodManager) ServerActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ServerActivity.this.findViewById(R.id.cancel).getWindowToken(), 0);
            }
        });
        final Button okServer = (Button) findViewById(R.id.okSer);
        view.setVisibility(View.GONE);
        newServerLayout.setVisibility(View.VISIBLE);
        okServer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean z;
                ((InputMethodManager) ServerActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(okServer.getWindowToken(), 0);
                String password = sPass3.getText().toString().trim();
                String server = sName.getText().toString();
                Log.e("password entered", password);
                Context context = ServerActivity.thisCon;
                if (!server.equals("")) {
                    z = true;
                } else {
                    z = false;
                }
                new HandleNewServer(server, password, context, false, z, sPass3, ServerActivity.uid, ServerActivity.currentUserName);
                sName.setText("");
                ServerActivity.this.findViewById(R.id.newServerLayout).setVisibility(View.GONE);
                sPass3.setText("");
            }
        });
    }

    public void openSettings(MenuItem item) {
        startActivity(new Intent(thisCon, SettingActivity.class));
    }

    public void showPass(View view) {
        if (!this.show.booleanValue()) {
            ((ImageButton) view).setImageResource(R.drawable.ic_visibility_off_black);
            ((EditText) findViewById(R.id.serverPass)).setInputType(145);
            this.show = true;
            return;
        }
        ((ImageButton) view).setImageResource(R.drawable.ic_visibility_black);
        ((EditText) findViewById(R.id.serverPass)).setInputType(129);
        this.show = false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.server, menu);
        return true;
    }

    public void ShowOtherServers(View view) {
        if (this.showlow.booleanValue()) {
            this.newServerButton.setVisibility(View.GONE);
            this.showlow = false;
            animateLowView(25);
            return;
        }
        animateLowView(325);
        this.showlow = true;
        this.newServerButton.setVisibility(View.VISIBLE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(findViewById(R.id.cancel).getWindowToken(), 0);
        this.sPass2.setText("");
        ((ListView) findViewById(R.id.selector)).setItemChecked(-1, true);
    }

    /* access modifiers changed from: private */
    public void animateLowView(int j) {
        ObjectAnimator sdad = ObjectAnimator.ofFloat(findViewById(R.id.exisServers), "translationY", new float[]{((float) j) * getResources().getDisplayMetrics().density});
        changePassLay(false);
        sdad.setDuration(400);
        sdad.start();
    }

    public void showPass2(View view) {
        if (!this.show2.booleanValue()) {
            ((ImageButton) view).setImageResource(R.drawable.ic_visibility_off_black);
            ((EditText) findViewById(R.id.serverPass2)).setInputType(145);
            this.show2 = true;
            return;
        }
        ((ImageButton) view).setImageResource(R.drawable.ic_visibility_black);
        ((EditText) findViewById(R.id.serverPass2)).setInputType(129);
        this.show2 = false;
    }

    public void AddExisServer(View view) {
        boolean z;
        String password = this.sPass2.getText().toString().trim();
        String str = this.wdw;
        Context context = thisCon;
        if (!this.wdw.equals("")) {
            z = true;
        } else {
            z = false;
        }
        new HandleNewServer(str, password, context, z, false, this.sPass2, uid, currentUserName);
        animateLowView(325);
        this.showlow = true;
        this.newServerButton.setVisibility(View.VISIBLE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(findViewById(R.id.cancel).getWindowToken(), 0);
        this.sPass2.setText("");
        ((ListView) findViewById(R.id.selector)).setItemChecked(-1, true);
    }

    public void showPpl(MenuItem item) {
        this.dialog = new Dialog(this);
        final View v = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ppl_dialoge, (ViewGroup) null, false);
        final ListView sda = (ListView) v.findViewById(R.id.pplView);
        FirebaseDatabase.getInstance().getReference().child("UserDB").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ServerActivity.userDB = dataSnapshot;
                    Map<String, String> xd = (HashMap) dataSnapshot.getValue();
                    int k = 0;
                    String[] users = new String[xd.keySet().size()];
                    for (Object a : xd.keySet()) {
                        if (a != null) {
                            users[k] = a.toString();
                            k++;
                        }
                    }
                    Arrays.sort(users);
                    sda.setAdapter(new PplViewListAdapter(users, ServerActivity.thisCon));
                    ServerActivity.this.dialog.setContentView(v);
                    ServerActivity.this.dialog.show();
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void hideNow(View view) {
        this.dialog.hide();
    }
}
