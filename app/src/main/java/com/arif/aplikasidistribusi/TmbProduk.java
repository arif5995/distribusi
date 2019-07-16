package com.arif.aplikasidistribusi;

import android.app.Notification;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arif.aplikasidistribusi.Db.ItemProduk;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

import static com.arif.aplikasidistribusi.Notifikasion.App.CHANNEL_1;

public class TmbProduk extends AppCompatActivity implements View.OnClickListener{


    ImageView ImgPhoto, AddPhoto, Camera, back;
    EditText etNama, etHarga;
    Button simpanProduk, Pesan;
    private NotificationManagerCompat notificationManager;

    DatabaseReference FotoProduk;
    private FirebaseAuth auth;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private static final int PICK_IMAGE_FILE = 1;
    private StorageReference mStorage;
    private static String TAG = TmbProduk.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmb_produk);

        //database
        mStorage = FirebaseStorage.getInstance().getReference("Produk_Petani");
        FotoProduk = FirebaseDatabase.getInstance().getReference("User");

        notificationManager = NotificationManagerCompat.from(this);
        ImgPhoto = findViewById(R.id.ViewPhoto);
        AddPhoto = findViewById(R.id.ImgPoto);
        auth = FirebaseAuth.getInstance();
        // Camera = findViewById(R.id.Camera);
        etNama = findViewById(R.id.edtnamePdr);
        etHarga = findViewById(R.id.edtHarga);
        simpanProduk = findViewById(R.id.btnSimpanPdr);
        Pesan = findViewById(R.id.btnPesan);
        back = findViewById(R.id.back);

        simpanProduk.setOnClickListener(this);
        AddPhoto.setOnClickListener(this);
        Pesan.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void UploadFile() {
        final android.app.AlertDialog waitingDialog = new SpotsDialog(TmbProduk.this, "Harap Sabar");
        waitingDialog.show();

        if (mImageUri != null) {
            StorageReference fileReference = mStorage.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(TmbProduk.this, "success", Toast.LENGTH_SHORT).show();
                    waitingDialog.dismiss();
                    ItemProduk itemProduk = new ItemProduk();
                    itemProduk.setNama(etNama.getText().toString());
                    itemProduk.setHarga(etHarga.getText().toString());
                    itemProduk.setImage(taskSnapshot.getDownloadUrl().toString());

                    FotoProduk.child("Poto")
                            .child(FotoProduk.push().getKey()).setValue(itemProduk)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    etNama.setText("");
                                    etHarga.setText("");
                                    ImgPhoto.setImageDrawable(null);
                                }
                            });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TmbProduk.this, e.getMessage(), Toast.LENGTH_SHORT).
                                    show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        }
                    });
            String Nama = etNama.getText().toString();
            String Harga = etHarga.getText().toString();
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1)
                    .setSmallIcon(R.drawable.ic_pengepul)
                    .setContentTitle(Nama)
                    .setContentText(Harga)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();

            notificationManager.notify(1, notification);

            startActivity(new Intent(TmbProduk.this, MenuChatlActivity.class));
            waitingDialog.dismiss();
        } else {
            Toast.makeText(this, "no file selected", Toast.LENGTH_SHORT).show();
        }

        waitingDialog.dismiss();

    }

//    private void OtwChat(){
//        Intent uid2 = getIntent();
//        String UID2 = uid2.getStringExtra("UID");
//        Intent i = new Intent(TmbProduk.this,MenuChatlActivity.class);
//        i.putExtra("UID2",UID2);
//        Log.e(TAG, "UID2 "+UID2);
//        startActivity(i);
//    }

    private void OpenFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FILE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_FILE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).fit().into(ImgPhoto);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImgPoto:
                OpenFile();
                break;
            case R.id.btnSimpanPdr:
                UploadFile();
                break;
            case R.id.btnPesan:
             //  OtwChat();
                Intent i = new Intent(TmbProduk.this,MenuChatlActivity.class);
                startActivity(i);
                break;
            case R.id.back:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(TmbProduk.this, LoginActivity.class));
                finish();
                break;
        }
    }
}
