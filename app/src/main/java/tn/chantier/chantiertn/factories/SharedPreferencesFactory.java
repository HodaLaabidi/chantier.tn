package tn.chantier.chantiertn.factories;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import tn.chantier.chantiertn.models.City;
import tn.chantier.chantiertn.models.EditableSubCategory;
import tn.chantier.chantiertn.models.Professional;
import tn.chantier.chantiertn.notifications.Notification;
import tn.chantier.chantiertn.utils.Utils;

public class SharedPreferencesFactory {

public static SharedPreferences pref ;
public static SharedPreferences.Editor editor;
private static  Gson gson = Utils.getGsonInstance() ;





public static void initializedPreferences(Context context){
 pref = context.getSharedPreferences("MyPref" , 0) ;
 editor = pref.edit() ;
}


 public static void storeUserSession(Professional user){

     String json = gson.toJson(user);
     editor.putString("user_session" , json);
     editor.apply();

 }

    public static Professional retrieveUserData(){
        String json = pref.getString("user_session" , null);
        if (json != null) {
            Professional professionnal = gson.fromJson(json, Professional.class);
            return professionnal;
        } else {
            return null ;
        }
    }

static  public void storeUserEmailForConnexionField(String email){

    editor.putString("user_email" , email);
    editor.apply();

 }
 static public void removeUserEmail(){
     pref.edit().remove("user_email").apply();
 }

 static public String retrieveUserEmail(){
    return pref.getString("user_email" , null );

 }

 public static void removeUesrSession(){

    pref.edit().remove("user_session").apply();

 }



    public static void saveListOfCodesCities(Context context , ArrayList<City> listCities){
        String json = gson.toJson(listCities) ;
        editor.putString("list_cities" , json);
        editor.apply();
    }

    public static ArrayList<City> getListOfCodesCities(Context context){

        String json = pref.getString("list_cities", null);
        Type type = new TypeToken<ArrayList<City>>(){}.getType() ;
        return  gson.fromJson(json , type);

    }


    public static void saveNotifications(Context context , ArrayList<Notification> listOfNotifications){

        pref.edit().remove("list_notification").apply();
        String json = gson.toJson(listOfNotifications) ;
        editor.putString("list_notification" , json);
        editor.apply();
    }

    public static  ArrayList<Notification> getListOfNotifications (Context context){
        initializedPreferences(context);
        String json = pref.getString("list_notification", null);
        Type type = new TypeToken<ArrayList<Notification>>(){}.getType() ;
        if (json != null)
            return  gson.fromJson(json , type);
        else
            return  new ArrayList<Notification>();


    }
    public static void saveSpecialities(Context context , ArrayList<EditableSubCategory> listOfSpecialities){

        pref.edit().remove("list_specialities").apply();
        String json = gson.toJson(listOfSpecialities) ;
        editor.putString("list_specialities" , json);
        editor.apply();
    }

    public static  ArrayList<EditableSubCategory> getListOfSpecialities (Context context){
        initializedPreferences(context);
        String json = pref.getString("list_specialities", null);
        Type type = new TypeToken<ArrayList<EditableSubCategory>>(){}.getType() ;
        if (json != null)
            return  gson.fromJson(json , type);
        else
            return  new ArrayList<EditableSubCategory>();


    }
}
