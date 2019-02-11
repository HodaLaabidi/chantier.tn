package tn.chantier.chantiertn.activities;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.factories.RetrofitBrowseContactServiceFactory;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.fragments.HomeFragment;
import tn.chantier.chantiertn.models.Applicant;
import tn.chantier.chantiertn.models.Contacts;
import tn.chantier.chantiertn.models.Offer;
import tn.chantier.chantiertn.utils.ConnectivityService;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

import static tn.chantier.chantiertn.utils.Utils.hasPermissions;

public class ContactDetailsActivity extends AppCompatActivity {

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
    ImageView itemDetailOfferIcon ;
    @BindView(R.id.icon_commission_details)
    ImageView itemCommissionDetails;
    @BindView(R.id.contact_progress_bar)
     ProgressBar contactProgressBar ;

    String id ;
    private static Offer offer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initialiseViews();
        retriveDataFromBackOffice();
    }

    private void retriveDataFromBackOffice() {

        layoutDetails.setVisibility(View.GONE);
        if (!ConnectivityService.isOnline(ContactDetailsActivity.this)){

        } else {
            probgressBarDetailsItem.setVisibility(View.VISIBLE);
            JsonObject postParams = new JsonObject();
            postParams.addProperty("id_lead", id);
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

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                     catch (IOException e) {
                        e.printStackTrace();
                    }
                    } else {
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
            for (int i = 0; i < HomeFragment.listCities.size() ; i++){
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


            itemDetailLayoutBrowseContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemDetailLayoutBrowseContact.setVisibility(View.GONE);
                    contactProgressBar.setVisibility(View.VISIBLE);
                    setContactLayoutsValues();


                }
            });

            setIcons();






        }


    }

    private void setContactLayoutsValues() {

        if (offer != null){

           /* final String requestUrl = "https://www.chantier.tn/mobile/afficherContact";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("volley response ", response);
                    XmlToJson xmlToJson = new XmlToJson.Builder(response).build();
                    JSONObject jsonObject = xmlToJson.toJson();
                    Gson gson = Utils.getGsonInstance();
                    final Contacts contacts = gson.fromJson(jsonObject.toString(), Contacts.class);
                    itemDetailNameClient.setText(contacts.getNom());
                    itemDetailPhoneNumberClient.setText(contacts.getTel());
                    itemDetailMailClient.setText(contacts.getEmail());
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
                            if (hasPermissions(ContactDetailsActivity.this, Utils.MY_PERMISSIONS_REQUEST_CALL_PHONE , Utils.PERMISSIONS)) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+contacts.getTel()+""));
                                Log.e( " test onClickListener", " "+ "ok!");
                                startActivity(intent);
                            }
                        }
                    });

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String , String> postMap = new HashMap<>();
                    postMap.put("id_lead",4920);
                    //postMap.put("id_client","1980");
                    //postMap.put("etat","connected");
                    return postMap;
                }
            };
            Volley.newRequestQueue(getBaseContext()).add(stringRequest);*/
            final JsonObject postParams = new JsonObject();
            postParams.addProperty("id_lead" , offer.getId()+"");
            postParams.addProperty("id_client" , SharedPreferencesFactory.retrieveUserData().getId()+"");
            postParams.addProperty("etat" , "connected");
            Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().browseContactValues(postParams);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if ( response.code() == 200){
                        try {
                            contactProgressBar.setVisibility(View.GONE);
                            itemDetailContact.setVisibility(View.VISIBLE);
                            Gson gson = Utils.getGsonInstance();
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            Log.e("jsonObject" , jsonObject.toString());
                            final Applicant applicant = gson.fromJson(jsonObject.toString(), Applicant.class);

                            /*Log.e(" contacts" , response.body().string());
                            String responseString = response.body().string();
                            Log.e(" contacts response code" , response.body()+"");
                            XmlToJson xmlToJson = new XmlToJson.Builder(responseString).build();
                            Log.e("json string", xmlToJson.toString());
                            JSONObject jsonObject = xmlToJson.toJson();
                            Log.e("xmlToJson", xmlToJson.toString());
                            Gson gson = Utils.getGsonInstance();
                            Log.e("jsonObject.toString() ", jsonObject.toString());
                            final Contacts contacts = gson.fromJson(jsonObject.toString(), Contacts.class);
                            Log.e("contacts ", contacts.toString());
                            String test = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n <contacts>\n" +
                                    "    <nom>ACHOURI Shady</nom>\n" +
                                    "    <tel>21009858</tel>\n" +
                                    "    <email>shady.achouri@gmail.com</email>\n" +
                                    "</contacts>";
                            Log.e("test =", test);
                            XmlToJson xmlToJson2 = new XmlToJson.Builder(test).build();
                            Log.e("xmlToJson2", xmlToJson2.toString());*/

                            //Contacts contacts = response.body();
                            // set layouts
                            itemDetailNameClient.setText(applicant.getNom());
                            itemDetailPhoneNumberClient.setText(applicant.getTel());
                            itemDetailMailClient.setText(applicant.getEmail());
                            itemDetailLayoutMail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e( " test onClickListener", " "+ "ok!");
                                    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                                        intent.setData(Uri.parse("mailto:" + applicant.getEmail()));
                                        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{applicant.getEmail()+""});
                                        startActivity(intent);
                                        // intent.setData(Uri.parse("mailto:" + followedOffer.getEmail_client() + ""));
                                        //context.startActivity(Intent.createChooser(intent, "Send mail"));
                                    } else {
                                        //Intent intent = new Intent(Intent.ACTION_SEND);
                                        //intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{followedOffer.getEmail_client()+""});
                                        //intent.setData(Uri.parse("mailto:" + followedOffer.getEmail_client() + ""));
                                        //context.startActivity(Intent.createChooser(intent, "Send mail"));
                                        //context.startActivity(intent);
                                        Intent emailIntent = new Intent (Intent.ACTION_SENDTO, Uri.parse("mailto:"+Uri.encode(applicant.getEmail())));
                                        startActivity(Intent.createChooser(emailIntent, "Send email via..."));


                                    }
                                 /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        Log.e(" test onClickListener", " "+ " ok!");
                                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                                        intent.setData(Uri.parse("mailto:"));
                                        startActivity(intent);

                                    } else {
                                        Log.e(" test onClickListener", " "+ " ok!");

                                    }*/
                                }
                            });
                            itemDetailLayoutCall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e( " test onClickListener", " "+ "ok!");
                                        Intent intent = null ;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (Utils.hasPermissions(ContactDetailsActivity.this, Utils.MY_PERMISSIONS_REQUEST_CALL_PHONE, Utils.PERMISSIONS)) {
                                                intent = new Intent(Intent.ACTION_CALL, Uri.parse(
                                                        "tel:" + applicant.getTel()
                                                ));
                                                startActivity(intent);
                                            }
                                        }
                                         else {
                                            intent = new Intent(Intent.ACTION_CALL, Uri.parse(
                                                    "tel:" + applicant.getTel()
                                            ));
                                            startActivity(intent);
                                        }
                                    }

                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        contactProgressBar.setVisibility(View.GONE);
                        {
                            try {
                                Log.e( "Retrofit Response: " ,response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.e( "Error message: " , response.raw().message());
                            Log.e("Error code: " , String.valueOf(response.raw().code()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e( "Error message: " , "onFailure" );
                    contactProgressBar.setVisibility(View.GONE);

                }
            });
        }
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

        ButterKnife.bind(this);
        id = getIntent().getExtras().getString("id_offer_item");

        Log.e("id offer details" , id);

        llArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetailsActivity.this , HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
