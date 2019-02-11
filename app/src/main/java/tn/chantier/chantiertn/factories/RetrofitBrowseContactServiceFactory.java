package tn.chantier.chantiertn.factories;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import tn.chantier.chantiertn.services.ChantierServices;
import tn.chantier.chantiertn.services.FirebaseAPIService;
import tn.chantier.chantiertn.utils.Utils;

public class RetrofitBrowseContactServiceFactory {

    static OnConnectionTimeoutListener onConnectionTimeOutListener;
    private static ChantierServices chantierService;
    private static FirebaseAPIService firebaseAPIService ;



    public static Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            Response response = chain.proceed(chain.request());
            String content = response.body().toString();
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();

        } catch (SocketTimeoutException exception) {
            exception.printStackTrace();
            if (onConnectionTimeOutListener != null)
                onConnectionTimeOutListener.onConnectionTimeout();

        }
        return chain.proceed(chain.request());
    }


    public static ChantierServices getChantierService() {

        if (chantierService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ChantierServices.DATA_ENDPOINT)
                    .client(new OkHttpClient())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
            chantierService = retrofit.create(ChantierServices.class);

        }
        return chantierService;
    }


    public interface OnConnectionTimeoutListener {
        void onConnectionTimeout();
    }

    public static FirebaseAPIService getFirebaseAPIService() {

        if (firebaseAPIService == null) {
            OkHttpClient client = new OkHttpClient();

            client.newBuilder().connectTimeout(Utils.TIMEOUT, TimeUnit.SECONDS);
            client.newBuilder().readTimeout(Utils.TIMEOUT, TimeUnit.SECONDS);
            client.newBuilder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return onOnIntercept(chain);
                }
            }).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FirebaseAPIService.FIREBASE_DATA_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            firebaseAPIService = retrofit.create(FirebaseAPIService.class);

        }
        return firebaseAPIService;
    }


}
