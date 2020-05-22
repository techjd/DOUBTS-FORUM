package jee.techjd.jeeebooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

public class edit_question extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Question");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        String title = intent.getStringExtra("mquestionTitle");
        String description = intent.getStringExtra("mDescription");


        final EditText edt_title = findViewById(R.id.ques_title_edit);
        final EditText edt_desc = findViewById(R.id.ques_desc_edit);
        Button update = findViewById(R.id.editques);

        edt_title.setText(title);
        edt_desc.setText(description);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String id = intent.getStringExtra("mQuesId");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("mquestionTilte",edt_title.getText().toString());
                hashMap.put("mDescription", edt_desc.getText().toString());

                FirebaseDatabase.getInstance().getReference("Questions")
                        .child(id).updateChildren(hashMap);
                Toast.makeText(getApplication(),"Successfully Updated",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
