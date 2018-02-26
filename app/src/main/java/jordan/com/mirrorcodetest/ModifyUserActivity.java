package jordan.com.mirrorcodetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jordan on 2/25/2018.
 */

public class ModifyUserActivity extends AppCompatActivity {
    EditText userUsernameEditText;
    EditText userAgeEditText;
    EditText feetEditText;
    EditText inchesEditText;
    CheckBox userLikesJavaScriptCheckBox;
    TextView userMagicNumberTextView;
    TextView userMagicHashTextView;

    private final long NUM_MILISECONDS_IN_A_YEAR = 31540000000L;
    private final float NUM_INCHES_IN_A_CM = 0.393701f;

    private User userDefaultValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);
        userUsernameEditText = (EditText) findViewById(R.id.user_name_edittext);
        userAgeEditText = (EditText) findViewById(R.id.age_edittext);
        feetEditText = (EditText) findViewById(R.id.feet_edittext);
        inchesEditText = (EditText) findViewById(R.id.inches_edittext);
        userMagicNumberTextView = (TextView) findViewById(R.id.magic_number_textview);
        userMagicHashTextView = (TextView) findViewById(R.id.magic_hash_textview);
        userLikesJavaScriptCheckBox = (CheckBox) findViewById(R.id.likes_javascript_checkbox);

        User loggedInUser = UserManager.getInstance().getUser();
        populateFields(loggedInUser);
        userDefaultValues = new User(loggedInUser);

        Button resetToDefaultButton = (Button) findViewById(R.id.reset_to_default_button);
        resetToDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateFields(userDefaultValues);
            }
        });

        Button applyChangesButton = (Button) findViewById(R.id.apply_changes_button);
        applyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Updating current values",Toast.LENGTH_SHORT).show();
                User userChanges = GetUserDataFromFields();
                PatchUserNetworkResponse punr = new PatchUserNetworkResponse();
                NetworkController.getInstance().UpdateActiveUser(userChanges,punr);
            }
        });

        //Refresh the credentials for later
        AuthTokenNetworkResponse atnr = new AuthTokenNetworkResponse(this);
        NetworkController.getInstance().LoginUser(loggedInUser.getUsername(),loggedInUser.getPassword(),atnr);
    }

    private class PatchUserNetworkResponse implements Callback<User> {
        private final String Log_Tag = "PatchUserResponse";
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if(response.isSuccessful()){
                try {
                    Log.d(Log_Tag,"Got user from server");
                    User verifiedUser = response.body();
                    UserManager.getInstance().setUser(verifiedUser);

                    Toast.makeText(getApplicationContext(),"User Successfully Patched",Toast.LENGTH_SHORT).show();
                    populateFields(verifiedUser);
                } catch(Exception e){
                    Log.e(Log_Tag,e.toString());
                }
            } else {
                String error_key = "error";
                String jsonErrorMessage = "";
                try{
                    JSONObject jsonResponse = new JSONObject(response.errorBody().string());
                    jsonErrorMessage = jsonResponse.getString(error_key);
                    Toast.makeText(getApplicationContext(),jsonErrorMessage,Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Log.e(Log_Tag,e.toString());
                }

                int errorCode = response.raw().code();
                String failureMessage = response.raw().message();
                Log.d(Log_Tag, "Failed to create new user: Error Code " + errorCode + " " + failureMessage + " Json Error: " + jsonErrorMessage);
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            Log.e(Log_Tag,"onFailure called for responsebody");
            t.printStackTrace();
        }
    }

    //Pulls data from all editable fields to form a new User Object
    private User GetUserDataFromFields(){
        //Clone the current user
        User updatedUser = new User(UserManager.getInstance().getUser());

        //Update the current user with applicable editable fields
        updatedUser.setUsername(userUsernameEditText.getText().toString());
        updatedUser.setLikes_javascript(userLikesJavaScriptCheckBox.isChecked());

        //Update the age
        Long ageInYears = Long.parseLong(userAgeEditText.getText().toString());
        long ageInmilis = ((long)NUM_MILISECONDS_IN_A_YEAR) * ageInYears;
        updatedUser.setAge(ageInmilis);

        //Update the height
        int numInches = Integer.parseInt(inchesEditText.getText().toString());
        int numFeet = Integer.parseInt(feetEditText.getText().toString());
        int totalInches = (numFeet * 12) + numInches;
        int totalCentimeters = (int)((float)totalInches / NUM_INCHES_IN_A_CM);
        updatedUser.setHeight(totalCentimeters);

        return updatedUser;
    }

    //Populates all the user related fields with data from the passed in user
    private void populateFields(User userToPullDataFrom){
        userUsernameEditText.setText(userToPullDataFrom.getUsername());

        long userAgeInMilis = userToPullDataFrom.getAge();
        //convert user age to years
        long years = userAgeInMilis / NUM_MILISECONDS_IN_A_YEAR;
        userAgeEditText.setText(Long.toString(years));

        int userHeightInCM = userToPullDataFrom.getHeight();
        int inches = (int)(userHeightInCM * NUM_INCHES_IN_A_CM);
        int feet = inches / 12;
        inches = inches % 12;
        feetEditText.setText(Integer.toString(feet));
        inchesEditText.setText(Integer.toString(inches));

        userMagicHashTextView.setText(userToPullDataFrom.getMagic_hash());
        userMagicNumberTextView.setText(String.valueOf(userToPullDataFrom.getMagic_number()));
    }
}
