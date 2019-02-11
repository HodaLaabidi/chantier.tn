package tn.chantier.chantiertn.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.Interfaces.Callback;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.fragments.ActivityLogFragment;
import tn.chantier.chantiertn.fragments.HomeFragment;
import tn.chantier.chantiertn.fragments.NotificationsFragment;
import tn.chantier.chantiertn.models.Professional;
import tn.chantier.chantiertn.notifications.MyFirebaseMessaging;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;

import static tn.chantier.chantiertn.notifications.MyFirebaseMessaging.refreshedToken;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Callback{

    /*@Nullable
    @BindView(R.id.tv_email_professional)
    TextView tvEmailProfessional ;
    @Nullable
    @BindView(R.id.tv_socity_name)
    TextView tvSocityName ;
    @Nullable
    @BindView(R.id.icon_nav_bar)
    ImageView iconNavBar ;*/

    private BottomNavigationView mainNavBar ;
    private FrameLayout mainFrameLayout ;
    private RalewayTextView tvEmailProfessional;
    private RalewayTextView tvSocityName;
    private Professional professional ;
    private HomeFragment homeFragment ;
    private NotificationsFragment notificationsFragment ;
    private ActivityLogFragment activityLogFragment ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferencesFactory.initializedPreferences(HomeActivity.this );
        professional = new Professional();


        professional= SharedPreferencesFactory.retrieveUserData();
        // get professional (session_user data )

        initialiseFragments();

        sendFirebaseTokenToBackEnd();

        initialiseViewsNdValues();

        createFirebaseTopics();


    }

    private void sendFirebaseTokenToBackEnd() {
        if (refreshedToken != null) {
            JsonObject postParams = new JsonObject();
            postParams.addProperty("id_client",professional.getId());
            postParams.addProperty("reg_token", refreshedToken);

            Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().sendRegToken(postParams);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body().string() == "Successfully created new entry") {
                            Log.e("created token ", "in back-end");
                        } else if (response.body().string() == "Successfully updated entry") {
                            Log.e("updated token", "in back-end");
                        } else {
                            Log.e("response code = ", response.code() + " !");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    }

    public static  void createFirebaseTopics() {

        // not working with API22
        // Log.e(" inAppMessaging id " , FirebaseInAppMessaging.getInstance()+ " !");

        FirebaseMessaging.getInstance().subscribeToTopic("ConstructionetRenovation")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if ( task.isSuccessful()){
                           Log.e("subscribe to topic" , "subscribe failed"+"");
                       } else {
                           Log.e("subscribe to topic" , "subscribe successed");
                       }
                    }
                });
    }


    private void initialiseFragments() {

        mainNavBar = (BottomNavigationView) findViewById(R.id.main_nav_bar);
        mainFrameLayout = (FrameLayout) findViewById(R.id.main_frame);
        homeFragment = new HomeFragment();
        notificationsFragment = new NotificationsFragment();
        activityLogFragment = new ActivityLogFragment();
        if (Utils.isNotification) {
            mainNavBar.getMenu().getItem(2).setChecked(true);
            setFragment(notificationsFragment);
            if (Utils.isNotificationSaved){
                Utils.isNotificationSaved = false ;
            }
            Log.e("HomeActivity log", "send to next");
        }else {
            mainNavBar.getMenu().getItem(0).setChecked(true);
            setFragment(homeFragment);

        }





        mainNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.main_nav_home) {

                    setFragment(homeFragment);
                    return  true ;

                }  else if (item.getItemId() == R.id.main_nav_suivi) {
                    setFragment(activityLogFragment);
                    return  true ;

                } else if (item.getItemId() == R.id.main_nav_notifications) {
                    setFragment(notificationsFragment);
                    return  true ;

                }
                     return false ;
            }
        });
    }

    private void initialiseViewsNdValues() {



        // set ids without ButterKnife  !! same dumn error happening here  !! don't know why


        // setting toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);



        // setting drawer navigation

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // ButterKnife.bind(this) ;


      //  i had removed the below code from app_bar_home.xml
        /*
        <android.support.design.widget.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:backgroundTint="@color/colorYellow"
        android:layout_margin="@dimen/fab_margin"
        app:srcCompat="@drawable/ic_chat_black" />
         */

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this , ChatActivity.class);
                startActivity(intent);

            }
        });*/

        View header = navigationView.getHeaderView(0);

        tvEmailProfessional = header.findViewById(R.id.tv_email_professional);
        tvSocityName = header.findViewById(R.id.tv_socity_name);
        // setting compte data in navigation bar ;

        if (professional != null) {

            setNavBarData(professional);
        } else {
            setNavBarData(new Professional());

            }

            // setting item menu "communicate" form drawer navigation
        Menu menu = navigationView.getMenu();
        //MenuItem communicateItem = menu.findItem(R.id.menu_item_communicate);
       // SpannableString spannableString = new SpannableString(communicateItem.getTitle());
       // spannableString.setSpan(new TextAppearanceSpan(this ,R.style.menu_item_nav_drawer)
       // , 0 , spannableString.length() , 0);
        //communicateItem.setTitle(spannableString);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setNavBarData(Professional professional) {
        tvEmailProfessional.setText(professional.getEmail());
        tvSocityName.setText(professional.getRaison_social());
        // don't forget to set the icon plz !!!
    }

    @Override
    protected void onDestroy() {
        Utils.isNotification = false;
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Utils.isNotification = false;
        if (Utils.isNotificationSaved){
            Utils.isNotificationSaved = false ;
        }
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if (Utils.isNotificationSaved){
            Utils.isNotificationSaved = false ;
        }
        Utils.isNotification = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (Utils.isNotificationSaved){
            Utils.isNotificationSaved = false ;
        }
        Utils.isNotification = false;
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() > 0){
                if (manager.getBackStackEntryCount() == 1){
                    if (Utils.isNotificationSaved){
                        Utils.isNotificationSaved = false ;
                    }
                    finish();
                }
                super.onBackPressed();
                Fragment currentFragment = manager.findFragmentById(R.id.main_frame);
                if (currentFragment instanceof  HomeFragment){
                    mainNavBar.getMenu().getItem(0).setChecked(true);
                } else if (currentFragment instanceof  ActivityLogFragment){
                    mainNavBar.getMenu().getItem(1).setChecked(true);
                }else if (currentFragment instanceof NotificationsFragment){
                    mainNavBar.getMenu().getItem(2).setChecked(true);
                }
            } else {
                if (Utils.isNotificationSaved){
                    Utils.isNotificationSaved = false ;
                }
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            item.setIcon(R.drawable.ic_yellow_filter);
            Intent intent = new Intent ( HomeActivity.this , SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame , fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_déconnexion ){

            // remove all saved values
            Utils.isNotification = false ;
            if (Utils.isNotificationSaved){
                Utils.isNotificationSaved = false ;
            }
            SharedPreferencesFactory.removeUesrSession();
            FirebaseAuth.getInstance().signOut();
            // move the main activity
            Intent intent = new Intent(HomeActivity.this , ConnexionActivity.class);
            startActivity(intent);
            finish();

        } else if ( id == R.id.item_pack){
            Intent intent = new Intent ( HomeActivity.this ,PackActivity.class );
            startActivity( intent );

        }  /*else if (id == R.id.nav_send){
            DialogFactoryUtils.showChatDialog(HomeActivity.this);
        } */ else if ( id == R.id.item_settings){
            Intent intent = new Intent ( HomeActivity.this , SettingsActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.item_activities){
            Intent intent = new Intent ( HomeActivity.this , CategoriesActivity.class);
            intent.putExtra("editCategories","yes");
            startActivity(intent);
            finish();

        } else if (id == R.id.item_notification){
            // settings notifications
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onReady() {

    }
}
