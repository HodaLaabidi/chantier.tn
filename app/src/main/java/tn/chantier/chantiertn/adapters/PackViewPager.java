package tn.chantier.chantiertn.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import lib.kingja.switchbutton.SwitchMultiButton;
import tn.chantier.chantiertn.activities.PackActivity;
import tn.chantier.chantiertn.fragments.ElitePackFragment;
import tn.chantier.chantiertn.fragments.FreePackFragment;
import tn.chantier.chantiertn.fragments.ProPackFragment;
import tn.chantier.chantiertn.fragments.SponsorPackFragment;

public class PackViewPager extends FragmentStatePagerAdapter {

    int numberOfPacks ;


    public  PackViewPager (FragmentManager fm , int numberOfPacks){
        super(fm);
        this.numberOfPacks = numberOfPacks;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 1:
                ElitePackFragment elitePackFragment = new ElitePackFragment();
                return  elitePackFragment;
            case 0:
                ProPackFragment proPackFragment = new ProPackFragment();
                return proPackFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberOfPacks;
    }
}
