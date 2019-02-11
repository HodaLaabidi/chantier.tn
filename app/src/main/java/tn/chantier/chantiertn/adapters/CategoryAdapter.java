package tn.chantier.chantiertn.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.ArrayList;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.models.Category;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;
import tn.chantier.chantiertn.widgets.NetworkImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context ;
    private ArrayList<Category> listCategory ;

    public CategoryAdapter(Context context, ArrayList<Category> listCategories){

        this.listCategory = listCategories;
        this.context = context ;

    }
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.category_item, parent , false);
        return new CategoryAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder holder,final int position) {
       final  Category category = listCategory.get(position);

        holder.labelCategory.setText(category.getCategoryLabel());
        holder.iconCategory.setImageUrl(category.getIcon());
        Log.e(" url icon " , category.getIcon()+" ");
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        LinearLayout itemCategory;
        NetworkImageView iconCategory;
        RalewayTextView labelCategory;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemCategory = itemView.findViewById(R.id.category_item);
            iconCategory = itemView.findViewById(R.id.category_icon);
            labelCategory = itemView.findViewById(R.id.category_label);




        }
    }
}
