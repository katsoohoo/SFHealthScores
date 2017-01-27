package edu.sjsu.cs175.sfhealthscores.retrofit;

import android.util.Log;
import android.widget.Toast;

import edu.sjsu.cs175.sfhealthscores.helpers.Globals;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Custom callback with onFailure implemented.
 *
 * @param <T> generic type
 */
public abstract class RetrofitCallback<T> implements Callback<T> {
    @Override
    public abstract void onResponse(Call<T> call, Response<T> response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Toast.makeText(Globals.APP_CONTEXT, "API Failure", Toast.LENGTH_SHORT).show();
        Log.e("API", "Failure on: " + call, t);
    }
}
