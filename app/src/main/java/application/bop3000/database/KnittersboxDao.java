package application.bop3000.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface KnittersboxDao {
    //Insert for FAQ, hardkodet i første omgang
    @Insert
    void registerUser(User user);

    @Query("Select * from FAQ where question = (:qst)")
    List<FAQ> faqliste(String qst);

    @Insert
    void registerNewPost(Post post);

    @Query("SELECT * FROM Post ORDER BY postID")
    List<Post> loadAllPost();

    @Update
    void updatePost(Post post);

    @Delete
    void delete(Post post);

    @Query("SELECT * FROM Post WHERE postID = :id")
    Post loadPostById(int id);


}
