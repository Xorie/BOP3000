package application.bop3000.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.inspiration.Inspiration;
import application.bop3000.login.Login;
import application.bop3000.sharedpreference.SharedPreferenceConfig;

public class DatabaseGet {
    private static MyDatabase userDatabase;
    private static KnittersboxDao userDao;
    static String mail, pass, firstname, lastname, city, streetName, displayName, postnr, sub;

    public static void syncUserId(String email, String password, Context context) {
        //Room DB connection
        userDatabase = MyDatabase.getDatabase(context.getApplicationContext());
        userDao = userDatabase.getKnittersboxDao();

        User user = userDao.login(email, password);

        // Get user data
        mail = user.getEmail();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // HA EN EGEN STRING SOM BRUKES OVERALT!!!!!!!!!!!!!!!!!!!!!!!!!
        String url = Constants.IP + "syncUserId.php?";
        url += "email=" + mail;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(!response.contains("Bruker finnes ikke")) {
                                    int userId = Integer.parseInt(response);
                                    userDao.updateUserIdQuery(userId, mail);
                                } else {
                                    //Toast.makeText(context, "SQL feil" + response, Toast.LENGTH_LONG).show();
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

    public static void getUserFromExternal(String email, String password, Context context) {
        //Room DB connection
        userDatabase = MyDatabase.getDatabase(context.getApplicationContext());
        userDao = userDatabase.getKnittersboxDao();

        // Creating User Entity
        User user = new User();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // HA EN EGEN STRING SOM BRUKES OVERALT!!!!!!!!!!!!!!!!!!!!!!!!!
        String url = Constants.IP + "getUser.php?";
        url += "email=" + email + "&PW=" + password;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(!response.contains("Bruker finnes ikke")) {
                                    // Splits response string to user data
                                    String lines[] = response.split("/");

                                    user.setEmail(email);
                                    user.setPassword(password);

                                    //UserID
                                    user.setUserID(Integer.valueOf(lines[1].substring(1)));
                                    Log.d("UPDATE", "ID: " + user.getUserID());

                                    // Checks if there are information //
                                    // Firstname
                                    if(!lines[2].substring(1).isEmpty()) {
                                        user.setFirstname(lines[2].substring(1));
                                    }
                                    Log.d("UPDATE", "FIRSTANME: " + user.getFirstname());
                                    // Lastname
                                    if(!lines[3].substring(1).isEmpty()) {
                                        user.setLastname(lines[3].substring(1));
                                    }
                                    Log.d("UPDATE", "LASTNAME: " + user.getLastname());
                                    // Streetname
                                    if(!lines[4].substring(1).isEmpty()) {
                                        user.setStreetname(lines[4].substring(1));
                                    }
                                    Log.d("UPDATE", "STREETNAME: " + user.getStreetname());
                                    // Displayname
                                    if(!lines[5].substring(1).isEmpty()) {
                                        user.setDisplayname(lines[5].substring(1));
                                    }
                                    Log.d("UPDATE", "DISPLAYNAME: " + user.getDisplayname());
                                    // Postnr
                                    if(!lines[6].substring(1).isEmpty()) {
                                        user.setPostnr(lines[6].substring(1));
                                    }
                                    Log.d("UPDATE", "POSTNR: " + user.getPostnr());
                                    // SubscriptionID
                                    if(!lines[7].substring(1).isEmpty()) {
                                        user.setSubscription_subscriptionID(lines[7].substring(1));
                                    }
                                    Log.d("UPDATE", "SUBSCRIPTION: " + user.getSubscription_subscriptionID());

                                    // Register user Room DB
                                    userDao.registerUser(user);
                                    Login.userExternalLogin(true, context, user.getEmail(), user.getPassword(), user.getDisplayname());
                                } else {
                                    // User does not exist external = User is not registered
                                    Looper.prepare();
                                    Toast.makeText(context, "Ugyldig informasjon!", Toast.LENGTH_LONG).show();
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
}
