package tn.chantier.chantiertn.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.ContactDetailsActivity;
import tn.chantier.chantiertn.activities.DetailsActivity;
import tn.chantier.chantiertn.models.FollowedOffer;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

public class FollowedOffersFragmentAdapter extends RecyclerView.Adapter<FollowedOffersFragmentAdapter.MyViewHolder> {

    ArrayList<FollowedOffer> listFollowedOffers;
    Context context;

    public FollowedOffersFragmentAdapter(Context context, ArrayList<FollowedOffer> listFollowedOffers) {

        this.context = context;
        this.listFollowedOffers = listFollowedOffers;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_followed_offer, parent, false);
        return new FollowedOffersFragmentAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final FollowedOffer followedOffer = listFollowedOffers.get(position);

        Log.e ( " followed offer values " , followedOffer.toString() +" ");

        if (!followedOffer.getTitre().equalsIgnoreCase("")) {
            holder.titre.setText(followedOffer.getTitre() + "");
        } else {
            holder.titre.setText("non mentionné ");
        }

        if (!followedOffer.getNom_client().equalsIgnoreCase("") && !followedOffer.getPrenom_client().equalsIgnoreCase("")) {
            holder.owner.setText(followedOffer.getNom_client()+ " "+followedOffer.getPrenom_client() );
        } else if (!followedOffer.getNom_client().equalsIgnoreCase("")) {
            holder.owner.setText(followedOffer.getNom_client());
        } else if (!followedOffer.getPrenom_client().equalsIgnoreCase("")) {
            holder.owner.setText(followedOffer.getPrenom_client() + "");
        } else {
            holder.owner.setText("non mentionné ");
        }

        if (!followedOffer.getTel_client().equalsIgnoreCase("")) {
            holder.phoneNumber.setText(followedOffer.getTel_client() + "");

        } else {
            holder.phoneNumber.setText("non mentionné ");
        }

        if (!followedOffer.getDate().equalsIgnoreCase("")) {
            holder.date.setText(followedOffer.getDate() + "");
        } else {
            holder.date.setText("non mentionné ");
        }


        holder.itemIconCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!followedOffer.getTel_client().equalsIgnoreCase("")) {
                    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Utils.hasPermissions(context, Utils.MY_PERMISSIONS_REQUEST_CALL_PHONE, Utils.PERMISSIONS)) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(
                                    "tel:" + followedOffer.getTel_client()
                            ));
                            context.startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(
                                "tel:" + followedOffer.getTel_client()
                        ));
                        context.startActivity(intent);
                    }
                } else {
                    new CustomToast(context, context.getResources().getString(R.string.error), context.getResources().getString(R.string.no_phone_number), R.drawable.ic_erreur, CustomToast.ERROR).show();


                }
            }
        });

        holder.itemIconSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!followedOffer.getEmail_client().equalsIgnoreCase("")) {

                    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + followedOffer.getEmail_client() + ""));
                        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{followedOffer.getEmail_client()+""});
                        context.startActivity(intent);
                       // intent.setData(Uri.parse("mailto:" + followedOffer.getEmail_client() + ""));
                        //context.startActivity(Intent.createChooser(intent, "Send mail"));
                    } else {
                         //Intent intent = new Intent(Intent.ACTION_SEND);
                        //intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{followedOffer.getEmail_client()+""});
                        //intent.setData(Uri.parse("mailto:" + followedOffer.getEmail_client() + ""));
                        //context.startActivity(Intent.createChooser(intent, "Send mail"));
                        //context.startActivity(intent);
                        Intent emailIntent = new Intent (Intent.ACTION_SENDTO, Uri.parse("mailto:"+Uri.encode(followedOffer.getEmail_client())));
                        context.startActivity(Intent.createChooser(emailIntent, "Send email via..."));


                    }

                } else {
                    new CustomToast(context, context.getResources().getString(R.string.error), context.getResources().getString(R.string.no_email_address), R.drawable.ic_erreur, CustomToast.ERROR).show();


                }
            }
        });


        holder.goToFollowedOfferDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context , ContactDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("id_offer_item" , followedOffer.getId());
                extras.putString("from_followed_offers_list" , "yes");
                intent.putExtras(extras);
                context.startActivity(intent);
                ((Activity)  context).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return listFollowedOffers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        RalewayTextView titre = itemView.findViewById(R.id.item_followed_offer_title);
        LinearLayout itemIconCall = itemView.findViewById(R.id.item_icon_call);
        LinearLayout itemIconSend = itemView.findViewById(R.id.item_icon_send);
        RalewayTextView owner = itemView.findViewById(R.id.item_followed_offer_owner);
        RalewayTextView phoneNumber = itemView.findViewById(R.id.item_followed_offer_phone_number);
        RalewayTextView date = itemView.findViewById(R.id.item_followed_offer_date);
        LinearLayout goToFollowedOfferDetail = itemView.findViewById(R.id.ll_go_to_followed_offer_detail);
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
