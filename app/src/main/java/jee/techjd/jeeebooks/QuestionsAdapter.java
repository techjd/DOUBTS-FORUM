package jee.techjd.jeeebooks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import jee.techjd.jeeebooks.Models.post;



import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    Context context;
    List<post> QuesInfo;
    private FirebaseUser firebaseUser;

    public QuestionsAdapter(Context context, List<post> quesInfo) {
        this.context = context;
        QuesInfo = quesInfo;

    }

    @NonNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ques, parent, false);
        return new QuestionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final post post = QuesInfo.get(position);
        holder.questitle.setText(post.getMquestionTilte());
        holder.sub.setText(post.getmSubject());
        holder.quesdesc.setText(post.getmDescription());
        Picasso.get()
                .load(post.getMimageUrl())

                .into(holder.quesimg);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, detail_ques.class);
                intent.putExtra("mDescription", post.getmDescription());
                intent.putExtra("mImageUrl", post.getMimageUrl());
                intent.putExtra("mquestionTitle", post.getMquestionTilte());
                intent.putExtra("mSubject", post.getmSubject());
                intent.putExtra("mQuesId", post.getmQuesId());
                context.startActivity(intent);
            }
        });

        holder.answerthis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent answer = new Intent(context, give_answer.class);
                answer.putExtra("mQuesId", post.getmQuesId());
                answer.putExtra("publisher", post.getPublisher());
                context.startActivity(answer);
            }
        });

        holder.viewanswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, view_answers.class);
                intent.putExtra("mDescription", post.getmDescription());
                intent.putExtra("mImageUrl", post.getMimageUrl());
                intent.putExtra("mquestionTitle", post.getMquestionTilte());
                intent.putExtra("mSubject", post.getmSubject());
                intent.putExtra("mQuesId", post.getmQuesId());
                context.startActivity(intent);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = post.getMquestionTilte();
                String shareDesc = post.getmDescription();

                intent.putExtra(Intent.EXTRA_TEXT, "Check out this app and try to solve my question"
                + "\n" + "\n" + post.getMquestionTilte() + "\n" + "\n" + "Get more details about the question on App " + "\n" + "\n" +

                        "If you know how to solve this question then " + "\n" +


                        "\nDownload this app now: " + "\n" + "\n" + "https://play.google.com/store/apps/details?id=jee.techjd.jeeebooks&hl=en");
                context.startActivity(Intent.createChooser(intent, "Send To"));
            }
        });



        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (post.getPublisher().equals(firebaseUser.getUid()))
                {



                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();

                    alertDialog.setTitle("Do you want to delete this question OR Edit ? ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String id = post.getmQuesId();
                                    FirebaseDatabase.getInstance().getReference("Questions")
                                            .child(post.getmQuesId()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(context,"Successfully Deleted",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Edit",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(context, edit_question.class);
                                    intent.putExtra("mDescription", post.getmDescription());

                                    intent.putExtra("mquestionTitle", post.getMquestionTilte());

                                    intent.putExtra("mQuesId", post.getmQuesId());
                                    context.startActivity(intent);
                                }
                            });
                    alertDialog.show();
                }

                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return QuesInfo.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView questitle;
        public TextView quesdesc;
        public ImageView quesimg;
        public TextView sub;
        public Button answerthis;
        View mView;
        private Button viewanswers;
        private ImageButton share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub = itemView.findViewById(R.id.subject);
            questitle = itemView.findViewById(R.id.text_ques);
            quesdesc = itemView.findViewById(R.id.text_desc);
            quesimg = itemView.findViewById(R.id.img_ques);
            answerthis = itemView.findViewById(R.id.give_ans);
            viewanswers = itemView.findViewById(R.id.view_ans);
            share = itemView.findViewById(R.id.share_intent);
            mView = itemView;

        }
    }
}






