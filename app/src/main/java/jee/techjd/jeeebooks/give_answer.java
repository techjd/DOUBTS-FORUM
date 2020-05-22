package jee.techjd.jeeebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class give_answer extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private EditText ans;
    private ImageView sol_img;
    private Button post,choose;
    private DatabaseReference mDatabaseRef;
    FirebaseUser firebaseUser;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_answer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Answer This");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ans = findViewById(R.id.ques_detailed_ans);
        sol_img = findViewById(R.id.sol_image);
        post = findViewById(R.id.postanswer);
        choose= findViewById(R.id.choose_sol_image);
        Intent intent = getIntent();
        String quesId = intent.getStringExtra("mQuesId");

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd= new ProgressDialog(give_answer.this);
                pd.setMessage("Please wait...");
                pd.show();
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                postAnswer();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(sol_img);
        }
    }

    private void postAnswer() {
        final String solution = ans.getText().toString();
        String randomkey = getSaltString();
        Intent get = getIntent();
        String quesId = get.getStringExtra("mQuesId");
        String publisherId = get.getStringExtra("publisher");

        if (mImageUri == null) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Solutions").child(quesId);
            HashMap<String, Object> newImage = new HashMap();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String userId = auth.getUid();
            String solutionId = mDatabaseRef.push().getKey();

            newImage.put("publisher", userId);
            newImage.put("solution", solution);
            newImage.put("solutionId", solutionId);


            mDatabaseRef.child(solutionId).setValue(newImage);
            Intent intent1 = getIntent();
            String quesId1 = intent1.getStringExtra("mQuesId");
            if (!publisherId.equals(firebaseUser.getUid())){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisherId);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("publisher", userId);
                hashMap.put("text", "commented: "+solution);
                hashMap.put("solutionId", quesId1);
                hashMap.put("issolution", true);
                reference.push().setValue(hashMap);
            }
            pd.dismiss();
            Intent intent = new Intent(give_answer.this,MainActivity.class);
            startActivity(intent);
            finish();
        }



        else {
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("SolutionImages").child(randomkey);
            Bitmap bitmap = null;


            try {
                bitmap = MediaStore.Images.Media.getBitmap(give_answer.this.getContentResolver(), mImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri) {
                            Intent get = getIntent();
                            String quesId = get.getStringExtra("mQuesId");
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Solutions").child(quesId);
                            HashMap<String, Object> newImage = new HashMap();
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String userId = auth.getUid();
                            String solutionId = mDatabaseRef.push().getKey();

                            newImage.put("publisher", userId);
                            newImage.put("solution",solution);
                            newImage.put("solutionId",solutionId);
                            newImage.put("mimageUrl",uri.toString());
                            mDatabaseRef.child(solutionId).setValue(newImage);

                            Intent intent2 = getIntent();
                            String quesId2 = intent2.getStringExtra("mQuesId");
                            String publisherId2 = intent2.getStringExtra("publisher");
                            if (!publisherId2.equals(firebaseUser.getUid())) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisherId2);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("publisher", userId);
                                hashMap.put("text", "answered: "+solution);
                                hashMap.put("solutionId", quesId2);
                                hashMap.put("issolution", true);
                                reference.push().setValue(hashMap);
                            }


                            pd.dismiss();
                            Intent intent = new Intent(give_answer.this,MainActivity.class);
                            startActivity(intent);
                            finish();


                        }
                    });
                }
            });

        }

    }



    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
