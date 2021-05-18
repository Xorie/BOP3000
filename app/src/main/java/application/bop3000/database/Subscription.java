package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Room DB table for subscription types
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
}
