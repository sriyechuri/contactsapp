package example.com.contactapp.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import example.com.contactapp.database.UserContactDatabase;
import example.com.contactapp.entities.UserContact;

/**
 * Acts like a presenter layer separating business logic
 */
public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void insertDataIntoDatabase(@NonNull final UserContactDatabase db, @NonNull UserContact userContact) {
        PopulateDataAsync task = new PopulateDataAsync(db, userContact);
        task.execute();
    }

    public static void updateDataIntoDatabase(@NonNull final UserContactDatabase db, @NonNull UserContact userContact) {
        UpdateDataAsync task = new UpdateDataAsync(db, userContact);
        task.execute();
    }


    private static UserContact addUserContact(final UserContactDatabase db, UserContact user) {
        db.userContactDAO().insertSingleContact(user);
        return user;
    }

    private static UserContact updateUserContact(final UserContactDatabase db, UserContact user) {
        db.userContactDAO().updateSingleContact(user);
        return user;
    }

    public static List<UserContact> retrieveContactList(UserContactDatabase userContactDatabase) {
        return userContactDatabase.userContactDAO().getAllContacts();
    }

    private static class PopulateDataAsync extends AsyncTask<Void, Void, Void> {

        private final UserContactDatabase userContactDatabase;

        private final UserContact userContact;

        PopulateDataAsync(UserContactDatabase userContactDatabase, UserContact userContact) {
            this.userContactDatabase = userContactDatabase;
            this.userContact = userContact;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            addUserContact(userContactDatabase, userContact);
            return null;
        }
    }

    private static class UpdateDataAsync extends AsyncTask<Void, Void, Void> {

        private final UserContactDatabase userContactDatabase;

        private final UserContact userContact;

        UpdateDataAsync(UserContactDatabase userContactDatabase, UserContact userContact) {
            this.userContactDatabase = userContactDatabase;
            this.userContact = userContact;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            updateUserContact(userContactDatabase, userContact);
            return null;
        }
    }
}