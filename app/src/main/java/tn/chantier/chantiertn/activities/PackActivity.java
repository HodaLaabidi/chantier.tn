package tn.chantier.chantiertn.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lib.kingja.switchbutton.SwitchMultiButton;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.adapters.PackViewPager;
import tn.chantier.chantiertn.fragments.ElitePackFragment;
import tn.chantier.chantiertn.fragments.ElitePackStaticFragment;
import tn.chantier.chantiertn.fragments.ProPackFragment;
import tn.chantier.chantiertn.fragments.ProPackStaticFragment;
import tn.chantier.chantiertn.models.Tag;


public class PackActivity extends AppCompatActivity {

    @BindView(R.id.pack_view_pager)
    ViewPager packViewPager ;
    @BindView(R.id.chipview_pack)
    ChipView chipView ;
    List<Chip> chipList = new ArrayList<>();
    @BindView(R.id.btn_arrow_back_pack)
    ImageView btnArrowBackPack ;
    @BindView(R.id.fragment_pack)
    FrameLayout frameLayout ;
    public static SwitchMultiButton switchMultiButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        ButterKnife.bind(this);
        switchMultiButton = findViewById(R.id.switchMBPack);
        packViewPager.setAdapter(new PackViewPager(getSupportFragmentManager(), 2));
        /*FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        String tag = "ProPackFragment";
        ProPackStaticFragment fragment = new ProPackStaticFragment();
        transaction.replace(R.id.fragment_pack, fragment);
        //transaction.addToBackStack(tag);
        transaction.commit();*/


        btnArrowBackPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (PackActivity.this , HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        packViewPager.setCurrentItem(0);

        packViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0){

                    switchMultiButton.clearSelection();
                    switchMultiButton.setSelectedTab(0);

                } else {
                    switchMultiButton.clearSelection();
                    switchMultiButton.setSelectedTab(1);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        switchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
               if (position == 0){

                   packViewPager.setCurrentItem(0);

                  /* FragmentManager manager = getFragmentManager();
                   FragmentTransaction transaction = manager.beginTransaction();
                   String tag = "ProPackFragment";
                   ProPackStaticFragment fragment = new ProPackStaticFragment();
                   transaction.replace(R.id.fragment_pack,  fragment);
                   //transaction.addToBackStack(tag);
                   transaction.commit();*/


               } else {
                   packViewPager.setCurrentItem(1);

                   /*FragmentManager manager = getFragmentManager();
                   FragmentTransaction transaction = manager.beginTransaction();
                   String tag = "ElitePackFragment";
                   ElitePackStaticFragment fragment = new ElitePackStaticFragment();
                   transaction.replace(R.id.fragment_pack, fragment);
                  //transaction.addToBackStack(tag);
                   transaction.commit();*/
               }
            }
        });




    }


    @Override
    public void onBackPressed(){

            super.onBackPressed();

    }
}
