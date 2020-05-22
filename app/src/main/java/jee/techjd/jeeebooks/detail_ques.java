package jee.techjd.jeeebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


public class detail_ques extends AppCompatActivity {

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ques);



        mContext = detail_ques.this;

        TextView sub = findViewById(R.id.subject);
        TextView tit = findViewById(R.id.text_ques);
        TextView des = findViewById(R.id.text_desc);
        ImageView img = findViewById(R.id.img_ques);


        Intent intent = getIntent();
        String subject = intent.getStringExtra("mSubject");
        String title = intent.getStringExtra("mquestionTitle");
        String description = intent.getStringExtra("mDescription");
        String image = intent.getStringExtra("mImageUrl");

        sub.setText(subject);
        tit.setText(title);
        des.setText(description);


        Picasso.get().load(image).into(img);

    }
}
