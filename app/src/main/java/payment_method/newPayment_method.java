package payment_method;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import application.bop3000.R;

public class newPayment_method extends AppCompatActivity implements View.OnClickListener {
    Button btn_save, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment_method);

        btn_save = findViewById(R.id.btn_save);
        btn_back = findViewById(R.id.btn_back);

        btn_save.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                //kode for å lagre ny betalingsmetode
                break;
            case R.id.btn_back:
                Intent intent = new Intent(newPayment_method.this, Payment_method.class);
                startActivity(intent);
                break;
        }
    }
}