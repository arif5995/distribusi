package com.arif.aplikasidistribusi.Register;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arif.aplikasidistribusi.Db.User;
import com.arif.aplikasidistribusi.LoginActivity;
import com.arif.aplikasidistribusi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class RegPengepulActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etFullname, etEmail, etPassword, etAlamat;
    private Button btnSimpan;
    private ImageView back;

    FirebaseAuth auth;
    DatabaseReference userPengepul;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_pengepul);

        //firebase
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userPengepul = firebaseDatabase.getReference("User");

        auth = FirebaseAuth.getInstance();
        back = findViewById(R.id.back);
        etFullname = findViewById(R.id.edtFullname);
        etEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);
        etAlamat = findViewById(R.id.edtAlamat);
        btnSimpan = findViewById(R.id.btnSimpan);

        btnSimpan.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void RegisterPetani() {
        final String Fullname = etFullname.getText().toString().trim();
        final String Email = etEmail.getText().toString().trim();
        final String Password = etPassword.getText().toString().trim();
        final String Alamat = etAlamat.getText().toString().trim();
        final String User = "Pengepul";

        if(Fullname.isEmpty()){
            etFullname.setError("Cek Fullname");
            etFullname.requestFocus();
            return;
        }
        if(Email.isEmpty()){
            etEmail.setError("Cek Email");
            etEmail.requestFocus();
            return;
        }
        if(Password.length()<6){
            etPassword.setError("Cek Password");
            etPassword.requestFocus();
            return;
        }
        if(Alamat.isEmpty()){
            etAlamat.setError("Cek Alamat");
            etAlamat.requestFocus();
            return;
        }
        final AlertDialog waitingDialog = new SpotsDialog(RegPengepulActivity.this);
        waitingDialog.show();

        auth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(Fullname,Email,Password,Alamat,User);
                            //input db
                            userPengepul.child(auth.getCurrentUser().getUid()).child("Profil")
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())  {
                                        etAlamat.setText("");
                                        etEmail.setText("");
                                        etPassword.setText("");
                                        etAlamat.setText("");
                                        Toast.makeText(RegPengepulActivity.this, "Alhamdulillah Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegPengepulActivity.this,LoginActivity.class));
                                        waitingDialog.dismiss();
                                    }else {
                                        waitingDialog.dismiss();
                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                            Toast.makeText(getApplicationContext(), "Email Sudah Ada", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(RegPengepulActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegPengepulActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSimpan:
                RegisterPetani();
                break;
            case R.id.back:
                startActivity(new Intent(RegPengepulActivity.this,LoginActivity.class));
                break;
        }
    }
}
