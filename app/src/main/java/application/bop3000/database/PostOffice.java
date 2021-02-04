package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "PostOffice")
public class PostOffice implements Serializable {
    @ColumnInfo(name = "postnr")
    @PrimaryKey
    @NonNull
    private int postnr;

    @ColumnInfo (name = "post_office")
    private String post_office;
}
