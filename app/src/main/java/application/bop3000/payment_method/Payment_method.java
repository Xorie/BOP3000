package application.bop3000.payment_method;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Payment;
import application.bop3000.faq.faq;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.login.Login;
import application.bop3000.sharedpreference.SharedPreferenceConfig;
import application.bop3000.subscription.Subscription;
import application.bop3000.userprofile.UserProfile;

public class Payment_method extends AppCompatActivity {
    private SharedPreferenceConfig sharedPreferenceConfig;
    Button btn_change;
    MyDatabase DB;
    PayAdapter pAdapter;
    RecyclerView recView;
    TextView paymentcardnr, paymentexp;

    //Menu
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View itemLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        btn_change = findViewById(R.id.btn_new_pay);
        recView = findViewById(R.id.pay_recview);
        recView.setLayoutManager(new LinearLayoutManager(this));
        DB = MyDatabase.getDatabase(getApplicationContext());
        paymentcardnr = findViewById(R.id.srPaymentCardnr);
        paymentexp = findViewById(R.id.srPaymentExp);
        pAdapter = new PayAdapter(this);
        recView.setAdapter(pAdapter);

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

        //SKAL BLI RØD
        itemLogout = findViewById(R.id.logout);

        setupDrawerContent(navigationView);
        View header = navigationView.getHeaderView(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.close();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    private void retrieveTasks() {
        AtomicReference<String> myData = new AtomicReference<>("");
        AppExecutors.getInstance().diskIO().execute(() -> {
            Intent i = getIntent();
            final String user = Login.getUser().getEmail();
            final List<Payment> pay = DB.getKnittersboxDao().loadAllPay(user);
            runOnUiThread(() -> pAdapter.setTasks(pay));
        });
    }

    public void goNewpayment(View view){
        Intent intent = new Intent(Payment_method.this, newPayment_method.class);
        startActivity(intent);
    }

    //slette betalingsmetode
    //ikke testet
    /*
    public void onClickDelete(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final MyDatabase database = MyDatabase.getDatabase(getApplicationContext());
                final KnittersboxDao kdao = database.getUserDao();
                Intent i = getIntent();
                final String bruker = i.getStringExtra("SID");
                System.out.println(bruker);
                kdao.deleteById(bruker);
                Looper.prepare();
                Toast.makeText(Payment_method.this, "Betalingsmetode slettet!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Payment_method.this, MainActivity.class);
                startActivity(intent);
            }
        }).start();

    }*/
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
                break;

            case R.id.subscription:
                startActivity(intent_subscription);
                break;

            case R.id.faq:
                startActivity(intent_faq);
                break;
            case R.id.payment:
                startActivity(intent_payment);
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