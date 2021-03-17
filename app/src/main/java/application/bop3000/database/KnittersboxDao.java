package application.bop3000.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface KnittersboxDao {
    //Insert for FAQ, hardkodet i første omgang
    @Insert
    void registerUser(User user);

    @Query("Select * from FAQ where question = (:qst)")
    List<FAQ> faqliste(String qst);

    //intert for Post
    @Insert
    void insertNewPost(Post post);

    //for å hente alle postene
    @Query("SELECT * FROM Post ORDER BY postID DESC")
    List<Post> loadAllPost();

    //for å hente payment_method for en bruker
    @Query("SELECT * FROM Payment WHERE user_userID = (:user)")
    List<Payment> loadAllPay(String user);

    @Insert
    void insertNewPayment(Payment pay);

    /*
    @Query("DELETE FROM User WHERE uid = :id")
    void deleteById(String id);
*/
}
