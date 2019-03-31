package tn.chantier.chantiertn.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.fragments.HomeFragment;
import tn.chantier.chantiertn.models.City;
import tn.chantier.chantiertn.models.EditableSubCategory;
import tn.chantier.chantiertn.models.Gouvernerat;
import tn.chantier.chantiertn.widgets.NetworkImageView;

public class CityRecyclerAdapter extends RecyclerView.Adapter<CityRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final List<Gouvernerat> cities ;




    public CityRecyclerAdapter(Context context, List<Gouvernerat> cities) {
        this.context = context;
        this.cities = cities;


    }

    @Override
    public CityRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_sous_categorie, parent, false);
        return new ViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Gouvernerat city = this.cities.get(position);

        if (HomeFragment.selectedCityMap.containsKey(position)) {
            holder.cbCategory.setChecked(true);


        } else holder.cbCategory.setChecked(false);


        holder.tvCategoryName.setText(city.getGouvernerat());

        holder.ivCategory.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cbCategory.isChecked()) {
                    HomeFragment.selectedCityMap.remove(position);
                    HomeFragment.resetListSelectedSpecialities();
                    HomeFragment.nbSelectedCity--;
                    holder.cbCategory.setChecked(false);

                } else {
                    HomeFragment.selectedCityMap.put(position, city);
                    HomeFragment.nbSelectedCity++;
                    holder.cbCategory.setChecked(true);
                }


            }
        });


    }
    public HashMap<Integer, Gouvernerat> getSelectedCityMap(){
        return HomeFragment.selectedCityMap ;
    }



    @Override
    public int getItemCount() {
        return this.cities != null ? this.cities.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void resetSelectedCity() {

        HomeFragment.selectedCityMap = new HashMap<>();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        CheckBox cbCategory;
        NetworkImageView ivCategory;


        public ViewHolder(View itemView) {
            super(itemView);

            ivCategory = itemView.findViewById(R.id.iv_category);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            cbCategory = itemView.findViewById(R.id.cb_category);

        }
    }
}

