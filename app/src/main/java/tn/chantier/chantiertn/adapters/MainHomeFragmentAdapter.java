package tn.chantier.chantiertn.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;
import java.util.RandomAccess;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.activities.AdsActivity;
import tn.chantier.chantiertn.activities.ContactDetailsActivity;
import tn.chantier.chantiertn.activities.DetailsActivity;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.fragments.HomeFragment;
import tn.chantier.chantiertn.models.Ads;
import tn.chantier.chantiertn.models.Offer;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;
import tn.chantier.chantiertn.widgets.NetworkImageView;

public class MainHomeFragmentAdapter extends RecyclerView.Adapter<MainHomeFragmentAdapter.MyViewHolder> {


    public static final int SEARCH_RESULT_LIMIT = 200;
    private static final int AD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;
    private static int countAds = 0, count = 0;
    private static int cursor = 0;
    private Context context;
    private View probressBar;
    private ArrayList<Offer> listOfOffers;
    private boolean state = false;
    private static boolean isAd = false;
    private ArrayList<Ads> listOfAds = new ArrayList<>();

    private int size = 0;
    private JsonObject postParams;
    Integer adsDrawable[] = new Integer[3];


    public MainHomeFragmentAdapter(Context context, ArrayList<Offer> listOfOffers, View progressBar, ArrayList<Ads> listOfAds) {
        this.context = context;
        if (listOfAds.size() != 0) {
            for (int i = 0; i < listOfOffers.size(); i++) {
                if (i % 5 == 0) {
                    listOfOffers.add(i, new Offer());
                }
            }
        } else {
            // static images for ads
            adsDrawable[1] = R.drawable.ads_gif;
            adsDrawable[2] = R.drawable.img1;
            adsDrawable[0] = R.drawable.logo;
        }
        this.listOfOffers = listOfOffers;
        this.postParams = postParams;
        this.probressBar = progressBar;
        size = getItemCount() - 100;
        this.listOfAds = listOfAds;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = null;

        if (viewType == AD_TYPE) {
            item = LayoutInflater.from(context).inflate(R.layout.ad_item, parent, false);

        } else {
            item = LayoutInflater.from(context).inflate(R.layout.item_offer, parent, false);


        }

        return new MainHomeFragmentAdapter.MyViewHolder(item);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 5 == 0 && position != 0) {
            isAd = true;
            return AD_TYPE;


        } else {
            isAd = false;

            return CONTENT_TYPE;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        /*count ++ ;
        if (count % 5 == 0 ) {
            isAd = true;
        } else {
            isAd = false ;
        }
        if (isAd) {
            holder.adsGif.setVisibility(View.VISIBLE);
            holder.itemOfferTitle.setVisibility(View.GONE);
            holder.itemOfferDetails.setVisibility(View.GONE);
            holder.itemOfferIcon.setVisibility(View.GONE);
            holder.adsIcon.setVisibility(View.VISIBLE);
            holder.itemOfferSeen.setVisibility(View.GONE);
            holder.itemDetails.setVisibility(View.GONE);
            isAd = false ;
            //holder.itemllOfferItem.setClickable(false);
        } else {
            //holder.itemllOfferItem.setClickable(true);
            holder.adsGif.setVisibility(View.GONE);
            holder.itemOfferTitle.setVisibility(View.VISIBLE);
            holder.itemOfferDetails.setVisibility(View.VISIBLE);
            holder.itemOfferIcon.setVisibility(View.VISIBLE);
            holder.adsIcon.setVisibility(View.GONE);
            holder.itemOfferSeen.setVisibility(View.VISIBLE);
            holder.itemDetails.setVisibility(View.VISIBLE);*/


        Log.e(" size from adapter", size + " !");

        /*if (position == size   && ! isNotified()){
            Log.e("adapter swap" , " it's ok");
            swap();
        }*/
        if (!isAd) {
            getLayout(holder, position);
        } else {

            // -------------------------------Dynamic method------------------------------------
                    /* Ads ads = listOfAds.get(countAds);

                setAdsValues(ads, holder);
                     count++;
                     if ( countAds >= listOfAds.size() ){
                         countAds = 0 ;
                     }*/

            // -------------------------------Static method-------------------------------------


            int drawableAds = adsDrawable[countAds];


            setStaticAdsValues(drawableAds, holder);
            countAds++;
            if (countAds >= 3) {
                countAds = 0;
            }
        }

    }

    private void setStaticAdsValues(Integer drawableAds, MyViewHolder holder) {

        holder.adsGif.setImageResource(drawableAds);
        holder.adsGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , AdsActivity.class);
                context.startActivity(intent);
            }
        });
    }


    private void setDynamicAdsValues(final Ads ads, MyViewHolder holder) {
        if (ads.getEtat() == "1") {
            String link = ads.getLink();
            String redirectLink = ads.getRedirect_link();
            // TODO Don't miss to retrieve ads link and redirectLink and find method to load the image ( gif or jpg -_-)
            if (ads.getLink().contains(".jpg")) {
                holder.adsGif.setImageUrlWithoutBorderWithoutResizing(ads.getLink(), R.drawable.ads_gif);
            } else if (ads.getLink().contains(".gif")) {
                holder.adsGif.setGifImageUrl(ads.getLink() + "");
            }
            holder.adsGif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , AdsActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    private void getLayout(MyViewHolder holder, int position) {

        final Offer offer = listOfOffers.get(position);

        Log.e("offer icon sector ", offer.getId_sec() + " ");

        Log.e("adapter", offer.getTitre() + " ");
        int postalCode = offer.getCode_postal();
        Log.e(" postal code ", offer.getCode_postal() + "!");
        Log.e("list cities", listOfOffers.size() + " !");
        String idSecteur = offer.getId_sec() + "";

        if (idSecteur.equalsIgnoreCase("1")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img1);

        } else if (idSecteur.equalsIgnoreCase("2")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img2);


        } else if (idSecteur.equalsIgnoreCase("13")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img13);
        } else if (idSecteur.equalsIgnoreCase("3")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img3);
        } else if (idSecteur.equalsIgnoreCase("4")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img4);
        } else if (idSecteur.equalsIgnoreCase("5")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img5);
        } else if (idSecteur.equalsIgnoreCase("6")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img6);
        } else if (idSecteur.equalsIgnoreCase("8")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img8);
        } else if (idSecteur.equalsIgnoreCase("9")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img9);
        } else if (idSecteur.equalsIgnoreCase("10")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img10);
        } else if (idSecteur.equalsIgnoreCase("11")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img11);
        } else if (idSecteur.equalsIgnoreCase("12")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img12);
        } else if (idSecteur.equalsIgnoreCase("15")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img15);
        } else if (idSecteur.equalsIgnoreCase("7")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img7);
        } else if (idSecteur.equalsIgnoreCase("16")) {
            holder.itemOfferIcon.setImageResource(R.drawable.img16);
        }
        for (int i = 0; i < HomeFragment.listCities.size(); i++) {
            String code = postalCode + "";
            Log.e(" code postal item ", code + " !");
            if (code.equals(HomeFragment.listCities.get(i).getCode())) {
                Log.e(" list offer item ", HomeFragment.listCities.get(i).getLocalite() + "");
                holder.itemOfferAdress.setText(HomeFragment.listCities.get(i).getLocalite() + "");
            }

        }
        holder.itemOferUnit.setText(offer.getUnite() + "");
        holder.itemOfferProfil.setText(offer.getTypeClient());
        holder.itemOfferQuantity.setText(offer.getQuantite() + "");
        holder.itemOfferSeenNumber.setText(offer.getContact() + "");
        holder.itemOfferTitle.setText(offer.getTitre() + "");
        holder.itemllOfferItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContactDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_offer_item", offer.getId() + "");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


        Log.e("offer commission main f", offer.toString() + "!!");

        if (offer.getCommission() != null) {
            if (!offer.getCommission().equalsIgnoreCase("0")) {
                holder.iconCommission.setVisibility(View.VISIBLE);
            }
        }


    }


    private boolean isNotified() {
        return this.state;
    }


    private void swap() {
        switchState();
        updateList();
    }

    private void switchState() {
        this.state = !this.state;
    }

    private void updateList() {
        JsonObject postParams = new JsonObject();
        cursor += 10;
        postParams.addProperty("id", SharedPreferencesFactory.retrieveUserData().getId());
        postParams.addProperty("limit", SEARCH_RESULT_LIMIT);
        postParams.addProperty("cursor", cursor);


        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getAllOffers(postParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                probressBar.setVisibility(View.GONE);
                if (response.code() == 200) {


                    JSONArray jsonArray = null;
                    Gson gson = Utils.getGsonInstance();
                    try {
                        jsonArray = new JSONArray(response.body().string());
                        Type type = new TypeToken<ArrayList<Offer>>() {
                        }.getType();
                        listOfOffers = gson.fromJson(jsonArray.toString(), type);
                        Log.e("list from adapter", listOfOffers.size() + " !");
                        size += jsonArray.length();
                        notifyDataSetChanged();
                        if (jsonArray.length() > 0) {
                            switchState();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        Log.e("HomeAdapter", response.body().string() + response.code() + response.toString() + " !");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                probressBar.setVisibility(View.GONE);
                Log.e("HomeFragmentAdapter", " onFailure process" + " !");


            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfOffers.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        RalewayTextView itemOfferAdress;


        ImageView itemOfferIcon;

        RalewayTextView itemOfferQuantity, itemOfferProfil;

        RalewayTextView itemOfferSeenNumber;

        RalewayTextView itemOfferTitle;
        RalewayTextView itemOferUnit;
        LinearLayout itemllOfferItem;
        ImageView iconCommission;
        //NetworkImageView adsGif ;
        NetworkImageView adsGif;
        NetworkImageView adsIcon;
        LinearLayout itemOfferSeen;
        LinearLayout itemOfferDetails, itemDetails;


        public MyViewHolder(View itemView) {
            super(itemView);
            iconCommission = itemView.findViewById(R.id.icon_commission);
            itemOfferAdress = itemView.findViewById(R.id.item_offer_adress);
            itemOferUnit = itemView.findViewById(R.id.item_offer_unit);
            itemOfferIcon = itemView.findViewById(R.id.item_offer_icon);
            itemOfferQuantity = itemView.findViewById(R.id.item_offer_quantity);
            itemOfferSeenNumber = itemView.findViewById(R.id.item_offer_seen_number);
            itemOfferTitle = itemView.findViewById(R.id.item_offer_title);
            itemllOfferItem = itemView.findViewById(R.id.ll_offer_item);
            itemOfferProfil = itemView.findViewById(R.id.item_offer_profil);
            adsGif = itemView.findViewById(R.id.ads_gif);
            //adsGif = itemView.findViewById(R.id.ads_gif);
            /* itemOfferSeen = itemView.findViewById(R.id.item_offer_seen);
            itemOfferDetails = itemView.findViewById(R.id.item_offer_details);
            adsIcon = itemView.findViewById(R.id.item_ad_icon);
            itemDetails = itemView.findViewById(R.id.item_details);*/

        }
    }
}
