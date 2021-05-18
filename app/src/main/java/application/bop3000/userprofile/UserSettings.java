package application.bop3000.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import appexecutors.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.login.Login;
import application.bop3000.network.DatabasePost;
import application.bop3000.sharedpreference.SharedPreferenceConfig;

public class UserSettings extends AppCompatActivity {

    // Input fields
    EditText usrname;
    EditText fname;
    EditText sname;
    EditText email;

    // Save/update button
    Button update;

    // Database
    private MyDatabase mDb;

    // Email for logged-in user
    String email_usr = Login.getUser().getEmail();

    private SharedPreferenceConfig sharedPreferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        // Finding views
        usrname = findViewById(R.id.usrname);
        fname = findViewById(R.id.fname);
        sname = findViewById(R.id.sname);
        email = findViewById(R.id.email);
        update = findViewById(R.id.btn_updatename);

        // Database connection
        mDb = MyDatabase.getDatabase(getApplicationContext());

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Method for showing data in the input fields
        showData();

    }

    // Back button in the toolbar
    public void userprofileBack(View view) {
        Intent user_profile_back = new Intent(this, UserProfile.class);
        startActivity(user_profile_back);
        finish();
    }

    // Android back button; to update the data in user profile activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent user_profile_back = new Intent(this, UserProfile.class);
        startActivity(user_profile_back);
        finish();
    }

    // Displaying data in input fields
    private void showData(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Retrieving user data
                User user = mDb.getKnittersboxDao().loadUser(email_usr);

                // Setting data in input fields
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        usrname.setText(user.getDisplayname());
                        fname.setText(user.getFirstname());
                        sname.setText(user.getLastname());
                        email.setText(user.getEmail());
                    }
                });
            }
        });
    }

    // Update button to save/update user info
    public void updateUserinfo(View view){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Getting data/text from inputs
                String username = usrname.getText().toString();
                String firstname = fname.getText().toString();
                String lastname = sname.getText().toString();
                String emailnew = email.getText().toString(); //NB: MÅ FIKSES

                // Retrieving user
                User user = Login.getUser(); //mDb.getKnittersboxDao().loadUser(email_usr);

                // For external database update
                String emailold = user.getEmail();

                // If nothing is changed
                if(username.equals(user.getDisplayname()) && firstname.equals(user.getFirstname()) && lastname.equals(user.getLastname()) && emailnew.equals(user.getEmail())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ingen endringer gjort", Toast.LENGTH_LONG).show();
                        }
                    });
                // If changed has been done
                }
                else if(emailnew.isEmpty() || emailnew.equals(" ")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "E-post kan ikke være tom!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                // If user name exists in the database
                else if(mDb.getKnittersboxDao().displayname(username) != null && !username.equals(user.getDisplayname())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Visningsnavn eksisterer!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                // If e-mail exists in the database
                else if(mDb.getKnittersboxDao().userEmail(emailnew) != null && !emailnew.equals(user.getEmail())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "E-post eksisterer!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {

                    // Setting data in the database
                    user.setDisplayname(username);
                    user.setFirstname(firstname);
                    user.setLastname(lastname);
                    user.setEmail(emailnew); //NB: MÅ FIKSES

                    // Updating local database
                    //mDb.getKnittersboxDao().updateName(user);
                    mDb.getKnittersboxDao().updateUserInfo(username, firstname, lastname, emailnew, emailold);

                    // Syncing with external database
                    DatabasePost.syncUserData(emailnew, user.getPassword(), UserSettings.this);

                    // Message, updating sharedPreferences with new email
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Profil oppdatert", Toast.LENGTH_LONG).show();
                            sharedPreferenceConfig.setPreference(UserSettings.this,"PREFS_LOGIN_EMAIL", emailnew);
                        }
                    });
                    // Finishing activity and returning to the user profile
                    finish();
                    Intent intent_profile = new Intent(UserSettings.this, UserProfile.class);
                    startActivity(intent_profile);
                }
            }
        });
    }

}