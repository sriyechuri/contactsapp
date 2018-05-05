package example.com.contactapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import example.com.contactapp.R;
import example.com.contactapp.dataAccessObject.UserContactDAO;
import example.com.contactapp.database.UserContactDatabase;
import example.com.contactapp.entities.UserContact;
import example.com.contactapp.utils.DatabaseInitializer;
import example.com.contactapp.utils.ValidationUtils;

/**
 * Edits existing contact
 */
public class EditContactActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    public static final String USER_NAME = "username";

    public static final String PHONE_NUMBER = "phoneNumber";

    public static final String EMAIL_ADDRESS = "emailAddress";

    private String originalUserNameValue;

    private String originalPhoneNumberValue;

    private String originalEmailAddressValue;

    private String newUserNameValue;

    private String newPhoneNumberValue;

    private String newEmailAddressValue;

    private Button saveButton;

    private EditText userNameEditText;

    private EditText phoneNumberEditText;

    private EditText emailEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_contact);

        Intent intent = getIntent();
        if (intent != null) {
            originalUserNameValue = intent.getStringExtra(USER_NAME);
            originalPhoneNumberValue = intent.getStringExtra(PHONE_NUMBER);
            originalEmailAddressValue = intent.getStringExtra(EMAIL_ADDRESS);
        } else {
            return;
        }

        setupUserName();
        setupPhoneNumber();
        setupEmail();

        saveButton = findViewById(R.id.save_button);
        setupSaveContactButton();
    }

    private void setupUserName() {
        userNameEditText = findViewById(R.id.contact_user_name);
        userNameEditText.setText(originalUserNameValue);
        userNameEditText.setOnFocusChangeListener(this);
        userNameEditText.addTextChangedListener(this);
    }

    private void setupPhoneNumber() {
        phoneNumberEditText = findViewById(R.id.contact_phone_number);
        phoneNumberEditText.setText(originalPhoneNumberValue);
        phoneNumberEditText.setOnFocusChangeListener(this);
        phoneNumberEditText.addTextChangedListener(this);
    }

    private void setupEmail() {
        emailEditText = findViewById(R.id.contact_email);
        emailEditText.setText(originalEmailAddressValue);
        emailEditText.setOnFocusChangeListener(this);
        emailEditText.addTextChangedListener(this);
    }

    private void setupSaveContactButton() {

        newUserNameValue = userNameEditText.getText().toString();
        newPhoneNumberValue = phoneNumberEditText.getText().toString();
        newEmailAddressValue = emailEditText.getText().toString();

        if (newUserNameValue.length() >= 5
            && ValidationUtils.validatePhoneNumber(newPhoneNumberValue)
            && ValidationUtils.emailValid(newEmailAddressValue)) {
            saveButton.setEnabled(true);
            saveButton.setClickable(true);

            saveButton.setOnClickListener(this);
        } else {
            saveButton.setEnabled(false);
            saveButton.setClickable(false);

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

        UserContactDAO userContactDAO = UserContactDatabase.getUserContactDatabase(EditContactActivity.this).userContactDAO();
        // clicked on item row -> update
        UserContact userContact = userContactDAO.findUserByName(originalUserNameValue);
        boolean userNameChanged = false, phoneNumberChanged = false, emailAddressChanged = false;
        if (userContact != null) {
            if (!userContact.userName.equals(newUserNameValue)) {
                userContact.userName = newUserNameValue;
                userNameChanged = true;
            }

            if (!userContact.phoneNumber.equals(newPhoneNumberValue)) {
                userContact.phoneNumber = newPhoneNumberValue;
                phoneNumberChanged = true;
            }

            if (!userContact.emailAddress.equals(newEmailAddressValue)) {
                userContact.emailAddress = newEmailAddressValue;
                emailAddressChanged = true;
            }

            if (userNameChanged || phoneNumberChanged || emailAddressChanged) {
                DatabaseInitializer
                    .updateDataIntoDatabase(UserContactDatabase.getUserContactDatabase(EditContactActivity.this), userContact);
                Snackbar.make(v, getString(R.string.contact_update), Snackbar.LENGTH_SHORT).show();
                finish();
            } else {
                Snackbar.make(v, getString(R.string.contact_details_no_change), Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(v, getString(R.string.contact_details_update_error), Snackbar.LENGTH_SHORT).show();
        }
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
        setupSaveContactButton();
    }
}
