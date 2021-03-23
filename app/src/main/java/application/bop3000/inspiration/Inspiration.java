package application.bop3000.inspiration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import application.bop3000.AppExecutors;
import application.bop3000.MainActivity;
import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Post;
import application.bop3000.faq.faq;
import application.bop3000.payment_method.Payment_method;
import application.bop3000.subscription.Subscription;
import application.bop3000.userprofile.UserProfile;

public class Inspiration extends AppCompatActivity {
    MyDatabase DB;
    PostAdapter pAdapter;
    RecyclerView recView;
    TextView posttxt, posttitle;
    ImageView imageview;

    //Menu
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private View itemLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspiration);

        recView = findViewById(R.id.insp_recview);
        recView.setLayoutManager(new LinearLayoutManager(this));
        DB = MyDatabase.getDatabase(getApplicationContext());
        posttxt = findViewById(R.id.srPostTxt);
        posttitle = findViewById(R.id.srPostTitle);
        imageview = findViewById(R.id.srPostImage);

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


        pAdapter = new PostAdapter(this);
        recView.setAdapter(pAdapter);
        //pAdapter.setCustomItemClickListener(onItemClickListener);

        //fra insperasjonssiden til nytt innlegg siden
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(Inspiration.this, Inspiration_newpost.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    private void retrieveTasks() {
        AtomicReference<String> myData = new AtomicReference<>("");
        AppExecutors.getInstance().diskIO().execute(() -> {
            final List<Post> post = DB.getKnittersboxDao().loadAllPost();
            runOnUiThread(() -> pAdapter.setTasks(post));
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