package application.bop3000.login;

import android.content.Context;
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

import application.bop3000.MainActivity;
import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Subscription;
import application.bop3000.database.User;
import application.bop3000.faq.faq;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.register.Register;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login, registration;

    SharedPreferences sp;

    public static User user;

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


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                if(emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fyll alle feltene!", Toast.LENGTH_SHORT).show();
                } else {
                    // Utfører query
                    MyDatabase myDatabase = MyDatabase.getDatabase(getApplicationContext());
                    final KnittersboxDao knittersboxDao = myDatabase.getKnittersboxDao();
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
                                        //intent_logginn.putExtra("useremail", emailText);
//                                        sp = getSharedPreferences("prefs", Context.MODE_PRIVATE);
//                                        SharedPreferences.Editor editor = sp.edit();
//                                        editor.putString("username", emailText);
//                                        editor.commit();

                                        startActivity(intent_logginn);
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
    public static User getUser() {
        return user;
    }


}
