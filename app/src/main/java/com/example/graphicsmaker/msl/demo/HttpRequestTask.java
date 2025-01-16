package com.example.graphicsmaker.msl.demo;

//import android.os.AsyncTask;
//import android.util.Log;
//
//import androidx.recyclerview.widget.ItemTouchHelper.Callback;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpGetHC4;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//
//public class HttpRequestTask extends AsyncTask<String, String, Void> {
//    private RequestListener mRequestListener;
//    String response = null;
//
//    public interface RequestListener {
//        void onError();
//
//        void onResponse(String str);
//    }
//
//    public HttpRequestTask(RequestListener requestListener) {
//        this.mRequestListener = requestListener;
//    }
//
//    protected Void doInBackground(String... params) {
//        try {
//
//            String url = params[0];
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpGet httpGet = new HttpGet(url);
//            httpGet.addHeader("application-id", "");
//            httpGet.addHeader("secret-key", "");
//            httpGet.addHeader("application-type", "REST");
//            HttpResponse httpResponse = httpClient.execute(httpGet);
//            if (httpResponse.getStatusLine().getStatusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
//                this.response = EntityUtils.toString(httpResponse.getEntity());
//                Log.i("Interstitial response", this.response);
//            } else {
//                Log.i("Interstitial response", "Failed to get response");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);
//        if (this.response == null) {
//            this.mRequestListener.onError();
//        } else {
//            this.mRequestListener.onResponse(this.response);
//        }
//    }
//}
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequestTask {
    private RequestListener mRequestListener;
    private final OkHttpClient client = new OkHttpClient();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface RequestListener {
        void onError();
        void onResponse(String response);
    }

    public HttpRequestTask(RequestListener requestListener) {
        this.mRequestListener = requestListener;
    }

    public void execute(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("application-id", "")
                .addHeader("secret-key", "")
                .addHeader("application-type", "REST")
                .build();

        executorService.execute(() -> client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpRequestTask", "Request failed", e);
                mainHandler.post(() -> mRequestListener.onError());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    Log.i("HttpRequestTask", "Response: " + responseBody);
                    mainHandler.post(() -> mRequestListener.onResponse(responseBody));
                } else {
                    Log.i("HttpRequestTask", "Failed to get a successful response");
                    mainHandler.post(() -> mRequestListener.onError());
                }
            }
        }));
    }
}
