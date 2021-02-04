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
    private int postID;

    @ColumnInfo (name = "post_text")
    private String post_text;

    @ColumnInfo (name = "user_userID")
    private String userID;

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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
