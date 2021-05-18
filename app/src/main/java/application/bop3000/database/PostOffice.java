package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Room DB for Zipcodes and Locations
@Entity(tableName = "PostOffice")
public class PostOffice implements Serializable {
    @ColumnInfo(name = "postnr")
    @PrimaryKey
    @NonNull
    private int postnr;

    @ColumnInfo (name = "post_office")
    private String post_office;

    public PostOffice(int postnr, String post_office) {
        this.postnr = postnr;
        this.post_office = post_office;
    }

    public int getPostnr() {
        return postnr;
    }

    public void setPostnr(int postnr) {
        this.postnr = postnr;
    }

    public String getPost_office() {
        return post_office;
    }

    public void setPost_office(String post_office) {
        this.post_office = post_office;
    }

    public static PostOffice[] populatePostOfficeData() {
        return new PostOffice[] {
                new PostOffice(3510, "Hønefoss"),
                new PostOffice(3511, "Hønefoss"),
                new PostOffice(3512, "Hønefoss"),
                new PostOffice(3513, "Hønefoss"),
                new PostOffice(3514, "Hønefoss"),
                new PostOffice(3515, "Hønefoss"),
        };
    }
}
