package jee.techjd.jeeebooks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Books extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_CODE = 100;
    String[] subjects = {"Physics", "Chemistry", "Maths"};
    private Uri mImageUri;
    private ImageView imageView;
    private EditText questionTitle , Description;

    private Button post;
    private Spinner spinner;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String subject, questiontitle , questionDescription , ImageUrl ;
    private StorageTask mUploadTask;
    private ProgressDialog pd;

    public Books() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       spinner = view.findViewById(R.id.spinner);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, subjects);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
        imageView = view.findViewById(R.id.image_view);
        questionTitle = view.findViewById(R.id.ques_title);


        Description = view.findViewById(R.id.ques_desc);
        post = view.findViewById(R.id.postques);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestionDetails();
                pd= new ProgressDialog(getActivity());
                pd.setMessage("Please wait...");
                pd.show();
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);

            }
        });
        Button chooseImage = view.findViewById(R.id.chooseImage);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String pos = subjects[position];

    }

    private void saveQuestionDetails()
    {

        subject = spinner.getSelectedItem().toString();
        questiontitle = questionTitle.getText().toString();
        questionDescription = Description.getText().toString();
        String key = FirebaseDatabase.getInstance().getReference().getKey();
        String randomkey = getSaltString();

        if (mImageUri == null) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Questions");
            HashMap<String, Object> newImage = new HashMap();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String userId = auth.getUid();
            String quesId = mDatabaseRef.push().getKey();
            newImage.put("mSubject", subject);
            newImage.put("publisher", userId);
            newImage.put("mquestionTilte",questiontitle);
            newImage.put("mDescription",questionDescription);
            newImage.put("mQuesId",quesId);

            mDatabaseRef.child(quesId).setValue(newImage);
            pd.dismiss();
            getActivity().finish();

            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }






        if(mImageUri != null) {
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("QuestionImages").child(randomkey);
            Bitmap bitmap = null;


            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mImageUri);
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
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Questions");
                            HashMap<String, Object> newImage = new HashMap();
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String userId = auth.getUid();
                            String quesId = mDatabaseRef.push().getKey();
                            newImage.put("mSubject", subject);
                            newImage.put("publisher", userId);
                            newImage.put("mquestionTilte",questiontitle);
                            newImage.put("mDescription",questionDescription);
                            newImage.put("mQuesId",quesId);
                            newImage.put("mimageUrl",uri.toString());
                            mDatabaseRef.child(quesId).setValue(newImage);
                            pd.dismiss();
                            getActivity().finish();
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            startActivity(intent);



                        }
                    });
                }
            });


        }

        else {

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

            Picasso.get().load(mImageUri).into(imageView);
        }
    }





    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
