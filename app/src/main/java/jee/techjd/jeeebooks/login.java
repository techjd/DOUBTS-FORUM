package jee.techjd.jeeebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private EditText email, password;
    private TextView creaet;
    private Button button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edit_password);
        button = findViewById(R.id.login);

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        creaet = findViewById(R.id.createAccount);
        mAuth = FirebaseAuth.getInstance();
        creaet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(login.this);
                pd.setMessage("Please wait...");
                pd.show();
                String mEmail = email.getText().toString();
                String mPassword = password.getText().toString();

                mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(login.this, "sign in error", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            pd.dismiss();
                            String currentuserId = mAuth.getCurrentUser().getUid();
                            String devicetoken = FirebaseInstanceId.getInstance().getToken();

                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserId);
                            Map userInfo = new HashMap<>();

                            userInfo.put("device_token",devicetoken);

                            currentUserDb.updateChildren(userInfo);
                            pd.dismiss();
                            Intent intent = new Intent(login.this, MainActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
