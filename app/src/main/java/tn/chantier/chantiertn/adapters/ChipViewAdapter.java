package tn.chantier.chantiertn.adapters;

import android.content.Context;
import android.view.View;

import com.plumillonforge.android.chipview.Chip;

import java.util.ArrayList;
import java.util.List;

import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.models.SubCategory;
import tn.chantier.chantiertn.models.PackChip;
import tn.chantier.chantiertn.models.Tag;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;

public class ChipViewAdapter extends com.plumillonforge.android.chipview.ChipViewAdapter {

    Context context ;

    public ChipViewAdapter(Context context , ArrayList<SubCategory> listSubCategories) {
        super(context);
        this.context = context ;
    }

    @Override
    public int getLayoutRes(int position) {
        return R.layout.item_sub_category_chip ;
    }
    @Override
    public int getBackgroundColor(int position) {
        return 0;
    }




    @Override

    public int getBackgroundRes(int position) {
        // this is a process to rebuild chip layouts just add an id item from item layout
        Tag tag = (Tag) getChip(position);
        if(tag.getText().equalsIgnoreCase("Pro")){
            return R.drawable.button_radius_18dp ;
        }
        if( tag.getText().equalsIgnoreCase("Elite")){
            return R.drawable.button_gray_transparent_radius_18dp ;
        }
        return  R.drawable.button_gray_transparent_radius_18dp;

    }



    @Override
    public int getBackgroundColorSelected(int position) {
        return 0;
    }

    @Override
    public void onLayout(View view, int position) {
        RalewayTextView tvChip = view.findViewById(R.id.tv_chip);
        final Tag tag = (Tag) getChip(position);
        tvChip.setText(tag.getText());



    }

}
