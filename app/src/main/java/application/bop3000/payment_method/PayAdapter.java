package application.bop3000.payment_method;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import application.bop3000.R;
import application.bop3000.database.Payment;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    Context context;
    private List<Payment> PayList;

    public PayAdapter(Context context) {this.context = context;}

    @NonNull
    @Override
    public application.bop3000.payment_method.PayAdapter.PayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.pay_recview, viewGroup, false);
        return new application.bop3000.payment_method.PayAdapter.PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull application.bop3000.payment_method.PayAdapter.PayViewHolder PayViewholder, int i) {
        application.bop3000.payment_method.PayAdapter.PayViewHolder.pay_cardnr.setText(PayList.get(i).getPayment_cardnr());
        application.bop3000.payment_method.PayAdapter.PayViewHolder.pay_expirationDate.setText(PayList.get(i).getPayment_expirationDate());
    }

    static class PayViewHolder extends RecyclerView.ViewHolder {
        static TextView pay_cardnr, pay_expirationDate;

        PayViewHolder( @NonNull final View itemView){
            super(itemView);
            pay_cardnr = itemView.findViewById(R.id.srPaymentCardnr);
            pay_expirationDate= itemView.findViewById(R.id.srPaymentExp);
        }
    }

    @Override
    public int getItemCount() {
        if (PayList == null) {
            return 0;
        }
        return PayList.size();
    }

    public void setTasks(List<Payment> payList) {
        PayList = payList;
        notifyDataSetChanged();
    }
}
