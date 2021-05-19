package application.bop3000.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;

import appexecutors.AppExecutors;
import application.bop3000.login.Login;
import application.bop3000.network.DatabasePost;
import application.bop3000.security.EncryptDecrypt;

public class Register extends AppCompatActivity {
    EditText firstname, lastname, email, displayname, password, rePassword;
    Button register, back;
    TextView TaC;
    CheckBox checkBox;
    private MyDatabase userDatabase;
    private KnittersboxDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Variabels for user input
        email = findViewById(R.id.registration_email);
        displayname = findViewById(R.id.registration_displayname);
        password = findViewById(R.id.registration_password);
        rePassword = findViewById(R.id.registration_repassword);

        // Buttons
        register = findViewById(R.id.registration_button);
        back = findViewById(R.id.registration_back);



        //Room DB connection
        userDatabase = MyDatabase.getDatabase(getApplicationContext());
        userDao = userDatabase.getKnittersboxDao();

        //Terms and condition text
        TaC = findViewById(R.id.terms_condition);
        TaC.setMovementMethod(LinkMovementMethod.getInstance());

        //Checkbox
        checkBox = findViewById(R.id.terms_condition_checkbox);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( Register.this, Login.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        // Creating User Entity
                        User user = new User();

                        user.setEmail(email.getText().toString());
                        user.setDisplayname(displayname.getText().toString());
                        user.setPassword(password.getText().toString());

                        if(validateInput(user)) {
                            if(validateDisplayname(user)) {
                                if(isValidEmail(email.getText().toString())) {
                                    if (validateEmail(user)) {
                                        if (isValidPassword(password.getText().toString())) {
                                            if (validatePassword(user)) {
                                                if (checkBox.isChecked()) {
                                                    // Do insert operation
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String psw = null;
                                                            try {
                                                                psw = EncryptDecrypt.encrypt(user.getPassword());
                                                                // Trim to remove whitespace at end of string, problems with sharedPreferences
                                                                psw = psw.trim();
                                                                user.setPassword(psw);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            // Register user Room DB
                                                            userDao.registerUser(user);

                                                            // Registrer user external DB ->
                                                            try {
                                                                DatabasePost.sendUser(email.getText().toString(), psw, getApplicationContext());
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(Register.this, "Bruker registert eksternt", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                // End registration activity (for back-pressed)
                                                                finish();
                                                            } catch (Exception e) {
                                                                Looper.prepare();
                                                                Toast.makeText(Register.this, "Feil ved kobling til server", Toast.LENGTH_SHORT).show();
                                                            }

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(getApplicationContext(), "Bruker registrert!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            startActivity(new Intent(Register.this, Login.class));
                                                        }
                                                    }).start();
                                                } else {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getApplicationContext(), "Godta personvern!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getApplicationContext(), "Passord samsvarer ikke!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), "Passord må ha en stor bokstav, et tall og tegn!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Email finnes fra før!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Ugyldig emailadresse!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Visningsnavn eksisterer!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Fyll alle feltene", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }});
            }
        });
    }

    private Boolean validateInput(User user) {
        if(user.getPassword().isEmpty() ||
                user.getEmail().isEmpty() ||
                user.getDisplayname().isEmpty()){
            return false;
        }
        return true;
    }

    // Method to check for correct user input
    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        //Email must contain letter from a-z, @, a-z, ., and a-z
        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    //Method to check for correct user password
    public boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;

        // Must contain 1-9, a-z, A-Z with a length of minimum 4
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    // Check password against re-password
    private Boolean validatePassword(User user) {
        if(user.getPassword().equals(rePassword.getText().toString())){
            return true;
        }
        return false;
    }

    // Checks database for already registered user on displayname
    private Boolean validateDisplayname(User user) {
        if(userDao.displayname(user.getDisplayname()) == null) {
            return true;
        }
        return false;
    }

    // Checks database for already registered user on email
    private Boolean validateEmail(User user) {
        if(userDao.userEmail(user.getEmail()) == null) {
            return true;
        }
        return false;
    }
}