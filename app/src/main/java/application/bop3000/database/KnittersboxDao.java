package application.bop3000.database;

import androidx.room.Dao;
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

    //Find registered users
    @Query("SELECT * FROM User WHERE email = (:email) AND password = (:password)")
    User login(String email, String password);

    //Find display name of users
    @Query("SELECT * FROM User WHERE displayname = (:displayname)")
    User displayname(String displayname);

    //Find email of users
    @Query("SELECT * FROM User WHERE email = (:email)")
    User userEmail(String email);

    //Update userID
    @Update
    void updateUserId(User user);

    @Query("UPDATE User SET userID = (:userId) WHERE email = (:email)")
    int updateUserIdQuery(int userId, String email);
}
