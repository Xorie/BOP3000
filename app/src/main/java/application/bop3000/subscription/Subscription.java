package application.bop3000.subscription;

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
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.faq.faq;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.login.Login;
import application.bop3000.payment_method.Payment_method;
import application.bop3000.userprofile.UserProfile;

public class Subscription extends AppCompatActivity {

    private MyDatabase mDb;
    private TextView userSub;
    private TextView userPost;
    private TextView userCity;
    private TextView userAddress;
    private Button button;
    private String subDesc;
    private String post;
    private String city;
    private String address;

    //Menu
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View itemLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        mDb = MyDatabase.getDatabase(getApplicationContext());
        button = findViewById(R.id.subscription_btn_change);
        userSub = findViewById(R.id.userSub);
        userPost = findViewById(R.id.userPost);
        userCity = findViewById(R.id.userCity);
        userAddress = findViewById(R.id.userAddress);

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

        AppExecutors.getInstance().diskIO().execute( new Runnable() {
            @Override
            public void run() {
                int userID = Login.getUser().getUserID();

                // Henter informasjon på brukerID
                User user = mDb.getKnittersboxDao().hentBrukerID(userID);

                // Sjekker om man får nullverdi
                if (user.getSubscription_subscriptionID() == null) {
                    subDesc = "Ingen";
                } else {
                    String sub = user.getSubscription_subscriptionID();
                    //Henter subscription desc fra ID
                    int subscriptID = Integer.parseInt(sub);
                    application.bop3000.database.Subscription subscription = mDb.getKnittersboxDao().hentSubDesc(subscriptID);
                    subDesc = subscription.getDescription();
                }

                if (user.getPostnr() == null) {
                    post = "Ingen";
                } else { post = user.getPostnr(); }

                if (user.getCity() == null) {
                    city = "Ingen";
                } else { city = user.getCity(); }

                if (user.getStreetname() == null) {
                    address = "Ingen";
                } else { address = user.getStreetname(); }





                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Fyller textview
                        userSub.setText(subDesc);
                        userPost.setText(post);
                        userCity.setText(city);
                        userAddress.setText(address);
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Subscription_change.class);
                startActivity(intent);
            }
        });





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
                startActivity(intent_home);
                break;

            case R.id.userprofile:
                startActivity(intent_profile);
                break;

            case R.id.subscription:
                startActivity(intent_subscription);
                break;

            case R.id.faq:
                startActivity(intent_faq);
                break;
            case R.id.payment:
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