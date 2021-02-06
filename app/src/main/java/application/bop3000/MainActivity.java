package application.bop3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.inspiration.Inspiration;

public class MainActivity extends AppCompatActivity {
    EditText firstname, lastname, email, displayname, password;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        //firstname = findViewById(R.id.registration_firstname);
        //lastname = findViewById(R.id.registration_lastname);
        email = findViewById(R.id.registration_email);
        displayname = findViewById(R.id.registration_displayname);
        password = findViewById(R.id.registration_password);
        // Registrer knappen
        register = findViewById(R.id.registration_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating User Entity
                User user = new User();
               // userEntity.setFirstname(firstname.getText().toString());
               // userEntity.setLastname(lastname.getText().toString());
                user.setEmail(email.getText().toString());
                user.setDisplayname(displayname.getText().toString());
                user.setPassword(password.getText().toString());

                if(validateInput(user)) {
                    // Do insert operation
                    MyDatabase userDatabase = MyDatabase.getDatabase(getApplicationContext());
                    KnittersboxDao userDao = userDatabase.getKnittersboxDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Register user
                            userDao.registerUser(user);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(getApplicationContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validateInput(User user) {
        if(user.getPassword().isEmpty()) {
            return false;
        }
        return true;
    }
*/
    }
    public void goInspiration (View view){
        Intent intent = new Intent(MainActivity.this, Inspiration.class);
        startActivity(intent);
    }
}