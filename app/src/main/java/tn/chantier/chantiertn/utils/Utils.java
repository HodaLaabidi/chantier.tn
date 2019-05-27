package tn.chantier.chantiertn.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.AnswersEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.classes.MyApplication;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.models.Category;
import tn.chantier.chantiertn.models.SubCategory;

public class Utils {
    public static final int TIMEOUT = 15;
    private static Gson gson;
    private static Answers answers ;
    public static boolean isNotification = false ;
    public static boolean isNotificationSaved = false ;
    public static String resetEmail = "";
    public static final String CHANTIER_TEL_NUMBER = "71 900 500";
    public static final String CONFIDENTIALITY_POLITICS_URL = "https://www.chantier.tn/statique/politiquedeconfidentialite";


    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 130;
    public static final String[] PERMISSIONS = {
            Manifest.permission.CALL_PHONE
    };
    public static void changeCheckboxColor(CheckBox checkBox) {
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
    public static String  getDate(long timeStamp) {

        Date date = new Date (timeStamp);
        if (DateUtils.isToday(timeStamp)){
            return DateUtils.getRelativeTimeSpanString(timeStamp)+"";
        }

        if (isYesterday(timeStamp)){
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-1"));
            return "Hier"+ " à "+ sdf.format(date) +"";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM 'à' hh:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT-1"));
            return sdf.format(date) + "";
        }
    }

    public static boolean isYesterday(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(date);

        now.add(Calendar.DATE,-1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }

    public static void setBackgroundImage(Context context, View layout, int image){
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(ContextCompat.getDrawable(context, image) );
        } else {
            layout.setBackground(ContextCompat.getDrawable(context, image));
        }
    }
    public static void setBackgroundColor(Context context, View layout, int color){
        layout.setBackgroundColor(context.getResources().getColor(color));

    }



    public static Gson getGsonInstance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static Answers getAnswersInstance(){
        if ( answers == null){
            answers = new Answers();
        }
        return answers ;
    }
    public static boolean hasPermissions(Context context, int permission_request, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, permissions, permission_request);
                    return false;
                }
            }
            return true;
        } else {
            for (String permission : permissions) {
                if (PermissionChecker.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, permissions, permission_request);
                    return false;
                }
            }
            return true;

        }
    }

    public static void hideKeyboard(AppCompatActivity appCompatActivity) {

        // not working !!
        if (appCompatActivity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) appCompatActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromInputMethod(appCompatActivity.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_HIDDEN);
            }
        }

    }



}
