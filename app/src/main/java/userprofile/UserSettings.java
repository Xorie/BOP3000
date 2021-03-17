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

public class UserSettings extends AppCompatActivity {

    // Inputfelt
    EditText usrname;
    EditText fname;
    EditText sname;
    EditText email;
    EditText password_old;
    EditText password_new;

    // Lagre knapp
    Button update;

    // Database
    private MyDatabase mDb;

    // Email for bruker (blir hentet i onStart)
    String email_usr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        // Finner views
        usrname = findViewById(R.id.usrname);
        fname = findViewById(R.id.fname);
        sname = findViewById(R.id.sname);
        email = findViewById(R.id.email);
        update = findViewById(R.id.btn_updatename);

        //Kobler til database
        mDb = MyDatabase.getDatabase(getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Finner email som ble sent fra brukerprofil
        Intent user_settings = getIntent();
        email_usr = user_settings.getStringExtra("useremail");

        //Viser data i inputfeltene om det er lagt inn noe
        showData();

    }

    // Viser data i inputfeltene om det er lagt inn noe
    private void showData(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                //Henter data om brukeren
                User user = mDb.getKnittersboxDao().loadUser(email_usr);

                // Setter data på riktig plass
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        usrname.setText(user.getDisplayname());
                        fname.setText(user.getFirstname());
                        sname.setText(user.getLastname());
                        email.setText(user.getEmail());
                        //password_old.setText(user.getPassword());
                    }
                });
            }
        });
    }

    //Knapp for å oppdatere info som er skrevet inn
    public void updateName(View view){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Henter info som er skrevet inn
                String username = usrname.getText().toString();
                String firstname = fname.getText().toString();
                String lastname = sname.getText().toString();
                String emailnew = email.getText().toString(); //NB: MÅ FIKSES

                // Henter data om brukeren
                User user = mDb.getKnittersboxDao().loadUser(email_usr);

                // Hvis ingenting er endret
                if(username.equals(user.getDisplayname()) && firstname.equals(user.getFirstname()) && lastname.equals(user.getLastname()) && emailnew.equals(user.getEmail())) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ingen endringer gjort", Toast.LENGTH_LONG).show();
                        }
                    });
                // Hvis endringer er gjort
                } else {

                    // Setter data om brukeren
                    user.setDisplayname(username);
                    user.setFirstname(firstname);
                    user.setLastname(lastname);
                    user.setEmail(emailnew); //NB: MÅ FIKSES

                    // Kjører sql og oppdaterer brukerinfo
                    mDb.getKnittersboxDao().updateName(user);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Profil oppdatert", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

}