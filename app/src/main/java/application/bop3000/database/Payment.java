package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Payment",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userID",
                childColumns = "user_userID",
                onDelete = ForeignKey.CASCADE)
)

public class Payment implements Serializable {
    @ColumnInfo(name = "payID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int payID;

    @ColumnInfo (name = "payment_cardnr")
    private String payment_cardnr;

    @ColumnInfo (name = "payment_expirationDate")
    private String payment_expirationDate;

    @ColumnInfo (name = "user_userID")
    private String userID;

    public int getPayID() {
        return payID;
    }

    public void setPayID(int payID) {
        this.payID = payID;
    }

    public String getPayment_cardnr() {
        return payment_cardnr;
    }

    public void setPayment_cardnr(String payment_cardnr) {
        this.payment_cardnr = payment_cardnr;
    }

    public String getPayment_expirationDate() {
        return payment_expirationDate;
    }

    public void setPayment_expirationDate(String payment_expirationDate) {
        this.payment_expirationDate = payment_expirationDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
