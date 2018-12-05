package swap.irfanullah.com.swap.Libraries;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyLib {

    //public VolleyListener volleyListener= null;

    public static void postRequest(final Context context, String URL, final Map<String,String> params, final VolleyListener volleyListener)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             volleyListener.onRecieve(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyListener.onError(error);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(context);
        rq.add(stringRequest);
    }


    public static void getRequest(final Context context, String URL, final Map<String,String> params, final VolleyListener volleyListener)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyListener.onRecieve(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyListener.onError(error);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(context);
        rq.add(stringRequest);
    }


    public interface VolleyListener {
        void onRecieve(String data);
        void onError(VolleyError volleyError);
    }
}
