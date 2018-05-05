package example.com.contactapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import example.com.contactapp.R;
import example.com.contactapp.database.UserContactDatabase;
import example.com.contactapp.entities.UserContact;
import example.com.contactapp.utils.DatabaseInitializer;
import example.com.contactapp.utils.ValidationUtils;

/**
 * UI to perform add and view contacts
 */
public class HomeActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener, TextWatcher {

    private EditText userNameEditText;

    private EditText phoneNumberEditText;

    private EditText emailEditText;

    private Button contactButton;

    private String userNameValue;

    private String phoneNumberValue;

    private String emailValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUserName();
        setupPhoneNumber();
        setupEmailAddress();
        contactButton = findViewById(R.id.add_contact_button);
        Button viewContactsButton = findViewById(R.id.view_contacts_button);
        viewContactsButton.setOnClickListener(this);
    }

    private void setupUserName() {
        userNameEditText = findViewById(R.id.contact_user_name);
        userNameEditText.setOnFocusChangeListener(this);
        userNameEditText.addTextChangedListener(this);
    }

    private void setupPhoneNumber() {
        phoneNumberEditText = findViewById(R.id.contact_phone_number);
        phoneNumberEditText.setOnFocusChangeListener(this);
        phoneNumberEditText.addTextChangedListener(this);
    }

    private void setupEmailAddress() {
        emailEditText = findViewById(R.id.contact_email);
        emailEditText.setOnFocusChangeListener(this);
        emailEditText.addTextChangedListener(this);
    }

    private void setupContactButton() {

        userNameValue = userNameEditText.getText().toString();
        phoneNumberValue = phoneNumberEditText.getText().toString();
        emailValue = emailEditText.getText().toString();

        if (userNameValue.length() >= 5
            && ValidationUtils.validatePhoneNumber(phoneNumberValue)
            && ValidationUtils.emailValid(emailValue)) {
            contactButton.setEnabled(true);
            contactButton.setClickable(true);

            contactButton.setOnClickListener(this);
        } else {
            contactButton.setEnabled(false);
            contactButton.setClickable(false);

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.contact_user_name:
                ValidationUtils.handleUserNameFocusLogic(v.getContext(), hasFocus, userNameEditText);
                break;
            case R.id.contact_phone_number:
                ValidationUtils.handlePhoneNumberFocusLogic(v.getContext(), hasFocus, phoneNumberEditText);
                break;
            case R.id.contact_email:
                ValidationUtils.handleEmailFocusLogic(v.getContext(), hasFocus, emailEditText);
                break;
            default:
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_contact_button:
                UserContact userContact = new UserContact();
                userContact.setUserName(userNameValue);
                userContact.setPhoneNumber(phoneNumberValue);
                userContact.setEmailAddress(emailValue);
                DatabaseInitializer.insertDataIntoDatabase(UserContactDatabase.getUserContactDatabase(HomeActivity.this), userContact);
                Snackbar.make(v, getString(R.string.contact_save), Snackbar.LENGTH_LONG).show();
                clearData();
                break;
            case R.id.view_contacts_button:
                startActivity(new Intent(HomeActivity.this, UserContactsListActivity.class));
                clearData();
                break;
            default:
                break;
        }
    }

    private void clearData() {
        userNameEditText.getText().clear();
        phoneNumberEditText.getText().clear();
        emailEditText.getText().clear();
        userNameEditText.requestFocus();
    }

    @Override
    protected void onDestroy() {
        UserContactDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setupContactButton();
    }
}
