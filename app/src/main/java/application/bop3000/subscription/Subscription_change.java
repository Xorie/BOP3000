package application.bop3000.subscription;

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
import application.bop3000.database.PostOffice;
import application.bop3000.database.Subscription;
import application.bop3000.database.User;
import application.bop3000.login.Login;

public class Subscription_change extends AppCompatActivity {

    private MyDatabase mDb;
    private EditText et_city;
    private EditText et_postnr;
    private EditText et_address;
    private Button button;
    private Spinner spinner;
    private int subID;
    private int size;
    private int count;
    private String str_subID;
    private String subTemp;
    private String city;
    private String postnr;
    private String address;
    private String subscript;
    private ArrayAdapter adapter;
    private Subscription subDesc;
    private User user;
    //Endelig liste
    private ArrayList<String> subscriptionList;
    private ArrayList<String> postOfficeList;
    //Liste som henter abonnement
    private List<Subscription> getSubList;
    private List<PostOffice> getPostList;


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

        subscriptionList = new ArrayList<>();
        postOfficeList = new ArrayList<>();
        adapter = new ArrayAdapter(Subscription_change.this, android.R.layout.simple_spinner_item, subscriptionList);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSub();
        buttonClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        city = et_city.getText().toString();
        postnr = et_postnr.getText().toString();
        address = et_address.getText().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_city.setText(city);
        et_postnr.setText(postnr);
        et_address.setText(address);
    }

    private void getSub() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                getSubList = mDb.getKnittersboxDao().subList();

                size = getSubList.size();
                for (count = 0; count < size; count++) {
                    Subscription sub = getSubList.get(count);
                    subTemp = sub.getDescription();
                    subscriptionList.add(subTemp);
                }
                setSub();
            }
        });
    }

    private void setSub() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Setter spinner adapter
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        });
    }

    private void buttonClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int userID = Login.getUser().getUserID();
                        //Finner brukeren som er pålogget
                        user = mDb.getKnittersboxDao().hentBrukerID(userID);

                        //inputdata
                        city = et_city.getText().toString();
                        postnr = et_postnr.getText().toString();
                        address = et_address.getText().toString();
                        subscript = spinner.getSelectedItem().toString();

                        getSubID();

                        getPostOffice();
                        if (postOfficeList.contains(postnr)) {
                            if(city.isEmpty() || postnr.isEmpty() || address.isEmpty()) {
                                runOnUiThread( new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Fyll felt", Toast.LENGTH_SHORT ).show();
                                    }
                                });

                            } else {
                                //Setter
                                user.setCity(city);
                                user.setPostnr(postnr);
                                user.setStreetname(address);
                                user.setSubscription_subscriptionID(str_subID);
                                //Oppdaterer bruker
                                mDb.getKnittersboxDao().updateUser(user);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Bruker er oppdatert", Toast.LENGTH_SHORT ).show();
                                        startActivity(new Intent(Subscription_change.this, application.bop3000.subscription.Subscription.class ));
                                    }
                                });
                            }
                        } else {
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Postnummer finnes ikke", Toast.LENGTH_SHORT ).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    private void getSubID() {
        //Henter abonnementet som er vald og finner ID
        subDesc = mDb.getKnittersboxDao().hentSubID(subscript);
        subID = subDesc.getSubscriptionID();
        str_subID = String.valueOf(subID);
    }

    private void getPostOffice() {
        //Henter en liste med PostOffice
        getPostList = mDb.getKnittersboxDao().hentPostOffice();
        size = getPostList.size();
        for (count = 0; count < size; count++) {
            PostOffice post = getPostList.get(count);
            int postDB = post.getPostnr();
            String postString = String.valueOf(postDB);
            postOfficeList.add(postString);
        }
    }
}
