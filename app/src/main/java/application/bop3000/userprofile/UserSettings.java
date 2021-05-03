package application.bop3000.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.login.Login;
import application.bop3000.network.DatabasePost;
import application.bop3000.sharedpreference.SharedPreferenceConfig;

public class UserSettings extends AppCompatActivity {

    // Inputfelt
    EditText usrname;
    EditText fname;
    EditText sname;
    EditText email;

    // Lagre knapp
    Button update;

    // Database
    private MyDatabase mDb;

    // Email for bruker (blir hentet i onStart)
    String email_usr = Login.getUser().getEmail();

    private SharedPreferenceConfig sharedPreferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        // Finner views
        usrname = findViewById(R.id.usrname);
        fname = findViewById(R.id.fname);
        sname = findViewById(R.id.sname);
        email = findViewById(R.id.email);
        update = findViewById(R.id.btn_updatename);

        //Kobler til database
        mDb = MyDatabase.getDatabase(getApplicationContext());

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Finner email som ble sent fra brukerprofil
        //Intent user_settings = getIntent();
        //email_usr = user_settings.getStringExtra("useremail");

        //Viser data i inputfeltene om det er lagt inn noe
        showData();

    }

    public void userprofileBack(View view) {
        Intent user_profile_back = new Intent(this, UserProfile.class);
        startActivity(user_profile_back);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent user_profile_back = new Intent(this, UserProfile.class);
        startActivity(user_profile_back);
        finish();
    }

    // Viser data i inputfeltene om det er lagt inn noe
    private void showData(){
        AppExecutors_OLD.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                //Henter data om brukeren
                User user = mDb.getKnittersboxDao().loadUser(email_usr);

                // Setter data på riktig plass
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        usrname.setText(user.getDisplayname());
                        fname.setText(user.getFirstname());
                        sname.setText(user.getLastname());
                        email.setText(user.getEmail());
                        //password_old.setText(user.getPassword());
                    }
                });
            }
        });
    }

    //Knapp for å oppdatere info som er skrevet inn
    public void updateUserinfo(View view){
        AppExecutors_OLD.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Henter info som er skrevet inn
                String username = usrname.getText().toString();
                String firstname = fname.getText().toString();
                String lastname = sname.getText().toString();
                String emailnew = email.getText().toString(); //NB: MÅ FIKSES

                // Henter data om brukeren
                User user = Login.getUser();//mDb.getKnittersboxDao().loadUser(email_usr);

                // For oppdatering av ekstern database
                String emailold = user.getEmail();

                // Hvis ingenting er endret
                if(username.equals(user.getDisplayname()) && firstname.equals(user.getFirstname()) && lastname.equals(user.getLastname()) && emailnew.equals(user.getEmail())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Ingen endringer gjort", Toast.LENGTH_LONG).show();
                        }
                    });
                // Hvis endringer er gjort
                }
                else if(emailnew.isEmpty() || emailnew.equals(" ")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "E-post kan ikke være tom!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(mDb.getKnittersboxDao().displayname(username) != null && !username.equals(user.getDisplayname())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Visningsnavn eksisterer!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else if(mDb.getKnittersboxDao().userEmail(emailnew) != null && !emailnew.equals(user.getEmail())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "E-post eksisterer!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {

                    // Setter data om brukeren
                    user.setDisplayname(username);
                    user.setFirstname(firstname);
                    user.setLastname(lastname);
                    user.setEmail(emailnew); //NB: MÅ FIKSES


                    // Oppdaterer lokal database
                    //mDb.getKnittersboxDao().updateName(user);
                    mDb.getKnittersboxDao().updateUserInfo(username, firstname, lastname, emailnew, emailold);

                    // Oppdaterer ekstern database
//                    RequestQueue queue = Volley.newRequestQueue(UserSettings.this);
//                    String url = "http://192.168.1.160/bach/updateUser.php?";
//                    url += "email=" + emailold;
//
//                    url += "&displayname=" + username + "&firstname=" + firstname + "&lastname=" + lastname + "&emailnew=" + emailnew;
//
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            if(!response.isEmpty()) {
//                                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getApplicationContext(),"Error: sjekk internett-tilkobling",Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                    queue.add(stringRequest);

                    DatabasePost.syncUserData(emailnew, user.getPassword(), UserSettings.this);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Profil oppdatert", Toast.LENGTH_LONG).show();
                            //System.out.println("EMAIL ER DEN DER: " + user.getEmail());
                            sharedPreferenceConfig.setPreference(UserSettings.this,"PREFS_LOGIN_EMAIL",emailnew);
                        }
                    });
                }
            }
        });
    }

}