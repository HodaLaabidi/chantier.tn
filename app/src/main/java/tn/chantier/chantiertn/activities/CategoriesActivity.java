package tn.chantier.chantiertn.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.adapters.CategoryAdapter;
import tn.chantier.chantiertn.adapters.ChipViewAdapter;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.models.Category;
import tn.chantier.chantiertn.models.EditableSubCategory;
import tn.chantier.chantiertn.models.SubCategory;
import tn.chantier.chantiertn.models.PackChip;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

public class CategoriesActivity extends AppCompatActivity{

    private static final String TAG_ACT ="CategoriesActivity";
    private static final int CATEGORIES_TIME_OUT = 2000;
    private static ArrayList<EditableSubCategory> editableSubCategories = new ArrayList<>();

    @BindView(R.id.rv_categories)
    RecyclerView recyclerViewCategories;
    @BindView(R.id.categories_progress_bar)
    ProgressBar categoriesProgressBar;
    @BindView(R.id.chip_layout)
    ChipView categoriesChipView ;
    @BindView(R.id.text_action_categories)
    RalewayTextView textActionCategories;
    @BindView(R.id.button_validate_categories)
    LinearLayout buttonValidateCategories;
    ArrayList<SubCategory> checkBoxes = new ArrayList<>();
    ArrayList<Category> listCategory = new ArrayList<Category>();
    private final int MAX_LIMIT = 20 ;
    private  int count = 0 ;
    String editCategories ;
    @BindView(R.id.arrow_back_categories)
    ImageView arrowBackCategories ;


    // Alarme & Sécurité
    @BindView(R.id.checkbox_pointage)
    CheckBox checkBoxPointage ;
    @BindView(R.id.checkbox_alarme_anti_intrusion)
    CheckBox checkBoxAlarmeAntiIntrusion ;
    @BindView(R.id.checkbox_automatisme_portes)
    CheckBox checkBoxAutomatismePortes ;
    @BindView(R.id.checkbox_controle_acces)
    CheckBox checkBoxControleAcces ;
    @BindView(R.id.checkbox_detection_incendies)
    CheckBox checkBoxDetectionIncendies ;
    @BindView(R.id.checkbox_extinteur)
    CheckBox checkBoxEctincteur ;
    @BindView(R.id.checkbox_interphone_videophone)
    CheckBox checkBoxInterphoneVideophone;
    @BindView(R.id.checkbox_videosurveillance)
    CheckBox checkBoxVideosurveillance ;
    @BindView(R.id.checkbox_gardiennage)
    CheckBox checkBoxGardiennage ;


    // Cloison et faux plafond
    @BindView(R.id.checkbox_cloison_aluminium)
    CheckBox checkCloisonAluminium ;
    @BindView(R.id.checkbox_cloison_platre)
    CheckBox checkBoxCloisonPlatre ;
    @BindView(R.id.checkbox_cloison_mobile)
    CheckBox checkBoxCloisonMobile ;
    @BindView(R.id.checkbox_faux_plafonds)
    CheckBox checkBoxFauxPlafond ;
    @BindView(R.id.checkbox_plafond_plaque_platre)
    CheckBox checkBoxPlafondPlaqueDePlatre ;

    // Conception et études
    @BindView(R.id.checkbox_architecte)
    CheckBox checkBoxArchitecte ;
    @BindView(R.id.checkbox_architecte_interieur)
    CheckBox checkBoxArchitecteInterieur ;
    @BindView(R.id.checkbox_bureau_etudes)
    CheckBox checkBoxBureauDEtudes ;
    @BindView(R.id.checkbox_designer)
    CheckBox checkBoxDesigner ;

    // Construction & Rénovation
    @BindView(R.id.checkbox_agencement)
    CheckBox checkBoxAgencement ;
    @BindView(R.id.checkbox_charpentes)
    CheckBox checkBoxCharpentes ;
    @BindView(R.id.checkbox_construction_ComInd)
    CheckBox checkBoxConstructionComInd ;
    @BindView(R.id.checkbox_construction_legere)
    CheckBox checkBoxConstructionLegere ;
    @BindView(R.id.checkbox_construction_maison)
    CheckBox checkBoxConstructionMaison;
    @BindView(R.id.checkbox_renovation_maison)
    CheckBox checkBoxRenovationMaison ;

    // Cuisine & salle de bains
    @BindView(R.id.checkbox_electromenager_cuisine)
    CheckBox checkBoxElectromenagerCuisine ;
    @BindView(R.id.checkbox_fourniture_salle_de_bain)
    CheckBox checkBoxFournitureSalleDeBain ;
    @BindView(R.id.checkbox_meuble_cuisine)
    CheckBox checkBoxMeubleCuisine ;


    // Domotique
    @BindView(R.id.checkbox_controle_audio_video)
    CheckBox checkBoxControleAudioVideo ;
    @BindView(R.id.checkbox_controle_porte_portail_garage)
    CheckBox checkBoxControlePortePortailGarage ;
    @BindView(R.id.checkbox_controle_eclairages)
    CheckBox checkBoxControleEclairages ;
    @BindView(R.id.checkbox_controle_volets)
    CheckBox checkBoxControleVolets ;
    @BindView(R.id.checkbox_controle_chauffage)
    CheckBox checkBoxControleChauffage;
    @BindView(R.id.checkbox_gestion_consommations)
    CheckBox checkBoxGestionConsommations ;

    // Electricité et éclairage
    @BindView(R.id.checkbox_eclairage_exterieur)
    CheckBox checkBoxEclairageExterieur ;
    @BindView(R.id.checkbox_eclairage_interieur)
    CheckBox checkBoxEclairageInterieur ;
    @BindView(R.id.checkbox_fourniture_electricite)
    CheckBox checkBoxFournitureElectricite ;
    @BindView(R.id.checkbox_installation_electrique)
    CheckBox checkBoxInstallationElectrique ;
    @BindView(R.id.checkbox_renovation_installation_electrique)
    CheckBox checkBoxRenovationInstallationElectrique;

    // Energies renouvelables
    @BindView(R.id.checkbox_chauffage_eau_solaire)
    CheckBox checkBoxChauffageEauSolaire ;
    @BindView(R.id.checkbox_photovoltaique)
    CheckBox checkBoxPhotovoltaique ;

    // Isolation & étanchéité
    @BindView(R.id.checkbox_etancheite)
    CheckBox checkBoxEtancheite ;
    @BindView(R.id.checkbox_isolation_acoustique)
    CheckBox checkBoxIsolationAcoustique ;
    @BindView(R.id.checkbox_isolation_thermique)
    CheckBox checkBoxIsolationThermique ;


    // Jardin & piscine
    @BindView(R.id.checkbox_abris_voiture)
    CheckBox checkBoxAbrisVoiture ;
    @BindView(R.id.checkbox_entretien_maintenance_piscine)
    CheckBox checkBoxEntretienMaintenancePiscine ;
    @BindView(R.id.checkbox_fourniture_accessoire_piscine)
    CheckBox checkBoxFournitureAccessoirePiscine ;
    @BindView(R.id.checkbox_paysagiste)
    CheckBox checkBoxPaysagiste ;
    @BindView(R.id.checkbox_pergola)
    CheckBox checkBoxPergola ;
    @BindView(R.id.checkbox_piscine_beton)
    CheckBox checkBoxPiscineBeton ;
    @BindView(R.id.checkbox_piscine_kit)
    CheckBox checkBoxPiscineKit ;

    // Peinture
    @BindView(R.id.checkbox_peinture_decorative)
    CheckBox checkBoxPeintureDecorative ;
    @BindView(R.id.checkbox_peinture_standard_exterieur)
    CheckBox checkBoxPeintureStandardExterieur ;
    @BindView(R.id.checkbox_peinture_standard_interieur)
    CheckBox checkBoxPeintureStandardInterieur ;

    // Plomberie, chaud, froid
    @BindView(R.id.checkbox_chauffage)
    CheckBox checkBoxChauffage ;
    @BindView(R.id.checkbox_climatisation)
    CheckBox checkBoxClimatisation ;
    @BindView(R.id.checkbox_climatisation_centrale)
    CheckBox checkBoxClimatisationCentrale ;
    @BindView(R.id.checkbox_plancher_chauffant)
    CheckBox checkBoxPlancherChauffant ;
    @BindView(R.id.checkbox_plomberie)
    CheckBox checkBoxPlomberie ;

    // Portail, porte et fenêtre
    @BindView(R.id.checkbox_menuiserie_aluminium)
    CheckBox checkBoxMenuiserieAluminium ;
    @BindView(R.id.checkbox_menuiserie_pvc)
    CheckBox checkBoxMenuiseriePVC ;
    @BindView(R.id.checkbox_placard_dressing)
    CheckBox checkBoxPlacardDressing ;
    @BindView(R.id.checkbox_portail)
    CheckBox checkBoxPortail ;
    @BindView(R.id.checkbox_porte_blindee)
    CheckBox checkBoxPorteBlindee ;
    @BindView(R.id.checkbox_porte_coulissante_interieur)
    CheckBox checkBoxPorteCoulissanteInterieur ;
    @BindView(R.id.checkbox_porte_interieur)
    CheckBox checkBoxPorteInterieur ;
    @BindView(R.id.checkbox_porte_garage)
    CheckBox checkBoxPorteGarage ;
    @BindView(R.id.checkbox_rampe_garde_corps)
    CheckBox checkBoxRampeGardeCorps ;
    @BindView(R.id.checkbox_portes_entree)
    CheckBox checkPortesEntree ;

    // Revêtement mur
    @BindView(R.id.checkbox_revetement_mur_bois)
    CheckBox checkBoxRevetementMurBois ;
    @BindView(R.id.checkbox_revetement_mur_faience)
    CheckBox checkBoxRevetementMurFaience ;
    @BindView(R.id.checkbox_revetement_mur_gres)
    CheckBox checkBoxRevetementMurGres ;
    @BindView(R.id.checkbox_revetement_mur_marbre)
    CheckBox checkBoxRevetementMurMarbre ;
    @BindView(R.id.checkbox_revetement_mur_papier_peint)
    CheckBox checkBoxRevetementMurPapierPeint ;
    @BindView(R.id.checkbox_revetement_mur_pierre)
    CheckBox checkBoxRevetementMurPierre ;

    // Revêtement sol
    @BindView(R.id.checkbox_revetement_sols)
    CheckBox checkBoxRevetemenSols ;
    @BindView(R.id.checkbox_revetement_sol_carrelage)
    CheckBox checkBoxRevetementSolCarrelage ;
    @BindView(R.id.checkbox_revetement_sol_gres)
    CheckBox checkBoxRevetementSolGres ;
    @BindView(R.id.checkbox_revetement_sol_marbre)
    CheckBox checkBoxRevetementSolMarbre ;
    @BindView(R.id.checkbox_revetement_sol_parquet)
    CheckBox checkBoxRevetementSolParquet ;
    @BindView(R.id.checkbox_revetement_sol_resine)
    CheckBox checkBoxRevetementSolResine ;
    @BindView(R.id.checkbox_revetement_sol_beton_cire)
    CheckBox checkBoxRevetementSolBetonCire ;
    @BindView(R.id.checkbox_revetement_moquete_PVC)
    CheckBox checkBoxRevetementMoquettePVC ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ButterKnife.bind(this);
        editCategories = getIntent().getStringExtra("editCategories");
        arrowBackCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCategories != null){
                    if ( editCategories.equalsIgnoreCase("yes")){
                        Intent intent = new Intent (CategoriesActivity.this , HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent (CategoriesActivity.this , InscriptionActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        setLayoutsCheckBoxes();
       // setLayoutsCategories();


        testCategoriesFunctionnality();


    }

    private void testCategoriesFunctionnality() {


        Log.e("editCategories" , editCategories + "!");
        if (editCategories != null ) {
            if (editCategories.equalsIgnoreCase("yes")) {
                Log.e("test from editableSS" , "OK");
                textActionCategories.setText("Modifier");
               setSelectedLayouts();
                testMaxSelectedItems();
                testMaxSelectedItems();
                textActionCategories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCategoriesWebService();
                    }
                });

                buttonValidateCategories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCategoriesWebService();
                    }
                });
            }
        } else {

            testMaxSelectedItems();
            textActionCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendCategoriesWebService();
                }
            });

            buttonValidateCategories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendCategoriesWebService();
                }
            });

        }
    }

    private void sendCategoriesWebService() {

        JsonObject postParams= new JsonObject();
        JsonArray jsonArray = new JsonArray();


        for (int i = 0 ; i < checkBoxes.size() ; i++) {
            if (checkBoxes.get(i).getCheckBox().isChecked()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id_secteur", checkBoxes.get(i).getId());
                jsonArray.add(jsonObject);
                Log.e("jsonobject", " ! " + jsonObject.toString());
                Log.e("jsonobject id", " ! " + jsonObject.toString());
            }

        }
            for( int j =0; j <jsonArray.size() ; j++){
                Log.e ("jsonArray "+ j , jsonArray.get(j).toString()+ "!");
            }
            if (jsonArray.size() == 0 ){
                new CustomToast(CategoriesActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.empty_webService_values), R.drawable.ic_erreur, CustomToast.ERROR).show();


            } else {
                // test id from inscriptionactivity
                if(getIntent().getStringExtra("id_new_client") != null ) {
                    postParams.addProperty("id_client",   getIntent().getStringExtra("id_new_client"));
                } else {
                    postParams.addProperty("id_client", SharedPreferencesFactory.retrieveUserData().getId() + "");
                }
                postParams.add("secteurs", jsonArray);
                if ( editCategories != null){
                    if ( editCategories.equalsIgnoreCase("yes")){
                        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().editSubCategories(postParams);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    Log.e("categories web service ", response.code() + "!");
                                    Log.e("categories web service ", response.body().toString() + "!");
                                    final LayoutInflater inflater = LayoutInflater.from(CategoriesActivity.this);
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesActivity.this, R.style.AppCompatAlertDialogStyleSuggest);
                                    final View customDialog = inflater.inflate(R.layout.pop_up_reset_password, null, false);
                                    RalewayTextView textCongratulations = customDialog.findViewById(R.id.text_congratulations);
                                    textCongratulations.setText(" Votre demande de changement de spécialités est bien reçue. Votre nouvelle sélection sera validée dans les plus brefs délais.");
                                    final AlertDialog alertDialog = builder.create();
                                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    alertDialog.setView(customDialog);

                                    alertDialog.show();
                                    LinearLayout goToConnexionButton = customDialog.findViewById(R.id.btn_go_to_connexion);
                                    goToConnexionButton.setVisibility(View.GONE);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            alertDialog.dismiss();
                                            Intent intent = new Intent(CategoriesActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }, CATEGORIES_TIME_OUT);
                                } else {
                                    new CustomToast(CategoriesActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.technical_error_alert), R.drawable.ic_erreur, CustomToast.ERROR).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }

                } else {
                    Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().addSubCategories(postParams);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Log.e("categories web service ", response.code() + "!");
                                Log.e("categories web service ", response.body().toString() + "!");
                                final LayoutInflater inflater = LayoutInflater.from(CategoriesActivity.this);
                                final AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesActivity.this, R.style.AppCompatAlertDialogStyleSuggest);
                                final View customDialog = inflater.inflate(R.layout.pop_up_reset_password, null, false);
                                RalewayTextView textCongratulations = customDialog.findViewById(R.id.text_congratulations);
                                textCongratulations.setText(" Votre compte a bien été validé");
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.setView(customDialog);

                                alertDialog.show();
                                LinearLayout goToConnexionButton = customDialog.findViewById(R.id.btn_go_to_connexion);
                                goToConnexionButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                        Intent intent = new Intent(CategoriesActivity.this, ConnexionActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                });

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        alertDialog.dismiss();
                                        Intent intent = new Intent(CategoriesActivity.this, ConnexionActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                }, CATEGORIES_TIME_OUT);
                            } else {
                                new CustomToast(CategoriesActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.technical_error_alert), R.drawable.ic_erreur, CustomToast.ERROR).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }
            }


        }


    private void setSelectedLayouts() {

        JsonObject postParams = new JsonObject();
        postParams.addProperty("id_client" , SharedPreferencesFactory.retrieveUserData().getId()+"");
        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getAllSelectedCategories(postParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){

                    JSONArray jsonArray = null ;
                    Gson gson = Utils.getGsonInstance();
                    try {
                        String remoteResponse = response.body().string()+"";
                        jsonArray = new JSONArray(remoteResponse);
                        Type type = new TypeToken<ArrayList<EditableSubCategory>>(){

                        }.getType();
                        editableSubCategories = gson.fromJson(jsonArray.toString(), type);
                        setCheckedCheckBoxes(editableSubCategories);
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

    private void setCheckedCheckBoxes(ArrayList<EditableSubCategory> editableSubCategories) {

        for (int i = 0 ; i< editableSubCategories.size() ; i++ ){
            for (int j = 0 ; j < checkBoxes.size() ; j++){
                if (editableSubCategories.get(i).getId_act().equalsIgnoreCase(checkBoxes.get(j).getId())){
                    checkBoxes.get(j).getCheckBox().setChecked(true);
                }
            }
        }

        count = editableSubCategories.size();

        Log.e("editable count" , count+"!");
        for (int i = 0 ; i < editableSubCategories.size() ; i++){
        Log.e("ediatbleSubcategories("+i+")",editableSubCategories.get(i).getId());
        }
    }

    private void testMaxSelectedItems() {


        for (int i = 0 ; i < checkBoxes.size() ; i++){
            checkboxTest(checkBoxes.get(i).getCheckBox());
        }
            }

    private void checkboxTest(final CheckBox checkBox) {


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if ((count == MAX_LIMIT) &&  isChecked){

                    buttonView.setChecked(false);

                        new CustomToast(CategoriesActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.Warning), R.drawable.ic_warning, CustomToast.WARNING).show();



                } else if (isChecked){
                    if (count == 1 && editCategories != null  ){

                    }
                    count ++;
                } else if (! isChecked){
                    count --;
                }
            }
        });
       /* checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    if (count == 3) {
                        //checkBox.setEnabled(false);
                        for (int i = 0 ; i < checkBoxes.size() ; i++){
                            if (checkBoxes.get(i) != checkBox) {
                                checkBoxes.get(i).setEnabled(false);
                            }
                            new CustomToast(CategoriesActivity.this, getResources().getString(R.string.warning), getResources().getString(R.string.Warning), R.drawable.ic_warning, CustomToast.WARNING).show();

                        }
                    } else {
                        if (!checkBox.isEnabled()){
                            checkBox.setEnabled(true);
                        }

                        count++;
                    }

                    Log.e("count checkbox", count + "");
                } else {
                    if (!checkBox.isEnabled()){
                        checkBox.setEnabled(true);
                    }
                    count--;
                    Log.e("count checkbox", count + "");
                }


            }
        });*/
    }


    private void setLayoutsCheckBoxes() {
        // Alarme er sécurité
        changeCheckboxColor(checkBoxAlarmeAntiIntrusion);
        changeCheckboxColor(checkBoxAutomatismePortes);
        changeCheckboxColor(checkBoxControleAcces);
        changeCheckboxColor(checkBoxDetectionIncendies);
        changeCheckboxColor(checkBoxEctincteur);
        changeCheckboxColor(checkBoxInterphoneVideophone);
        changeCheckboxColor(checkBoxPointage);
        changeCheckboxColor(checkBoxVideosurveillance);
        changeCheckboxColor(checkBoxGardiennage);

        // Cloison et faux plafond

        changeCheckboxColor(checkBoxArchitecte);
        changeCheckboxColor(checkBoxCloisonPlatre);
        changeCheckboxColor(checkBoxCloisonMobile);
        changeCheckboxColor(checkBoxFauxPlafond);
        changeCheckboxColor(checkBoxPlafondPlaqueDePlatre);

        // Conception et études

        changeCheckboxColor(checkCloisonAluminium);
        changeCheckboxColor(checkBoxArchitecteInterieur);
        changeCheckboxColor(checkBoxBureauDEtudes);
        changeCheckboxColor(checkBoxDesigner);

        // Construction & Rénovation

        changeCheckboxColor(checkBoxAgencement);
        changeCheckboxColor(checkBoxCharpentes);
        changeCheckboxColor(checkBoxConstructionComInd);
        changeCheckboxColor(checkBoxConstructionLegere);
        changeCheckboxColor(checkBoxConstructionMaison);
        changeCheckboxColor(checkBoxRenovationMaison);

        // Cuisine & salle de bains

        changeCheckboxColor(checkBoxElectromenagerCuisine);
        changeCheckboxColor(checkBoxFournitureSalleDeBain);
        changeCheckboxColor(checkBoxMeubleCuisine);

        // Domotique

        changeCheckboxColor(checkBoxControleAudioVideo);
        changeCheckboxColor(checkBoxControlePortePortailGarage);
        changeCheckboxColor(checkBoxControleEclairages);
        changeCheckboxColor(checkBoxControleVolets);
        changeCheckboxColor(checkBoxControleChauffage);
        changeCheckboxColor(checkBoxGestionConsommations);

        // Electricité et éclairage

        changeCheckboxColor(checkBoxEclairageExterieur);
        changeCheckboxColor(checkBoxEclairageInterieur);
        changeCheckboxColor(checkBoxFournitureElectricite);
        changeCheckboxColor(checkBoxInstallationElectrique);
        changeCheckboxColor(checkBoxRenovationInstallationElectrique);

        // Energies renouvelables

        changeCheckboxColor(checkBoxChauffageEauSolaire);
        changeCheckboxColor(checkBoxPhotovoltaique);

        // Isolation & étanchéité

        changeCheckboxColor(checkBoxEtancheite);
        changeCheckboxColor(checkBoxIsolationAcoustique);
        changeCheckboxColor(checkBoxIsolationThermique);

        // Jardin et piscine

        changeCheckboxColor(checkBoxAbrisVoiture);
        changeCheckboxColor(checkBoxEntretienMaintenancePiscine);
        changeCheckboxColor(checkBoxFournitureAccessoirePiscine);
        changeCheckboxColor(checkBoxPaysagiste);
        changeCheckboxColor(checkBoxPergola);
        changeCheckboxColor(checkBoxPiscineBeton);
        changeCheckboxColor(checkBoxPiscineKit);

        // Peinture

        changeCheckboxColor(checkBoxPeintureDecorative);
        changeCheckboxColor(checkBoxPeintureStandardExterieur);
        changeCheckboxColor(checkBoxPeintureStandardInterieur);

        // Plomberie, chaud , froid

        changeCheckboxColor(checkBoxChauffage);
        changeCheckboxColor(checkBoxClimatisation);
        changeCheckboxColor(checkBoxClimatisationCentrale);
        changeCheckboxColor(checkBoxPlancherChauffant);
        changeCheckboxColor(checkBoxPlomberie);

        // Portail, porte et fenêtre

        changeCheckboxColor(checkBoxMenuiserieAluminium);
        changeCheckboxColor(checkBoxMenuiseriePVC);
        changeCheckboxColor(checkBoxPlacardDressing);
        changeCheckboxColor(checkBoxPortail);
        changeCheckboxColor(checkBoxPorteBlindee);
        changeCheckboxColor(checkBoxPorteCoulissanteInterieur);
        changeCheckboxColor(checkBoxPorteInterieur);
        changeCheckboxColor(checkBoxPorteGarage);
        changeCheckboxColor(checkBoxRampeGardeCorps);
        changeCheckboxColor(checkPortesEntree);

        // Revêtement mur

        changeCheckboxColor(checkBoxRevetementMurBois);
        changeCheckboxColor(checkBoxRevetementMurFaience);
        changeCheckboxColor(checkBoxRevetementMurGres);
        changeCheckboxColor(checkBoxRevetementMurMarbre);
        changeCheckboxColor(checkBoxRevetementMurPapierPeint);
        changeCheckboxColor(checkBoxRevetementMurPierre);


        // Revêtement sol

        changeCheckboxColor(checkBoxRevetemenSols);
        changeCheckboxColor(checkBoxRevetementSolCarrelage);
        changeCheckboxColor(checkBoxRevetementSolGres);
        changeCheckboxColor(checkBoxRevetementSolMarbre);
        changeCheckboxColor(checkBoxRevetementSolParquet);
        changeCheckboxColor(checkBoxRevetementSolResine);
        changeCheckboxColor(checkBoxRevetementSolBetonCire);
        changeCheckboxColor(checkBoxRevetementMoquettePVC);
                    // -----------------------------------------------------------------------

        // Alarme er sécurité

        checkBoxes.add(new SubCategory("38",checkBoxAlarmeAntiIntrusion));
        checkBoxes.add(new SubCategory("44",checkBoxAutomatismePortes));
        checkBoxes.add(new SubCategory("39",checkBoxControleAcces));
        checkBoxes.add(new SubCategory("40",checkBoxDetectionIncendies));
        checkBoxes.add(new SubCategory("41",checkBoxEctincteur));
        checkBoxes.add(new SubCategory("91",checkBoxInterphoneVideophone));
        checkBoxes.add(new SubCategory("92",checkBoxPointage));
        checkBoxes.add(new SubCategory("43",checkBoxVideosurveillance));
        checkBoxes.add(new SubCategory("42",checkBoxGardiennage));

        // Cloison et faux plafond
        checkBoxes.add(new SubCategory("100",checkCloisonAluminium));
        checkBoxes.add(new SubCategory("101",checkBoxCloisonPlatre));
        checkBoxes.add(new SubCategory("102",checkBoxCloisonMobile));
        checkBoxes.add(new SubCategory("103",checkBoxFauxPlafond));
        checkBoxes.add(new SubCategory("104",checkBoxPlafondPlaqueDePlatre));

        // Conception et études

        checkBoxes.add(new SubCategory("78",checkBoxArchitecte));
        checkBoxes.add(new SubCategory("79",checkBoxArchitecteInterieur));
        checkBoxes.add(new SubCategory("81",checkBoxBureauDEtudes));
        checkBoxes.add(new SubCategory("80",checkBoxDesigner));

        // Construction & Rénovation
        checkBoxes.add(new SubCategory("7",checkBoxAgencement));
        checkBoxes.add(new SubCategory("85",checkBoxCharpentes));
        checkBoxes.add(new SubCategory("2",checkBoxConstructionComInd));
        checkBoxes.add(new SubCategory("84",checkBoxConstructionLegere));
        checkBoxes.add(new SubCategory("1",checkBoxConstructionMaison));
        checkBoxes.add(new SubCategory("4",checkBoxRenovationMaison));

        // Cuisine & salle de bain
        checkBoxes.add(new SubCategory("12",checkBoxElectromenagerCuisine));
        checkBoxes.add(new SubCategory("9",checkBoxFournitureSalleDeBain));
        checkBoxes.add(new SubCategory("8",checkBoxMeubleCuisine));

        // Domotique
        checkBoxes.add(new SubCategory("93",checkBoxControleAudioVideo));
        checkBoxes.add(new SubCategory("94",checkBoxControlePortePortailGarage));
        checkBoxes.add(new SubCategory("95",checkBoxControleEclairages));
        checkBoxes.add(new SubCategory("96",checkBoxControleVolets));
        checkBoxes.add(new SubCategory("97",checkBoxControleChauffage));
        checkBoxes.add(new SubCategory("98",checkBoxGestionConsommations));

        // Electricité & éclairage

        checkBoxes.add(new SubCategory("29",checkBoxEclairageExterieur));
        checkBoxes.add(new SubCategory("113",checkBoxEclairageInterieur));
        checkBoxes.add(new SubCategory("28",checkBoxFournitureElectricite));
        checkBoxes.add(new SubCategory("30",checkBoxInstallationElectrique));
        checkBoxes.add(new SubCategory("114",checkBoxRenovationInstallationElectrique));

        // Energies renouvelables
        checkBoxes.add(new SubCategory("59",checkBoxChauffageEauSolaire));
        checkBoxes.add(new SubCategory("60",checkBoxPhotovoltaique));

        // Isolation & étanchéité
        checkBoxes.add(new SubCategory("26",checkBoxEtancheite));
        checkBoxes.add(new SubCategory("25",checkBoxIsolationAcoustique));
        checkBoxes.add(new SubCategory("99",checkBoxIsolationThermique));

        // Jardin & piscine

        checkBoxes.add(new SubCategory("50",checkBoxAbrisVoiture));
        checkBoxes.add(new SubCategory("55",checkBoxEntretienMaintenancePiscine));
        checkBoxes.add(new SubCategory("54",checkBoxFournitureAccessoirePiscine));
        checkBoxes.add(new SubCategory("56",checkBoxPaysagiste));
        checkBoxes.add(new SubCategory("105",checkBoxPergola));
        checkBoxes.add(new SubCategory("52",checkBoxPiscineBeton));
        checkBoxes.add(new SubCategory("53",checkBoxPiscineKit));

        // Peinture

        checkBoxes.add(new SubCategory("35",checkBoxPeintureDecorative));
        checkBoxes.add(new SubCategory("34",checkBoxPeintureStandardExterieur));
        checkBoxes.add(new SubCategory("108",checkBoxPeintureStandardInterieur));

        // Plomberie, chaud, froid

        checkBoxes.add(new SubCategory("23",checkBoxChauffage));
        checkBoxes.add(new SubCategory("24",checkBoxClimatisation));
        checkBoxes.add(new SubCategory("110",checkBoxClimatisationCentrale));
        checkBoxes.add(new SubCategory("87",checkBoxPlancherChauffant));
        checkBoxes.add(new SubCategory("22",checkBoxPlomberie));

        // Portail, portes et fenêtre
        checkBoxes.add(new SubCategory("47",checkBoxMenuiserieAluminium));
        checkBoxes.add(new SubCategory("48",checkBoxMenuiseriePVC));
        checkBoxes.add(new SubCategory("49",checkBoxPlacardDressing));
        checkBoxes.add(new SubCategory("88",checkBoxPortail));
        checkBoxes.add(new SubCategory("106",checkBoxPorteBlindee));
        checkBoxes.add(new SubCategory("107",checkBoxPorteCoulissanteInterieur));
        checkBoxes.add(new SubCategory("46",checkBoxPorteInterieur));
        checkBoxes.add(new SubCategory("109",checkBoxPorteGarage));
        checkBoxes.add(new SubCategory("86",checkBoxRampeGardeCorps));
        checkBoxes.add(new SubCategory("45",checkPortesEntree));

        // Revêtement mur
        checkBoxes.add(new SubCategory("19",checkBoxRevetementMurBois));
        checkBoxes.add(new SubCategory("18",checkBoxRevetementMurFaience));
        checkBoxes.add(new SubCategory("111",checkBoxRevetementMurGres));
        checkBoxes.add(new SubCategory("83",checkBoxRevetementMurMarbre));
        checkBoxes.add(new SubCategory("21",checkBoxRevetementMurPapierPeint));
        checkBoxes.add(new SubCategory("112",checkBoxRevetementMurPierre));

        // Revêtement sol

        checkBoxes.add(new SubCategory("17",checkBoxRevetemenSols));
        checkBoxes.add(new SubCategory("16",checkBoxRevetementSolCarrelage));
        checkBoxes.add(new SubCategory("14",checkBoxRevetementSolGres));
        checkBoxes.add(new SubCategory("82",checkBoxRevetementSolMarbre));
        checkBoxes.add(new SubCategory("15",checkBoxRevetementSolParquet));
        checkBoxes.add(new SubCategory("89",checkBoxRevetementSolResine));
        checkBoxes.add(new SubCategory("13",checkBoxRevetementSolBetonCire));
        checkBoxes.add(new SubCategory("115", checkBoxRevetementMoquettePVC));


    }

    private void setLayoutsCategories() {

        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getCategoriesService();
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e("sucess", response.body() + "");
                categoriesProgressBar.setVisibility(View.VISIBLE);
                if (response.code() == 200) {
                    categoriesProgressBar.setVisibility(View.GONE);
                    Log.e("sucess", response.body() + "");

                    Gson gson =  Utils.getGsonInstance();
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        Type type = new TypeToken<ArrayList<Category>>() {
                        }.getType();
                        listCategory = gson.fromJson(jsonArray.toString(), type);
                        Log.e("list size ", listCategory.size() + "");
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(CategoriesActivity.this);
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerViewCategories.setHasFixedSize(false);
                        recyclerViewCategories.setLayoutManager(mLayoutManager);
                        final CategoryAdapter categoryAdapter = new CategoryAdapter(CategoriesActivity.this, listCategory);
                        recyclerViewCategories.setAdapter(categoryAdapter);
                        for (int index = 0; index < listCategory.size(); index++) {
                            // browse subCategories for each item of listCategory
                            //browseItemChips(index);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    // progress bar gone and show a custom pop up
                    categoriesProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // progress bar gone and show a custom pop up
                categoriesProgressBar.setVisibility(View.GONE);

            }
        });
    }





    // here we draw chips ( items subCategories ) for one Category

    private void changeCheckboxColor(CheckBox checkBox) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        ContextCompat.getColor(checkBox.getContext(), R.color.colorYellow),
                        ContextCompat.getColor(checkBox.getContext(), R.color.colorYellow)
                });
        checkBox.setButtonTintList(colorStateList);
    }

    private void drawChips(ArrayList<SubCategory> listSubCategories) {
        List<Chip> listChips = new ArrayList<>() ;
        for( SubCategory sC: listSubCategories){
            listChips.add(new PackChip());
        }
        ChipViewAdapter subCategoryChipViewAdapter = new ChipViewAdapter(getBaseContext() ,listSubCategories );
        subCategoryChipViewAdapter.setChipList(listChips);
        categoriesChipView.setAdapter(subCategoryChipViewAdapter);



            // here we use  the adapter  and the layout manager to set items in the recycler view

    }

    @Override
    public void onBackPressed() {

        if (editCategories != null ) {
            if (editCategories.equalsIgnoreCase("yes")) {
                Intent intent = new Intent(CategoriesActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(CategoriesActivity.this, InscriptionActivity.class);
            startActivity(intent);
            finish();

        }
    }

    private ArrayList<SubCategory> browseAllSubCategories() {

        Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().getAllSubCategoriesService();
        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    Log.e(TAG_ACT , " list subCatégories response" + response.body().toString());

                    Gson gson = new Gson();
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        Type type = new TypeToken<ArrayList<SubCategory>>() {
                        }.getType();
                        ArrayList<SubCategory> listSubCategories = gson.fromJson(jsonArray.toString(), type) , listSubCategories2 = new ArrayList<>();
                        for(int index = 0 ; index < listSubCategories.size() ; index++) {

                            Log.e(TAG_ACT , " list subCatégories response size" + listSubCategories.size());
                           listSubCategories2.add(listSubCategories2.size(), listSubCategories.get(index));

                            ArrayList<SubCategory>  listChips = new ArrayList<SubCategory>();
                            Log.e(TAG_ACT , " listSubCategories size = "+ listSubCategories2.size());
                            /*if (listSubCategories2.size() > 0) {
                                for (int indexSubCategories = 0; indexSubCategories < listSubCategories2.size(); indexSubCategories++) {
                                    if (listCategory.size() > 0){
                                        Log.e(TAG_ACT , " listChips size = "+ listChips.size());

                                        if ( listSubCategories2.get(indexCategories).getId() == listSubCategories2.get(indexSubCategories).getId_sec()){

                                            listChips.add(listSubCategories2.get(indexSubCategories));
                                        }
                                    }
                                }
                                if (listChips.size() > 0){
                                    drawChips(listChips);
                                } else {
                                    // show a  layout to invert the user that there is no item in this Category
                                }
                            }*/
                        }
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
   // only return null to not get  syntaxe errors in compilation !!!!
      return null ;
    }

}
