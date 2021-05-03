package application.bop3000.subscription;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.PostOffice;
import application.bop3000.database.Subscription;
import application.bop3000.database.User;
import application.bop3000.login.Login;
import application.bop3000.network.DatabasePost;


public class SubscriptionChangeFragment extends Fragment {
    private MyDatabase mDb;
    private EditText et_city;
    private EditText et_postnr;
    private EditText et_address;
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
    private application.bop3000.database.Subscription subDesc;
    private User user;
    //Endelig liste
    private ArrayList<String> subscriptionList;
    private ArrayList<String> postOfficeList;
    //Liste som henter abonnement
    private List<Subscription> getSubList;
    private List<PostOffice> getPostList;
    private ImageButton backbutton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subscription_change, container, false);

        mDb = MyDatabase.getDatabase(getActivity().getApplicationContext());
        spinner = view.findViewById(R.id.subscription_spinner);
        et_city = view.findViewById(R.id.subscription_city);
        et_postnr = view.findViewById(R.id.subscription_postnr);
        et_address = view.findViewById(R.id.subscription_address);
        backbutton = view.findViewById(R.id.toolbarBack);

        subscriptionList = new ArrayList<>();
        postOfficeList = new ArrayList<>();
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, subscriptionList);

        //getSub();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://192.168.1.29/BACH/spinner.php?";

        StringRequest stringRequest = new StringRequest( Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] desc = response.split("¤" );
                        subscriptionList.addAll(Arrays.asList(desc));

                        setSub();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        //RequestQueue
        queue.add(stringRequest);

        showUserData();
        return view;
    }

    private void showUserData(){
        AppExecutors.getInstance().diskIO().execute( new Runnable() {
            @Override
            public void run() {
                String userMail = Login.getUser().getEmail();
                //Henter data om brukeren
                User user = mDb.getKnittersboxDao().loadUser(userMail);

                // Setter data på riktig plass
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        et_postnr.setText(user.getPostnr());
                        et_city.setText(user.getCity());
                        et_address.setText(user.getStreetname());
                    }
                });
            }
        });
    }

    private void getSub() {
        AppExecutors.getInstance().diskIO().execute( new Runnable() {
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Setter spinner adapter
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().findViewById(R.id.btn_save).setOnClickListener(v ->  {
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
                            getActivity().runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Fyll felt", Toast.LENGTH_SHORT ).show();
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

                            DatabasePost.syncUserData(user.getEmail(), user.getPassword(), getContext());

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Bruker er oppdatert", Toast.LENGTH_SHORT ).show();
                                    //Sender tilbake til forrige fragment
                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                    SubscriptionFragment fragment = new SubscriptionFragment();
                                    manager.beginTransaction()
                                            .replace(R.id.fragment_container, fragment)
                                            .addToBackStack(null)
                                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                            .commit();
                                }
                            });
                        }
                    } else {
                        getActivity().runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getApplicationContext(), "Postnummer finnes ikke", Toast.LENGTH_SHORT ).show();
                            }
                        });
                    }
                }
            }).start();
        });
    }
}