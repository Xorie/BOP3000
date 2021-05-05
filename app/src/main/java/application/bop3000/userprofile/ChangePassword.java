package application.bop3000.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import appexecutors.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.login.Login;
import application.bop3000.network.DatabasePost;
import application.bop3000.sharedpreference.SharedPreferenceConfig;
import application.bop3000.security.EncryptDecrypt;

public class ChangePassword extends AppCompatActivity {

    // Inputfelt
    EditText password_old;
    EditText password_new;

    // Lagre knapp
    Button update;

    // Database
    private MyDatabase mDb;

    //String email_usr = "melon@gmail.com";

    // Email fra login
    String email_usr = Login.getUser().getEmail();

    private SharedPreferenceConfig sharedPreferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        password_old = findViewById(R.id.password_old);
        password_new = findViewById(R.id.password_new);

        update = findViewById(R.id.btn_updatepwd);

        mDb = MyDatabase.getDatabase(getApplicationContext());

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Finner email som ble sent fra brukerprofil
        //Intent change_pwd = getIntent();
        //email_usr = change_pwd.getStringExtra("useremail");

    }

    public void userprofileBack(View view) {
        Intent user_profile_back = new Intent(this, UserProfile.class);
        startActivity(user_profile_back);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent user_profile_back = new Intent(this, UserProfile.class);
//        startActivity(user_profile_back);
//        finish();
//    }

    //Knapp for å oppdatere info som er skrevet inn
    public void updatePassword(View view){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                String pass_old = password_old.getText().toString();
                String pass_new = password_new.getText().toString();

                User user = mDb.getKnittersboxDao().loadUser(email_usr);

                //User user = Login.getUser();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String pass_decrypted = user.getPassword();
                        pass_decrypted = EncryptDecrypt.decrypt(pass_decrypted);

                        if(pass_old.matches("") && pass_new.matches("")) {
                            Toast.makeText(getApplicationContext(), "Feltene er ikke fylt inn", Toast.LENGTH_LONG).show();
                        }
                        else if(!pass_decrypted.equals(pass_old)) {
                            Toast.makeText(getApplicationContext(), "Gammelt passord stemmer ikke", Toast.LENGTH_LONG).show();
                        }

                        else {
                            String pass_new_encrypted = EncryptDecrypt.encrypt(pass_new);
                            user.setPassword(pass_new_encrypted);

                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mDb.getKnittersboxDao().updateName(user);
                                    Log.d("LOKAL oppdatert", "Med passord: " + user.getPassword());
                                    DatabasePost.syncUserData(email_usr, pass_new, ChangePassword.this);
                                    Log.d("Ekstern oppdatert?", "parameters: " + email_usr + ", " + pass_new);
                                    //DatabasePost.syncUserData(user.getEmail(), pass_old, ChangePassword.this);
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Passord endret", Toast.LENGTH_SHORT).show();
                            sharedPreferenceConfig.setPreference(ChangePassword.this,"PREFS_LOGIN_PASSWORD",pass_new);
                            finish();
                        }
                    }
                });
            }
        });
    }
}