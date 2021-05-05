package application.bop3000.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Matrix;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Arrays;

@Entity(tableName = "Post",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "userID",
                childColumns = "user_userID",
                onDelete = ForeignKey.CASCADE)
)
public class Post implements Serializable {

    @ColumnInfo(name = "postID")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int postID;

    @ColumnInfo (name = "post_tittle")
    private String post_tittle;

    @ColumnInfo (name = "post_text")
    private String post_text;

    @ColumnInfo (name = "user_userID")
    private String userID;

    @ColumnInfo (name = "post_imagepath")
    private String post_imagepath;

    @ColumnInfo (name = "post_checkbox")
    private int post_checkbox;

    public Post() {

    }

    public Post(String toString) {
    }

    @NonNull
    public int getPostID() {
        return postID;
    }

    public void setPostID(@NonNull int postID) {
        this.postID = postID;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getPost_tittle() {
        return post_tittle;
    }

    public void setPost_tittle(String post_tittle) {
        this.post_tittle = post_tittle;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPost_imagepath() {
        return post_imagepath;
    }

    public void setPost_imagepath(String post_imagepath){this.post_imagepath = post_imagepath;}

    public int getPost_checkbox() {
        return post_checkbox;
    }

    public void setPost_checkbox(int post_checkbox){this.post_checkbox = post_checkbox;}

}
