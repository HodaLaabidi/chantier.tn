package tn.chantier.chantiertn.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.adapters.MainHomeFragmentAdapter;
import tn.chantier.chantiertn.factories.DialogFactoryUtils;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.models.Ads;
import tn.chantier.chantiertn.models.City;
import tn.chantier.chantiertn.models.Offer;
import tn.chantier.chantiertn.utils.ConnectivityService;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

import static android.support.v7.widget.LinearLayoutManager.*;
import static tn.chantier.chantiertn.adapters.MainHomeFragmentAdapter.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.main_progress_bar)
    ProgressBar mainProgressBar ;
    @BindView(R.id.rl_filter)
    RelativeLayout rlFilter ;
    View rootView ;
    @BindView(R.id.filter_button)
    LinearLayout filterButton ;
    @BindView(R.id.scroll_view_list)
    ScrollView scrollViewList ;
    @BindView(R.id.ll_background_fragment_home)
    LinearLayout llBackgroundFragmentHome ;
    @BindView(R.id.ll_offer_bar)
    LinearLayout llOfferBar ;
    @BindView(R.id.back_button)
    LinearLayout backButton;
    @BindView(R.id.checkbox_Par)
    CheckBox checkBoxPar ;
    @BindView(R.id.checkbox_prof)
    CheckBox checkBoxProf ;
    @BindView(R.id.layout_city)
    LinearLayout layoutCity ;
    @BindView(R.id.tv_city)
    RalewayTextView tvCity;
    public static ArrayList<City> listCities = new ArrayList<>() ;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mainRecyclerView;
    private ArrayList<Offer> listOfOffers = new ArrayList<>();
    private ArrayList<Ads> listOfAds = new ArrayList();
    MainHomeFragmentAdapter mainHomeFragmentAdapter ;



    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter img1.
     * @param param2 Parameter img2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (SharedPreferencesFactory.getListOfCodesCities(getContext()) != null){
            listCities = SharedPreferencesFactory.getListOfCodesCities(getContext());
            Log.e("test sharedpreferences" , listCities.size() + " !");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_home, container, false);
         mainRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_recycler_view);
        initialiseFragment();
        browseListOfOffers();
        setFilter();



        return rootView ;
    }

    private void initialiseFragment() {
        ButterKnife.bind(this,rootView);
        rlFilter.setVisibility(View.GONE);
        scrollViewList.setVisibility(View.VISIBLE);
    }

    private void setFilter() {
        changeCheckboxColor(checkBoxPar);
        changeCheckboxColor(checkBoxProf);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rlFilter.getVisibility() == View.GONE){
                    openFilter(rlFilter);
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rlFilter.getVisibility() == View.VISIBLE){
                    closeFilter(rlFilter);
                }
            }
        });
        layoutCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogFactoryUtils dialogFactoryUtils = new DialogFactoryUtils();
                dialogFactoryUtils.showCitiesListDialog(getContext());
                dialogFactoryUtils.setOnConfirmCitiesSelectListener(new DialogFactoryUtils.OnConfirmCitiesSelect() {
                    @Override
                    public void OnConfirmCitiesSelectListener(City city) {
                        try {

                            City cityToSend = city;
                            tvCity.setText(city.getLocalite());

                        } catch (NullPointerException e) {

                            tvCity.setText("");
                        }
                    }
                });
            }
        });
    }
    private void changeCheckboxColor(CheckBox checkBox) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        ContextCompat.getColor(checkBox.getContext(), R.color.lightGray),
                        ContextCompat.getColor(checkBox.getContext(), R.color.colorYellow)
                });
        checkBox.setButtonTintList(colorStateList);
    }
    private void openFilter(View view){
        llOfferBar.setVisibility(View.GONE);

        view.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up));
        view.setVisibility(View.VISIBLE);
        llBackgroundFragmentHome.setBackgroundColor(getResources().getColor(R.color.lightGray));
        scrollViewList.setVisibility(View.GONE);


    }

    private void closeFilter(View view) {
        llOfferBar.setVisibility(View.VISIBLE);
        view.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down));
        view.setVisibility(View.GONE);
        scrollViewList.setVisibility(View.VISIBLE);
        llBackgroundFragmentHome.setBackgroundColor(getResources().getColor(R.color.lightGray));

    }


    private void browseListOfOffers() {


        getOffersFormBackend();


    }

    private void getOffersFormBackend() {

        mainProgressBar.setVisibility(View.VISIBLE);

        if( ! ConnectivityService.isOnline(getContext())){
            mainProgressBar.setVisibility(View.GONE);
            new CustomToast(getContext(), getResources().getString(R.string.error), getResources().getString(R.string.verify_internet), R.drawable.ic_erreur, CustomToast.ERROR).show();




        } else {


            Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getAds();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if ( response.isSuccessful()){
                        Log.e("test array of ads", " response  successful");
                    } else {
                        Log.e("test array of ads", " response  failed");
                    }
                    if ( response.code() == 200){
                        try {
                            Log.e("test array of ads", response.body().string());
                            JSONObject jsonObject= null;

                            Gson gson = Utils.getGsonInstance();
                            jsonObject = new JSONObject( response.body().string());
                            Type type = new TypeToken<ArrayList<Ads>>() {
                            }.getType();
                            listOfAds  = gson.fromJson(jsonObject.toString(), type);
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
            });
            JsonObject postParams = new JsonObject();
            postParams.addProperty("id" , SharedPreferencesFactory.retrieveUserData().getId());
            postParams.addProperty("limit" , SEARCH_RESULT_LIMIT );
            postParams.addProperty("cursor" , 0);


            Call<ResponseBody> call2 = RetrofitServiceFactory.getChantierService().getAllOffers(postParams);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    mainProgressBar.setVisibility(View.GONE);
                    if (response.code() == 200) {



                        JSONArray jsonArray= null;
                        Gson gson = Utils.getGsonInstance();
                        try {
                            jsonArray = new JSONArray( response.body().string());
                            Type type = new TypeToken<ArrayList<Offer>>() {
                            }.getType();
                            listOfOffers = gson.fromJson(jsonArray.toString(), type);

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            mainRecyclerView.setLayoutManager(mLayoutManager);
                            Log.e("HomeFragment" , listOfOffers.toString());
                            MainHomeFragmentAdapter mainHomeFragmentAdapter = new MainHomeFragmentAdapter(getContext() , listOfOffers , mainProgressBar , listOfAds);
                            mainRecyclerView.setAdapter(mainHomeFragmentAdapter);
                            Log.e(" test size list" , listOfOffers.size()+ " !");

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            Log.e("HomeAdapter", response.body().string() + response.code() + response.toString()+ " !");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mainProgressBar.setVisibility(View.GONE);
                    Log.e("HomeFragmentAdapter", " onFailure process" + " !");



                }
            });



        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
