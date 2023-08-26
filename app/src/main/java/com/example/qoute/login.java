package com.example.qoute;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class login extends AppCompatActivity {
    TextView register,forgetP;
    EditText email,password;
    Button login;
    ProgressBar progressBar;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.pass);
        register=findViewById(R.id.Register);
        login=findViewById(R.id.Login);
        forgetP=findViewById(R.id.forgetPass);
        mAuth = FirebaseAuth.getInstance();
        progressBar =findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        // checking if user is null or not
        currentUser = mAuth.getCurrentUser();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailT = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                // if format of email doesn't matches return null
                if (!Patterns.EMAIL_ADDRESS.matcher(emailT).matches()) {
                    email.setError("Invalid Email");
                    email.setFocusable(true);

                } else {
                    loginUser(emailT, pass);
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login.this, signup.class);
                startActivity(intent);
                login.this.finish();

            }
        });

        // Recover Your Password using email
        forgetP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailet = new EditText(this);//write your registered email
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emaill = emailet.getText().toString().trim();
                beginRecovery(emaill);//send a mail on the mail to recover password
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void beginRecovery(String emaill) {
        progressBar.setVisibility(View.VISIBLE);

        // send reset password email
        mAuth.sendPasswordResetEmail(emaill).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(login.this, "Sent done.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(login.this, "Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(login.this, "Error Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void loginUser(String emaill, String pass) {
        progressBar.setVisibility(View.VISIBLE);

        // sign in with email and password after authenticating
        mAuth.signInWithEmailAndPassword(emaill, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    progressBar.setVisibility(View.GONE);
                    FirebaseUser user = mAuth.getCurrentUser();

                    if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                        assert user != null;
                        String email = user.getEmail();
                        String uid = user.getUid();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("email", email);
                        hashMap.put("uid", uid);
                        hashMap.put("name", "");
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        // store the value in Database in "Users" Node
                        DatabaseReference reference = database.getReference("Users");

                        // storing the value in Firebase
                        reference.child(uid).setValue(hashMap);
                    }
                    assert user != null;
                    Toast.makeText(login.this, "Registered User " + user.getEmail(), Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(login.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(login.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(login.this, "Error Occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}