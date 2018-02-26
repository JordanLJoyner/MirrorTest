package jordan.com.mirrorcodetest;

import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jordan on 2/25/2018.
 */

public class NetworkController {
    static final String Log_Tag = "GetTokenResponse";
    static final String BASE_URL = "https://mirror-android-test.herokuapp.com/";
    private String API_Access_Token;
    MirrorAPI mMirrorAPI;

    private static NetworkController instance = null;

    protected NetworkController() {
        // Exists only to defeat instantiation.
    }

    public static NetworkController getInstance() {
        if(instance == null) {
            instance = new NetworkController();
        }
        return instance;
    }

    public void start() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mMirrorAPI = retrofit.create(MirrorAPI.class);

    }
    public void LoginUser(String username, String password, Callback<ResponseBody> callback){
        UserCredentials creds = new UserCredentials();
        creds.setUsername(username);
        creds.setPassword(password);
        Call<ResponseBody> getAuthTokenNetworkCall = mMirrorAPI.getAuthToken("application/json", creds);
        getAuthTokenNetworkCall.enqueue(callback);
    }

    public void CreateUser(String username, String password, Callback<User> callbacks){
        UserCredentials creds = new UserCredentials();
        creds.setUsername(username);
        creds.setPassword(password);
        Call<User> createUserNetworkCall = mMirrorAPI.createUser("application/json", creds);
        createUserNetworkCall.enqueue(callbacks);
    }

    public void UpdateActiveUser(User newUserData, Callback<User> callback){
        Call<User> createUserNetworkCall = mMirrorAPI.UpdateUser("application/json",
                "JWT " + getAPI_Access_Token(),
                Integer.toString(UserManager.getInstance().getUser().getId()),
                newUserData);
        createUserNetworkCall.enqueue(callback);
    }

    public String getAPI_Access_Token() {
        return API_Access_Token;
    }

    public void setAPI_Access_Token(String API_Access_Token) {
        this.API_Access_Token = API_Access_Token;
    }

    public void GetAuthToken(UserCredentials creds){


    }
}
