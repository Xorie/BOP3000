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
    private int imageID;

    @ColumnInfo (name = "location")
    private String location;

    @ColumnInfo (typeAffinity = ColumnInfo.BLOB, name = "image")
    private byte[] image;

    @ColumnInfo (name = "post_postID")
    private String post_postID;

    @NonNull
    public int getImageID() {
        return imageID;
    }

    public void setImageID(@NonNull int imageID) {
        this.imageID = imageID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPost_postID() {
        return post_postID;
    }

    public void setPost_postID(String post_postID) {
        this.post_postID = post_postID;
    }
}
