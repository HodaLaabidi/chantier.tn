package tn.chantier.chantiertn.activities;

import android.animation.Animator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.Slide;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.resource.UnitTransformation;
import com.dx.dxloadingbutton.lib.AnimationType;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rmiri.buttonloading.ButtonLoading;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.classes.MyApplication;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.models.City;
import tn.chantier.chantiertn.models.Professional;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayEditText;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

import static android.view.View.GONE;

public class ConnexionActivity extends AppCompatActivity {

    @BindView(R.id.inscription_button)
    LinearLayout inscriptionLinkLayout;
    @BindView(R.id.button_connexion)
    LinearLayout connexionButton ;
    @BindView(R.id.et_password_connexion)
    RalewayEditText etPasswordConnexion ;
    @BindView(R.id.et_login_connexion)
    RalewayEditText etLoginConnexion ;
    @BindView(R.id.link_forgotten_password)
    LinearLayout linkForgottenPassword ;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Professional professional ;
    DatabaseReference reference ;
    @BindView(R.id.progress_bar_connexion)
    ProgressBar progressBarConnexion;
    @BindView(R.id.rl_actions)
    RelativeLayout rlActions;
    @BindView(R.id.animate_view)
    View animateView ;
    LoadingButton loadingButtonDXLB ;
    @BindView(R.id.layout_connexion_activity)
    LinearLayout layoutConnexionActivity;
    @BindView(R.id.layout_button)
    LinearLayout layoutButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // bind views with Butter Knife
        ButterKnife.bind(this);
         loadingButtonDXLB = findViewById(R.id.button_loading_dxLB);
        SharedPreferencesFactory.initializedPreferences(ConnexionActivity.this );
        Utils.hideKeyboard((AppCompatActivity) MyApplication.getAppContext());
        loadingButtonDXLB.setCornerRadius(40f);
        loadingButtonDXLB.setResetAfterFailed(true);
        userConnectivity();



        }

    private void userConnectivity() {

        if (SharedPreferencesFactory.retrieveUserEmail() != null){
            etLoginConnexion.setText(SharedPreferencesFactory.retrieveUserEmail());
        }

        if (SharedPreferencesFactory.retrieveUserData() == null) {
            setClickableLyouts();



        } else {
            startHomeActivity();

        }
    }

    private void startHomeActivity() {


        Intent intent = new Intent(ConnexionActivity.this , HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (loadingButtonDXLB.isActivated()){
            loadingButtonDXLB.loadingFailed();
        }
        super.onBackPressed();
    }

    private void setClickableLyouts() {

        // retrive saved email from shared preferences
        if (SharedPreferencesFactory.retrieveUserEmail() != null)
        etLoginConnexion.setText(SharedPreferencesFactory.retrieveUserEmail());


        inscriptionLinkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext() , InscriptionActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loadingButtonDXLB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if ((etLoginConnexion.getText()+"").equalsIgnoreCase("")){


                            new CustomToast(ConnexionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.no_email), R.drawable.ic_erreur, CustomToast.ERROR).show();
                            loadingButtonDXLB.loadingFailed();



                }else if ((etPasswordConnexion.getText()+"").equalsIgnoreCase("")) {



                                new CustomToast(ConnexionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.no_password), R.drawable.ic_erreur, CustomToast.ERROR).show();
                                   loadingButtonDXLB.loadingFailed();



                } else {
                    loadingButtonDXLB.startLoading();
                    connectToPlatform();

                }

            }

        });

        /*connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((etLoginConnexion.getText()+"").equalsIgnoreCase("")){
                    new CustomToast(ConnexionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.no_email), R.drawable.ic_erreur, CustomToast.ERROR).show();
                }else if ((etPasswordConnexion.getText()+"").equalsIgnoreCase("")) {
                    new CustomToast(ConnexionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.no_password), R.drawable.ic_erreur, CustomToast.ERROR).show();

                } else {
                    connectToPlatform();
                }


            }
        });*/

        linkForgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this , ReinitializationPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }






    private void connectToPlatform() {


        final JsonObject postParams = new JsonObject();
        postParams.addProperty("email" , etLoginConnexion.getText()+"");
        Log.e("ConnexionActivity" , etLoginConnexion.getText()+" !");
        postParams.addProperty("password" , etPasswordConnexion.getText()+"");
        Log.e("ConnexionActivity" , etPasswordConnexion.getText()+" !");
        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().connexionChantierService(postParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("ConnexionActivity" , response.code()+" !");
            if (response.code() == 200 || response.code() == 201){

                Gson gson = Utils.getGsonInstance();
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    professional = gson.fromJson(object.toString() , Professional.class);
                    Log.e("ConnexionActivity" , object.toString()+" !");
                    loadingButtonDXLB.loadingSuccessful();
                    loadingButtonDXLB.setAnimationEndAction(new Function1<AnimationType, Unit>() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public Unit invoke(AnimationType animationType) {
                            toNextPage();
                            connectToFirebase();
                            saveCitiesCodes();
                            Log.e("ConnexionActivity" , professional.toString()+ " !");
                            SharedPreferencesFactory.storeUserSession(professional);
                            if (SharedPreferencesFactory.retrieveUserEmail() == null){
                                SharedPreferencesFactory.storeUserEmailForConnexionField(professional.getEmail());
                            }


                            return Unit.INSTANCE;
                        }
                    });





                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    loadingButtonDXLB.loadingFailed();
                    e.printStackTrace();
                    loadingButtonDXLB.loadingFailed();
                }



            }else {
                etPasswordConnexion.setText("");
                loadingButtonDXLB.loadingFailed();
                new CustomToast(ConnexionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.incorrect_email_or_password), R.drawable.ic_erreur, CustomToast.ERROR).show();

            }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                etPasswordConnexion.setText("");
                loadingButtonDXLB.loadingFailed();
                new CustomToast(ConnexionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.technical_error_alert), R.drawable.ic_erreur, CustomToast.ERROR).show();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void toNextPage() {

        int cx = (layoutConnexionActivity.getLeft() + layoutConnexionActivity.getRight()) / 2;
        int cy = (layoutConnexionActivity.getTop() + layoutConnexionActivity.getBottom()) / 2;

        Animator animator = ViewAnimationUtils.createCircularReveal(animateView, cx, cy, 0, getResources().getDisplayMetrics().heightPixels * 3.9f);
        animator.setDuration(3500);
        animator.setInterpolator(new AccelerateInterpolator());
        animateView.setVisibility(View.VISIBLE);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(ConnexionActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                loadingButtonDXLB.reset();
                finish();
                animateView.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onAnimationCancel(Animator animation) {
                loadingButtonDXLB.reset();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void connectToFirebase() {

        auth.signInWithEmailAndPassword(etLoginConnexion.getText()+"", etPasswordConnexion.getText()+"")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            /*Intent intent = new Intent(ConnexionActivity.this , HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();*/
                            Log.e("successful connexion to" , "firebase");
                        } else {
                            saveInFirebase();



                        }
                    }
                });
    }

    private void saveInFirebase() {
        auth = FirebaseAuth.getInstance() ;
        auth.createUserWithEmailAndPassword(etLoginConnexion.getText()+"" , etPasswordConnexion.getText()+"")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNew) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userId = firebaseUser.getUid();
                                reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userId);
                                hashMap.put("username", professional.getRaison_social());
                                hashMap.put("role", "1");
                                hashMap.put("id_plateform" , professional.getId()+"");

                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // get the token for the first connexion , here i will use a webservice to send token to DB

                                            /*Intent intent = new Intent(ConnexionActivity.this, HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();*/
                                            Log.e("successful connexion to" , "firebase");

                                        }
                                    }
                                });
                            }
                        }  else {
                            /*Intent intent = new Intent(ConnexionActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();*/
                            Log.e("failed connexion to" , "firebase");
                        }

                    }
                });

    }

    private void saveCitiesCodes() {
        if (SharedPreferencesFactory.getListOfCodesCities(ConnexionActivity.this) == null){
            Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getCodeCities();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        Log.e("service code cities", response.code()+" !");
                        Gson gson = Utils.getGsonInstance();
                        try {
                            JSONArray jsonArray = new JSONArray(response.body().string());
                            Type type = new TypeToken<ArrayList<City>>(){}.getType();
                            ArrayList<City> listCities = gson.fromJson(jsonArray.toString(), type);
                            Log.e("service code cities", listCities.size()+" !");
                            for( int i = 0 ; i < listCities.size() ; i++){
                                listCities.get(i).setLocalite(listCities.get(i).getLocalite().toLowerCase());

                            }
                            SharedPreferencesFactory.saveListOfCodesCities(ConnexionActivity.this , listCities);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Log.e("service code cities", response.code()+" !");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }



}
