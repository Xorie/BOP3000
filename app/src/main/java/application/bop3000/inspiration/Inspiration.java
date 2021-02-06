package application.bop3000.inspiration;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import application.bop3000.R;

public class Inspiration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            //fra insperasjonssiden til nytt innlegg siden
            public void onClick(View view) {
                Intent intent = new Intent(Inspiration.this, Inspiration_newpost.class);
                startActivity(intent);
            }
        });
    }
}