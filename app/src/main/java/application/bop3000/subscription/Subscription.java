package application.bop3000.subscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;

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

        AppExecutors.getInstance().diskIO().execute( new Runnable() {
            @Override
            public void run() {
                int userID = 1;

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
}