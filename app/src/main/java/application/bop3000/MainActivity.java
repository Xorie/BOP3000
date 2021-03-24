package application.bop3000;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import application.bop3000.faq.faq;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Subscription;
import application.bop3000.database.User;

public class MainActivity extends AppCompatActivity {
    EditText firstname, lastname, email, displayname, password;
    Button register;
    //Menu
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View itemLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //firstname = findViewById(R.id.registration_firstname);
        //lastname = findViewById(R.id.registration_lastname);
        email = findViewById(R.id.registration_email);
        displayname = findViewById(R.id.registration_displayname);
        password = findViewById(R.id.registration_password);
        // Registrer knappen
        register = findViewById(R.id.registration_button);

        //Menu
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerToggle = setupDrawerToggle();
        drawerToggle.syncState();

        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.naviView);

        //SKAL BLI RØD
        itemLogout = findViewById(R.id.logout);

        setupDrawerContent(navigationView);
        View header = navigationView.getHeaderView(0);


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


    //Menu
    private void selectDrawerItem(MenuItem menuItem) {
        Intent intent_home = new Intent(this, MainActivity.class);
        Intent intent_subscription = new Intent(this, Subscription.class);
        Intent intent_faq = new Intent(this, faq.class);

        switch(menuItem.getItemId()) {
            case R.id.home:
                startActivity(intent_home);
                break;

            case R.id.subscription:
                startActivity(intent_subscription);
                break;

            case R.id.faq:
                startActivity(intent_faq);
        }
    }

    ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer( GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
}