package application.bop3000.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Blob;

@Entity(tableName = "Image",
        foreignKeys = @ForeignKey(entity = Post.class,
                parentColumns = "postID",
                childColumns = "post_postID",
                onDelete = ForeignKey.CASCADE))
public class Image implements Serializable {
    @ColumnInfo(name = "imageID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private String imageID;

    @ColumnInfo (name = "location")
    private String location;

    @ColumnInfo (name = "image")
    private Blob image;

    @ColumnInfo (name = "post_postID")
    private String post_postID;
}
