package tn.chantier.chantiertn.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
    View rootView ;
    private ArrayList<FollowedOffer> followedOffers ;
    FollowedOffersFragmentAdapter followedOffersFragmentAdapter;
    @BindView(R.id.pb_followed_offers_fragment)
    ProgressBar pbFollowedOffersFragment;
    @BindView(R.id.rv_followed_offers)
    RecyclerView rvFollowedOffers ;



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
        pbFollowedOffersFragment.setVisibility(View.GONE);
    }


    private void browseLisrOfFollowedOffers() {

        if ( ! ConnectivityService.isOnline(getContext())){
            new CustomToast(MyApplication.getAppContext(), getResources().getString(R.string.error), getResources().getString(R.string.verify_internet), R.drawable.ic_erreur, CustomToast.ERROR).show();

        } else {
            pbFollowedOffersFragment.setVisibility(View.VISIBLE);

            final JsonObject postParams = new JsonObject();
            postParams.addProperty("id_client" , SharedPreferencesFactory.retrieveUserData().getId()+"");
            Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getFollowedOffers(postParams);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    Log.e(" on response code" , response.code()+ " !");
                    Log.e(" on response code" , response.body().toString()+ " !");
                    if ( response.code() == 200){
                        Log.e("statut", SharedPreferencesFactory.retrieveUserData().toString());
                        pbFollowedOffersFragment.setVisibility(View.GONE);
                        JSONArray jsonArray= null;
                        Gson gson = Utils.getGsonInstance();

                            try {
                                jsonArray = new JSONArray( response.body().string());
                                Type type = new TypeToken<ArrayList<FollowedOffer>>(){

                                }.getType();
                                followedOffers = gson.fromJson(jsonArray.toString() , type);


                            for (int i = 0 ; i < followedOffers.size() ; i++){
                                Log.e( " followed offer values" , followedOffers.get(i).getNom_client());
                            }
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            rvFollowedOffers.setLayoutManager(mLayoutManager);
                            followedOffersFragmentAdapter = new FollowedOffersFragmentAdapter(getContext() , followedOffers);
                            rvFollowedOffers.setAdapter(followedOffersFragmentAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        pbFollowedOffersFragment.setVisibility(View.GONE);
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
