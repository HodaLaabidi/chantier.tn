package tn.chantier.chantiertn.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayEditText;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

public class ReinitializationPasswordActivity extends AppCompatActivity {

    @BindView(R.id.create_account_from_forgottenPW_layout)
    LinearLayout createAccountFromForgottenPWLayout;
    @BindView(R.id.validate_button_reset)
    LinearLayout validateButtonReset;
    @BindView(R.id.et_email_connexion_reset)
    RalewayEditText etMailConnexionReset;
    Dialog dialog;
    @BindView(R.id.ic_arrow_back_connexion)
    LinearLayout arrowBackConnexion ;
    private final static int Reinitialization_TIME_OUT = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reinitialization_password);
        ButterKnife.bind(ReinitializationPasswordActivity.this);
        if (Utils.resetEmail != ""){
            etMailConnexionReset.setText(Utils.resetEmail);
        }


        setClickableLayouts();
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        if (Intent.ACTION_VIEW == appLinkAction){
            Log.e("intent from gmail" , appLinkAction+ " not null");
            Intent intent = new Intent(ReinitializationPasswordActivity.this , EditPasswordActivity.class);
            startActivity(intent);



        }
    }
    @Override
    public void onBackPressed() {
        if (Utils.resetEmail != ""){
            etMailConnexionReset.setText("");
        }
        Intent intent = new Intent( ReinitializationPasswordActivity.this , ConnexionActivity.class);
        startActivity(intent);
        finish();
    }

    private void setClickableLayouts() {
        arrowBackConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.resetEmail != ""){
                    etMailConnexionReset.setText("");
                }
                Intent intent = new Intent (ReinitializationPasswordActivity.this , ConnexionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        createAccountFromForgottenPWLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.resetEmail != ""){
                    etMailConnexionReset.setText("");
                }
                Intent intent = new Intent(ReinitializationPasswordActivity.this, InscriptionActivity.class);
                startActivity(intent);
                finish();
            }
        });


        validateButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.resetEmail != ""){
                    etMailConnexionReset.setText("");
                }
                sendEmailReset();


            }

        });
    }

    private void sendEmailReset() {
        if ((etMailConnexionReset.getText() + "").equals("")) {
            new CustomToast(ReinitializationPasswordActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.no_email), R.drawable.ic_erreur, CustomToast.ERROR).show();
        } else if (!((etMailConnexionReset.getText() + "").matches("^[A-Za-z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]{2,6}$"))) {
            new CustomToast(ReinitializationPasswordActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_form_email), R.drawable.ic_erreur, CustomToast.ERROR).show();
        } else {
            sendEmailResetService();
        }
    }

    private void sendEmailResetService() {

        final JsonObject postParams = new JsonObject();
        Utils.resetEmail = etMailConnexionReset.getText()+"";
        postParams.addProperty("email", Utils.resetEmail);
        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().sendEmailReset(postParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){

                    etMailConnexionReset.setText("");
                    validateReinitialization();




                } else {
                    new CustomToast(ReinitializationPasswordActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.verify_form_email_reset), R.drawable.ic_erreur, CustomToast.ERROR).show();

                }
            }

            private void validateReinitialization(){
     /*
     final LayoutInflater inflater = LayoutInflater.from(context);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyleSuggest);
                final View customDialog = inflater.inflate(R.layout.dialog_cities, null , false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.setView(customDialog);
                tvConfirm = customDialog.findViewById(R.id.tv_confirm);
                tvTunis = customDialog.findViewById(R.id.label_tunis);
                etCities = customDialog.findViewById(R.id.tv_region_place);
                tvRetour = customDialog.findViewById(R.id.tv_retour);
                tvRetour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });
                tvTunis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onConfirmCitiesSelectListener.OnConfirmCitiesSelectListener(new City("1000", context.getString(R.string.label_grand_tunis)));
                        alertDialog.dismiss();
                    }
                });*/
                final LayoutInflater inflater = LayoutInflater.from(ReinitializationPasswordActivity.this);
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReinitializationPasswordActivity.this, R.style.AppCompatAlertDialogStyleSuggest);
                final View customDialog = inflater.inflate(R.layout.pop_up_reset_password, null , false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.setView(customDialog);
                alertDialog.show();
                LinearLayout goToConnexionButton = customDialog.findViewById(R.id.btn_go_to_connexion);
                goToConnexionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (Utils.resetEmail != ""){
                            etMailConnexionReset.setText("");
                        }
                        Intent intent = new Intent( ReinitializationPasswordActivity.this , ConnexionActivity.class);
                        startActivity(intent);
                        finish();


                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        alertDialog.dismiss();


                    }
                }, Reinitialization_TIME_OUT);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void sendResetPassword() {
    }
}
