package application.bop3000.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface KnittersboxDao {
    //Insert for FAQ, hardkodet i første omgang
    @Insert
    void registerUser(User user);

    @Insert
    void registerSub(Subscription sub);

    @Query("Select * from FAQ where question = (:qst)")
    List<FAQ> faqliste(String qst);

    @Query("Select * from Subscription")
    List<Subscription> subListe();

    @Query("Select * from Subscription where description = (:subscript)")
    Subscription hentSubID(String subscript);

}
