package jee.techjd.jeeebooks;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import jee.techjd.jeeebooks.Models.post;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class own_questions extends Fragment {
    private RecyclerView mRecyclerView;
    private QuestionsAdapter mquestionAdapter;
    private FirebaseAuth mAuth ;
    private DatabaseReference mDatabaseRef;
    private List<post> mpost;

    public own_questions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_own_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        final String userId = mAuth.getCurrentUser().getUid();

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        pd.show();





        mpost = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Questions");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    post posts = snapshot.getValue(post.class);
                    if (posts.getPublisher().equals(userId)){
                        mpost.add(posts);
                    }
                    pd.dismiss();
                    mquestionAdapter = new QuestionsAdapter(getActivity(), mpost);
                    Collections.reverse(mpost);
                    mRecyclerView.setAdapter(mquestionAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
