package com.arif.aplikasidistribusi;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arif.aplikasidistribusi.Db.User;
import com.arif.aplikasidistribusi.Register.RegPengepulActivity;
import com.arif.aplikasidistribusi.Register.RegPetaniActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button btnMasukPetani, btnMasukPengepul;
    private ImageView ImgPetani, ImgPengepul;

    FirebaseAuth auth;
    DatabaseReference userr, uid;
    FirebaseDatabase firebaseDatabase;
    private static String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);
        btnMasukPetani = findViewById(R.id.btnMasukPetani);
        ImgPetani = findViewById(R.id.imgPetani);
        ImgPengepul = findViewById(R.id.imgPengepul);


        btnMasukPetani.setOnClickListener(this);
        ImgPengepul.setOnClickListener(this);
        ImgPetani.setOnClickListener(this);

        //firebase
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    private void LoginUserPetani() {
        final String Email = etEmail.getText().toString().trim();
        final String Password = etPassword.getText().toString().trim();

        if (Email.isEmpty()) {
            etEmail.setError("Cek Email");
            etEmail.requestFocus();
            return;
        }
        if (Password.isEmpty()) {
            etPassword.setError("Cek Password");
            etPassword.requestFocus();
            return;
        }

        if (!etPassword.getText().toString().equals("")) {
            Toast.makeText(this, etPassword.getText(), Toast.LENGTH_LONG).show();

        } else {

        }
        final AlertDialog waitingDialog = new SpotsDialog(LoginActivity.this, "Harap Sabar");
        waitingDialog.show();
        auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    CheckEmail();


                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    waitingDialog.dismiss();
                }

            }
        });
    }

    public void CheckEmail() {
        final AlertDialog waitingDialog = new SpotsDialog(LoginActivity.this, "Harap Sabar");
        userr = firebaseDatabase.getReference("User").child(auth.getCurrentUser().getUid()).child("Profil");
        userr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String akses = user.getUser();

                if (akses.equals("Petani")) {
//                    String uid = auth.getCurrentUser().getUid();
                    Intent intent = new Intent(LoginActivity.this, TmbProduk.class);
//                    intent.putExtra("UID",uid);
//                    Log.e(TAG, "UID "+uid);
                    startActivity(intent);
                    waitingDialog.dismiss();
                    finish();

                } else {
                    startActivity(new Intent(LoginActivity.this, MenuChatlActivity.class));
                    finish();
                    waitingDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMasukPetani:
                LoginUserPetani();
                break;
            case R.id.imgPengepul:
                startActivity(new Intent(LoginActivity.this, RegPengepulActivity.class));
                finish();
                break;
            case R.id.imgPetani:
                startActivity(new Intent(LoginActivity.this, RegPetaniActivity.class));
                finish();
                break;
        }
    }


}
