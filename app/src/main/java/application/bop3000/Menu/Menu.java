package application.bop3000.Menu;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import application.bop3000.AppExecutors;
import application.bop3000.FAQ.faq;
import application.bop3000.MainActivity;
import application.bop3000.R;
import application.bop3000.Subscription.Subscription;
import application.bop3000.database.FAQ;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.PostOffice;
import application.bop3000.database.User;

public class Menu extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View itemLogout;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

        // Button skal vekk
        MyDatabase mDb = MyDatabase.getDatabase(getApplicationContext());
        Button buttoninsert = findViewById(R.id.subscription_btn_insert);
        buttoninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute( new Runnable() {
                    @Override
                    public void run() {
                        application.bop3000.database.Subscription sub1 = new application.bop3000.database.Subscription();
                        application.bop3000.database.Subscription sub2 = new application.bop3000.database.Subscription();
                        application.bop3000.database.Subscription sub3 = new application.bop3000.database.Subscription();
                        User user = new User();
                        PostOffice postnr1 = new PostOffice();
                        PostOffice postnr2 = new PostOffice();
                        FAQ faq1 = new FAQ();
                        FAQ faq2 = new FAQ();
                        FAQ faq3 = new FAQ();
                        sub1.setType(1);
                        sub2.setType(2);
                        sub3.setType(3);
                        sub1.setDescription("Abonnement 1");
                        sub2.setDescription("Abonnement 2");
                        sub3.setDescription("Abonnement 3");
                        user.setEmail("test@test.no");
                        user.setDisplayname("test");
                        user.setPassword("test");
                        postnr1.setPostnr(3510);
                        postnr1.setPost_office("Hønefoss");
                        postnr2.setPostnr(3511);
                        postnr2.setPost_office("Hønefoss");
                        faq1.setQuestion("Spørsmål 1");
                        faq1.setAnswer("Her er svaret på spørsmål 1, svar svar svar svar svar svar svar svar svar svar svar svar svar svar svar. LANGT SVAR svar svar svar svar svar svar svar svar svar svar");
                        faq2.setQuestion("Spørsmål 2");
                        faq2.setAnswer("Svar 2");
                        faq3.setQuestion("Spørsmål 3");
                        faq3.setAnswer("Svar 3");

                        KnittersboxDao subDao = mDb.getKnittersboxDao();
                        KnittersboxDao userDao = mDb.getKnittersboxDao();
                        KnittersboxDao postOfficeDao = mDb.getKnittersboxDao();
                        KnittersboxDao faqDao = mDb.getKnittersboxDao();


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // Register user
                                subDao.registerSub(sub1);
                                subDao.registerSub(sub2);
                                subDao.registerSub(sub3);
                                userDao.registerUser(user);
                                postOfficeDao.registerPostnr(postnr1);
                                postOfficeDao.registerPostnr(postnr2);
                                faqDao.registerFaq(faq1);
                                faqDao.registerFaq(faq2);
                                faqDao.registerFaq(faq3);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Lagt til Subscription", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        });

    }


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

