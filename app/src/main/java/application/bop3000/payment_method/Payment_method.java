package application.bop3000.payment_method;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import application.bop3000.AppExecutors;
import application.bop3000.MainActivity;
import application.bop3000.R;
import application.bop3000.database.KnittersboxDao;
import application.bop3000.database.MyDatabase;
import application.bop3000.database.Payment;
import application.bop3000.database.Post;
import application.bop3000.inspiration.PostAdapter;

public class Payment_method extends AppCompatActivity {
    Button btn_change;
    MyDatabase DB;
    PayAdapter pAdapter;
    RecyclerView recView;
    TextView paymentcardnr, paymentexp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        btn_change = findViewById(R.id.btn_new_pay);
        recView = findViewById(R.id.pay_recview);
        recView.setLayoutManager(new LinearLayoutManager(this));
        DB = MyDatabase.getDatabase(getApplicationContext());
        paymentcardnr = findViewById(R.id.srPaymentCardnr);
        paymentexp = findViewById(R.id.srPaymentExp);
        pAdapter = new PayAdapter(this);
        recView.setAdapter(pAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTasks();
    }

    private void retrieveTasks() {
        AtomicReference<String> myData = new AtomicReference<>("");
        AppExecutors.getInstance().diskIO().execute(() -> {
            Intent i = getIntent();
            final String user = i.getStringExtra("SID"); //sjekk med Bjørge med navnet
            final List<Payment> pay = DB.getKnittersboxDao().loadAllPay(user);
            runOnUiThread(() -> pAdapter.setTasks(pay));
        });
    }

    public void goNewpayment(View view){
        Intent intent = new Intent(Payment_method.this, newPayment_method.class);
        startActivity(intent);
    }

    //slette betalingsmetode
    //ikke testet
    /*
    public void onClickDelete(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final MyDatabase database = MyDatabase.getDatabase(getApplicationContext());
                final KnittersboxDao kdao = database.getUserDao();
                Intent i = getIntent();
                final String bruker = i.getStringExtra("SID");
                System.out.println(bruker);
                kdao.deleteById(bruker);
                Looper.prepare();
                Toast.makeText(Payment_method.this, "Betalingsmetode slettet!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Payment_method.this, MainActivity.class);
                startActivity(intent);
            }
        }).start();

    }*/
}