package subscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import application.bop3000.R;

public class Subscription extends AppCompatActivity {
    Button btn_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        btn_new = findViewById(R.id.btn_new);
    }

    public void goNewsubscription(View view){
        Intent intent = new Intent(Subscription.this, newSubscription.class);
        startActivity(intent);
    }
}