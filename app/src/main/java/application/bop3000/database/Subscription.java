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
    private String subscriptionID;

    @ColumnInfo (name = "type")
    private int type;

    @ColumnInfo (name = "description")
    private String description;
}
