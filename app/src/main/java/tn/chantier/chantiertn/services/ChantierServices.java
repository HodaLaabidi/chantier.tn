package tn.chantier.chantiertn.services;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tn.chantier.chantiertn.models.Contacts;

public interface ChantierServices {

    public static final String DATA_ENDPOINT = "https://www.chantier.tn/mobile/";

    public static final String DATA_ENDPOINT_BROWSE_CONTACT = "https://www.chantier.tn/leads/";

    @GET("listesecteurs")
    Call<ResponseBody> getCategoriesService();

    @POST("registerpro")
    Call<ResponseBody> inscriptionChantierService(  @Body JsonObject requestObj);

    @GET("listeActivites")
    Call<ResponseBody>  getAllSubCategoriesService();

    @POST("login")
    Call<ResponseBody>  connexionChantierService(@Body JsonObject requestObj);
    @POST ("mySpace")
    Call <ResponseBody> getAllOffers(@Body JsonObject requestObj);
    @POST("reset")
    Call<ResponseBody> sendEmailReset(@Body JsonObject requestObject);

    @GET("villes")
    Call<ResponseBody> getCodeCities();

    @POST("getDemande")
    Call<ResponseBody> getOfferDetails(@Body JsonObject requestObject);
    @POST("afficherContact")
    Call<ResponseBody> browseContactValues(@Body JsonObject requesrObject);
    @POST("listeSuivi")
    Call<ResponseBody> getFollowedOffers(@Body JsonObject  id_client  );
    @POST("AddSecteurs")
    Call<ResponseBody> addSubCategories(@Body JsonObject requestObject);

    @POST("EditSecteurs")
    Call<ResponseBody> editSubCategories(@Body JsonObject requestObject);

    @POST ("demandePack")
    Call<ResponseBody> sendPackRequest(@Body JsonObject requestObject);

    @POST ("specialites")
    Call<ResponseBody>  getAllSelectedCategories(@Body JsonObject requestObject);

    @POST("updateregtoken")

    Call<ResponseBody> sendRegToken(@Body JsonObject requestObject);

    @POST("setNewPassword")
    Call<ResponseBody>  setNewPassword(@Body JsonObject requestObject);

    @POST("getAds")
    Call<ResponseBody> getAds();

}
