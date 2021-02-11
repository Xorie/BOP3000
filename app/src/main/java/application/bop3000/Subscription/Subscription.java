package application.bop3000.Subscription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import application.bop3000.R;

public class Subscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Button button = (Button) findViewById(R.id.subscription_btn_change);
        Button buttoninsert = (Button) findViewById(R.id.subscription_btn_insert);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Subscription_change.class);
                startActivity(intent);
            }
        });
    }
}