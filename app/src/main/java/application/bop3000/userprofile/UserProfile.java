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

import java.lang.reflect.Field;

import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.faq.faq;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.login.Login;
import application.bop3000.payment_method.Payment_method;
import application.bop3000.subscription.Subscription;

public class UserProfile extends AppCompatActivity {

    // Database
    private MyDatabase mDb;

    // Tekstfelt
    private TextView username;
    private TextView subscription;
    private TextView firstname;
    //private TextView lastname;
    private TextView email;
    private TextView street;
    private TextView postnr;
    private TextView city;

    //Menu
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View itemLogout;


    // Email for logget inn bruker (HARDKODET NÅ, MÅ KOMME FRA LOGG INN eller noe)
    //String email_usr = "hei@gmail.com";

    // Email for bruker (blir hentet i onStart)
    String email_usr = Login.getUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Database
        mDb = MyDatabase.getDatabase(getApplicationContext());

        // Finner views
        username = findViewById(R.id.userprofile_username);
        subscription = findViewById(R.id.userprofile_sub);
        firstname = findViewById(R.id.userprofile_fname);
        //lastname = findViewById(R.id.userprofile_sname);
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

        //SKAL BLI RØD
        itemLogout = findViewById(R.id.logout);

        setupDrawerContent(navigationView);
        View header = navigationView.getHeaderView(0);

    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("ONSTART: ");
        //Intent intent_insp = getIntent();
        //email_usr = intent_insp.getStringExtra("useremail");
        // Metode for å vise data om bruker
        showListData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("ONPAUSE: ");
        //Går ut av aktivitet
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("ONRESUME: ");

//        User interacts with the app

    }

    // Metode for å vise data om bruker
    private void showListData(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Finner data om bruker
                User user = mDb.getKnittersboxDao().loadUser(email_usr);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String full_name = user.getFirstname() + " " + user.getLastname();

                        // Setter data på riktig plass
                        username.setText(user.getDisplayname());
                        //Sjekker om bruker har lagt til navn (fullt navn, fornavn, etternavn)
                        if(full_name.equals(" ") || full_name.equals("null null")) {
                            firstname.setText(R.string.userprofile_noname);
                        } else if (user.getFirstname() == null || user.getFirstname().isEmpty()) {
                            full_name = user.getLastname();
                            firstname.setText(full_name);
                        }else {
                            firstname.setText(full_name);
                        }

                        email.setText(user.getEmail());
                        street.setText(user.getStreetname());
                        postnr.setText(user.getPostnr());
                        city.setText(user.getCity());

                        // Vising av abonnementtype
                        String sub_type = user.getSubscription_subscriptionID();
                        // Hvis bruker ikke har lagt inn noe abonnement
                        if(sub_type == null) {
                            sub_type = "Ingen";
                        }
                        // Hvis bruker har abonnement (NB: MÅ LEGGE INN FLERE)
                        else if(sub_type.equals("1")) {
                            sub_type = "Jordboksen";
                        }
                        //NB: IF FOR RESTEN AV ABONNEMENTTYPENE!
                        subscription.setText(sub_type);

                    }
                });
            }
        });
    }

    // Metode for knapp til redigering av profil
    public void userSettings(View view) {

        // Kobling til redigere profil aktivitet
        Intent user_settings = new Intent(this, UserSettings.class);

        // Sender med email for bruker
        //user_settings.putExtra("useremail", email_usr);

        // Starter aktivitet
        startActivity(user_settings);
        finish();
    }

    // Metode for knapp til endring av passord
    public void changePassword(View view) {

        // Kobling til endre passord aktivitet
        Intent change_pwd = new Intent(this, ChangePassword.class);

        // Sender med email for bruker
        //change_pwd.putExtra("useremail", email_usr);

        // Starter aktivitet
        startActivity(change_pwd);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back_to_insp = new Intent(this, Inspiration.class);
        startActivity(back_to_insp);
        finish();
    }

    //Menu
    private void selectDrawerItem(MenuItem menuItem) {
        Intent intent_home = new Intent(this, Inspiration.class);
        Intent intent_subscription = new Intent(this, Subscription.class);
        Intent intent_faq = new Intent(this, faq.class);
        Intent intent_profile = new Intent(this, UserProfile.class);
        Intent intent_payment = new Intent(this, Payment_method.class);

        switch(menuItem.getItemId()) {
            case R.id.home:
                //finish();
                //intent_home.putExtra("useremail", email_usr);
                startActivity(intent_home);
                break;

            case R.id.userprofile:
                //intent_profile.putExtra("useremail", email_usr);
                startActivity(intent_profile);
                finish();
                break;

            case R.id.subscription:
                //intent_subscription.putExtra("useremail", email_usr);
                startActivity(intent_subscription);
                break;

            case R.id.faq:
                //intent_faq.putExtra("useremail", email_usr);
                startActivity(intent_faq);
                break;
            case R.id.payment:
                //intent_payment.putExtra("useremail", email_usr);
                startActivity(intent_payment);
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