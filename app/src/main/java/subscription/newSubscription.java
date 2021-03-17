package subscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import application.bop3000.R;

public class newSubscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subscription);
    }

    public void goBack(View view){
        Intent intent = new Intent(newSubscription.this, Subscription.class);
        startActivity(intent);
    }
}