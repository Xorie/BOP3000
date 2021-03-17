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

    // ------ BRUKERPROFIL -----------
    // Finner brukerinfo for brukerprofil
    @Query("Select * from User Where email = (:email_usr)")
    User loadUser(String email_usr);

    // Insert/Oppdatering for navn
//    @Query("Update  User Set firstname = (:fname), lastname = (:sname) Where email = (:email)")
//    User updateName(String email);

    @Update
    void updateName(User user);

    // ------ BRUKERPROFIL SLUTT -----------




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
