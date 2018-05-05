package example.com.contactapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import example.com.contactapp.R;
import example.com.contactapp.entities.UserContact;

public class UserContactListAdapter extends RecyclerView.Adapter<UserContactListAdapter.UserContactViewHolder> {

    public List<UserContact> userContactList;

    public UserContactListAdapter(List<UserContact> userContactList) {
        this.userContactList = userContactList;
    }

    @NonNull
    @Override
    public UserContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_contact_list_item, parent, false);
        return new UserContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserContactViewHolder userContactViewHolder, int position) {
        userContactViewHolder.firstNameListItemTv.setText(userContactList.get(position).getUserName());
        userContactViewHolder.lastNameListItemTv.setText(userContactList.get(position).getPhoneNumber());
        userContactViewHolder.emailListItemTv.setText(userContactList.get(position).getEmailAddress());

    }

    @Override
    public int getItemCount() {
        return userContactList.size();
    }

    public static class UserContactViewHolder extends RecyclerView.ViewHolder {

        private CardView contactCardView;

        private TextView firstNameListItemTv;

        private TextView lastNameListItemTv;

        private TextView emailListItemTv;

        private TextView phoneListItemTv;

        UserContactViewHolder(View itemView) {
            super(itemView);
            contactCardView = itemView.findViewById(R.id.contact_card_view);
            firstNameListItemTv = itemView.findViewById(R.id.contact_list_first_name);
            lastNameListItemTv = itemView.findViewById(R.id.contact_list_last_name);
            emailListItemTv = itemView.findViewById(R.id.contact_email);
            phoneListItemTv = itemView.findViewById(R.id.contact_phone_number);
        }
    }
}
