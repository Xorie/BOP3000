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

    @Insert
    void registerPostnr(PostOffice postnr);

    @Insert
    void registerFaq(FAQ faq);

    @Update
    void updateUser(User user);

    @Query("Select * from User where userID = (:userID)")
    User hentBrukerID(int userID);

    //FAQ
    @Query("Select * from `APPLICATION/BOP3000/FAQ`")
    List<FAQ> faqList();

    //Subscription
    @Query("Select * from Subscription")
    List<Subscription> subList();

    @Query("Select * from Subscription where description = (:subscript)")
    Subscription hentSubID(String subscript);

    @Query("Select * from Subscription where subscriptionID = (:subscriptID)")
    Subscription hentSubDesc(int subscriptID);

    //PostOffice
    @Query("Select * from PostOffice")
    List<PostOffice> hentPostOffice();

}
