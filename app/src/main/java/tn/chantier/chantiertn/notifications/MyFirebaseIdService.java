package tn.chantier.chantiertn.notifications;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import tn.chantier.chantiertn.activities.HomeActivity;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    public static String id = "";
    @Override

    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
         id = FirebaseInstanceId.getInstance().getId() ;

        if (firebaseUser != null){
            Log.e(" firebase token  ", refreshToken);
            Log.e("id" , id +" !");
            HomeActivity.createFirebaseTopics();
            //HomeActivity.firebaseSubscription(getBaseContext());
        }
    }



}
