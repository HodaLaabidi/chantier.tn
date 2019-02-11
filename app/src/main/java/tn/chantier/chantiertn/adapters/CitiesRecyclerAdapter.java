package tn.chantier.chantiertn.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.models.City;


public class CitiesRecyclerAdapter extends RecyclerView.Adapter<CitiesRecyclerAdapter.ViewHolder> {

    private static OnRegionSelected onRegionSelected;
    static OnTagItemClickListener onTagClickListener;
    private final Context context;
    private List<City> regions;
    private TextView tvRegion;


    public CitiesRecyclerAdapter(Context context, List<City> regions) {
        this.context = context;
        try {
            this.regions = regions.subList(0, 3);
        } catch (IndexOutOfBoundsException e) {
            this.regions = regions;
        }

        this.tvRegion = tvRegion;

    }

    public static void setRegionSelected(OnRegionSelected onRegionSelectedl) {
        onRegionSelected = onRegionSelectedl;
    }

    @Override
    public CitiesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.one_item_city, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final City currentRegion = regions.get(position);
        holder.tvRegion.setText(currentRegion.getLocalite());
        holder.tvRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("1");
                if (onTagClickListener != null) {
                    System.out.println("2");
                    onTagClickListener.onTagItemClickListener(currentRegion);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("3");
                if (onTagClickListener != null) {
                    System.out.println("4");
                    onTagClickListener.onTagItemClickListener(currentRegion);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return regions != null ? regions.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnRegionSelected {
        void OnRegionSelected(City region);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRegion;


        public ViewHolder(View itemView) {
            super(itemView);

            tvRegion = itemView.findViewById(R.id.tv_region);


        }
    }

    public static void setOnTagClickListener(OnTagItemClickListener onTagClickListener1) {
        onTagClickListener = onTagClickListener1;
    }


    public interface OnTagItemClickListener {
        void onTagItemClickListener(City region);
    }
}
