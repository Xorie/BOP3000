package application.bop3000.userprofile;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;


import appexecutors.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.faq.faq;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.login.Login;
import application.bop3000.payment_method.Payment_method;
import application.bop3000.sharedpreference.SharedPreferenceConfig;
import application.bop3000.subscription.Subscription;

public class UserProfile extends AppCompatActivity {
    private SharedPreferenceConfig sharedPreferenceConfig;

    // Database
    private MyDatabase mDb;

    // Textfields
    private TextView username;
    private TextView subscription;
    private TextView firstname;
    private TextView email;
    private TextView street;
    private TextView postnr;
    private TextView city;

    private String full_name;

    //User object
    private User user;

    //Menu
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View itemLogout;


    // Email for logged-in user
    String email_usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        // Database
        mDb = MyDatabase.getDatabase(getApplicationContext());

        // Finding views
        username = findViewById(R.id.userprofile_username);
        subscription = findViewById(R.id.userprofile_sub);
        firstname = findViewById(R.id.userprofile_fname);
        email = findViewById(R.id.userprofile_email);
        street = findViewById(R.id.userprofile_street);
        postnr = findViewById(R.id.userprofile_postnr);
        city = findViewById(R.id.userprofile_city);

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

        itemLogout = findViewById(R.id.logout);

        setupDrawerContent(navigationView);
        View header = navigationView.getHeaderView(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        email_usr = Login.getUser().getEmail();

        //Retrieving userdata from the database
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                user = mDb.getKnittersboxDao().loadUser(email_usr);

            }
        });
        // Method to show userdata
        showListData();
    }

    //Closing meny when going to another activity
    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // Method to show userdata
    private void showListData(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        full_name = user.getFirstname() + " " + user.getLastname();

                        // Setting data on textviews
                        username.setText(user.getDisplayname());
                        // Checking if user has saved data about name (full name, firstname, lastname)
                        if(full_name.equals(" ") || full_name.equals("null null")) {
                            firstname.setText(R.string.userprofile_noname);
                        } else if (user.getFirstname() == null || user.getFirstname().isEmpty()) {
                            full_name = user.getLastname();
                            firstname.setText(full_name);
                        } else {
                            firstname.setText(full_name);
                        }
                        email.setText(user.getEmail());
                        street.setText(user.getStreetname());
                        postnr.setText(user.getPostnr());
                        city.setText(user.getCity());

                        // Displaying subscription type
                        String sub_type = user.getSubscription_subscriptionID();
                        // If user has no subscription
                        if(sub_type == null) {
                            sub_type = "Ingen";
                        }
                        // If user has subscription
                        else if(sub_type.equals("1")) {
                            sub_type = "Fargeboksen";
                        }
                        else if(sub_type.equals("2")) {
                            sub_type = "Jordboksen";
                        }
                        subscription.setText(sub_type);

                    }
                });
            }
        });
    }

    // Method for user settings button
    public void userSettings(View view) {

        Intent user_settings = new Intent(this, UserSettings.class);

        startActivity(user_settings);
        finish();
    }

    // Method for change password button
    public void changePassword(View view) {

        Intent change_pwd = new Intent(this, ChangePassword.class);

        startActivity(change_pwd);
        finish();
    }

    //Menu
    private void selectDrawerItem(MenuItem menuItem) {
        Intent intent_home = new Intent(this, Inspiration.class);
        Intent intent_subscription = new Intent(this, Subscription.class);
        Intent intent_faq = new Intent(this, faq.class);
        Intent intent_profile = new Intent(this, UserProfile.class);
        Intent intent_payment = new Intent(this, Payment_method.class);
        Intent intent_loggout = new Intent(this, Login.class);

        switch(menuItem.getItemId()) {
            case R.id.home:
                startActivity(intent_home);
                break;

            case R.id.userprofile:
                startActivity(intent_profile);
                finish();
                break;

            case R.id.subscription:
                startActivity(intent_subscription);
                break;

            case R.id.faq:
                startActivity(intent_faq);
                break;
            case R.id.payment:
                startActivity(intent_payment);
                break;
            case R.id.logout:
                sharedPreferenceConfig.login_status(false);
                startActivity(intent_loggout);
                finish();


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