package payment_method;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import application.bop3000.R;

public class Payment_method extends AppCompatActivity {
    Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        btn_change = findViewById(R.id.btn_change);
    }

    public void goNewpayment(View view){
        Intent intent = new Intent(Payment_method.this, newPayment_method.class);
        startActivity(intent);
    }
}