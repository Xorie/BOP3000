package application.bop3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import inspiration.Inspiration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goInspiration(View view) {
        Intent intent = new Intent(MainActivity.this, Inspiration.class);
        startActivity(intent);
    }
}