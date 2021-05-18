package application.bop3000.faq;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.FAQ;
import application.bop3000.database.MyDatabase;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.login.Login;
import application.bop3000.network.Constants;
import application.bop3000.payment_method.Payment_method;
import application.bop3000.sharedpreference.SharedPreferenceConfig;
import application.bop3000.subscription.Subscription;
import application.bop3000.userprofile.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class faq extends AppCompatActivity {

    private SharedPreferenceConfig sharedPreferenceConfig;
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

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        mDb = MyDatabase.getDatabase(getApplicationContext());
        faqList = new ArrayList<>();
        itemSet = new ArrayList<>();
        expandableListView = findViewById(R.id.expandable_listview);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);

        //Calling the internal FAQ method
        getFaq();

        //Calling the external FAQ method
        //getFaqEksternt();


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
        //itemLogout = findViewById(R.id.logout);

        setupDrawerContent(navigationView);
        View header = navigationView.getHeaderView(0);
    }


    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.close();
    }


    private void getFaq() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //Getting the FAQ list from database
                faqList = mDb.getKnittersboxDao().faqList();
                size = faqList.size();

                //Separating the question and answer
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
                Log.d( "SIZE", String.valueOf( size ) );
                //Binding the question and answer together
                for (count = 0; count < size; count++) {
                    listItem.put(listGroup.get(increment), Collections.singletonList(itemSet.get(increment)));
                    ++increment;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getFaqEksternt() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.IP + "faq.php?";

        StringRequest stringRequest = new StringRequest( Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Splitting question and answer and putting them in different arrays
                        String[] kombo = response.split( "¤" );
                        String[] question = kombo[0].split( "#" );
                        String[] answer = kombo[1].split( "#" );

                        listGroup.addAll(Arrays.asList(question));
                        itemSet.addAll(Arrays.asList(answer));

                        Log.d( "TUSS", String.valueOf( listGroup ) );
                        Log.d( "TATT", String.valueOf( itemSet ) );

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                increment = 0;
                                size = listGroup.size();
                                for (count = 0; count < size; count++) {
                                    listItem.put(listGroup.get(increment), Collections.singletonList(itemSet.get(increment)));
                                    ++increment;
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        //RequestQueue
        queue.add(stringRequest);
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
                break;
            case R.id.faq:
                startActivity(intent_faq);
                finish();
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