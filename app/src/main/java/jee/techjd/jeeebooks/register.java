package jee.techjd.jeeebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private FirebaseAuth mAuth ;
    private DatabaseReference mDatabaseRef;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        final EditText reg_email = findViewById(R.id.reg_email);
        final EditText reg_password = findViewById(R.id.reg_password);

        Button signup = findViewById(R.id.SignUp);

        final EditText reg_name = findViewById(R.id.reg_name);

        final String name = reg_name.getText().toString();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(register.this);
                pd.setMessage("Please wait...");
                pd.show();
                final String Email = reg_email.getText().toString().trim();
                final String Password = reg_password.getText().toString();
                final String name = reg_name.getText().toString();
                mAuth.createUserWithEmailAndPassword(Email, Password)

                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    pd.dismiss();
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    pd.dismiss();
                                    String userId = mAuth.getCurrentUser().getUid();
                                    String devicetoken = FirebaseInstanceId.getInstance().getToken();
                                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                    Map userInfo = new HashMap<>();

                                    userInfo.put("name",name);
                                    userInfo.put("device_token",devicetoken);
                                    userInfo.put("email",Email);
                                    currentUserDb.updateChildren(userInfo);

                                    Intent intent = new Intent(register.this, MainActivity.class);

                                    startActivity(intent);

                                }


                            }
                        });
            }
        });


    }


}
