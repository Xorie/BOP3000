package application.bop3000.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;

import appexecutors.AppExecutors;
import application.bop3000.login.Login;
import application.bop3000.network.DatabasePost;

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

        //firstname = findViewById(R.id.registration_firstname);
        //lastname = findViewById(R.id.registration_lastname);
        email = findViewById(R.id.registration_email);
        displayname = findViewById(R.id.registration_displayname);
        password = findViewById(R.id.registration_password);
        rePassword = findViewById(R.id.registration_repassword);

        // Buttons
        register = findViewById(R.id.registration_button);
        back = findViewById(R.id.registration_back);


        // Button TEST ex DB /////////////////////////////////////
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Button button = findViewById(R.id.test);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabasePost.syncUserData("tisstiss", "t", getApplicationContext());
                            }
                        });
                    }
                }).start();
            }
        });
        ////////////////////////////////////////////



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
                    // userEntity.setFirstname(firstname.getText().toString());
                    // userEntity.setLastname(lastname.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setDisplayname(displayname.getText().toString());
                    user.setPassword(password.getText().toString());

                    if(validateInput(user)) {
                        if(validateDisplayname(user)) {
                            if(validateEmail(user)) {
                                if (validatePassword(user)) {
                                    if (checkBox.isChecked()) {
                                        // Do insert operation
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Register user Room DB
                                                userDao.registerUser(user);
                                                // Registrer user external DB ->
                                                // Eventuelt try catch om netter er tilkoblet
                                                try {
                                                    DatabasePost.sendUser(email.getText().toString(), password.getText().toString(), getApplicationContext());
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(Register.this, "Bruker registert eksternt", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } catch (Exception e) {
                                                    Toast.makeText(Register.this, "Feil ved kobling til server", Toast.LENGTH_SHORT).show();
                                                }

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getApplicationContext(), "Bruker registrert!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
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
                                        Toast.makeText(getApplicationContext(), "Email finnes fra før!", Toast.LENGTH_SHORT).show();
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

    private Boolean validatePassword(User user) {
        if(user.getPassword().equals(rePassword.getText().toString())){
            return true;
        }
        return false;
    }

    private Boolean validateDisplayname(User user) {
        if(userDao.displayname(user.getDisplayname()) == null) {
            return true;
        }
        return false;
    }

    private Boolean validateEmail(User user) {
        if(userDao.userEmail(user.getEmail()) == null) {
            return true;
        }
        return false;
    }

//    private Boolean registerExternal() {
//        if(DatabasePost.sendUser(email.getText().toString(), password.getText().toString(), getApplicationContext())) {
//            return true;
//        }
//        return false;
//    }
//
//    private Boolean syncUserId() {
//        if(DatabaseGet.syncUserId(email.getText().toString(), password.getText().toString(), getApplicationContext())) {
//            return true;
//        }
//        return false;
//    }
}