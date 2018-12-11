package swap.irfanullah.com.swap.APIs;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import swap.irfanullah.com.swap.Models.Followers;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.Swap;
import swap.irfanullah.com.swap.Models.SwapsTab;

public interface ApiService {
    String AFTER_BASE_URL = "swap/public/api/";
    @POST(AFTER_BASE_URL+"status/compose")
    @FormUrlEncoded
    Call<Status> composeStatus(@Field("token") String token,@Field("status") String status);

    @GET(AFTER_BASE_URL+"status/getStatuses")
    Call<Status> getStatuses(@Query("token") String token);

    @GET(AFTER_BASE_URL+"followers/")
    Call<Followers> getFollowers(@Query("token") String token, @Query("status_id") int status_id);

    @GET(AFTER_BASE_URL+"followers/swapStatus")
    Call<Swap> swapStatus(@Query("token") String token, @Query("swaped_with") int swaped_with_user_id,@Query("status_id") int status_id);

    @GET(AFTER_BASE_URL+"followers/deSwapStatus")
    Call<Swap> unSwapStatus(@Query("token") String token, @Query("swaped_with") int swaped_with_user_id,@Query("status_id") int status_id);

    @GET(AFTER_BASE_URL+"swaps/")
    Call<SwapsTab> getSwaps(@Query("token") String token);

    @GET(AFTER_BASE_URL+"status/rateStatus")
    Call<Status> rateStatus(@Query("token") String token,@Query("status_id") int status_id, @Query("rating") float rating);
    @GET(AFTER_BASE_URL+"swaps/unswap")
    Call<Swap> unswap(@Query("token") String token,@Query("swap_id") int swap_id);
}
