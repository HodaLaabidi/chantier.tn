package tn.chantier.chantiertn.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.ContactDetailsActivity;
import tn.chantier.chantiertn.activities.HomeActivity;
import tn.chantier.chantiertn.fragments.HomeFragment;
import tn.chantier.chantiertn.notifications.Notification;
import tn.chantier.chantiertn.utils.Utils;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;

public class NotificationsFragmentAdapter extends RecyclerView.Adapter<NotificationsFragmentAdapter.MyViewHolder> {

  ArrayList<Notification> listNotifications ;
  Context context ;

  public NotificationsFragmentAdapter(Context context , ArrayList<Notification> listNotifications){
      this.listNotifications = listNotifications;
      this.context = context;
  }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationsFragmentAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

      final  Notification notification = listNotifications.get(position);
      Log.e ("notif"+ position , notification.getTitle() + " "+ notification.getContent() + " ");


              holder.titleNotification.setText(notification.getTitle()+"");


                holder.contentNotification.setText(notification.getContent()+"");



                holder.dateNotification.setText(Utils.getDate(Long.parseLong(notification.getDate()+"")) );

        if (notification.getType() != null){
          if (notification.getType() != ""){
              if (notification.getType().equalsIgnoreCase("1")){
                  holder.imageNotification.setImageResource(R.drawable.message);
              }
              else if (notification.getType().equalsIgnoreCase("3")){
                  holder.imageNotification.setImageResource(R.drawable.expiration_compte);
              }
              else if (notification.getId_lead() != null){
                  if (!notification.getId_lead().equalsIgnoreCase("")){

                          holder.imageNotification.setImageResource(R.drawable.valid_offer);
                          holder.itemView.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                          Intent intent = new Intent(context, ContactDetailsActivity.class);
                          Bundle bundle = new Bundle();
                          bundle.putString("id_offer_item" , notification.getId_lead());
                          Bundle bundle1 = new Bundle();
                          bundle1.putString("from_notifications_adapter" , "yes");
                          intent.putExtras(bundle);
                          intent.putExtras(bundle1);
                          context.startActivity(intent);

                              }
                          });

                  }
              }
          }
        }

    }

    @Override
    public int getItemCount() {
        return listNotifications.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RalewayTextView contentNotification , titleNotification , dateNotification ;
        ImageView imageNotification;
        public MyViewHolder(View itemView){
            super(itemView);

            contentNotification = itemView.findViewById(R.id.notification_content);
            titleNotification = itemView.findViewById(R.id.notification_title);
            dateNotification = itemView.findViewById(R.id.notification_date);
            imageNotification = itemView.findViewById(R.id.notification_image);


        }
    }
}
