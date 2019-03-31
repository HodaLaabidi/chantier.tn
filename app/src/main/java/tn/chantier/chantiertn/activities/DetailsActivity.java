package tn.chantier.chantiertn.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
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
import tn.chantier.chantiertn.factories.RetrofitBrowseContactServiceFactory;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.fragments.ActivityLogFragment;
import tn.chantier.chantiertn.fragments.HomeFragment;
import tn.chantier.chantiertn.models.Applicant;
import tn.chantier.chantiertn.models.Offer;
import tn.chantier.chantiertn.utils.ConnectivityService;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;

import static tn.chantier.chantiertn.utils.Utils.hasPermissions;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.ll_arrow_back)
    LinearLayout llArrowButton ;
    @BindView(R.id.item_detail_offer_title)
    RalewayTextView itemDetailOfferTitle;
    @BindView(R.id.item_detail_seen)
    RalewayTextView itemDetailSeen ;
    @BindView(R.id.item_detail_offer_adress)
    RalewayTextView itemDetailOfferAdress ;
    @BindView(R.id.item_offer_profil)
    RalewayTextView itemOfferProfil ;
    @BindView(R.id.item_offer_quantity)
    RalewayTextView itemOfferQuantity ;
    @BindView(R.id.item_offer_unit)
    RalewayTextView itemOfferUnit ;
    @BindView(R.id.item_detail_description)
    RalewayTextView itemDetailDescription ;
    @BindView(R.id.item_detail_layout_browse_contact)
    LinearLayout itemDetailLayoutBrowseContact ;
    @BindView(R.id.item_detail_contact)
    LinearLayout itemDetailContact ;
    @BindView(R.id.item_detail_name_client)
    RalewayTextView itemDetailNameClient ;
    @BindView(R.id.item_detail_phone_number_client)
    RalewayTextView itemDetailPhoneNumberClient ;
    @BindView(R.id.item_detail_mail_client)
    RalewayTextView itemDetailMailClient ;
    @BindView(R.id.item_detail_layout_mail)
    LinearLayout itemDetailLayoutMail ;
    @BindView(R.id.item_detail_layout_call)
    LinearLayout itemDetailLayoutCall ;
    @BindView(R.id.layout_details)
    LinearLayout layoutDetails ;
    @BindView(R.id.progress_bar_details_item)
    ProgressBar probgressBarDetailsItem ;
    @BindView(R.id.item_detail_offer_icon)
    ImageView  itemDetailOfferIcon ;
    @BindView(R.id.icon_commission_details)
    ImageView itemCommissionDetails;
    Intent intent;
    Bundle bundle = new Bundle();
    String fromFOList = "";












    String id ;
    private static Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initialiseViews();
        retriveDataFromBackOffice();



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fromFOList.equals("yes")) {
            Log.e("from_followedofferslist", "yes");
            intent = new Intent(DetailsActivity.this, HomeActivity.class);

            bundle.putString("from_detail_activity", "ok");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            Log.e("from_followedofferslist", "no");
            intent = new Intent(DetailsActivity.this, HomeActivity.class);
            bundle.putString("from_detail_activity", "no");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    private void retriveDataFromBackOffice() {
        layoutDetails.setVisibility(View.GONE);
        if ( !ConnectivityService.isOnline(DetailsActivity.this)){
            // show a Toast

        } else {
            probgressBarDetailsItem.setVisibility(View.VISIBLE);
            JsonObject postParams = new JsonObject();
            postParams.addProperty("id_lead" ,id );

            Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getOfferDetails(postParams);
            call.enqueue(new Callback<ResponseBody>() {


                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        probgressBarDetailsItem.setVisibility(View.GONE);
                        layoutDetails.setVisibility(View.VISIBLE);

                        JSONObject jsonObject = null ;

                        Gson gson = Utils.getGsonInstance();
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            offer = gson.fromJson(jsonObject.toString(), Offer.class);
                            setAllViews(offer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        probgressBarDetailsItem.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    probgressBarDetailsItem.setVisibility(View.GONE);

                }
            });
        }
    }

    private void setAllViews(Offer offer) {
        if( offer != null) {
            itemDetailOfferTitle.setText(offer.getTitre() + "");
            itemDetailSeen.setText(" Contacté par "+offer.getContact()+ " professionnels");
            for (int i = 0 ; i < HomeFragment.listCities.size() ; i++){
                String code =offer.getCode_postal()+"";
                if (code.equals(HomeFragment.listCities.get(i).getCode())){
                    Log.e(" list offer item " , HomeFragment.listCities.get(i).getLocalite()+"");
                    itemDetailOfferAdress.setText(HomeFragment.listCities.get(i).getLocalite()+""+"("+offer.getCode_postal()+ ")");
                }

            }
            itemOfferProfil.setText(offer.getTypeClient());
            itemOfferQuantity.setText(offer.getQuantite());
            itemOfferUnit.setText(offer.getUnite());
            itemDetailDescription.setText(offer.getDescription());

            //  set contact layouts fonctionnalities

            setContactLayoutsValues();

            if (offer.getCheck() == 1){
                itemDetailLayoutBrowseContact.setVisibility(View.GONE);
                itemDetailContact.setVisibility(View.VISIBLE);
            }
            if (offer.getCheck() == 0){

                itemDetailLayoutBrowseContact.setVisibility(View.VISIBLE);
                itemDetailContact.setVisibility(View.GONE);
                itemDetailLayoutBrowseContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemDetailLayoutBrowseContact.setVisibility(View.GONE);
                        itemDetailContact.setVisibility(View.VISIBLE);


                    }
                });

            }


            setIcons();






        }


    }

    private void setContactLayoutsValues() {


        if ( offer != null){
            /*final JsonObject postParams = new JsonObject();

           *//* postParams.addProperty("idLead" , offer.getId()+"");
            postParams.addProperty("id_client" , SharedPreferencesFactory.retrieveUserData().getId()+"");
            postParams.addProperty("etat" , "connected");*//*
            postParams.addProperty("idLead" , "4920");
            postParams.addProperty("id_client" , "1980");
            postParams.addProperty("etat" , "connected");
            Call<ResponseBody> call = RetrofitBrowseContactServiceFactory.getChantierService().browseContactValues(postParams);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if ( response.code() == 200){
                        try {
                            Log.e(" contact" , response.body().string()+"");
                            Log.e(" contact response code" , response.code()+"");
                            Gson gson = Utils.getGsonInstance();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Log.e("jsonObject" , jsonObject.toString());
                            final Applicant applicant = gson.fromJson(jsonObject.toString(), Applicant.class);
                            Log.e("applicant" , applicant.toString());
                            // set layouts
                            itemDetailNameClient.setText(applicant.getNom());
                            itemDetailPhoneNumberClient.setText(applicant.getTel());
                            itemDetailMailClient.setText(applicant.getEmail());
                            itemDetailLayoutMail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e( " test onClickListener", " "+ "ok!");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        Log.e(" test onClickListener", " "+ " ok!");
                                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                                        intent.setData(Uri.parse("mailto:"));
                                        startActivity(intent);

                                    } else {
                                        Log.e(" test onClickListener", " "+ " ok!");

                                    }
                                }
                            });
                            itemDetailLayoutCall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e( " test onClickListener", " "+ "ok!");
                                    if (hasPermissions(DetailsActivity.this, Utils.MY_PERMISSIONS_REQUEST_CALL_PHONE , Utils.PERMISSIONS)) {
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+applicant.getTel()+""));
                                        Log.e( " test onClickListener", " "+ "ok!");
                                        startActivity(intent);
                                    }
                                }
                            });
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
            });*/
        }
       /* if (offer != null){

            final JsonObject postParams = new JsonObject();

            postParams.addProperty("idLead" , offer.getId()+"");
            postParams.addProperty("id_client" , SharedPreferencesFactory.retrieveUserData().getId()+"");
            postParams.addProperty("etat" , "connected");


            Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().browseContactValues(offer.getId()+"",
                    SharedPreferencesFactory.retrieveUserData().getId()+"","connected" );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        Log.e(" get contact service" , response.code()+" ");
                        try {
                            Log.e("  contact service body" , response.body().string()+" ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // set layouts values
                        Gson gson = Utils.getGsonInstance();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Log.e("jsonObject" , jsonObject.toString());
                            //JSONObject object = jsonObject.getJSONObject("contacts");
                           // Log.e("jsonObject contacts", object.toString()+" !");

                           final Applicant applicant = gson.fromJson(jsonObject.toString(), Applicant.class);
                            Log.e("applicant" , applicant.toString());
                            // set layouts
                            itemDetailNameClient.setText(applicant.getNom());
                            itemDetailPhoneNumberClient.setText(applicant.getTel());
                            itemDetailMailClient.setText(applicant.getEmail());
                            itemDetailLayoutMail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e( " test onClickListener", " "+ "ok!");
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        Log.e(" test onClickListener", " "+ " ok!");
                                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                                        intent.setData(Uri.parse("mailto:"));
                                        startActivity(intent);

                                    } else {
                                        Log.e(" test onClickListener", " "+ " ok!");

                                    }
                                }
                            });
                            itemDetailLayoutCall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e( " test onClickListener", " "+ "ok!");
                                    if (hasPermissions(DetailsActivity.this, Utils.MY_PERMISSIONS_REQUEST_CALL_PHONE , Utils.PERMISSIONS)) {
                                        *//*Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+applicant.getTel()+""));
                                        Log.e( " test onClickListener", " "+ "ok!");
                                        startActivity(intent);*//*
                                    }
                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        // don't set any contact layout

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }*/



    }

    private void setIcons() {
        String idSecteur = offer.getId_sec()+"";
        if (idSecteur.equalsIgnoreCase("1")){
            itemDetailOfferIcon.setImageResource(R.drawable.img1);

        } else if (idSecteur.equalsIgnoreCase("2"))
        {
            itemDetailOfferIcon.setImageResource(R.drawable.img2);


        } else if ( idSecteur.equalsIgnoreCase("13")){
            itemDetailOfferIcon.setImageResource(R.drawable.img13);
        } else if (idSecteur.equalsIgnoreCase("3")){
            itemDetailOfferIcon.setImageResource(R.drawable.img3);
        } else if (idSecteur.equalsIgnoreCase("4")){
            itemDetailOfferIcon.setImageResource(R.drawable.img4);
        } else if (idSecteur.equalsIgnoreCase("5")){
            itemDetailOfferIcon.setImageResource(R.drawable.img5);
        } else if (idSecteur.equalsIgnoreCase("6")){
            itemDetailOfferIcon.setImageResource(R.drawable.img6);
        } else if (idSecteur.equalsIgnoreCase("8")){
            itemDetailOfferIcon.setImageResource(R.drawable.img8);
        } else if (idSecteur.equalsIgnoreCase("9")){
            itemDetailOfferIcon.setImageResource(R.drawable.img9);
        } else if (idSecteur.equalsIgnoreCase("10")){
            itemDetailOfferIcon.setImageResource(R.drawable.img10);
        } else if (idSecteur.equalsIgnoreCase("11")){
            itemDetailOfferIcon.setImageResource(R.drawable.img11);
        } else if (idSecteur.equalsIgnoreCase("12")){
            itemDetailOfferIcon.setImageResource(R.drawable.img12);
        } else if (idSecteur.equalsIgnoreCase("15")){
            itemDetailOfferIcon.setImageResource(R.drawable.img15);
        } else if (idSecteur.equalsIgnoreCase("7")){
            itemDetailOfferIcon.setImageResource(R.drawable.img7);
        } else if (idSecteur.equalsIgnoreCase("16")){
            itemDetailOfferIcon.setImageResource(R.drawable.img16);
        }

        Log.e("offer commission" , offer.getCommission()+" !");

        if (!offer.getCommission().equalsIgnoreCase("")){
            itemCommissionDetails.setVisibility(View.VISIBLE);
        }
    }

    private void initialiseViews() {
        ButterKnife.bind(this );

        if (getIntent() != null) {
            final Bundle extras = getIntent().getExtras();
            fromFOList = extras.getString("from_followed_offers_list");
            Log.e("fromFollowedOffers_list", fromFOList);

            id = extras.getString("id_offer_item");
            Log.e("id offer details", id);


            llArrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if (fromFOList.equals("yes")) {
                            Log.e("from_followedofferslist", "yes");
                            intent = new Intent(DetailsActivity.this, HomeActivity.class);

                            bundle.putString("from_detail_activity", "ok");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("from_followedofferslist", "no");
                            intent = new Intent(DetailsActivity.this, HomeActivity.class);
                            bundle.putString("from_detail_activity", "no");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }



                }
            });
        }
    }

}
