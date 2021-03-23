package application.bop3000.faq;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import application.bop3000.AppExecutors;
import application.bop3000.MainActivity;
import application.bop3000.R;
import application.bop3000.database.FAQ;
import application.bop3000.database.MyDatabase;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.payment_method.Payment_method;
import application.bop3000.subscription.Subscription;
import application.bop3000.userprofile.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class faq extends AppCompatActivity {

    private List<String> itemSet;
    private MyDatabase mDb;
    private ExpandableListView expandableListView;
    private List<FAQ> faqList;
    private List<String> listGroup;
    private HashMap<String,List<String>> listItem;
    private MainAdapter adapter;
    private Integer size;
    private Integer count;
    private Integer increment;

    //Menu
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View itemLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        mDb = MyDatabase.getDatabase(getApplicationContext());
        faqList = new ArrayList<>();
        itemSet = new ArrayList<>();
        expandableListView = findViewById(R.id.expandable_listview);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
        getFaq();

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



    private void getFaq() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                faqList = mDb.getKnittersboxDao().faqList();
                size = faqList.size();

                for (count = 0; count < size; count++) {
                    FAQ faq = faqList.get(count);
                    String faqSp = faq.getQuestion();
                    String faqAn = faq.getAnswer();
                    listGroup.add(faqSp);
                    itemSet.add(faqAn);
                }
                showFaq();
            }
        });
    }
    private void showFaq() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                increment = 0;
                for (count = 0; count < size; count++) {
                    listItem.put(listGroup.get(increment), Collections.singletonList(itemSet.get(increment)));
                    ++increment;
                }
                adapter.notifyDataSetChanged();
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