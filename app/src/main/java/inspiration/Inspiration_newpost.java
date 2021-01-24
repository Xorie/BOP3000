package inspiration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import application.bop3000.R;

public class Inspiration_newpost extends AppCompatActivity implements View.OnClickListener{
    Button btn_upload, btn_save, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration_newpost);
        btn_upload = findViewById(R.id.btn_upload);
        btn_save = findViewById(R.id.inspiration_save);
        btn_back = findViewById(R.id.inspiration_back);

        //lettere og mindre kode for flere knapper & bruk dette for switch
        btn_upload.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    //switch for knappene
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_upload:
                Toast.makeText(getApplicationContext(), "Klikket på last opp bilde", Toast.LENGTH_SHORT).show();
                //kode for å laste opp bilde eller ta bilde
                break;
            case R.id.inspiration_save:
                Toast.makeText(getApplicationContext(), "Klikket på lagre knappen", Toast.LENGTH_SHORT).show();
                //kode for å lagre det nye innlegget
                break;
            case R.id.inspiration_back:
                Intent intent = new Intent(Inspiration_newpost.this, Inspiration.class);
                startActivity(intent);
                break;
        }
    }
}