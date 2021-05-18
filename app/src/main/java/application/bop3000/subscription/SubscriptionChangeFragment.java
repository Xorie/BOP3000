package application.bop3000.subscription;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.PostOffice;
import application.bop3000.database.Subscription;
import application.bop3000.database.User;
import application.bop3000.login.Login;
import application.bop3000.network.Constants;
import application.bop3000.network.DatabasePost;


public class SubscriptionChangeFragment extends Fragment {
    private MyDatabase mDb;
    private EditText et_city;
    private EditText et_postnr;
    private EditText et_address;
    private Spinner spinner;
    private int size;
    private int count;
    private String str_subID;
    private String subTemp;
    private String city;
    private String postnr;
    private String address;
    private String subscript;
    private ArrayAdapter adapter;

    private User user;
    private ArrayList<String> subscriptionList;
    private ArrayList<String> postOfficeList;
    private List<Subscription> getSubList;

    //private ImageButton backbutton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription_change, container, false);

        mDb = MyDatabase.getDatabase(getActivity().getApplicationContext());
        spinner = view.findViewById(R.id.subscription_spinner);
        et_city = view.findViewById(R.id.subscription_city);
        et_postnr = view.findViewById(R.id.subscription_postnr);
        et_address = view.findViewById(R.id.subscription_address);
        //backbutton = view.findViewById(R.id.toolbarBack);

        subscriptionList = new ArrayList<>();
        postOfficeList = new ArrayList<>();
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, subscriptionList);

        //calling the method for setting info in spinner
        getSub();

        //calling the external database method for setting info in spinner
        //getSubExternal();

        showUserData();
        return view;
    }

    private void showUserData(){
        //Puts existing address in the input field if there is any
        AppExecutors.getInstance().diskIO().execute( () -> {
            String userMail = Login.getUser().getEmail();
            //Getting info from user
            User user = mDb.getKnittersboxDao().loadUser(userMail);

            //Setting value
            getActivity().runOnUiThread( () -> {
                et_postnr.setText(user.getPostnr());
                et_city.setText(user.getCity());
                et_address.setText(user.getStreetname());
            } );
        } );
    }
    private void getSubExternal() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Constants.IP + "spinner.php?";

        StringRequest stringRequest = new StringRequest( Request.Method.GET, url,
                response -> {
                    //Splitting the list retrived from the database and add to Array
                    String[] desc = response.split("¤" );
                    subscriptionList.addAll(Arrays.asList(desc));

                    setSub();
                }, System.out::println);
        //RequestQueue
        queue.add(stringRequest);
    }

    private void getSub() {
        //Finds all the subscriptions available
        AppExecutors.getInstance().diskIO().execute( () -> {
            getSubList = mDb.getKnittersboxDao().subList();
            size = getSubList.size();
            for (count = 0; count < size; count++) {
                Subscription sub = getSubList.get(count);
                subTemp = sub.getDescription();
                subscriptionList.add(subTemp);
            }
            setSub();
        } );
    }

    private void setSub() {
        //Found subscriptions are set in adapter
        getActivity().runOnUiThread( () -> {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } );
    }

    private void getSubID() {
        //Retrieving the chosen subscription and finding it's ID
        int subID;
        Subscription subDesc;
        subDesc = mDb.getKnittersboxDao().hentSubID(subscript);
        subID = subDesc.getSubscriptionID();
        str_subID = String.valueOf(subID);
    }

    private void getPostOffice() {
        //Retrieves all the registered postOffices
        List<PostOffice> getPostList;
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

        //OnButtonClick saves the subscription and address
        getView().findViewById(R.id.btn_save).setOnClickListener(v -> new Thread( () -> {
            int userID = Login.getUser().getUserID();
            //Finding the user in the database
            user = mDb.getKnittersboxDao().hentBrukerID(userID);

            //getting the value from input fields
            city = et_city.getText().toString();
            postnr = et_postnr.getText().toString();
            address = et_address.getText().toString();
            subscript = spinner.getSelectedItem().toString();

            getSubID();
            getPostOffice();
            //Checking if the PostOffice given already exist
            if (postOfficeList.contains(postnr)) {
                if(city.isEmpty() || postnr.isEmpty() || address.isEmpty()) {
                    getActivity().runOnUiThread( () -> Toast.makeText(getActivity().getApplicationContext(),
                            "Fyll felt", Toast.LENGTH_SHORT ).show() );
                } else {
                    //Setting the values into user object
                    user.setCity(city);
                    user.setPostnr(postnr);
                    user.setStreetname(address);
                    user.setSubscription_subscriptionID(str_subID);
                    //Update user
                    mDb.getKnittersboxDao().updateUser(user);

                    //Sync to external database
                    DatabasePost.syncUserData(user.getEmail(), user.getPassword(), getContext());

                    //Changing displayed fragment
                    getActivity().runOnUiThread( () -> {
                        Toast.makeText(getActivity().getApplicationContext(), "Bruker er oppdatert", Toast.LENGTH_SHORT ).show();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        SubscriptionFragment fragment = new SubscriptionFragment();
                        manager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    } );
                }
            } else {
                getActivity().runOnUiThread( () -> Toast.makeText(getActivity().getApplicationContext(),
                        "Postnummer finnes ikke", Toast.LENGTH_SHORT ).show() );
            }
        } ).start() );
    }
}