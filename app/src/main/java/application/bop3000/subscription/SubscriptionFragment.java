package application.bop3000.subscription;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import application.bop3000.AppExecutors;
import application.bop3000.R;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.User;
import application.bop3000.login.Login;


public class SubscriptionFragment extends Fragment {

    private MyDatabase mDb;
    private TextView userSub;
    private TextView userPost;
    private TextView userCity;
    private TextView userAddress;
    private String subDesc;
    private String post;
    private String city;
    private String address;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);
        mDb = MyDatabase.getDatabase(getActivity().getApplicationContext());
        userSub = view.findViewById(R.id.userSub);
        userPost = view.findViewById(R.id.userPost);
        userCity = view.findViewById(R.id.userCity);
        userAddress = view.findViewById(R.id.userAddress);

        AppExecutors.getInstance().diskIO().execute( () -> {
            int userID = Login.getUser().getUserID();

            //Getting information on user
            User user = mDb.getKnittersboxDao().hentBrukerID(userID);

            //Checking for NULL values
            if (user.getSubscription_subscriptionID() == null) {
                subDesc = "Ingen";
            } else {
                String sub = user.getSubscription_subscriptionID();
                //Displaying the description instead of subscription ID
                int subscriptID = Integer.parseInt(sub);
                application.bop3000.database.Subscription subscription = mDb.getKnittersboxDao().hentSubDesc(subscriptID);
                subDesc = subscription.getDescription();
            }
            if (user.getPostnr() == null) {
                post = "Ingen";
            } else { post = user.getPostnr(); }

            if (user.getCity() == null) {
                city = "Ingen";
            } else { city = user.getCity(); }

            if (user.getStreetname() == null) {
                address = "Ingen";
            } else { address = user.getStreetname(); }

            getActivity().runOnUiThread( () -> {
                //Filling the textview
                userSub.setText(subDesc);
                userPost.setText(post);
                userCity.setText(city);
                userAddress.setText(address);
            } );
        } );
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Changing the displayed fragment
        getView().findViewById(R.id.subscription_btn_change).setOnClickListener(v ->  {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            SubscriptionChangeFragment fragmentChange = new SubscriptionChangeFragment();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, fragmentChange)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });

    }
}