package example.com.contactapp.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.contactapp.R;
import example.com.contactapp.adapter.UserContactListAdapter;
import example.com.contactapp.database.UserContactDatabase;
import example.com.contactapp.entities.UserContact;
import example.com.contactapp.utils.SwipeController;
import example.com.contactapp.utils.SwipeControllerActions;

/**
 * Displays list of contacts in card view. If there are no contacts, displays empty text
 */
public class UserContactsListActivity extends AppCompatActivity {

    private static RecyclerView contactListRecyclerView;

    private static UserContactListAdapter userContactListAdapter;

    private static List<UserContact> userContactList = new ArrayList<UserContact>();

    private static TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_contact_list);

    }

    @Override
    protected void onResume() {
        super.onResume();

        contactListRecyclerView = findViewById(R.id.contacts_list_recycler_view);
        emptyTextView = findViewById(R.id.empty_view);

        final UserContactDatabase userContactDatabase = UserContactDatabase.getUserContactDatabase(UserContactsListActivity.this);
        new RetrieveContactsAsyncTask(userContactDatabase).execute();

        userContactListAdapter = new UserContactListAdapter(userContactList);
        contactListRecyclerView.setAdapter(userContactListAdapter);

        if (userContactList == null || userContactList.isEmpty()) {
            contactListRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            contactListRecyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }

        setupSwipeController(contactListRecyclerView, userContactDatabase);
    }

    private void setupSwipeController(final RecyclerView contactListRecyclerView, final UserContactDatabase userContactDatabase) {
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onDeleteClicked(View view, int position) {
                new DeleteDataAsyncTask(userContactDatabase, userContactListAdapter.userContactList.get(position)).execute();
                userContactListAdapter.userContactList.remove(position);
                userContactListAdapter.notifyItemRemoved(position);
                userContactListAdapter.notifyItemRangeChanged(position, userContactListAdapter.getItemCount());
                updateUI();
                Snackbar.make(view, getString(R.string.contact_delete), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onEditClicked(View view, int position) {
                Intent intent = new Intent(UserContactsListActivity.this, EditContactActivity.class);
                UserContact userContact = userContactListAdapter.userContactList.get(position);
                String userName = userContact.getUserName();
                String phoneNumber = userContact.getPhoneNumber();
                String emailId = userContact.getEmailAddress();
                intent.putExtra(EditContactActivity.USER_NAME, userName);
                intent.putExtra(EditContactActivity.PHONE_NUMBER, phoneNumber);
                intent.putExtra(EditContactActivity.EMAIL_ADDRESS, emailId);
                startActivity(intent);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(contactListRecyclerView);

        contactListRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private static void updateUI() {
        if (userContactList == null || userContactList.isEmpty()) {
            contactListRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            contactListRecyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        }
    }

    private static class RetrieveContactsAsyncTask extends AsyncTask<Void, Void, List<UserContact>> {

        private UserContactDatabase userContactDatabase;

        RetrieveContactsAsyncTask(UserContactDatabase userContactDatabase) {
            this.userContactDatabase = userContactDatabase;
        }

        @Override
        protected List<UserContact> doInBackground(Void... params) {
            return userContactDatabase.userContactDAO().getAllContacts();
        }

        @Override
        protected void onPostExecute(List<UserContact> contactList) {
            userContactList = contactList;
            userContactListAdapter = new UserContactListAdapter(userContactList);
            contactListRecyclerView.setAdapter(userContactListAdapter);

            updateUI();
        }
    }

    private static class DeleteDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserContactDatabase userContactDatabase;

        private UserContact userContact;

        DeleteDataAsyncTask(UserContactDatabase userContactDatabase, UserContact userContact) {
            this.userContactDatabase = userContactDatabase;
            this.userContact = userContact;
        }

        @Override
        protected Void doInBackground(Void... params) {
            userContactDatabase.userContactDAO().deleteSingleContact(userContact);
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        UserContactDatabase.destroyInstance();
        super.onDestroy();
    }
}
