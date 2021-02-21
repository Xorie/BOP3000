package application.bop3000.Subscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;

public class Subscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Button button = (Button) findViewById(R.id.subscription_btn_change);
        Button buttoninsert = (Button) findViewById(R.id.subscription_btn_insert);



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

                MyDatabase subDatabase = MyDatabase.getDatabase(getApplicationContext());
                KnittersboxDao subDao = subDatabase.getKnittersboxDao();
                KnittersboxDao userDao = subDatabase.getKnittersboxDao();


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
}