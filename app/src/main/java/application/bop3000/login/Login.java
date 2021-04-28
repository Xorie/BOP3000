package application.bop3000.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Subscription;
import application.bop3000.database.User;
import application.bop3000.faq.faq;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.register.Register;
import application.bop3000.sharedpreference.SharedPreferenceConfig;

public class Login extends AppCompatActivity {
    private SharedPreferenceConfig sharedPreferenceConfig;

    EditText email, password;
    Button login, registration;
    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Fields
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        // Buttons
        login = findViewById(R.id.login_loginbtn);
        registration = findViewById(R.id.login_registrerbtn);

        // SharedPreference
        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        // Room DB and DAO
        MyDatabase myDatabase = MyDatabase.getDatabase(getApplicationContext());
        final KnittersboxDao knittersboxDao = myDatabase.getKnittersboxDao();

        // If user has logged inn
        if(sharedPreferenceConfig.read_login_status()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    user = knittersboxDao.login(sharedPreferenceConfig.getPreference(Login.this, "PREFS_LOGIN_EMAIL"), sharedPreferenceConfig.getPreference(Login.this, "PREFS_LOGIN_PASSWORD"));
                    startActivity(new Intent(Login.this, Inspiration.class));
                    finish();
                }
            }).start();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                if(emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fyll alle feltene!", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            user = knittersboxDao.login(emailText, passwordText);
                            if(user == null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Ugyldig informasjon!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                String name = user.getDisplayname();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Velkommen " + name, Toast.LENGTH_SHORT).show();
                                        Intent intent_logginn = new Intent(Login.this, Inspiration.class);
                                        startActivity(intent_logginn);
                                        sharedPreferenceConfig.setPreference(Login.this, "PREFS_LOGIN_EMAIL", user.getEmail());
                                        sharedPreferenceConfig.setPreference(Login.this, "PREFS_LOGIN_PASSWORD", user.getPassword());
                                        sharedPreferenceConfig.login_status(true);
                                        finish();
                                    }
                                });
                                //Log.d("LOGIN", "Velkommen " + name);
                            }
                        }
                    }).start();
                }
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    public static User getUser(){
        return user;
    }

    // Prevents user to login with back button
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
