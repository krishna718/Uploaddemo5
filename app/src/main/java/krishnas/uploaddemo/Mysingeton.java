package krishnas.uploaddemo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Personal on 18-06-2017.
 */
public class Mysingeton {
    private static Mysingeton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private  Mysingeton(Context context)
    {
        mCtx=context;
        requestQueue = getRequestQueue();

    }
    private RequestQueue getRequestQueue()
    {
        if (requestQueue==null)

            requestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());

            return requestQueue;

    }
    public static synchronized Mysingeton getInstance(Context context)
    {
        if (mInstance==null) {

            mInstance = new Mysingeton(context);

        }
        return mInstance;
    }
    public<T> void addToRequestQue(Request<T> request)
    {
        getRequestQueue().add(request);

    }






}
