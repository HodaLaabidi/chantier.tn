package tn.chantier.chantiertn.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.HomeActivity;
import tn.chantier.chantiertn.fragments.HomeFragment;
import tn.chantier.chantiertn.models.EditableSubCategory;
import tn.chantier.chantiertn.widgets.NetworkImageView;

public class SpecialitiesRecyclerAdapter extends RecyclerView.Adapter<SpecialitiesRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final List<EditableSubCategory> listSousCategorie;
    private final List<EditableSubCategory> subCategorieDesfault;



    public SpecialitiesRecyclerAdapter(Context context, List<EditableSubCategory> listSousCategorie, List<EditableSubCategory> subCategorieDesfault) {
        this.context = context;
        this.listSousCategorie = listSousCategorie;
        this.subCategorieDesfault = subCategorieDesfault;
    }

    @Override
    public SpecialitiesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_sous_categorie, parent, false);
        return new ViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final EditableSubCategory sousCategorie = listSousCategorie.get(position);

        if (HomeFragment.selectedSubCategoryMap.containsKey(position)) {
            holder.cbCategory.setChecked(true);


        } else holder.cbCategory.setChecked(false);


        holder.tvCategoryName.setText(sousCategorie.getNom_activite());

        setIcons(holder.ivCategory , sousCategorie);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cbCategory.isChecked()) {
                    HomeFragment.selectedSubCategoryMap.remove(position);
                    HomeFragment.resetListSelectedSpecialities();
                    HomeFragment.nbSelected--;
                    holder.cbCategory.setChecked(false);

                } else {
                    HomeFragment.selectedSubCategoryMap.put(position, sousCategorie);
                    HomeFragment.nbSelected++;
                    holder.cbCategory.setChecked(true);
                }


            }
        });


    }
    public HashMap<Integer, EditableSubCategory> getSelectedSubCategoryMap(){
        return HomeFragment.selectedSubCategoryMap ;
    }

    private void setIcons (ImageView imageView , EditableSubCategory sousCategorie){
        if (sousCategorie.getId_sec().equalsIgnoreCase("1")) {
            imageView.setImageResource(R.drawable.img1);

        } else if (sousCategorie.getId_sec().equalsIgnoreCase("2")) {
            imageView.setImageResource(R.drawable.img2);


        } else if (sousCategorie.getId_sec().equalsIgnoreCase("13")) {
            imageView.setImageResource(R.drawable.img13);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("3")) {
            imageView.setImageResource(R.drawable.img3);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("4")) {
            imageView.setImageResource(R.drawable.img4);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("5")) {
            imageView.setImageResource(R.drawable.img5);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("6")) {
            imageView.setImageResource(R.drawable.img6);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("8")) {
            imageView.setImageResource(R.drawable.img8);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("9")) {
            imageView.setImageResource(R.drawable.img9);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("10")) {
            imageView.setImageResource(R.drawable.img10);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("11")) {
            imageView.setImageResource(R.drawable.img11);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("12")) {
            imageView.setImageResource(R.drawable.img12);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("15")) {
            imageView.setImageResource(R.drawable.img15);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("7")) {
            imageView.setImageResource(R.drawable.img7);
        } else if (sousCategorie.getId_sec().equalsIgnoreCase("16")) {
            imageView.setImageResource(R.drawable.img16);
        }


    }


    @Override
    public int getItemCount() {
        return listSousCategorie != null ? listSousCategorie.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void resetSelectedSubCategories() {

        HomeFragment.selectedSubCategoryMap = new HashMap<>();

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

