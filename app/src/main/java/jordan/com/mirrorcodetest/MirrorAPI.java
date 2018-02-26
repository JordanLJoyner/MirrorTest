package jordan.com.mirrorcodetest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jordan on 2/25/2018.
 */

public interface MirrorAPI {
    //returns a Json Web Token
    @POST("/auth")
    Call<ResponseBody> getAuthToken(@Header("Content-Type") String contentType,
                                    @Body UserCredentials credentials);

    //Create a new user.  NOTE: No Auth token required
    @POST("/users")
    Call<User> createUser(@Header("Content-Type") String contentType,
                                  @Body UserCredentials credentials);

    @PATCH("/users/{id}")
    Call<User> UpdateUser(@Header("Content-Type") String contentType,
                          @Header("Authorization") String JWT_Token,
                          @Path("id") String userId,
                          @Body User newUserData);
}
