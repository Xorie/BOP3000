package application.bop3000.subscription;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.faq.faq;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.login.Login;
import application.bop3000.payment_method.Payment_method;
import application.bop3000.sharedpreference.SharedPreferenceConfig;
import application.bop3000.userprofile.UserProfile;

public class Subscription extends AppCompatActivity {

    private SharedPreferenceConfig sharedPreferenceConfig;
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

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

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

        //Setting the default displayed fragment
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, new SubscriptionFragment());
            transaction.commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.close();
    }

    //Menu
    private void selectDrawerItem(MenuItem menuItem) {
        Intent intent_home = new Intent(this, Inspiration.class);
        Intent intent_subscription = new Intent(this, Subscription.class);
        Intent intent_faq = new Intent(this, faq.class);
        Intent intent_profile = new Intent(this, UserProfile.class);
        Intent intent_loggout = new Intent(this, Login.class);

        switch(menuItem.getItemId()) {
            case R.id.home:
                startActivity(intent_home);
                break;

            case R.id.userprofile:
                startActivity(intent_profile);
                break;

            case R.id.subscription:
                startActivity(intent_subscription);
                finish();
                break;

            case R.id.faq:
                startActivity(intent_faq);
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
                drawerLayout.openDrawer(GravityCompat.START);
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