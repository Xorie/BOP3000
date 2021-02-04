package userprofile;

import androidx.appcompat.app.AppCompatActivity;
import application.bop3000.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
    }

    public void changeSubscription(View view) {
        Intent change_subscription = new Intent(this, ChangeSubscription.class);
        startActivity(change_subscription);
    }

}