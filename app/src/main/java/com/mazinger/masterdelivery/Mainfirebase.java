package com.mazinger.masterdelivery;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Mainfirebase extends AppCompatActivity {

    private static final String PATH_START = "start";
    private static final String PATH_MESSAGE = "message";
    @BindView(R.id.txtviewmessage)
    TextView txtviewmessage;
    @BindView(R.id.message)
    EditText message;
    @BindView(R.id.btnsend)
    Button btnsend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainfirebase);
        ButterKnife.bind(this);
        final TextView txtviewmessage = findViewById(R.id.txtviewmessage);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference(PATH_START).child(PATH_MESSAGE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                txtviewmessage.setText(dataSnapshot.getValue(String.class));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Mainfirebase.this, "error", Toast.LENGTH_LONG).show();

            }
        });


    }


    @OnClick(R.id.btnsend)
    public void onClick() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = database.getReference(PATH_START).child(PATH_MESSAGE);
reference.setValue(message.getText().toString().trim());
message.setText("");

    }
}
