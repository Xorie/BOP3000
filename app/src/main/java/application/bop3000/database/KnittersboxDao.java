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

    @Insert
    void registerSub(Subscription sub);

    @Insert
    void registerPostnr(PostOffice postnr);

    //INITAL DATA
    @Insert
    void registerFaq(FAQ... faq);
    @Insert
    void registerSubscription(Subscription... subscription);
    @Insert
    void registerPostOffice(PostOffice... postOffice);


    @Update
    void updateUser(User user);

    // ------ BRUKERPROFIL -----------
    // Finner brukerinfo for brukerprofil
    @Query("Select * from User Where email = (:email_usr)")
    User loadUser(String email_usr);

    @Update
    void updateName(User user);

    //Oppdaterer brukerinfo i UserSettings
    @Query("UPDATE User SET displayname = (:username), firstname = (:firstname), lastname = (:lastname), email = (:emailnew) WHERE email = (:email)")
    void updateUserInfo(String username, String firstname, String lastname, String emailnew, String email);
    // ------ BRUKERPROFIL SLUTT -----------




    @Query("Select * from User where userID = (:userID)")
    User hentBrukerID(int userID);

    //FAQ
    @Query("Select * from FAQ")
    List<FAQ> faqList();

    //For initialDataInput
    @Query("Select COUNT(*) from FAQ")
    int count();

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

    //intert for Post
    @Insert
    void insertNewPost(Post post);

    //for å hente alle postene
    @Query("SELECT * FROM Post ORDER BY postID DESC")
    List<Post> loadAllPost();


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
