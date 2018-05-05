package example.com.contactapp.dataAccessObject;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import example.com.contactapp.entities.UserContact;

@Dao
public interface UserContactDAO {

    @Query("SELECT * from contactInfo")
    List<UserContact> getAllContacts();

    @Query("SELECT * FROM contactInfo WHERE user_name = :userName LIMIT 1")
    UserContact findUserByName(String userName);

    @Insert
    void insertSingleContact(UserContact userContact);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateSingleContact(UserContact userContact);

    @Delete
    void deleteSingleContact(UserContact userContact);
}
