package application.bop3000.Subscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Subscription;
import application.bop3000.database.User;

public class Subscription_change extends AppCompatActivity {

    private MyDatabase mDb;
    private EditText et_city;
    private EditText et_postnr;
    private EditText et_address;
    private Button button;
    private Spinner spinner;
    private int subID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_change);

        mDb = MyDatabase.getDatabase(getApplicationContext());
        button = findViewById(R.id.btn_save);
        spinner = findViewById(R.id.subscription_spinner);
        et_city = findViewById(R.id.subscription_city);
        et_postnr = findViewById(R.id.subscription_postnr);
        et_address = findViewById(R.id.subscription_address);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //Endelig liste
                List<String> subscriptionList = new ArrayList<>();

                //Liste som henter abonnement
                List<Subscription> hentListe;
                hentListe = mDb.getKnittersboxDao().subListe();

                int size = hentListe.size();
                for (int listcounter = 0; listcounter < size; listcounter++) {
                    Subscription sub = hentListe.get(listcounter);
                    String sub1 = sub.getDescription();
                    subscriptionList.add(sub1);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Setter spinner adapter
                        ArrayAdapter adapter = new ArrayAdapter<>(Subscription_change.this,
                                android.R.layout.simple_spinner_item, subscriptionList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                });
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int userID = 1;
                        //Finner brukeren som er pålogget
                        User user = mDb.getKnittersboxDao().hentBrukerID(userID);

                        //inputdata
                        String city = et_city.getText().toString();
                        String postnr = et_postnr.getText().toString();
                        String address = et_address.getText().toString();
                        String subscript = spinner.getSelectedItem().toString();


                        //Henter abonnementet som er vald og finner ID
                        Subscription subDesc = mDb.getKnittersboxDao().hentSubID(subscript);
                        subID = subDesc.getSubscriptionID();
                        String Str_subID = String.valueOf(subID);


                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                if(city.isEmpty() || postnr.isEmpty() || address.isEmpty()) {
                                    Toast.makeText( getApplicationContext(), "Fyll felt", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        });
                        if(city.isEmpty() || postnr.isEmpty() || address.isEmpty()) {



                        } else {
                            //Setter
                            user.setCity(city);
                            user.setPostnr(postnr);
                            user.setStreetname(address);
                            user.setSubscription_subscriptionID(Str_subID);
                            //Oppdaterer bruker
                            mDb.getKnittersboxDao().updateUser(user);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Bruker er oppdatert", Toast.LENGTH_SHORT ).show();

                                    startActivity(new Intent(Subscription_change.this, application.bop3000.Subscription.Subscription.class ));
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}
