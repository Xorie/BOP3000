package application.bop3000.userprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;

public class UserProfile extends AppCompatActivity {

    // Database
    private MyDatabase mDb;

    // Tekstfelt
    private TextView username;
    private TextView subscription;
    private TextView firstname;
    //private TextView lastname;
    private TextView email;
    private TextView street;
    private TextView postnr;
    private TextView city;

    // Email for logget inn bruker (HARDKODET NÅ, MÅ KOMME FRA LOGG INN eller noe)
    String email_usr = "hei@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Database
        mDb = MyDatabase.getDatabase(getApplicationContext());

        // Finner views
        username = findViewById(R.id.userprofile_username);
        subscription = findViewById(R.id.userprofile_sub);
        firstname = findViewById(R.id.userprofile_fname);
        //lastname = findViewById(R.id.userprofile_sname);
        email = findViewById(R.id.userprofile_email);
        street = findViewById(R.id.userprofile_street);
        postnr = findViewById(R.id.userprofile_postnr);
        city = findViewById(R.id.userprofile_city);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Metode for å vise data om bruker
        showListData();
    }

    // Metode for å vise data om bruker
    private void showListData(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // Finner data om bruker
                User user = mDb.getKnittersboxDao().loadUser(email_usr);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String full_name = user.getFirstname() + " " + user.getLastname();

                        // Setter data på riktig plass
                        username.setText(user.getDisplayname());
                        firstname.setText(full_name);
                        //lastname.setText(user.getLastname());
                        email.setText(user.getEmail());
                        street.setText(user.getStreetname());
                        postnr.setText(user.getPostnr());
                        city.setText(user.getCity());

                        // Vising av abonnementtype
                        String sub_type = user.getSubscription_subscriptionID();
                        // Hvis bruker ikke har lagt inn noe abonnement
                        if(sub_type == null) {
                            sub_type = "Ingen";
                        }
                        // Hvis bruker har abonnement (NB: MÅ LEGGE INN FLERE)
                        else if(sub_type.equals("1")) {
                            sub_type = "Jordboksen";
                        }
                        //NB: IF FOR RESTEN AV ABONNEMENTTYPENE!
                        subscription.setText(sub_type);

                    }
                });
            }
        });
    }

    // Metode for knapp til redigering av profil
    public void userSettings(View view) {

        // Kobling til redigere profil aktivitet
        Intent user_settings = new Intent(this, UserSettings.class);

        // Sender med email for bruker
        user_settings.putExtra("useremail", email_usr);

        // Starter aktivitet
        startActivity(user_settings);
    }

    // Metode for knapp til endring av passord
    public void changePassword(View view) {

        // Kobling til endre passord aktivitet
        Intent change_pwd = new Intent(this, ChangePassword.class);

        // Sender med email for bruker
        change_pwd.putExtra("useremail", email_usr);

        // Starter aktivitet
        startActivity(change_pwd);
    }


}