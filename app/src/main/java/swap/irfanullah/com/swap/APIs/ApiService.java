package swap.irfanullah.com.swap.APIs;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import swap.irfanullah.com.swap.Models.Status;

public interface ApiService {
    @POST("swap/public/api/status/compose")
    @FormUrlEncoded
    Call<Status> composeStatus(@Field("token") String token,@Field("status") String status);

    @GET("swap/public/api/status/getStatuses")
    Call<Status> getStatuses(@Query("token") String token);
}
