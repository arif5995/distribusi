package com.arif.aplikasidistribusi;

import android.app.Notification;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arif.aplikasidistribusi.Db.ChatMassege;
import com.arif.aplikasidistribusi.Db.ItemProduk;
import com.arif.aplikasidistribusi.Db.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.arif.aplikasidistribusi.Notifikasion.App.CHANNEL_1;

public class MenuChatlActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST = 1;
    private FirebaseListAdapter<ChatMassege> adapter;
    RelativeLayout menuChat;
    FloatingActionButton fab;
    private DatabaseReference mDatabaseChat, chat, Menu;
    TextView Nama, Harga;
    ImageView ImgPdr, back, imgLok;
    private NotificationManagerCompat notificationManager;
    private static String TAG = MenuChatlActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_chatl);
//
//        Intent intent = getIntent();
//        String uid = intent.getStringExtra("UID2");
//        finish();
//        Log.e(TAG,"uid "+uid);
        imgLok = findViewById(R.id.imgLok);
        back = findViewById(R.id.back);
        Nama = findViewById(R.id.tvNameProduk);
        Harga = findViewById(R.id.tvHarga);
        ImgPdr = findViewById(R.id.imgGambar);
        menuChat = (RelativeLayout) findViewById(R.id.tmpChat);
        fab = findViewById(R.id.fab);
        notificationManager = NotificationManagerCompat.from(this);


        imgLok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat = FirebaseDatabase.getInstance().getReference("Chat");
                mDatabaseChat = FirebaseDatabase.getInstance().getReference("User").child("Poto");
                mDatabaseChat.removeValue();
                chat.removeValue();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuChatlActivity.this, LoginActivity.class));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference("Chat").push().setValue(new ChatMassege(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");

                Toast.makeText(getApplicationContext(),"Terkirim",Toast.LENGTH_LONG).show();
            }
        });
//        Menu = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance()
//                .getCurrentUser().getUid()).child("Profil");
//        Menu.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                String akses = user.getUser();
//                if (akses.equals("Petani")) {
//                    ambilData();
//                    notification();
//
//                } else {
//                    ambilData();
//                    notification();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        ambilData();
        displayChatMessege();

    }

    private void ambilData() {
        mDatabaseChat = FirebaseDatabase.getInstance().getReference("User").child("Poto");
        mDatabaseChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    ItemProduk itemProduk = post.getValue(ItemProduk.class);
                    Nama.setText(itemProduk.getNama().toString());
                    Harga.setText(itemProduk.getHarga().toString());
                    Picasso.get().load(itemProduk.getImage()).fit().into(ImgPdr);

                    notification();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void notification() {

        String etNama = Nama.getText().toString();
        String etHarga = Harga.getText().toString();
        if (mDatabaseChat != null) {

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1)
                    .setSmallIcon(R.drawable.ic_pengepul)
                    .setContentTitle(etNama)
                    .setContentText(etHarga)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();

            notificationManager.notify(1, notification);
        }
    }

    private void displayChatMessege() {
        ListView listOfMassege = findViewById(R.id.listOfmessege);
        adapter = new FirebaseListAdapter<ChatMassege>(this, ChatMassege.class, R.layout.list_item,
                FirebaseDatabase.getInstance().getReference("Chat")) {
            @Override
            protected void populateView(View v, ChatMassege model, int position) {
                TextView messageText, messageUser, messageTime;
                messageText = v.findViewById(R.id.message_text);
                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy", model.getMessageTime()));
            }
        };
        listOfMassege.setAdapter(adapter);
    }

}
