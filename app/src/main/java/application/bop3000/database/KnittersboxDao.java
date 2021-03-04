package application.bop3000.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface KnittersboxDao {
    //Insert for FAQ, hardkodet i første omgang
    @Insert
    void registerUser(User user);

    @Insert
    void registerSub(Subscription sub);

    @Update
    void updateUser(User user);

    @Query("Select * from FAQ where question = (:qst)")
    List<FAQ> faqliste(String qst);

    @Query("Select * from User where userID = (:userID)")
    User hentBrukerID(int userID);

    @Query("Select * from Subscription")
    List<Subscription> subListe();

    @Query("Select * from Subscription where description = (:subscript)")
    Subscription hentSubID(String subscript);

    @Query("Select * from Subscription where subscriptionID = (:subscriptID)")
    Subscription hentSubDesc(int subscriptID);



}
