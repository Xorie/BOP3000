package network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import appexecutors.AppExecutors;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import register.Register;

public class DatabasePost {
    //private static Context context;
    private static MyDatabase userDatabase;
    private static KnittersboxDao userDao;
    static String mail, pass, firstname, lastname, city, streetName, displayName, postnr, sub;
    static int userID;
    private static volatile Boolean success = false;

    public static void sendUser(String email, String password, Context context) {
        //Room DB connection
        userDatabase = MyDatabase.getDatabase(context.getApplicationContext());
        userDao = userDatabase.getKnittersboxDao();

        User user = userDao.login(email, password);

        // Get user data
        mail = user.getEmail();
        pass = user.getPassword();
        firstname = user.getFirstname();
        lastname = user.getLastname();
        city = user.getCity();
        streetName = user.getStreetname();
        displayName = user.getDisplayname();
        postnr = user.getPostnr();
        sub = user.getSubscription_subscriptionID();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // HA EN EGEN STRING SOM BRUKES OVERALT!!!!!!!!!!!!!!!!!!!!!!!!!
        String url = "http://192.168.10.199/BACH/user.php?";

        url += "PW=" + password + "&email=" + mail + "&FN=" + firstname + "&LN=" + lastname + "&city=" +
                city + "&SN=" + streetName + "&DN=" + displayName + "&PN=" + postnr + "&sub=" + sub;

        Log.d("USER", url);
        Log.d("USER", mail + pass + firstname + lastname + city + streetName + displayName + postnr + sub);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.contains("Brukeren er registrert")) {
                                    DatabaseGet.syncUserId(email, password, context);
                                } else {
                                    Log.d("USER", "SQL FEIL: " + response);
                                }
                            }
                        }).start();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Ingen tilkobling eksternt!", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void syncUserData(String email, String password, Context context) {
        Log.d("UPDATE", "parameters: " + email + password);
        // Change variables for user
        if (localUserData(email, password, context)) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);

            // HA EN EGEN STRING SOM BRUKES OVERALT!!!!!!!!!!!!!!!!!!!!!!!!!
            String url = "http://192.168.10.199/BACH/syncUserData.php?";
            url += "userID=" + userID + "&PW=" + pass + "&email=" + mail + "&FN=" + firstname + "&LN=" + lastname + "&city=" +
                    city + "&SN=" + streetName + "&DN=" + displayName + "&PN=" + postnr + "&sub=" + sub;

            Log.d("UPDATE", url);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!response.contains("Bruker finnes ikke")) {
                                        Log.d("UPDATE", "BRUKER INFO OPPDATERT EKSTERNT");
                                    } else {
                                        Log.d("UPDATE", "FEIL BRUKER INFO OPPDATERT EKSTERNT SQL-ERROR: " + response);
                                    }
                                }
                            }).start();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Ingen tilkobling eksternt!", Toast.LENGTH_LONG).show();
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            Log.d("UPDATE", "FEIL VED HENTING AV LOKAL DATA");
        }
    }

    public static Boolean localUserData(String email, String password, Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Room DB connection
                userDatabase = MyDatabase.getDatabase(context.getApplicationContext());
                userDao = userDatabase.getKnittersboxDao();

                // Find the right user
                User user = userDao.login(email, password);

                if (user != null) {
                    // Get user data
                    userID = user.getUserID();
                    mail = user.getEmail();
                    pass = user.getPassword();
                    firstname = user.getFirstname();
                    lastname = user.getLastname();
                    city = user.getCity();
                    streetName = user.getStreetname();
                    displayName = user.getDisplayname();
                    postnr = user.getPostnr();
                    sub = user.getSubscription_subscriptionID();

                    success = true;
                } else {
                    success = false;
                }
            }
        }).start();
        return success;
    }
}








//    //Room DB connection
//    userDatabase = MyDatabase.getDatabase(context.getApplicationContext());
//    userDao = userDatabase.getKnittersboxDao();
//
//    // Find the right user
//    User user = userDao.login(email, password);
//
//                if (user != null) {
//        // Get user data
//        userID = user.getUserID();
//        mail = user.getEmail();
//        pass = user.getPassword();
//        firstname = user.getFirstname();
//        lastname = user.getLastname();
//        city = user.getCity();
//        streetName = user.getStreetname();
//        displayName = user.getDisplayname();
//        postnr = user.getPostnr();
//        sub = user.getSubscription_subscriptionID();
//
//        success = true;
//    } else {
//        success = false;
//    }
