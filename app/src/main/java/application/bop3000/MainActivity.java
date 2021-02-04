package application.bop3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void getFaq(View view){
        Intent intent = new Intent(MainActivity.this, FAQ.faq.class);
        startActivity(intent);
    }
}