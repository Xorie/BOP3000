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
    private int userID;

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
    private int postnr;

    @ColumnInfo (name = "subscription_subscriptionID")
    private int subscription_subscriptionID;

    @NonNull
    public int getUserID() {
        return userID;
    }

    public void setUserID(@NonNull int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public int getPostnr() {
        return postnr;
    }

    public void setPostnr(int postoffice_postnr) {
        this.postnr = postoffice_postnr;
    }

    public int getSubscription_subscriptionID() {
        return subscription_subscriptionID;
    }

    public void setSubscription_subscriptionID(int subscription_subscriptionID) {
        this.subscription_subscriptionID = subscription_subscriptionID;
    }
}
