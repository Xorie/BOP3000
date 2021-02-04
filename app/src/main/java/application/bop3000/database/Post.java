package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Post",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userID",
                childColumns = "user_userID",
                onDelete = ForeignKey.CASCADE))
public class Post implements Serializable {
    @ColumnInfo(name = "postID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private String postID;

    @ColumnInfo (name = "post_text")
    private String post_text;

    @ColumnInfo (name = "user_userID")
    private String userID;
}
