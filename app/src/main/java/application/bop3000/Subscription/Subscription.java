package application.bop3000.Subscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;

public class Subscription extends AppCompatActivity {

    private MyDatabase mDb;
    private TextView userSub;
    private TextView userPost;
    private TextView userCity;
    private TextView userAddress;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        mDb = MyDatabase.getDatabase(getApplicationContext());
        button = findViewById(R.id.subscription_btn_change);
        Button buttoninsert = findViewById(R.id.subscription_btn_insert);
        userSub = findViewById(R.id.userSub);
        userPost = findViewById(R.id.userPost);
        userCity = findViewById(R.id.userCity);
        userAddress = findViewById(R.id.userAddress);

        AppExecutors.getInstance().diskIO().execute( new Runnable() {
            @Override
            public void run() {
                int userID = 1;

                //Henter informasjon på brukerID
                User user = mDb.getKnittersboxDao().hentBrukerID(userID);
                String sub = user.getSubscription_subscriptionID();
                String post = user.getPostnr();
                String city = user.getCity();
                String address = user.getStreetname();

                //Henter subscription desc fra ID
                int subscriptID = Integer.parseInt(sub);
                application.bop3000.database.Subscription subscription = mDb.getKnittersboxDao().hentSubDesc(subscriptID);
                String subDesc = subscription.getDescription();

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
                        sub1.setType(1);
                        sub2.setType(2);
                        sub3.setType(3);
                        user.setEmail("test@test.no");
                        user.setDisplayname("test");
                        user.setPassword("test");
                        sub1.setDescription("Abonnement 1");
                        sub2.setDescription("Abonnement 2");
                        sub3.setDescription("Abonnement 3");

                        KnittersboxDao subDao = mDb.getKnittersboxDao();
                        KnittersboxDao userDao = mDb.getKnittersboxDao();


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // Register user
                                subDao.registerSub(sub1);
                                subDao.registerSub(sub2);
                                subDao.registerSub(sub3);
                                userDao.registerUser(user);
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
}