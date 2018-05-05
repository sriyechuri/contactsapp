package example.com.contactapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import example.com.contactapp.dataAccessObject.UserContactDAO;
import example.com.contactapp.entities.UserContact;

@Database(entities = {UserContact.class}, version = 2)
public abstract class UserContactDatabase extends RoomDatabase {

    private static UserContactDatabase INSTANCE;

    public abstract UserContactDAO userContactDAO();

    public static UserContactDatabase getUserContactDatabase(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                UserContactDatabase.class, "userContact-database")
                // should not be used in production
                .allowMainThreadQueries()
                .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
