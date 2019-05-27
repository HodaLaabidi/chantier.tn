package tn.chantier.chantiertn.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.classes.MyApplication;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.models.Professional;
import tn.chantier.chantiertn.utils.ConnectivityService;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayEditText;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

import static tn.chantier.chantiertn.utils.Utils.CONFIDENTIALITY_POLITICS_URL;

public class InscriptionActivity extends AppCompatActivity {

    public static final String TAG_ACTIVITY = "INSCRIPTION_ACTIVITY";

    FirebaseAuth auth ;
    DatabaseReference reference ;
    @BindView(R.id.button_inscription)
    LinearLayout inscriptionButton ;
    @BindView(R.id.et_first_name)
    RalewayEditText etFirstName ;
    @BindView(R.id.et_last_name)
    RalewayEditText etLastName ;
    @BindView(R.id.et_email)
    RalewayEditText etEmail ;
    @BindView(R.id.et_phone_number)
    RalewayEditText etPhoneNumber ;
    @BindView(R.id.et_postal_code)
    RalewayEditText etCodePostal;
    @BindView(R.id.et_password)
    RalewayEditText etPassword ;
    @BindView(R.id.et_second_password)
    RalewayEditText etSecondPassword ;
    @BindView(R.id.et_sociaty_name)
    RalewayEditText etSociatyName ;
    @BindView(R.id.ic_arrow_back_inscription)
    LinearLayout arrowBackInscription;
    @BindView(R.id.link_confidentiality_politics)
    LinearLayout linkConfidentialityPolitics;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( InscriptionActivity.this , ConnexionActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // bind layouts with controllers

        ButterKnife.bind(this);

        // Confidentiality politics

        linkConfidentialityPolitics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(CONFIDENTIALITY_POLITICS_URL));
                startActivity(intent);
            }
        });
        arrowBackInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InscriptionActivity.this , ConnexionActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Utils.hideKeyboard((AppCompatActivity) MyApplication.getAppContext());
        // just for test to avoid executing the service of inscription
        inscriptionProcess();
       // noServiceJustForTest();
    }
    // plz don't forget just for test -_-

    private void noServiceJustForTest() {
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CategoriesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void inscriptionProcess() {

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // required values in all the edittexts contents

                if ((etFirstName.getText() + "").equalsIgnoreCase("") || ((etLastName.getText() + "")).equalsIgnoreCase("")
                        || (etEmail.getText() + "").equalsIgnoreCase("") || ((etPhoneNumber.getText() + "").equalsIgnoreCase(""))
                        || ((etCodePostal.getText() + "").equalsIgnoreCase("")) || ((etSociatyName.getText() + "").equalsIgnoreCase(""))
                        || ((etPassword.getText() + "").equalsIgnoreCase("")) || ((etSecondPassword.getText() + "").equalsIgnoreCase(""))) {

                    new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_info_login), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;

                } else if (!((etEmail.getText() + "").matches("^[A-Za-z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]{2,6}$"))) {
                    new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_form_email), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;


                } else if (!((etPhoneNumber.getText() + "").matches("\\d{8}"))) {
                    new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_phone_number), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;

                }
                 else if (!((etCodePostal.getText() + "").matches("\\d{4}")) ||
                        ((Integer.parseInt(etCodePostal.getText() + "")) > 9999) ||
                        ((Integer.parseInt(etCodePostal.getText() + "")) < 1000)) {
                    new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_postal_code), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;


                }
                else if (!(etPassword.getText() + "").matches("[\\W\\w]{6,}")) {
                    new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_length_password), R.drawable.ic_erreur, CustomToast.ERROR).show();


                } else if (!((etPassword.getText() + "").matches(etSecondPassword.getText() + ""))) {
                    new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_confirmed_password), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;

                }  else {
                    if (! (ConnectivityService.isOnline(InscriptionActivity.this))) {

                        new CustomToast(InscriptionActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.verify_internet), R.drawable.warning_icon, CustomToast.WARNING).show();


                    } else {
                        final JsonObject postParams = new JsonObject();
                        postParams.addProperty("nom", etFirstName.getText() + "");
                        Log.e(" test vars", etFirstName.getText() + "");
                        postParams.addProperty("prenom", etLastName.getText() + "");
                        postParams.addProperty("email", etEmail.getText() + "");
                        postParams.addProperty("tel", etPhoneNumber.getText() + "");
                        postParams.addProperty("code_postal", etCodePostal.getText() + "");
                        postParams.addProperty("password", etPassword.getText() + "");
                        postParams.addProperty("confirmPassword", etPassword.getText() + "");
                        postParams.addProperty("raison_social", etSociatyName.getText() + "");
                        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().inscriptionChantierService(postParams);



                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                String  remoteResponse = null;

                                try {
                                    remoteResponse = response.body().string();
                                if (response.code() == 200) {

                                // done !
                                        Log.e("register", remoteResponse);
                                    Gson gson = Utils.getGsonInstance();
                                    JSONObject object = new JSONObject(response.body().string());
                                    String idClient = object.getString("id_client");
                                        Intent intent = new Intent(InscriptionActivity.this, CategoriesActivity.class);
                                        intent.putExtra("id_new_client" , idClient);
                                        startActivity(intent);
                                        finish();



                                } else if (response.code() == 201) {
                                    //  existing user
                                    int typeOfError = 0;
                                    Log.e("typeOfError = 0" , remoteResponse+"");
                                    try {

                                        JSONObject object = new JSONObject(remoteResponse);
                                        typeOfError = object.getInt("existant");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (typeOfError == 2) {
                                        new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.existing_user_email), R.drawable.ic_erreur, CustomToast.ERROR).show();


                                    } else if (typeOfError == 1) {
                                        new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.existing_user_phone_number), R.drawable.ic_erreur, CustomToast.ERROR).show();


                                    } else {
                                        new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.technical_error_alert), R.drawable.ic_erreur, CustomToast.ERROR).show();
                                    }
                                } else if (response.code() == 500) {
                                    new CustomToast(InscriptionActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.technical_error_alert), R.drawable.ic_erreur, CustomToast.ERROR).show();

                                }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                Log.e("Technical error message", t.getMessage() + "");
                                new CustomToast(getBaseContext(), getResources().getString(R.string.error), getResources().getString(R.string.technical_error_alert), R.drawable.ic_erreur, CustomToast.ERROR).show();
                            }
                        });

                       }
                     }
                   }
                });
            };
        }