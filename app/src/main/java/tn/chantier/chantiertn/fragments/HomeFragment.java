package tn.chantier.chantiertn.fragments;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.ConnexionActivity;
import tn.chantier.chantiertn.activities.SplashScreen;
import tn.chantier.chantiertn.adapters.MainHomeFragmentAdapter;
import tn.chantier.chantiertn.factories.DialogFactoryUtils;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.models.Ads;
import tn.chantier.chantiertn.models.City;
import tn.chantier.chantiertn.models.EditableSubCategory;
import tn.chantier.chantiertn.models.Gouvernerat;
import tn.chantier.chantiertn.models.Offer;
import tn.chantier.chantiertn.utils.ConnectivityService;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

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
    private static final long WAIT_FOR_CLOSING_FILTER = 900;
    private static final long WAIT_FOR_PRO_Filter = 2500;
    private static final long WAITING_FOR_RESET_Connexion = 600;
    City cityToSend  ;
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
    @BindView(R.id.filter_validate_button)
    LinearLayout filterValidateButton ;
    public static ArrayList<City> listCities = new ArrayList<>() ;
    ArrayList<EditableSubCategory> subCategorieDesfault ;
    @BindView(R.id.filter_sub_category)
    RalewayTextView filterSubCategory;
    @BindView(R.id.filter_sub_category_counter)
    RalewayTextView getFilterSubCategoryCounter;
    @BindView(R.id.layout_no_connexion)
    LinearLayout layoutNoConnexion ;
    @BindView(R.id.ll_specialities)
    LinearLayout llSpecialities ;
    @BindView(R.id.image_dialog)
    ImageView imageDialog ;
    @BindView(R.id.text_layout_dialog_one)
    RalewayTextView textLayoutDialogOne ;
    @BindView(R.id.text_layout_dialog_two)
    RalewayTextView textLayoutDialogTwo ;
    @BindView(R.id.bar_ll_offer)
    LinearLayout barLlOffer ;
    @BindView(R.id.tv_city_counter)
    RalewayTextView tvCityCounter ;
    static ArrayList<Offer> listOfFilteredOffer ;
    @BindView(R.id.layout_no_filtred_offers)
    LinearLayout layoutNoFiltredOffers ;
    @BindView(R.id.reset_filter)
    ImageView resetFilter ;
     @BindView(R.id.button_reset_connexion_offers)
     LinearLayout buttonResetConnexionOffers ;

    static ArrayList<EditableSubCategory> specialities = new ArrayList<>();
    private List<EditableSubCategory> selectedSearchTags = new ArrayList<>();
    public static  HashMap<Integer, EditableSubCategory> selectedSubCategoryMap = new HashMap<>();
    public static ArrayList<String> selectedSpecialities = new ArrayList<>();
    public static ArrayList<String> listOfSelectedCities = new ArrayList<>();
    ArrayList<Offer> listOfUpdatedOffers = new ArrayList<>();
    public static String categoriesName = "";
    public static String categoriesNameToSet = "";
    public static String cityName = "";
    public static int cityCount = 0;
    public static boolean isValidateFilter = false ;
    public static int isPro = 1 ;
    public static int isPar = 1 ;
    private static boolean noFilter = false ;


    // ----------new city layout----------
    public static  int nbSelected = 0;
    public static HashMap<Integer, Gouvernerat> selectedCityMap = new HashMap<>();
    public static int nbSelectedCity = 0;
    @BindView(R.id.ll_cities)
    LinearLayout llCities ;
    @BindView(R.id.filter_cities)
    RalewayTextView filterCities ;
    @BindView(R.id.filter_city_counter)
    RalewayTextView filterCityCounter  ;
    public static String citiesName = "";
    public static String citiesNameToSet = "";
    static ArrayList<Gouvernerat> cities = new ArrayList<>();
    public static ArrayList<String> selectedCities = new ArrayList<>();
    ArrayList<City> cityListDefault  ;

    // ------------End --------------




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mainRecyclerView;
    private  static ArrayList<Offer> listOfOffers = new ArrayList<>();
    private ArrayList<Ads> listOfAds = new ArrayList();
    MainHomeFragmentAdapter mainHomeFragmentAdapter ;
    static ArrayList<EditableSubCategory> specialitiesList = new ArrayList<>();



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

        getSpecialitiesItems();
    }


    private void getSpecialitiesItems() {

        JsonObject postParams = new JsonObject();
        postParams.addProperty("id_client" , SharedPreferencesFactory.retrieveUserData().getId());
        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getAllSelectedCategories(postParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){


                    JSONArray jsonArray = null ;
                    Gson gson = Utils.getGsonInstance();
                    try {String remoteResponse = response.body().string()+"";
                        jsonArray = new JSONArray(remoteResponse);
                        Type type = new TypeToken<ArrayList<EditableSubCategory>>(){

                        }.getType();
                        specialitiesList = gson.fromJson(jsonArray.toString(), type);
                        SharedPreferencesFactory.saveSpecialities(getContext(), specialitiesList);
                        Log.e("SP listSpecialities" ,SharedPreferencesFactory.getListOfSpecialities(getContext()).size()+ "!" );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e(" test EditableSS WS", response.code()+" !");
                    try {
                        Log.e(" test EditableSS WS", response.body().string()+" !");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_home, container, false);
         mainRecyclerView = rootView.findViewById(R.id.main_recycler_view);
        ButterKnife.bind(this,rootView);
        initialiseFilter();
         browseListOfOffers();

        setFilter();



        return rootView ;
    }
    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }



    private void initialiseFilter() {


            checkBoxProf.setChecked(true);


            checkBoxPar.setChecked(true);

        isPar = 1 ;
        isPro = 1;

        cityCount = 0;
        cityName = "";
        tvCityCounter.setText("0");
        tvCityCounter.setTextColor(getResources().getColor(R.color.gray_3));
        tvCity.setText("Ville");
        listOfSelectedCities.clear();
        if (isValidateFilter){
            mainHomeFragmentAdapter.clearAdapter();
        }


            selectedSubCategoryMap.clear();
            selectedCityMap.clear();
            filterCities.setText("Ville");
            filterCityCounter.setText("0");
            filterCityCounter.setTextColor(getResources().getColor(R.color.colorDarkGrey));
            filterSubCategory.setText("Spécialités");
            getFilterSubCategoryCounter.setText("0");
            getFilterSubCategoryCounter.setTextColor(getResources().getColor(R.color.colorDarkGrey));
            nbSelected = 0 ;
            nbSelectedCity = 0;
            categoriesName = "";
            categoriesNameToSet = "";
            citiesName = "";
            citiesNameToSet = "";
            cities.clear();
            getGouvernerats();
            selectedCities.clear();

            specialities.clear();
            //--------------------- this is should not be saved into SharedPreferences list , we can change it when we change topics from backend !! -------------------
            //specialities.addAll(SharedPreferencesFactory.getListOfSpecialities(getContext()));
        specialities.addAll(specialitiesList);
           selectedSpecialities.clear();
           rlFilter.setVisibility(View.GONE);
           scrollViewList.setVisibility(View.VISIBLE);
    }

    private void getGouvernerats() {

       // cities.addAll(SharedPreferencesFactory.getListOfCodesCities(getContext()));


        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getGouvernerats();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Gson gson = Utils.getGsonInstance();

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(response.body().string());
                        Type type = new TypeToken<ArrayList<Gouvernerat>>(){}.getType();
                         cities = gson.fromJson(jsonArray.toString(), type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void setFilter() {

        changeCheckboxColor(checkBoxPar);
        changeCheckboxColor(checkBoxProf);
        filterValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rlFilter.getVisibility() == View.VISIBLE){
                    validateFilter(rlFilter);
                }

            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rlFilter.getVisibility() == View.GONE){
                    if (ConnectivityService.isOnline(getContext())) {
                        openFilter(rlFilter);
                    }
                    if (noFilter){
                        layoutNoFiltredOffers.setVisibility(View.GONE);
                        layoutNoConnexion.setVisibility(View.GONE);
                        scrollViewList.setVisibility(View.GONE);
                        rlFilter.setVisibility(View.VISIBLE);
                        llOfferBar.setVisibility(View.VISIBLE);
                        barLlOffer.setVisibility(View.VISIBLE);
                        openFilter(rlFilter);

                    }

                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rlFilter.getVisibility() == View.VISIBLE){
                    closeFilter(rlFilter);

                }
                mainHomeFragmentAdapter.clearAdapter();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isValidateFilter){
                            updateFiltredOffers();
                        } else {
                            browseListOfOffers();
                        }



                    }
                }, WAIT_FOR_CLOSING_FILTER);
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

                             cityToSend = city;
                            listOfSelectedCities.add(0, cityToSend.getCode()+"");
                            cityCount++;

                            cityName += " "+ city.getGouvernorat();
                            if (cityName.length() > 26) {
                                cityName = cityName.substring(0, 26) + "...";
                            }
                            if (cityName == ""){
                                tvCity.setText("Ville");
                            } else {
                                tvCity.setText(cityName);
                            }
                            if (cityCount == 0){
                                tvCityCounter.setTextColor(getResources().getColor(R.color.colorDarkGrey));

                            } else if (cityCount > 0) {
                                tvCityCounter.setTextColor(getResources().getColor(R.color.colorYellow));
                            }
                            tvCityCounter.setText(cityCount + "");

                        } catch (NullPointerException e) {

                            tvCity.setText("Ville");
                        }
                        for (int i = 0 ; i <listOfSelectedCities.size() ; i++  ){
                            Log.e( " item selectedCities "+ i , listOfSelectedCities.get(i)+" !");
                        }
                    }
                });
            }
        });

        llSpecialities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategorieDesfault = new ArrayList<>();
                try {
                    subCategorieDesfault.addAll(specialities);
                } catch (NullPointerException e) {

                }
                final DialogFactoryUtils dialogFactoryUtils = new DialogFactoryUtils();
                for (int i = 0 ; i < specialities.size() ; i++){
                    Log.e("specialities" + i , specialities.get(i).toString()+ " !");
                }
                dialogFactoryUtils.showCategoryListDialog(getContext(), specialities, subCategorieDesfault);
                dialogFactoryUtils.setOnConfirmSubCategoryConfirmListener(new DialogFactoryUtils.OnConfirmSubCategoryConfirmListener() {
                    @Override
                    public void OnConfirmSubCategoryConfirmListener(HashMap<Integer, EditableSubCategory> selectedSubCategoryMap) {
                         setLayoutCategories();
                        if (HomeFragment.categoriesName.equalsIgnoreCase("")){
                            filterSubCategory.setText("Spécialités");

                        } else {
                            filterSubCategory.setText(HomeFragment.categoriesNameToSet);
                        }

                        getFilterSubCategoryCounter.setText(nbSelected+"");
                        if( nbSelected == 0){
                            getFilterSubCategoryCounter.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                        } else if (nbSelected > 0){
                            getFilterSubCategoryCounter.setTextColor(getResources().getColor(R.color.colorYellow));
                        }

                        resetListSelectedSpecialities();
                    }
                });
            }
        });

        llCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                } catch (NullPointerException e) {

                }
                final DialogFactoryUtils dialogFactoryUtils = new DialogFactoryUtils();
                dialogFactoryUtils.showCityListDialog(getContext(), cities );
                dialogFactoryUtils.setOnConfirmCityConfirmListener(new DialogFactoryUtils.OnConfirmCityConfirmListener() {
                    @Override
                    public void OnConfirmCityConfirmListener(HashMap<Integer, Gouvernerat> selectedCityMap) {
                        setLayoutCities();
                        if (HomeFragment.citiesName.equalsIgnoreCase("")){
                            filterCities.setText("Spécialités");

                        } else {
                            filterCities.setText(HomeFragment.citiesNameToSet);
                        }

                        filterCityCounter.setText(nbSelectedCity+"");
                        if( nbSelectedCity == 0){
                            filterCityCounter.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                        } else if (nbSelectedCity > 0){
                            filterCityCounter.setTextColor(getResources().getColor(R.color.colorYellow));
                        }

                        resetListSelectedCities();
                    }
                });
            }
        });


    }

    private void setLayoutCities() {

        HomeFragment.citiesName = "";
        HomeFragment.citiesNameToSet = "";
        if ( HomeFragment.selectedCityMap.size() != 0) {
            for (int i = 0; i < HomeFragment.cities.size(); i++) {
                if (HomeFragment.selectedCityMap.containsKey(i)) {

                    HomeFragment.citiesName += " " + HomeFragment.selectedCityMap.get(i).getGouvernerat()  ;

                }
            }


            if (HomeFragment.citiesName.length() > 26) {
                HomeFragment.citiesNameToSet = HomeFragment.citiesName.substring(0, 26) + "...";
            } else {
                HomeFragment.citiesNameToSet = citiesName+"";
            }

        }
    }

    private void resetListSelectedCities() {
        selectedCities.clear();
        if ( selectedCityMap.size() != 0){

            for (Map.Entry<Integer, Gouvernerat> item : selectedCityMap.entrySet()) {

                String value = item.getValue().getGouvernerat(); // ou code
                selectedCities.add(0 , value);
            }

        }
    }

    public static void resetListSelectedSpecialities(){
        selectedSpecialities.clear();
        if ( selectedSubCategoryMap.size() != 0){

            for (Map.Entry<Integer, EditableSubCategory> item : selectedSubCategoryMap.entrySet()) {

                String value = item.getValue().getId_act();
                selectedSpecialities.add(0 , value);
            }

        }
    }

    public static  void setLayoutCategories() {
        HomeFragment.categoriesName = "";
        HomeFragment.categoriesNameToSet = "";
        if ( HomeFragment.selectedSubCategoryMap.size() != 0) {
            for (int i = 0; i < HomeFragment.specialities.size(); i++) {
                if (HomeFragment.selectedSubCategoryMap.containsKey(i)) {

                    HomeFragment.categoriesName += " " + HomeFragment.selectedSubCategoryMap.get(i).getNom_activite()  ;

                }
            }


            if (HomeFragment.categoriesName.length() > 26) {
                HomeFragment.categoriesNameToSet = HomeFragment.categoriesName.substring(0, 26) + "...";
            } else {
                HomeFragment.categoriesNameToSet = categoriesName+"";
            }

        }



    }




    private void changeCheckboxColor(CheckBox checkBox) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        ContextCompat.getColor(checkBox.getContext(), R.color.gray_3),
                        ContextCompat.getColor(checkBox.getContext(), R.color.colorYellow)
                });
        checkBox.setButtonTintList(colorStateList);
    }
    private void openFilter(View view){

        view.setVisibility(View.VISIBLE);
        view.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up));
        scrollViewList.setVisibility(View.GONE);
        llOfferBar.setVisibility(View.GONE);



        llBackgroundFragmentHome.setBackgroundColor(getResources().getColor(R.color.lightGray));




        if ( HomeFragment.cityName.equalsIgnoreCase("")){
            tvCity.setText("Ville");
        } else {
            tvCity.setText(cityName);
        }

        if ( HomeFragment.cityCount == 0){
            tvCityCounter.setText("0");
            tvCityCounter.setTextColor(getResources().getColor(R.color.gray_3));
        } else {
            tvCityCounter.setText(cityCount+"");
            tvCityCounter.setTextColor(getResources().getColor(R.color.colorYellow));
        }

        if (categoriesName.equalsIgnoreCase("")){
            filterSubCategory.setText("Spécialités");
        } else {
            filterSubCategory.setText(categoriesNameToSet);
        }
        if (nbSelected == 0 ){
            getFilterSubCategoryCounter.setText("0");
            getFilterSubCategoryCounter.setTextColor(getResources().getColor(R.color.gray_3));
        } else {
            getFilterSubCategoryCounter.setText(nbSelected+"");
            getFilterSubCategoryCounter.setTextColor(getResources().getColor(R.color.colorYellow));
        }

        if (citiesName.equalsIgnoreCase("")){
            filterCities.setText("Ville");
        } else {
            filterCities.setText(citiesNameToSet);
        }
        if (nbSelectedCity == 0 ){
            filterCityCounter.setText("0");
            filterCityCounter.setTextColor(getResources().getColor(R.color.gray_3));
        } else {
            filterCityCounter.setText(nbSelectedCity+"");
            filterCityCounter.setTextColor(getResources().getColor(R.color.colorYellow));
        }

        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonResetFilter();
            }
        });


    }

    private void setButtonResetFilter() {


            checkBoxProf.setChecked(true);


            checkBoxPar.setChecked(true);

        isPar = 1 ;
        isPro = 1;

        cityCount = 0;
        cityName = "";

        tvCityCounter.setTextColor(getResources().getColor(R.color.gray_3));
        tvCityCounter.setText("0");
        tvCity.setText("Ville");
        listOfSelectedCities.clear();
        selectedSubCategoryMap.clear();
        selectedCityMap.clear();
        filterSubCategory.setText("Spécialités");
        getFilterSubCategoryCounter.setText("0");
        filterCities.setText("Ville");
        filterCityCounter.setText("0");
        if (isValidateFilter){
            mainHomeFragmentAdapter.clearAdapter();
        }
        isValidateFilter = false ;
        getFilterSubCategoryCounter.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        nbSelected = 0 ;
        nbSelectedCity = 0;
        filterCityCounter.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        categoriesName = "";
        categoriesNameToSet = "";
        citiesName = "";
        citiesNameToSet = "";
        resetListSelectedSpecialities();
        resetListSelectedCities();


    }

    @Override
    public void onStop() {
        super.onStop();
        initialiseFilter();
        noFilter = false ;
    }

    @Override
    public void onPause() {
        super.onPause();
        noFilter = false ;

    }

    @Override
    public void onResume() {
        super.onResume();
        noFilter = false ;
        initialiseFilter();

    }

    private void setFilterVilleLayout() {
        if ( cityCount == 0){
            tvCityCounter.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        } else {
            tvCityCounter.setTextColor(getResources().getColor(R.color.colorYellow));
        }

        if ( cityName.equalsIgnoreCase("")){
            tvCity.setText("Ville");
        } else {
            tvCity.setText(cityName);
        }
    }

    private void closeFilter(View view) {
        scrollViewList.setVisibility(View.VISIBLE);
        llOfferBar.setVisibility(View.VISIBLE);
        if (isValidateFilter){
            mainHomeFragmentAdapter.clearAdapter();
        }

        view.startAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down));
        view.setVisibility(View.GONE);

        llBackgroundFragmentHome.setBackgroundColor(getResources().getColor(R.color.lightGray));




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isValidateFilter = false ;
        isPro = 1 ;
        isPar = 1 ;
        listOfSelectedCities.clear();
        selectedSpecialities.clear();
        selectedCities.clear();
        selectedSubCategoryMap.clear();
        selectedCityMap.clear();
        noFilter = false ;
    }

    private void validateFilter(View view) {

       // listOfSelectedCities
        //selectedSpecialities
        // isPar isPro  isValidateFilter
        if ( !checkBoxPar.isChecked() && !checkBoxProf.isChecked()){
            new CustomToast(getContext(), getResources().getString(R.string.error), getResources().getString(R.string.no_type_filter), R.drawable.ic_erreur, CustomToast.ERROR).show();
        } else  if (checkBoxPar.isChecked() && checkBoxProf.isChecked() ){
            isValidateFilter = true;
            isPro = 1 ;
            isPar = 1;
            closeFilter(view);
            updateFiltredOffers();
        } else if (checkBoxPar.isChecked()){
            isPro = 0 ;
            isPar = 1 ;
            isValidateFilter = true;
            closeFilter(view);
            updateFiltredOffers();

        }else if (checkBoxProf.isChecked()) {
            isValidateFilter = true;
            isPar = 0;
            isPro = 1 ;
            closeFilter(view);
            updateFiltredOffers();
        }

         Log.e("  test validate isPar" , isPar + " !");
        Log.e(" test validate isPro" , isPro + " !");
        for (int i = 0 ; i < listOfSelectedCities.size() ; i++){
            Log.e( "test validate listOfSelectedCities "+i, listOfSelectedCities.get(i).toString());
        }

        for (int i = 0 ; i < selectedSpecialities.size() ; i++){
            Log.e( "test validate selectedSpecialities "+i, selectedSpecialities.get(i).toString());
        }




    }

    private void updateFiltredOffers() {

        mainProgressBar.setVisibility(View.VISIBLE);

        if( ! ConnectivityService.isOnline(getContext())){
            mainProgressBar.setVisibility(View.GONE);
            layoutNoFiltredOffers.setVisibility(View.GONE);
            Utils.setBackgroundColor(getContext() , llOfferBar , R.color.colorWhite);
            layoutNoConnexion.setVisibility(View.VISIBLE);
            scrollViewList.setVisibility(View.GONE);
            rlFilter.setVisibility(View.GONE);
            llOfferBar.setVisibility(View.VISIBLE);
            barLlOffer.setVisibility(View.VISIBLE);
            filterButton.setClickable(false);
            buttonResetConnexionOffers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonResetConnexionOffers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mainProgressBar.setVisibility(View.VISIBLE);
                            layoutNoConnexion.setVisibility(View.GONE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    updateFiltredOffers();
                                }
                            }, WAITING_FOR_RESET_Connexion);
                        }
                    });
                }
            });






        } else {
            filterButton.setClickable(true);

            Utils.setBackgroundColor(getContext(), llOfferBar, R.color.lightGray);
            layoutNoConnexion.setVisibility(View.GONE);
            scrollViewList.setVisibility(View.VISIBLE);
            llOfferBar.setVisibility(View.VISIBLE);
            barLlOffer.setVisibility(View.VISIBLE);
            JsonObject postParams = new JsonObject();
            Log.e("id filtred list ", SharedPreferencesFactory.retrieveUserData().getId()+"!");
            postParams.addProperty("id_client", SharedPreferencesFactory.retrieveUserData().getId());

            JsonArray jsonArray = new JsonArray();
            List<String> customerList = new ArrayList<>();
            customerList.addAll(selectedSpecialities);

            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(customerList, new TypeToken<List<String>>() {
            }.getType());

            jsonArray = element.getAsJsonArray();

            postParams.add("specialites", jsonArray);
            JsonArray jsonArray2 = new JsonArray();
            List<String> customerList2 = new ArrayList<>();
            customerList2.addAll(selectedCities);

            Gson gson2 = new Gson();
            JsonElement element2 = gson2.toJsonTree(customerList2, new TypeToken<List<String>>() {
            }.getType());

            jsonArray2 = element2.getAsJsonArray();
            postParams.add("villes", jsonArray2);
            postParams.addProperty("pro", isPro);
            postParams.addProperty("particulier", isPar);
            postParams.addProperty("id_client" , SharedPreferencesFactory.retrieveUserData().getId());


            Call<ResponseBody> call2 = RetrofitServiceFactory.getChantierService().getFiltredOffers(postParams);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        mainProgressBar.setVisibility(View.GONE);
                        try {
                            String remoteResponse = response.body().string();
                            Log.e("filtred list ", remoteResponse);


                                    JSONArray jsonArray = null;
                                    Gson gson = Utils.getGsonInstance();
                                    try {
                                        jsonArray = new JSONArray(remoteResponse);
                                        Type type = new TypeToken<ArrayList<Offer>>() {
                                        }.getType();
                                         listOfUpdatedOffers = gson.fromJson(jsonArray.toString(), type);
                                         Log.e("filtred list" , listOfUpdatedOffers.size() +  " !");
                                        for (int i = 0 ; i < listOfUpdatedOffers.size() ; i++){
                                            Log.e("listOfUpdatedOffers "+i , listOfUpdatedOffers.get(i).toString() + listOfUpdatedOffers.get(i).getTypeClient());
                                        }
                                        mainHomeFragmentAdapter.updateAdapter(listOfUpdatedOffers);
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                        Log.e("filtred list ", e.getMessage());
                                    }



                        } catch
                                (IOException e) {
                            e.printStackTrace();
                            Log.e("filtred list ", e.getMessage());
                        }
                    } else  if ( response.code() == 204){
                        noFilter = true ;
                        Log.e("filtred list ", "no 200 code ");
                        mainProgressBar.setVisibility(View.GONE);
                        mainProgressBar.setVisibility(View.GONE);

                        Utils.setBackgroundColor(getContext() , llOfferBar , R.color.colorWhite);
                        layoutNoFiltredOffers.setVisibility(View.VISIBLE);
                        layoutNoConnexion.setVisibility(View.GONE);
                        scrollViewList.setVisibility(View.GONE);
                        rlFilter.setVisibility(View.GONE);
                        llOfferBar.setVisibility(View.VISIBLE);
                        barLlOffer.setVisibility(View.VISIBLE);
                    } else {

                        // technical error !
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mainProgressBar.setVisibility(View.GONE);
                    Log.e("filtred list ", "onFailure ");
                }
            });
        }


    }


    private void browseListOfOffers() {


        getOffersFormBackend();


    }

    private void getOffersFormBackend() {

        mainProgressBar.setVisibility(View.VISIBLE);

        if( ! ConnectivityService.isOnline(getContext())){
            mainProgressBar.setVisibility(View.GONE);

            Utils.setBackgroundColor(getContext() , llOfferBar , R.color.colorWhite);
            layoutNoConnexion.setVisibility(View.VISIBLE);
            scrollViewList.setVisibility(View.GONE);
            rlFilter.setVisibility(View.GONE);
            barLlOffer.setVisibility(View.VISIBLE);
            llOfferBar.setVisibility(View.VISIBLE);
            filterButton.setClickable(false);
            layoutNoFiltredOffers.setVisibility(View.GONE);

            buttonResetConnexionOffers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainProgressBar.setVisibility(View.VISIBLE);
                    layoutNoConnexion.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            getOffersFormBackend();
                        }
                    }, WAITING_FOR_RESET_Connexion);
                }
            });





        } else {
            filterButton.setClickable(true);
            Utils.setBackgroundColor(getContext() , llOfferBar , R.color.lightGray);
            layoutNoConnexion.setVisibility(View.GONE);
            scrollViewList.setVisibility(View.VISIBLE);
            layoutNoFiltredOffers.setVisibility(View.GONE);


            JsonObject postParams = new JsonObject();
            postParams.addProperty("id" , SharedPreferencesFactory.retrieveUserData().getId());
            postParams.addProperty("limit" , SEARCH_RESULT_LIMIT );
            postParams.addProperty("cursor" , 0);


            Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getAllOffers(postParams);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    mainProgressBar.setVisibility(View.GONE);
                    if (response.code() == 200) {



                        JSONArray jsonArray= null;
                        Gson gson = Utils.getGsonInstance();
                        try {
                            String remoteResponse = response.body().string()+"";
                            jsonArray = new JSONArray( remoteResponse);
                            Type type = new TypeToken<ArrayList<Offer>>() {
                            }.getType();
                            listOfOffers = gson.fromJson(jsonArray.toString(), type);

                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            mainRecyclerView.setLayoutManager(mLayoutManager);
                            Log.e("HomeFragment" , listOfOffers.toString());
                             mainHomeFragmentAdapter = new MainHomeFragmentAdapter(getContext() , listOfOffers , mainProgressBar , SplashScreen.listOfAds);
                            mainRecyclerView.setAdapter(mainHomeFragmentAdapter);
                            Log.e(" test size list" , listOfOffers.size()+ " !");

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        layoutNoConnexion.setVisibility(View.GONE);
                        layoutNoFiltredOffers.setVisibility(View.GONE);
                         scrollViewList.setVisibility(View.GONE);
                         rlFilter.setVisibility(View.GONE);
                        barLlOffer.setVisibility(View.VISIBLE);
                        llOfferBar.setVisibility(View.VISIBLE);
                        Utils.setBackgroundColor(getContext() , llOfferBar , R.color.colorWhite);



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
