
package tn.chantier.chantiertn.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import tn.chantier.chantiertn.utils.textstyle.RalewayEditText;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

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
        etPhoneNumber.setText(SharedPreferencesFactory.retrieveUserData().getGsm());
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


                // required values in all the edittexts contents

                if (
                        (etEmail.getText() + "").equalsIgnoreCase("") || ((etPhoneNumber.getText() + "").equalsIgnoreCase("")) ){

                    new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_mail_phone_number), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;

                } else if (!((etEmail.getText() + "").matches("^[A-Za-z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]{2,6}$"))) {
                    new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_form_email), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;


                }

                      else  if (!((etPhoneNumber.getText() + "").matches("\\d{8}"))) {

                        new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_phone_number), R.drawable.ic_erreur, CustomToast.ERROR).show();
                        return;


                }
                else if ( etCodePostal != null) {
                        if(!((etCodePostal.getText() + "").matches("\\d{4}")) ||
                        ((Integer.parseInt(etCodePostal.getText() + "")) > 9999) ||
                        ((Integer.parseInt(etCodePostal.getText() + "")) < 1000)) {
                            new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_postal_code), R.drawable.ic_erreur, CustomToast.ERROR).show();
                            return;
                        }


                }   else {
                    if (! (ConnectivityService.isOnline(SettingsActivity.this))) {

                        new CustomToast(SettingsActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.verify_internet), R.drawable.warning_icon, CustomToast.WARNING).show();


                    } else {
                        final JsonObject postParams = new JsonObject();
                        postParams.addProperty("nom", etFirstName.getText() + "");
                        Log.e(" test vars", etFirstName.getText() + "");
                        postParams.addProperty("prenom", etLastName.getText() + "");
                        postParams.addProperty("email", etEmail.getText() + "");
                        postParams.addProperty("tel", etPhoneNumber.getText() + "");
                        postParams.addProperty("code_postal", etCodePostal.getText() + "");
                        postParams.addProperty("password", SharedPreferencesFactory.retrieveUserData().getPassword()+ "");
                        postParams.addProperty("confirmPassword", SharedPreferencesFactory.retrieveUserData().getPassword() + "");
                        postParams.addProperty("raison_social", etSociatyName.getText() + "");
                        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().inscriptionChantierService(postParams);


                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {

                                    // edit sharedPreferences values



                                // done !
                                    Log.e("register", postParams.toString() + response.toString());
                                    Toast.makeText(SettingsActivity.this , "Félicitations , votre compte a bien été mis à jour" , Toast.LENGTH_LONG).show();
                                      Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                                      savedEditableValuesInSharedPreferences();
                                      startActivity(intent);
                                      finish();

                                }

                                 else if (response.code() == 500) {
                                    new CustomToast(SettingsActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.technical_error_alert), R.drawable.ic_erreur, CustomToast.ERROR).show();

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
    }

    private void savedEditableValuesInSharedPreferences() {
        // Save the new values into SharedPreferences memory
    }

    ;
}
