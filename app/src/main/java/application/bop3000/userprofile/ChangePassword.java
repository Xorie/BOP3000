package application.bop3000.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import appexecutors.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.login.Login;
import application.bop3000.network.DatabasePost;
import application.bop3000.sharedpreference.SharedPreferenceConfig;
import application.bop3000.security.EncryptDecrypt;

public class ChangePassword extends AppCompatActivity {

    // Input fields
    EditText password_old;
    EditText password_new;

    // Update button
    Button update;

    // Database
    private MyDatabase mDb;

    // E-mail for logged in user
    String email_usr;

    private SharedPreferenceConfig sharedPreferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Finding views
        password_old = findViewById(R.id.password_old);
        password_new = findViewById(R.id.password_new);
        update = findViewById(R.id.btn_updatepwd);

        // Finding E-mail for logged in user in the database
        email_usr = Login.getUser().getEmail();

        // Database connection
        mDb = MyDatabase.getDatabase(getApplicationContext());

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

    }

    // Back button in the toolbar
    public void userprofileBack(View view) {
        Intent user_profile_back = new Intent(this, UserProfile.class);
        startActivity(user_profile_back);
        finish();
    }

    //Knapp for å oppdatere info som er skrevet inn
    public void updatePassword(View view){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Getting data/text from inputs
                String pass_old = password_old.getText().toString();
                String pass_new = password_new.getText().toString();

                // Retrieving user
                User user = mDb.getKnittersboxDao().loadUser(email_usr); //User user = Login.getUser();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Decrypting password
                        String pass_decrypted = user.getPassword();
                        pass_decrypted = EncryptDecrypt.decrypt(pass_decrypted);

                        // Checking if inputfields are filled, if old password matches, if password follows rules
                        if(pass_old.matches("") && pass_new.matches("")) {
                            Toast.makeText(getApplicationContext(), "Feltene er ikke fylt inn", Toast.LENGTH_LONG).show();
                        }
                        else if(!pass_decrypted.equals(pass_old)) {
                            Toast.makeText(getApplicationContext(), "Gammelt passord stemmer ikke", Toast.LENGTH_LONG).show();
                        }
                        else if (!isValidPassword(pass_new)) {
                            Toast.makeText(getApplicationContext(), "Passord må innehholde minst en stor bokstav og et tall", Toast.LENGTH_LONG).show();
                        }
                        else {
                            // Encrypting new password
                            String pass_new_encrypted = EncryptDecrypt.encrypt(pass_new).trim();
                            user.setPassword(pass_new_encrypted);

                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    // Updating local database
                                    mDb.getKnittersboxDao().updateName(user);

                                    // Syncing new data with external database
                                    DatabasePost.syncUserData(email_usr, pass_new_encrypted, ChangePassword.this);
                                }
                            });

                            // Message, updating sharedPreferences with new password
                            Toast.makeText(getApplicationContext(), "Passord endret", Toast.LENGTH_SHORT).show();
                            sharedPreferenceConfig.setPreference(ChangePassword.this,"PREFS_LOGIN_PASSWORD",pass_new_encrypted);
                            finish();
                        }
                    }
                });
            }
        });
    }

    // Method for password rules
    public boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

}