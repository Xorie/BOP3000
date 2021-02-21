package application.bop3000.Subscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
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
    private Object Message;
    private Object Subscription;
    private int subID;
    private application.bop3000.database.Subscription subDesc;


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
                List<String> subscriptionList = new ArrayList<>();

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
                        ArrayAdapter adapter = new ArrayAdapter<>(Subscription_change.this,
                                android.R.layout.simple_spinner_item, subscriptionList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                });
            }
        });

        //spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                //KnittersboxDao hent = (KnittersboxDao) mDb.getKnittersboxDao();
                //Subscription sub = hent.hentSub();
                //int subid = sub.getSubscriptionID();
                //System.out.println(subid);
                //System.out.println(subid);
                //System.out.println(subid);
                //System.out.println(subid);
                //System.out.println("æsj");
                //int listcounter = 0;
                //List<Subscription> hente;
                //hente = mDb.getKnittersboxDao().subListe();
                //Subscription sub = hente.get(listcounter);
                //int subid = sub.getSubscriptionID();

                //Message mSelected = (Message) parent.getItemAtPosition(pos);
                //Log.i("Id:"+ mSelected.getSubscriptionID());
            //}

            //@Override
            //public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
               // Log.i("Message", "Nothing is selected");
           // }
       // });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //Oppretter brukerobjekt og henter inputdata
                        User user = new User();
                        final String city = et_city.getText().toString();
                        String postnr = et_postnr.getText().toString();
                        final int i_postnr = Integer.parseInt(postnr);
                        final String address = et_address.getText().toString();
                        String subscript = spinner.getSelectedItem().toString();

                        //Henter abonnementet som er valgt og finner ID
                        subDesc = mDb.getKnittersboxDao().hentSubID(subscript);
                        subID = subDesc.getSubscriptionID();

                        //test
                        System.out.println(subID);
                        System.out.println(city);
                        System.out.println(i_postnr);
                        System.out.println(address);
                        System.out.println(subscript);

                        //Setter
                        user.setCity(city);
                        user.setPostnr(i_postnr);
                        user.setStreetname(address);
                        user.setSubscription_subscriptionID(subID);

                        // Register user
                        KnittersboxDao userDao = mDb.getKnittersboxDao();
                        userDao.registerUser(user);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Lagt til Bruker", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
    }
}