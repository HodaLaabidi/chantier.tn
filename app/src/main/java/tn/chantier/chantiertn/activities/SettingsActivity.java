
package tn.chantier.chantiertn.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.models.Professional;
import tn.chantier.chantiertn.utils.ConnectivityService;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayEditText;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

import static tn.chantier.chantiertn.factories.SharedPreferencesFactory.storeUserEmailForConnexionField;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.button_validate_settings)
    LinearLayout validateSettingsButton ;
    @BindView(R.id.et_first_name_settings)
    RalewayEditText etFirstName ;
    @BindView(R.id.et_last_name_settings)
    RalewayEditText etLastName ;
    @BindView(R.id.et_email_settings)
    RalewayEditText etEmail ;
    @BindView(R.id.et_phone_number_settings)
    RalewayEditText etPhoneNumber ;
    @BindView(R.id.et_postal_code_settings)
    RalewayEditText etCodePostal;
    @BindView(R.id.et_sociaty_name_settings)
    RalewayEditText etSociatyName ;
    @BindView(R.id.btn_arrow_back_settings)
    ImageView btnArrowBackSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setPreviousValues();
        settingsProcess();
        btnArrowBackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent ( SettingsActivity.this , HomeActivity.class);
                 startActivity(intent);
                 finish();
            }
        });

    }

    private void setPreviousValues() {

        etFirstName.setText(SharedPreferencesFactory.retrieveUserData().getPrenom()+"");
        etLastName.setText(SharedPreferencesFactory.retrieveUserData().getNom()+"");
        etEmail.setText(SharedPreferencesFactory.retrieveUserData().getEmail());
        etPhoneNumber.setText(SharedPreferencesFactory.retrieveUserData().getTel());
        etCodePostal.setText(SharedPreferencesFactory.retrieveUserData().getCode_postal()+"");
        etSociatyName.setText(SharedPreferencesFactory.retrieveUserData().getRaison_social()+"");





    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent ( SettingsActivity.this , HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void settingsProcess() {

        validateSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Log.e("click settings" , "ok");
                // required values in all the edittexts contents

                if (
                        (etEmail.getText() + "").equalsIgnoreCase("") || ((etPhoneNumber.getText() + "").equalsIgnoreCase("")) ){
                    Log.e("test settings 1" , "ok");
                    new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_mail_phone_number), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;

                } else if (!((etEmail.getText() + "").matches("^[A-Za-z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]{2,6}$"))) {
                    Log.e("test settings 2" , "ok");
                    new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_form_email), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;


                }

                      else  if (!((etPhoneNumber.getText() + "").matches("\\d{8}"))) {
                    Log.e("test settings 3" , "ok");

                        new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_phone_number), R.drawable.ic_erreur, CustomToast.ERROR).show();
                        return;


                }
                else if (!((etCodePostal.getText() + "").matches("\\d{4}")) ||
                        ((Integer.parseInt(etCodePostal.getText() + "")) > 9999) ||
                        ((Integer.parseInt(etCodePostal.getText() + "")) < 1000)) {
                    Log.e("test settings 4" , "ok");
                            new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_postal_code), R.drawable.ic_erreur, CustomToast.ERROR).show();
                            return;
                        }


                else
                    if (! (ConnectivityService.isOnline(SettingsActivity.this))) {
                        Log.e("test settings 5" , "ok");

                        new CustomToast(SettingsActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.verify_internet), R.drawable.warning_icon, CustomToast.WARNING).show();


                    } else {
                        Log.e("test settings 6" , "ok");
                        final JsonObject postParams = new JsonObject();
                        postParams.addProperty("nom", etFirstName.getText() + "");
                        postParams.addProperty("prenom", etLastName.getText() + "");
                        postParams.addProperty("email", etEmail.getText() + "");
                        postParams.addProperty("tel", etPhoneNumber.getText() + "");
                        postParams.addProperty("code_postal", etCodePostal.getText() + "");
                        postParams.addProperty("id_client", SharedPreferencesFactory.retrieveUserData().getId()+"");
                        postParams.addProperty("raison_social", etSociatyName.getText() + "");
                        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().editChantierService(postParams);


                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    Log.e("test settings 7" , "ok");

                                    // edit sharedPreferences values



                                // done !
                                    Log.e("register", postParams.toString() + response.toString());
                                    Toast.makeText(SettingsActivity.this , "Félicitations , votre compte a bien été mis à jour" , Toast.LENGTH_LONG).show();
                                      Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);


                                        Professional professional2 = new Professional();
                                        professional2.setId(SharedPreferencesFactory.retrieveUserData().getId());
                                        professional2.setToken(SharedPreferencesFactory.retrieveUserData().getToken());
                                        professional2.setGsm(SharedPreferencesFactory.retrieveUserData().getGsm());
                                        professional2.setAdresse(SharedPreferencesFactory.retrieveUserData().getAdresse());
                                        professional2.setPartenaire(SharedPreferencesFactory.retrieveUserData().getPartenaire());
                                        professional2.setPack(SharedPreferencesFactory.retrieveUserData().getPack());
                                        professional2.setPassword(SharedPreferencesFactory.retrieveUserData().getPassword());

                                    professional2.setNom(etFirstName.getText() + "");
                                    professional2.setPrenom(etLastName.getText() + "");
                                    professional2.setEmail(etEmail.getText() + "");
                                    professional2.setTel(etPhoneNumber.getText() + "");
                                    professional2.setCode_postal(Integer.parseInt(etCodePostal.getText()+""));
                                    professional2.setId(SharedPreferencesFactory.retrieveUserData().getId());
                                    professional2.setRaison_social(etSociatyName.getText() + "");
                                        Log.e("save user data" , professional2.toString());
                                        savedEditableValuesInSharedPreferences(professional2);


                                        Log.e("save user data after SP" , SharedPreferencesFactory.retrieveUserData().toString());

                                      startActivity(intent);
                                      finish();


                                }

                                 else  {
                                    Log.e("test settings 8" , "ok");
                                    new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.technical_error_alert), R.drawable.ic_erreur, CustomToast.ERROR).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("test settings 9" , "ok");
                                Log.e("Technical error message", t.getMessage() + "");
                                new CustomToast(getBaseContext(), getResources().getString(R.string.error), getResources().getString(R.string.technical_error_alert), R.drawable.ic_erreur, CustomToast.ERROR).show();
                            }
                        });

                    }
                }

        });
    }

    private void savedEditableValuesInSharedPreferences(Professional professional) {
        // Save the new values into SharedPreferences memory
        SharedPreferencesFactory.removeUesrSession();
        SharedPreferencesFactory.storeUserSession(professional);
        SharedPreferencesFactory.removeUserEmail();
        SharedPreferencesFactory.storeUserEmailForConnexionField(professional.getEmail());

    }

    ;
}
