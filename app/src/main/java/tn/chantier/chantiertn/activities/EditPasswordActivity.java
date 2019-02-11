package tn.chantier.chantiertn.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.utils.ConnectivityService;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayEditText;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

public class EditPasswordActivity extends AppCompatActivity {

    private static final int RSET_PASSWORD_TIME_OUT = 2000;
    @BindView(R.id.et_reset_password)
    RalewayEditText etResetPassword ;
    @BindView(R.id.et_confirm_password)
    RalewayEditText etConfirmPassword ;
    @BindView(R.id.change__password_button)
    LinearLayout changePasswordButton ;
    RalewayTextView textCongratulations ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ButterKnife.bind(this );

        sendRequestChangePassword();
    }

    private void sendRequestChangePassword() {


        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(etResetPassword.getText() + "").matches("[\\W\\w]{6,}")) {
                    new CustomToast(EditPasswordActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_length_password), R.drawable.ic_erreur, CustomToast.ERROR).show();


                } else if  (!((etResetPassword.getText() + "").matches(etConfirmPassword.getText() + ""))) {
                    new CustomToast(EditPasswordActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_confirmed_password), R.drawable.ic_erreur, CustomToast.ERROR).show();
                    return;

                } else if (! (ConnectivityService.isOnline(EditPasswordActivity.this))) {

                            new CustomToast(EditPasswordActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.verify_internet), R.drawable.warning_icon, CustomToast.WARNING).show();


                        } else {

                    if ( Utils.resetEmail != ""){
                        // send web service
                        JsonObject postParams = new JsonObject();
                        postParams.addProperty("mail",Utils.resetEmail);
                        postParams.addProperty("password",etResetPassword.getText() + "");
                        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().setNewPassword(postParams);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.code() == 200) {
                                    // edit sharedPreferences user values


                                    final LayoutInflater inflater = LayoutInflater.from(EditPasswordActivity.this);
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(EditPasswordActivity.this, R.style.AppCompatAlertDialogStyleSuggest);
                                    final View customDialog = inflater.inflate(R.layout.pop_up_reset_password, null, false);
                                    final AlertDialog alertDialog = builder.create();
                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    alertDialog.setView(customDialog);
                                    textCongratulations = customDialog.findViewById(R.id.text_congratulations);
                                    textCongratulations.setText("Votre mot de passe à bien été mis à jour");
                                    alertDialog.show();

                                    LinearLayout goToConnexionButton = customDialog.findViewById(R.id.btn_go_to_connexion);
                                    goToConnexionButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();

                                            Intent intent = new Intent(EditPasswordActivity.this, ConnexionActivity.class);
                                            startActivity(intent);
                                            finish();


                                        }
                                    });

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            alertDialog.dismiss();


                                        }
                                    }, RSET_PASSWORD_TIME_OUT);


                                } else {
                                    // show toast


                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });


                    }


                        }


            }
        });
    }

    @Override
    protected void onDestroy() {
        Utils.resetEmail = "";
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent( EditPasswordActivity.this , ReinitializationPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}
