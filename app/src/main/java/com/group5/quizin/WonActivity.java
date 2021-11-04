package com.group5.quizin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class WonActivity extends AppCompatActivity {

    CircularProgressBar circularProgressBar;
    TextView resultText;
    int correct;
    int wrong;

    LinearLayout btnShare;

    FirebaseUser user;
    DatabaseReference rootReference;

    ImageView backBtn;
    TextView exitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        correct = getIntent().getIntExtra("correct", 0);
        wrong = getIntent().getIntExtra("wrong", 0);

        backBtn = findViewById(R.id.ic_back);
        exitBtn = findViewById(R.id.ic_exit);
        backBtn.setClickable(false);
        exitBtn.setClickable(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WonActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        user= FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String id = user.getUid();
        rootReference= FirebaseDatabase.getInstance().getReference();

        circularProgressBar = findViewById(R.id.circularProgressBar);
        resultText = findViewById(R.id.resultText);

        btnShare = findViewById(R.id.btnShare);

        circularProgressBar.setProgress(correct);

        resultText.setText(correct + "/10");
        //.child("Result").setValue(correct)
        //.child(user.getUid())
        //.child(user.getUid()).child("Email").setValue(email)

        rootReference.child("Score");
        //.addOnCompleteListener(new OnCompleteListener<Void>() {
        //            @Override
        //            public void onComplete(@NonNull Task<Void> task) {
        //                if(task.isSuccessful()){
        //                    Toast.makeText(WonActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
        //                } else {
        //                    Toast.makeText(WonActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        //                }
        //            }
        //        });

        DataHolder obj =  new DataHolder(email, correct);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference node = db.getReference("Score");
        node.child(id).setValue(obj);


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nI got " + correct + " Out of 10 You Can Also Try ";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
    }
}