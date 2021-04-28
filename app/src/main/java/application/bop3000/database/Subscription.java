package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Subscription")
public class Subscription implements Serializable {
    @ColumnInfo(name = "subscriptionID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int subscriptionID;

    @ColumnInfo (name = "type")
    private int type;

    @ColumnInfo (name = "description")
    private String description;

    public Subscription(int subscriptionID, int type, String description) {
        this.subscriptionID = subscriptionID;
        this.type = type;
        this.description = description;
    }

    @NonNull
    public int getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(@NonNull int subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Subscription[] populateSubscriptionData() {
        return new Subscription[] {
                new Subscription(1, 1, "Fargeboksen 349,- /m"),
                new Subscription(2, 1, "Jordboksen 349,- /m")
        };
    }
}


