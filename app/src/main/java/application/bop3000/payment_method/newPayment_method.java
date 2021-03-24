package application.bop3000.payment_method;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Payment;

public class newPayment_method extends AppCompatActivity {
    Button btn_save, btn_back;
    private MyDatabase DB;
    EditText paycardnr, payexp, paycvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment_method);
        btn_save = findViewById(R.id.pay_save);
        btn_back = findViewById(R.id.pay_back);
        paycardnr = findViewById(R.id.paymentcardnr);
        payexp = findViewById(R.id.paymentexp);
        paycvc = findViewById(R.id.paymentcvc);
        DB = MyDatabase.getDatabase(getApplicationContext());
    }

    //fungerer, ingen kryptering og larger ikke CVC kode
    public void save_payment(View view) {
        final KnittersboxDao payDao = DB.getKnittersboxDao();
        final Payment pay = new Payment();
        Intent i = getIntent();
        final String user = i.getStringExtra("SID"); //sjekk med Bjørge med navnet
        final String paymentcardnr = paycardnr.getText().toString();
        final String paymentexp = payexp.getText().toString();
        final String paymentcvc = paycvc.getText().toString();
        pay.setUserID(user);
        pay.setPayment_cardnr(paymentcardnr);
        pay.setPayment_expirationDate(paymentexp);
        //pay.setPayment_CVC(paycvc);
        new Thread(() -> payDao.insertNewPayment(pay)).start();
        Toast.makeText(getApplicationContext(), "Betalingsmetode vellykket lagt til.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Payment_method.class);
        startActivity(intent);
    }

    public void backtopayment(View view){
        Intent intentback = new Intent(newPayment_method.this, Payment_method.class);
        startActivity(intentback);

        }

}