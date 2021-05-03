package application.bop3000.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;

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
        String url = "http://192.168.1.160/BACH/syncUserId.php?";
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
}
