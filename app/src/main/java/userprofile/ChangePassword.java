package userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;

public class ChangePassword extends AppCompatActivity {

    // Inputfelt
    EditText password_old;
    EditText password_new;

    // Lagre knapp
    Button update;

    // Database
    private MyDatabase mDb;

    //String email_usr = "melon@gmail.com";

    // Email for bruker (blir hentet i onStart)
    String email_usr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        password_old = findViewById(R.id.password_old);
        password_new = findViewById(R.id.password_new);

        update = findViewById(R.id.btn_updatepwd);

        mDb = MyDatabase.getDatabase(getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Finner email som ble sent fra brukerprofil
        Intent change_pwd = getIntent();
        email_usr = change_pwd.getStringExtra("useremail");

    }

    //Knapp for å oppdatere info som er skrevet inn
    public void updatePassword(View view){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                String pass_old = password_old.getText().toString();
                String pass_new = password_new.getText().toString();

//                String usrname = "mikkelix";
                User user = mDb.getKnittersboxDao().loadUser(email_usr);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(pass_old.matches("") && pass_new.matches("")) {
                            Toast.makeText(getApplicationContext(), "Feltene er ikke fylt inn", Toast.LENGTH_LONG).show();
                        }

                        else if(!user.getPassword().equals(pass_old)) {
                            Toast.makeText(getApplicationContext(), "Gammelt passord stemmer ikke", Toast.LENGTH_LONG).show();
                        }

                        else {
                            user.setPassword(pass_new);

                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mDb.getKnittersboxDao().updateName(user);
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Passord endret", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}