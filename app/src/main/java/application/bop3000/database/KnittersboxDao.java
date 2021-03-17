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




}
