package jee.techjd.jeeebooks;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import jee.techjd.jeeebooks.Models.post;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import jee.techjd.jeeebooks.Models.Notifications;
import jee.techjd.jeeebooks.Models.Users;



import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Notifications> mNotification;

    public NotificationsAdapter(Context mContext, List<Notifications> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notification, parent, false);
        return new NotificationsAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Notifications notification = mNotification.get(position);
        if (notification.getPublisher().equals(firebaseUser)) {
            holder.questiontitle.setVisibility(View.GONE);
            holder.text.setVisibility(View.GONE);
            holder.username.setVisibility(View.GONE);
        }
        else {
            holder.text.setText(notification.getText());
            getQuesTitle(holder.questiontitle, notification.getSolutionId());
            getUserInfo(holder.username, notification.getPublisher());
        }


    }

    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {


        public TextView username, text, questiontitle;

        public ImageViewHolder(View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.user_ans);
            text = itemView.findViewById(R.id.user_sol);
            questiontitle = itemView.findViewById(R.id.ques_title_notification);

        }
    }

    private void getUserInfo(final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(publisherid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                username.setText(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getQuesTitle(final TextView questionTitle, String solutionId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Questions").child(solutionId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                post posts = dataSnapshot.getValue(post.class);
                questionTitle.setText(posts.getMquestionTilte());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
