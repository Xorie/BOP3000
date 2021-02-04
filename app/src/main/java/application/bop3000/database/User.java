package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "User",
        foreignKeys = {
                @ForeignKey(entity = PostOffice.class,
                        parentColumns = "postnr",
                        childColumns = "postoffice_postnr"),
                @ForeignKey(entity = Subscription.class,
                        parentColumns = "subscriptionID",
                        childColumns = "subscription_subscriptionID",
                        onDelete = ForeignKey.CASCADE)
        })
public class User implements Serializable {
    @ColumnInfo(name = "userID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private String userID;

    @ColumnInfo (name = "email")
    private String email;

    @ColumnInfo (name = "password")
    private String password;

    @ColumnInfo (name = "firstname")
    private String firstname;

    @ColumnInfo (name = "lastname")
    private String lastname;

    @ColumnInfo (name = "city")
    private String city;

    @ColumnInfo (name = "streetname")
    private String streetname;

    @ColumnInfo (name = "displayname")
    private String displayname;

    @ColumnInfo (name = "postoffice_postnr")
    private String postnr;
}
