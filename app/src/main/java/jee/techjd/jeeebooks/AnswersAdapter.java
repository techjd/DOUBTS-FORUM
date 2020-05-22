package jee.techjd.jeeebooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import jee.techjd.jeeebooks.Models.Users;
import jee.techjd.jeeebooks.Models.solution;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ImageViewwHolder> {

    Context context;
    List<solution> AnsInfo;

    public AnswersAdapter(Context context, List<solution> ansInfo) {
        this.context = context;
        AnsInfo = ansInfo;
    }

    @NonNull
    @Override
    public ImageViewwHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sol, parent, false);

        ImageViewwHolder viewHolder = new ImageViewwHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewwHolder holder, int position) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final solution sol = AnsInfo.get(position);

        holder.ans.setText(sol.getSolution());
       // holder.name.setText(sol.getPublisher());

        getUserInfo(holder.name, sol.getPublisher());
        Picasso.get()
                .load(sol.getMimageUrl())

                .into(holder.img_sol);


    }

    @Override
    public int getItemCount() {
        return AnsInfo.size();
    }

    public class ImageViewwHolder extends RecyclerView.ViewHolder{

        public TextView ans;
        public ImageView img_sol;
        public TextView name;

        public ImageViewwHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            ans = itemView.findViewById(R.id.text_ans);
            img_sol = itemView.findViewById(R.id.img_ans);


        }
    }

    private void getUserInfo( final TextView username, String publisher) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisher);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                username.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}


