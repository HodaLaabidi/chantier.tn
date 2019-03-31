package tn.chantier.chantiertn.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crashlytics.android.answers.Answers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeHelper;

import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.classes.MyApplication;
import tn.chantier.chantiertn.activities.classes.WelcomeScreen;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.models.Ads;
import tn.chantier.chantiertn.models.EditableSubCategory;
import tn.chantier.chantiertn.notifications.MyFirebaseMessaging;
import tn.chantier.chantiertn.notifications.Notification;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.UtilsSharefPreferences;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;

public class SplashScreen extends AppCompatActivity{

    private static int SPLASH_TIME_OUT = 5000;
    private static ArrayList<Notification> listNotifications = new ArrayList<>();
    public  static boolean firstInstallation  ;
    public static ArrayList<Ads> listOfAds = new ArrayList<>();



    @BindView(R.id.text_splash_screen)
    LinearLayout textSplashScreen ;
    @BindView(R.id.logo_c_splash_screen)
    ImageView logoCSplashScreen;
    @BindView(R.id.logo_splash_screen)
    ImageView logoSplashScreen;
    Animation upToDown, downToUp , animationText;


    WelcomeHelper welcomeScreen;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers());
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        getAdsListFromBackEnd();
        firstInstallation = UtilsSharefPreferences.getSplashScreenState(getBaseContext());


        // test data payload from firebase console ( data payload + notification )



                if (getIntent().getExtras() != null) {
                    String title = null , content = null , type = null ;


                    for (String key : getIntent().getExtras().keySet()) {


                        Log.e(" test notification", getIntent().getExtras().keySet() + "");
                        if (key.equals("title")) {
                            title = getIntent().getExtras().getString("title");
                            Log.e("title from SS", getIntent().getExtras().getString("title"));
                        }
                        if (key.equals("content")) {
                            content = getIntent().getExtras().getString("content");
                            Log.e("content from SS", getIntent().getExtras().getString("content"));
                        }
                        if (key.equals("type")) {
                            type = getIntent().getExtras().getString("type");
                            Log.e("type from SS", getIntent().getExtras().getString("type"));
                        }



                        if (title != null && content != null && type != null && !Utils.isNotification) {
                            Utils.isNotification = true ;
                            if (MyFirebaseMessaging.date == null){
                                MyFirebaseMessaging.date = System.currentTimeMillis()+"";
                                Utils.isNotificationSaved = false ;
                            }

                            listNotifications = SharedPreferencesFactory.getListOfNotifications(getBaseContext());
                            listNotifications.add(0, new Notification(title, content, MyFirebaseMessaging.date, type));
                            SharedPreferencesFactory.saveNotifications(getBaseContext(), listNotifications);
                            if (! firstInstallation){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent  = new Intent (SplashScreen.this , ConnexionActivity.class);
                                        intent.putExtra("To_Notifications_Fragment","yes");
                                        Log.e("Splash screen log" , "send to next");
                                        startActivity(intent);
                                        finish();


                                    }
                                }, SPLASH_TIME_OUT);


                            }

                        }
                    }
                }



            // save values into sharedpreferences list
            // go to notifications fragment ;)






        animateSplashScreen();
        welcomeScreen = new WelcomeHelper(SplashScreen.this, WelcomeScreen.class);



        if (! firstInstallation){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    startConnexionInterface();


                }
            }, SPLASH_TIME_OUT);


        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    UtilsSharefPreferences.saveSplashScreenState(getBaseContext(), false);
                    welcomeScreen.show(savedInstanceState);



                }
            }, SPLASH_TIME_OUT);

        }





    }



    private void getAdsListFromBackEnd() {
        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getAds();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if ( response.isSuccessful()){
                    Log.e("test array of ads", " response  successful");
                } else {
                    Log.e("test array of ads", " response  failed");
                }
                if ( response.code() == 200){
                    try {
                        //JSONObject jsonObject= null;
                        String remoteResponse= response.body().string();
                        //Log.e("test array of ads", remoteResponse);
                        Gson gson = Utils.getGsonInstance();
                        //JSONObject jsonObject = new JSONObject(remoteResponse);
                        JSONArray jarray = new JSONArray(remoteResponse);
                        for (int i = 0; i < jarray.length(); i++)
                        {
                            JSONObject jsonObj = jarray.getJSONObject(i);

                            Log.e("json object "+i, jsonObj.toString());
                        }
                        Type type = new TypeToken<ArrayList<Ads>>() {
                        }.getType();
                        listOfAds  = gson.fromJson(jarray.toString(), type);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void animateSplashScreen() {

        upToDown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downToUp = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        upToDown.reset();
        downToUp.reset();
        logoSplashScreen.clearAnimation();
        logoCSplashScreen.clearAnimation();
        logoSplashScreen.setAnimation(downToUp);
        logoCSplashScreen.setAnimation(upToDown);
         animationText  = AnimationUtils.loadAnimation(this, R.anim.text_anim);
        animationText.reset();
        textSplashScreen.clearAnimation();
        textSplashScreen.startAnimation(animationText);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
        Log.e("outState",outState+"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WelcomeHelper.DEFAULT_WELCOME_SCREEN_REQUEST) {

            // The key of the welcome screen is in the Intent

             String welcomeKey = data.getStringExtra(WelcomeActivity.WELCOME_SCREEN_KEY);

            if (resultCode == RESULT_OK) {

                // Code here will run if the welcome screen was completed
                UtilsSharefPreferences.saveSplashScreenState(getBaseContext(), false);
                finish();
                startConnexionInterface();





            } else {

                // CstartConnexionInterface();ode here will run if the welcome screen was canceled
                // In most cases you'll want to call finish() here
                finish();
            }


        }
    }

    private void startConnexionInterface(){
        Intent intent = new Intent(this, ConnexionActivity.class);
        startActivity(intent);
        finish();

    }
}
