package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "FAQ")
public class FAQ implements Serializable {
    @ColumnInfo(name = "faqID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private String faqID;

    @ColumnInfo (name = "question")
    private int question;

    @ColumnInfo (name = "answer")
    private String answer;
}
