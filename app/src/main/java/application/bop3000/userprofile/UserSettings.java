package application.bop3000.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.login.Login;
import application.bop3000.sharedpreference.SharedPreferenceConfig;

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
    String email_usr = Login.getUser().getEmail();

    private SharedPreferenceConfig sharedPreferenceConfig;

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

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Finner email som ble sent fra brukerprofil
        //Intent user_settings = getIntent();
        //email_usr = user_settings.getStringExtra("useremail");

        //Viser data i inputfeltene om det er lagt inn noe
        showData();

    }

    public void userprofileBack(View view) {
        Intent user_profile_back = new Intent(this, UserProfile.class);
        startActivity(user_profile_back);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent user_profile_back = new Intent(this, UserProfile.class);
        startActivity(user_profile_back);
        finish();
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
    public void updateUserinfo(View view){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Henter info som er skrevet inn
                String username = usrname.getText().toString();
                String firstname = fname.getText().toString();
                String lastname = sname.getText().toString();
                String emailnew = email.getText().toString(); //NB: MÅ FIKSES

                // Henter data om brukeren
                User user = Login.getUser();//mDb.getKnittersboxDao().loadUser(email_usr);

                // Hvis ingenting er endret
                if(username.equals(user.getDisplayname()) && firstname.equals(user.getFirstname()) && lastname.equals(user.getLastname()) && emailnew.equals(user.getEmail())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ingen endringer gjort", Toast.LENGTH_LONG).show();
                        }
                    });
                // Hvis endringer er gjort
                }
                else if(emailnew.isEmpty() || emailnew.equals(" ")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "E-post kan ikke være tom!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(mDb.getKnittersboxDao().displayname(username) != null && !username.equals(user.getDisplayname())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Visningsnavn eksisterer!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(mDb.getKnittersboxDao().userEmail(emailnew) != null && !emailnew.equals(user.getEmail())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "E-post eksisterer!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {

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
                            System.out.println("EMAIL ER DEN DER: " + user.getEmail());
                            sharedPreferenceConfig.setPreference(UserSettings.this,"PREFS_LOGIN_EMAIL",emailnew);
                        }
                    });
                }
            }
        });
    }

}