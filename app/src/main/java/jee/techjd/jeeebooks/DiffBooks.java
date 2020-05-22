package jee.techjd.jeeebooks;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;




/**
 * A simple {@link Fragment} subclass.
 */
public class DiffBooks extends Fragment {
    private Button physics;
    private Button chemistry;
    private Button maths;
    public DiffBooks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diff_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        physics = view.findViewById(R.id.physics);
        maths = view.findViewById(R.id.maths);
        chemistry = view.findViewById(R.id.chemistry);

        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(getActivity(), jee.techjd.jeeebooks.physics.class);
                startActivity(open);
            }
        });
        maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(getActivity(), Maths.class);
                startActivity(open);
            }
        });
        chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(getActivity(), Chemistry.class);
                startActivity(open);
            }
        });
    }

}
