package application.bop3000.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;

import application.bop3000.database.User;

import application.bop3000.inspiration.Inspiration;
import application.bop3000.network.DatabaseGet;
import application.bop3000.register.Register;
import application.bop3000.security.EncryptDecrypt;
import application.bop3000.sharedpreference.SharedPreferenceConfig;

public class Login extends AppCompatActivity {
    private static SharedPreferenceConfig sharedPreferenceConfig;

    // This activity, used for closing after login
    public static Activity activity = null;

    EditText email, password;
    Button login, registration;
    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set activity to this activity
        activity = this;

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
                    // Get correct user object, do local sql query from sharedPreference
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
                            try {
                                user = knittersboxDao.userEmail(emailText);
                                String passwordCheck = null;
                                String psw = user.getPassword();
                                String psw2 = psw;
                                psw = EncryptDecrypt.decrypt(psw);
                                System.out.println(psw);
                                if (psw.equals(passwordText)) {
                                    passwordCheck = psw2;
                                }
                                user = knittersboxDao.login(emailText, passwordCheck);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(user == null) {
                                // Encrypt user password to check in external
                                String encryptPass = EncryptDecrypt.encrypt(passwordText);

                                // Get user data from external to local
                                DatabaseGet.getUserFromExternal(emailText, encryptPass, getApplicationContext());
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Velkommen " + user.getDisplayname(), Toast.LENGTH_SHORT).show();
                                        Intent intent_logginn = new Intent(Login.this, Inspiration.class);
                                        startActivity(intent_logginn);
                                        sharedPreferenceConfig.setPreference(Login.this, "PREFS_LOGIN_EMAIL", user.getEmail());
                                        sharedPreferenceConfig.setPreference(Login.this, "PREFS_LOGIN_PASSWORD", user.getPassword());
                                        sharedPreferenceConfig.login_status(true);

                                        finish();
                                    }
                                });
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

    // Return user object
    public static User getUser(){
        return user;
    }

    // Prevents user to login with back button
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public static void userExternalLogin(Boolean success, Context context, String email, String password, String dName) {
        // Room DB and DAO
        MyDatabase myDatabase = MyDatabase.getDatabase(context);
        final KnittersboxDao knittersboxDao = myDatabase.getKnittersboxDao();

        if(success) {
            // Get correct user object after external login
            user = knittersboxDao.login(email, password);


            Looper.prepare();
            Toast.makeText(context, "Velkommen " + dName, Toast.LENGTH_LONG).show();

            Intent intent_logginn = new Intent(context, Inspiration.class);
            intent_logginn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent_logginn);
            sharedPreferenceConfig.setPreference(context, "PREFS_LOGIN_EMAIL", email);
            sharedPreferenceConfig.setPreference(context, "PREFS_LOGIN_PASSWORD", password);
            sharedPreferenceConfig.login_status(true);

            activity.finish();
        }
    }
}
