package tn.chantier.chantiertn.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ResponseCache;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.DetailsActivity;
import tn.chantier.chantiertn.activities.HomeActivity;
import tn.chantier.chantiertn.activities.classes.MyApplication;
import tn.chantier.chantiertn.adapters.FollowedOffersFragmentAdapter;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.models.FollowedOffer;
import tn.chantier.chantiertn.utils.ConnectivityService;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

import static tn.chantier.chantiertn.utils.Utils.hasPermissions;

public class ActivityLogFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final long WAITING_FOR_RESET_Connexion = 600;
    View rootView ;
    private ArrayList<FollowedOffer> followedOffers ;
    FollowedOffersFragmentAdapter followedOffersFragmentAdapter;
    @BindView(R.id.pb_followed_offers_fragment)
    ProgressBar pbFollowedOffersFragment;
    @BindView(R.id.rv_followed_offers)
    RecyclerView rvFollowedOffers ;
    @BindView(R.id.ll_followed_offer)
    LinearLayout llFollowedOffer ;
    @BindView(R.id.sv_followed_offers)
    ScrollView svFollowedOffers ;
    @BindView(R.id.layout_no_followed_offers)
    LinearLayout layoutNoFollowedOffers;
    @BindView(R.id.layout_no_connexion_followed_offers)
    LinearLayout layoutNoConnexionFollowedOffers ;
    @BindView(R.id.button_reset_connexion_followed_offers)
    LinearLayout buttonResetConnexionFollowedOffers ;



    public ActivityLogFragment() {
        // Required empty public constructor
    }

    public static ActivityLogFragment newInstance(String param1, String param2) {
        ActivityLogFragment fragment = new ActivityLogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_activity_log, container, false);
        // Inflate the layout for this fragment

        initialiseFragment();
        browseLisrOfFollowedOffers();
        return rootView ;
    }

    private void initialiseFragment() {

        ButterKnife.bind(this , rootView);

    }


    private void browseLisrOfFollowedOffers() {
        pbFollowedOffersFragment.setVisibility(View.VISIBLE);
        if ( ! ConnectivityService.isOnline(getContext())){
            pbFollowedOffersFragment.setVisibility(View.GONE);
            layoutNoFollowedOffers.setVisibility(View.GONE);
            Utils.setBackgroundColor(getContext() , llFollowedOffer , R.color.colorWhite);
            svFollowedOffers.setVisibility(View.GONE);

            layoutNoConnexionFollowedOffers.setVisibility(View.VISIBLE);
            buttonResetConnexionFollowedOffers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pbFollowedOffersFragment.setVisibility(View.VISIBLE);
                    layoutNoConnexionFollowedOffers.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            browseLisrOfFollowedOffers();
                        }
                    }, WAITING_FOR_RESET_Connexion);
                }
            });



        } else {
            layoutNoFollowedOffers.setVisibility(View.GONE);
            svFollowedOffers.setVisibility(View.VISIBLE);
            layoutNoConnexionFollowedOffers.setVisibility(View.GONE);
            Utils.setBackgroundColor(getContext() , llFollowedOffer , R.color.lightGray);

            pbFollowedOffersFragment.setVisibility(View.VISIBLE);

            final JsonObject postParams = new JsonObject();
            postParams.addProperty("id_client" , SharedPreferencesFactory.retrieveUserData().getId()+"");
            Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getFollowedOffers(postParams);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if ( response.code() == 200){
                        Log.e("status", SharedPreferencesFactory.retrieveUserData().toString());
                        pbFollowedOffersFragment.setVisibility(View.GONE);
                        JSONArray jsonArray= null;
                        Gson gson = Utils.getGsonInstance();


                            try {
                                String remoteResponse = response.body().string();
                                jsonArray = new JSONArray( remoteResponse);
                                Type type = new TypeToken<ArrayList<FollowedOffer>>(){

                                }.getType();
                                followedOffers = gson.fromJson(jsonArray.toString() , type);


                            if (followedOffers.size() == 0){
                                layoutNoFollowedOffers.setVisibility(View.VISIBLE);
                                layoutNoConnexionFollowedOffers.setVisibility(View.GONE);
                                svFollowedOffers.setVisibility(View.GONE);

                            } else {
                                layoutNoFollowedOffers.setVisibility(View.GONE);
                                layoutNoConnexionFollowedOffers.setVisibility(View.GONE);
                                svFollowedOffers.setVisibility(View.VISIBLE);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                rvFollowedOffers.setLayoutManager(mLayoutManager);
                                followedOffersFragmentAdapter = new FollowedOffersFragmentAdapter(getContext(), followedOffers);
                                rvFollowedOffers.setAdapter(followedOffersFragmentAdapter);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        pbFollowedOffersFragment.setVisibility(View.GONE);
                        layoutNoConnexionFollowedOffers.setVisibility(View.GONE);
                        svFollowedOffers.setVisibility(View.GONE);
                        Log.e(" code suivi" , " code != 200");



                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pbFollowedOffersFragment.setVisibility(View.GONE);
                    Log.e(" on Failure suivi" , " not workong ^_^");

                }
            });
        }
    }


}
