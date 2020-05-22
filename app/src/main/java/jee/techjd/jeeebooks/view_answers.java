package jee.techjd.jeeebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import jee.techjd.jeeebooks.Models.solution;



import java.util.ArrayList;
import java.util.List;

public class view_answers extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AnswersAdapter manswersAdapter;
    String mQuesId;

    private DatabaseReference mDatabaseRef;
    private List<solution> msol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answers);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Answers");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView = findViewById(R.id.view_answers_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent get = getIntent();
        mQuesId = get.getStringExtra("mQuesId");
        final ProgressDialog pd = new ProgressDialog(view_answers.this);
        pd.setMessage("Please wait...");
        pd.show();


        msol = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Solutions").child(mQuesId);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                msol.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    solution solu = snapshot.getValue(solution.class);
                    msol.add(solu);
                }
                pd.dismiss();
                manswersAdapter = new AnswersAdapter(getApplicationContext(), msol);
                mRecyclerView.setAdapter(manswersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
