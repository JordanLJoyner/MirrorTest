package jordan.com.mirrorcodetest;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jordan on 2/25/2018.
 */

public class AuthTokenNetworkResponse implements Callback<ResponseBody> {
    private final String Log_Tag = "LoginNetworkResponse";
    private Activity parentActivity = null;

    public AuthTokenNetworkResponse(Activity parentActivity){
        this.parentActivity = parentActivity;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if(response.isSuccessful()){
            try {
                JSONObject json = new JSONObject(response.body().string());
                String token_key = "access_token";
                String access_token = json.getString(token_key);
                NetworkController.getInstance().setAPI_Access_Token(access_token);
                Log.d(Log_Tag,json.getString(token_key));
                Toast.makeText(parentActivity,"Login Successful, Storing auth token",Toast.LENGTH_SHORT).show();
                //TODO: automatically query the server with the auth token to get the user's data
            } catch(Exception e){
                Log.e(Log_Tag,e.toString());
            }

        } else {
            String error_key = "description";
            String jsonErrorMessage = "";
            try{
                JSONObject jsonResponse = new JSONObject(response.errorBody().string());
                jsonErrorMessage = jsonResponse.getString(error_key);
                Toast.makeText(parentActivity,jsonErrorMessage,Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                Log.e(Log_Tag,e.toString());
            }

            int errorCode = response.raw().code();
            String failureMessage = response.raw().message();
            Log.d(Log_Tag, "Failed to authenticate user: Error Code " + errorCode + " " + failureMessage);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e(Log_Tag,"onFailure called for responsebody");
        t.printStackTrace();
    }
}