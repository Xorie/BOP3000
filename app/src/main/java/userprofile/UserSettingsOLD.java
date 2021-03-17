package userprofile;

import androidx.appcompat.app.AppCompatActivity;
import application.bop3000.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserSettingsOLD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings_old);
    }

//    public void changeSubscription(View view) {
//        Intent change_subscription = new Intent(this, ChangeSubscription.class);
//        startActivity(change_subscription);
//    }

    public void addName(View view) {
        Intent add_name = new Intent(this, UserSettings.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(add_name);
    }

}