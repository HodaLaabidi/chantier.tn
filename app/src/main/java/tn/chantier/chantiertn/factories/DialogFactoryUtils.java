package tn.chantier.chantiertn.factories;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.AEADBadTagException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.ChatActivity;
import tn.chantier.chantiertn.activities.ChatActivity_ViewBinding;
import tn.chantier.chantiertn.adapters.CitiesRecyclerAdapter;
import tn.chantier.chantiertn.adapters.CityRecyclerAdapter;
import tn.chantier.chantiertn.adapters.SpecialitiesRecyclerAdapter;
import tn.chantier.chantiertn.fragments.HomeFragment;
import tn.chantier.chantiertn.models.City;
import tn.chantier.chantiertn.models.EditableSubCategory;
import tn.chantier.chantiertn.models.Gouvernerat;
import tn.chantier.chantiertn.models.SubCategory;
import tn.chantier.chantiertn.models.Topic;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayEditText;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

public class DialogFactoryUtils {


    ArrayList<City>  cities = new ArrayList<>();
    ArrayList<Gouvernerat> gouvernerats = new ArrayList<>();
    ArrayList<EditableSubCategory>  specialities = new ArrayList<>();
    City selectedRegion;
    CardView cardRegionAutocomplete;
    private OnConfirmCitiesSelect onConfirmCitiesSelectListener;
    private OnConfirmCityConfirmListener onConfirmCityConfirmListener;
    private  OnConfirmSubCategoryConfirmListener onConfirmSubCategoryConfirmListener;
    private RecyclerView rvRegionAutoComplete;
    private CardView cardCitiesAutocomplete;
    private RalewayEditText etCities , etChatDialog;
    private boolean isCardAutoVisible = false;
    private RalewayTextView tvRetour ,tvRetourChatDialog, tvEnvoyer, tvConfirm , tvTunis ;
    RecyclerView rvCategoryList;

    private ArrayList<City> getAllCities(final Context context){

        if (HomeFragment.listCities.size() != 0){
            ArrayList<City> cities = HomeFragment.listCities;
            Log.e("list cities !!?", cities.size()+" ");

            return  cities;

        }
             return  cities;

    }

    private ArrayList<EditableSubCategory> getAllSpecialities(){
        JsonObject postParams = new JsonObject();
        postParams.addProperty("id_client" , SharedPreferencesFactory.retrieveUserData().getId()+"");
        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getAllSelectedCategories(postParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    Log.e(" Editable SC object" , response.body().toString());
                    JSONArray jsonArray = null ;
                    Gson gson = Utils.getGsonInstance();
                    try {
                        jsonArray = new JSONArray(response.body().string());
                        Type type = new TypeToken<ArrayList<EditableSubCategory>>(){

                        }.getType();
                        specialities = gson.fromJson(jsonArray.toString(), type);
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
        if (specialities.size() != 0){


            return  specialities;

        }
        return  specialities;

    }



    public void showCitiesListDialog(final Context context){
        cities = getAllCities(context);
        Log.e("list cities !", cities.size()+" ");
        if (cities != null){
            if ( cities.size() > 0){
                Log.e("list cities", cities.size()+" ");
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
                        onConfirmCitiesSelectListener.OnConfirmCitiesSelectListener(new City());
                        alertDialog.dismiss();
                    }
                });

                rvCategoryList = customDialog.findViewById(R.id.rv_category_list);
                rvCategoryList.setLayoutManager(new LinearLayoutManager(context));
                rvCategoryList.setHasFixedSize(true);
                rvRegionAutoComplete = customDialog.findViewById(R.id.rv_region_autocomplete);
                rvRegionAutoComplete.setLayoutManager(new LinearLayoutManager(context));
                rvRegionAutoComplete.setHasFixedSize(false);
                rvRegionAutoComplete.setNestedScrollingEnabled(false);
                cardRegionAutocomplete = customDialog.findViewById(R.id.card_region_autocomplete);
                etCities.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            final CitiesRecyclerAdapter regionRecyclerAdapter;
                            regionRecyclerAdapter = new CitiesRecyclerAdapter(context, DialogFactoryUtils.this.cities);
                            rvRegionAutoComplete.setAdapter(regionRecyclerAdapter);

                        } else {
                            isCardAutoVisible = false;
                            cardRegionAutocomplete.setVisibility(View.GONE);
                        }
                    }
                });


                etCities.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (DialogFactoryUtils.this.cities != null && DialogFactoryUtils.this.cities.size() != 0) {
                            if (s.toString().length() == 0) {
                                CitiesRecyclerAdapter regionRecyclerAdapter = new CitiesRecyclerAdapter(context, DialogFactoryUtils.this.cities);
                                rvRegionAutoComplete.setAdapter(regionRecyclerAdapter);
                                cardRegionAutocomplete.setVisibility(View.GONE);


                            } else {

                                ArrayList<City> displayedRegions = new ArrayList<City>();
                                try {


                                    for (int i = 0; i < DialogFactoryUtils.this.cities.size(); i++) {
                                        if (DialogFactoryUtils.this.cities.get(i).getLocalite().toLowerCase().contains(s.toString().toLowerCase())) {
                                            displayedRegions.add(DialogFactoryUtils.this.cities.get(i));
                                        }
                                    }
                                }catch (NullPointerException e)
                                {

                                }
                                if (displayedRegions.size() == 0) {
                                    cardRegionAutocomplete.setVisibility(View.GONE);
                                } else {
                                    cardRegionAutocomplete.setVisibility(View.VISIBLE);
                                    CitiesRecyclerAdapter regionRecyclerAdapter = new CitiesRecyclerAdapter(context, displayedRegions);
                                    rvRegionAutoComplete.setAdapter(regionRecyclerAdapter);
                                }
                            }

                        }
                    }

                });

                final CitiesRecyclerAdapter regionRecyclerAdapter = new CitiesRecyclerAdapter(context, cities);
                rvCategoryList.setAdapter(regionRecyclerAdapter);



                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onConfirmCitiesSelectListener != null)
                            try {

                                onConfirmCitiesSelectListener.OnConfirmCitiesSelectListener(selectedRegion);
                            } catch (NullPointerException e) {

                            }
                        alertDialog.dismiss();
                    }
                });


                CitiesRecyclerAdapter.setOnTagClickListener(new CitiesRecyclerAdapter.OnTagItemClickListener() {
                    @Override
                    public void onTagItemClickListener(City city) {
                        if (onConfirmCitiesSelectListener != null)
                            try {
                                onConfirmCitiesSelectListener.OnConfirmCitiesSelectListener(city);
                            } catch (NullPointerException e) {

                            }
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {

                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (isCardAutoVisible) {
                                cardRegionAutocomplete.setVisibility(View.GONE);
                                isCardAutoVisible = false;
                            } else {
                                return false;
                            }
                        }
                        return true;
                    }
                });

                alertDialog.show();


            }
        }

    }

    public static void showChatDialog(final Context context){

        final LayoutInflater inflater = LayoutInflater.from(context);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context , R.style.AppCompatAlertDialogStyleSuggest);
        final View customDialog = inflater.inflate(R.layout.dialog_chat, null , false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setView(customDialog);
        RalewayTextView tvRetourChatDialog = customDialog.findViewById(R.id.tv_retour_chat_dialog);
        final RalewayEditText etChatDialog = customDialog.findViewById(R.id.et_send_chat_dialog);
        final RalewayTextView tvEnvoyer = customDialog.findViewById(R.id.tv_confirm_chat_dialog);
        tvRetourChatDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

         etChatDialog.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(final Editable s) {

                 tvEnvoyer.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         if( (s+ "").equalsIgnoreCase("")){
                             new CustomToast(context, context.getResources().getString(R.string.error), context.getResources().getString(R.string.no_chat_text), R.drawable.ic_erreur, CustomToast.ERROR).show();

                         } else {

                             ChatActivity.sendMessage(FirebaseAuth.getInstance().getCurrentUser().getUid() , "123456" , (s+ "") );
                             alertDialog.dismiss();
                         }
                     }
                 });

             }
         });

        alertDialog.show();

    }
    public void showCategoryListDialog(final Context context, List<EditableSubCategory> specialities, List<EditableSubCategory> subCategorieDesfault) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final AlertDialog.Builder mainBuilder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyleSuggest);
        final View custom_dialog = inflater.inflate(
                R.layout.dialog_specialities, null, false);

        final AlertDialog alertDialog = mainBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvConfirm, tvRetour, tvNbrCat, labelTtCategories;
        RecyclerView rvCategoryList;

        alertDialog.setView(custom_dialog);

        tvRetour = custom_dialog.findViewById(R.id.tv_retour_specialities);
        labelTtCategories = custom_dialog.findViewById(R.id.label_tt_categories);
        tvRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvConfirm = custom_dialog.findViewById(R.id.tv_confirm_specialities);

        rvCategoryList = custom_dialog.findViewById(R.id.rv_category_list);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(context));
        rvCategoryList.setHasFixedSize(true);
        final SpecialitiesRecyclerAdapter selectedCategoriesRecyclerAdapter = new SpecialitiesRecyclerAdapter(context, specialities,subCategorieDesfault);
        rvCategoryList.setAdapter(selectedCategoriesRecyclerAdapter);
        labelTtCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedCategoriesRecyclerAdapter.resetSelectedSubCategories();
                onConfirmSubCategoryConfirmListener.OnConfirmSubCategoryConfirmListener(selectedCategoriesRecyclerAdapter.getSelectedSubCategoryMap());
                alertDialog.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onConfirmSubCategoryConfirmListener.OnConfirmSubCategoryConfirmListener(selectedCategoriesRecyclerAdapter.getSelectedSubCategoryMap());
                alertDialog.dismiss();

            }
        });


        alertDialog.show();
    }


    public void showCityListDialog(final Context context, List<Gouvernerat> gouvernerats) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final AlertDialog.Builder mainBuilder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyleSuggest);
        final View custom_dialog = inflater.inflate(
                R.layout.dialog_specialities, null, false);

        final AlertDialog alertDialog = mainBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvConfirm, tvRetour, labelTtCategories;
        RecyclerView rvCategoryList;

        alertDialog.setView(custom_dialog);

        tvRetour = custom_dialog.findViewById(R.id.tv_retour_specialities);
        labelTtCategories = custom_dialog.findViewById(R.id.label_tt_categories);
        tvRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvConfirm = custom_dialog.findViewById(R.id.tv_confirm_specialities);

        rvCategoryList = custom_dialog.findViewById(R.id.rv_category_list);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(context));
        rvCategoryList.setHasFixedSize(true);
        final CityRecyclerAdapter selectedCitiesRecyclerAdapter = new CityRecyclerAdapter(context, gouvernerats);
        rvCategoryList.setAdapter(selectedCitiesRecyclerAdapter);
        labelTtCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedCitiesRecyclerAdapter.resetSelectedCity();
                onConfirmCityConfirmListener.OnConfirmCityConfirmListener(selectedCitiesRecyclerAdapter.getSelectedCityMap());
                alertDialog.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onConfirmCityConfirmListener.OnConfirmCityConfirmListener(selectedCitiesRecyclerAdapter.getSelectedCityMap());
                alertDialog.dismiss();

            }
        });


        alertDialog.show();
    }

    public void setOnConfirmCityConfirmListener(OnConfirmCityConfirmListener onConfirmCityConfirmListener) {
        this.onConfirmCityConfirmListener = onConfirmCityConfirmListener;
    }



    public interface OnConfirmCityConfirmListener {
        void OnConfirmCityConfirmListener(HashMap<Integer, Gouvernerat> selectedCityMap);
    }



    public void setOnConfirmSubCategoryConfirmListener(OnConfirmSubCategoryConfirmListener onConfirmSubCategoryConfirmListener) {
        this.onConfirmSubCategoryConfirmListener = onConfirmSubCategoryConfirmListener;
    }



    public interface OnConfirmSubCategoryConfirmListener {
        void OnConfirmSubCategoryConfirmListener(HashMap<Integer, EditableSubCategory> selectedSubCategoryMap);
    }
    public void setOnConfirmCitiesSelectListener(OnConfirmCitiesSelect onConfirmRegionSelectListener) {
        this.onConfirmCitiesSelectListener = onConfirmRegionSelectListener;
    }



    public interface OnConfirmCitiesSelect {
        void OnConfirmCitiesSelectListener(City city);
    }



}
