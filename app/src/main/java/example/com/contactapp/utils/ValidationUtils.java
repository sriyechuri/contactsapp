package example.com.contactapp.utils;

import android.content.Context;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.com.contactapp.R;

/**
 * validates user input
 */
public class ValidationUtils {

    public static boolean emailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePhoneNumber(String phoneNumberValue) {
        //validate phone numbers of format "1234567890"
        if (phoneNumberValue.matches("\\d{10}")) {
            return true;
        }
        //validating phone number with -, . or spaces
        else if (phoneNumberValue.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        }
        //validating phone number with extension length from 3 to 5
        else if (phoneNumberValue.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        }
        //validating phone number where area code is in braces ()
        else if (phoneNumberValue.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
        }
        //return false if nothing matches the input
        else {
            return false;
        }
    }

    public static void handleUserNameFocusLogic(Context context, boolean hasFocus, EditText editText) {
        if (!hasFocus) {
            if (editText.getText().toString().length() > 0 && editText.getText().length() < 5) {
                editText.setError(context.getString(R.string.enter_valid_user_name_message));
            } else {
                editText.setError(null);
            }
        }
    }

    public static void handlePhoneNumberFocusLogic(Context context, boolean hasFocus, EditText editText) {
        if (!hasFocus) {
            if (editText.getText().toString().length() > 0 && !ValidationUtils.validatePhoneNumber(editText.getText().toString())) {
                editText.setError(context.getString(R.string.enter_valid_phone_number_message));
            } else {
                editText.setError(null);
            }
        }
    }

    public static void handleEmailFocusLogic(Context context, boolean hasFocus, EditText editText) {
        if (!hasFocus) {
            if (editText.getText().toString().length() > 0 && !ValidationUtils.emailValid(editText.getText().toString())) {
                editText.setError(context.getString(R.string.enter_valid_email_address_message));
            } else {
                editText.setError(null);
            }
        }
    }

}
