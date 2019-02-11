package tn.chantier.chantiertn.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.activities.classes.MyApplication;
import tn.chantier.chantiertn.adapters.MessageAdapter;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.models.Chat;
import tn.chantier.chantiertn.models.ChatUser;
import tn.chantier.chantiertn.notifications.Data;
import tn.chantier.chantiertn.notifications.MyResponse;
import tn.chantier.chantiertn.notifications.Sender;
import tn.chantier.chantiertn.notifications.Token;
import tn.chantier.chantiertn.utils.textstyle.RalewayEditText;
import tn.chantier.chantiertn.widgets.UI.CustomToast;

import static tn.chantier.chantiertn.utils.Utils.getDate;

public class ChatActivity extends AppCompatActivity {

    private static final String ADMIN_ID = "123456";
    static FirebaseUser fUser ;
    DatabaseReference reference ;
    @BindView(R.id.btn_send)
    ImageButton btnSend ;
    @BindView(R.id.et_text_send)
    RalewayEditText etTextSend ;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat ;
    MessageAdapter messageAdapter ;
    ArrayList<Chat> chats;
    static boolean notify = false ;
    ValueEventListener seenListener ;
    @BindView(R.id.btn_arrow_back_chat)
    ImageView btnArrowBackChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        rvChat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);
        btnArrowBackChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent ( ChatActivity.this , HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(ADMIN_ID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessage(fUser.getUid() , ADMIN_ID);
                Log.e(" firebase user id " , fUser.getUid()+" !");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true ;
                String message = etTextSend.getText().toString();
                if ( !message.equalsIgnoreCase("")){
                    sendMessage(fUser.getUid() , "123456" , message);
                } else {
                    new CustomToast(ChatActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.no_chat_text), R.drawable.ic_erreur, CustomToast.ERROR).show();

                }
                etTextSend.setText("");

            }
        });
        seenMessage(ADMIN_ID);
        sendNotificationsFromAdmin();

    }

    private void sendNotificationsFromAdmin() {
        final String msg = "" ;
        Log.e("fUser" , fUser.getUid()+ " !");
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatUser user = dataSnapshot.getValue(ChatUser.class);
                if (notify && user.getId().equalsIgnoreCase("123456")) {
                    sendNotification(fUser.getUid(), "Chantier.tn", msg , ADMIN_ID);
                }
                notify = false ;
            }

            private void sendNotification(String receiver , final String username , final String message , final String senderMsg){
                DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
                Query query = tokens.orderByKey().equalTo(receiver);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Token token = snapshot.getValue(Token.class);
                            Data data = new Data(fUser.getUid(), R.drawable.logo_c, username+": "+message,
                                    "Nouveau message", senderMsg);
                            Sender sender = new Sender(data , token.getToken());
                            Call<MyResponse> call = RetrofitServiceFactory.getFirebaseAPIService().sendNotification(sender);
                            call.enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        Log.e("service notification" , " ok !");
                                        if (response.body().success != 1){
                                            Toast.makeText(MyApplication.getAppContext(), "  Failed Notification" , Toast.LENGTH_SHORT).show();
                                            Log.e("service notif success" , "!= 1");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Toast.makeText(MyApplication.getAppContext(), "  onFailure - Failed Notification" , Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void seenMessage(final String userId){

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    Log.e(" chat values = " , chat.toString()+ " ! " );
                    if (chat.getReceiver().equals(fUser.getUid()) && chat.getSender().equals(userId)){
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("seen" , "1");
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public static  void sendMessage(final String sender, final String receiver, String message){
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("sender" , sender);
        hashMap.put("receiver", receiver);
        hashMap.put("created_date" , getDate(System.currentTimeMillis())+"");
        hashMap.put("message", message);
        hashMap.put("id_plateform", SharedPreferencesFactory.retrieveUserData().getId()+"");
        hashMap.put("seen" , "0");
        reference.child("Chats").push().setValue(hashMap);


    }

    private void readMessage(final String myId , final String userId ){
        chats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.e(" snapshot value !!!" , snapshot.child("created_date")+ " !");
                    Log.e("snapshot !!!", snapshot.toString() + " !");
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(userId) && chat.getSender().equals(myId) ||
                            chat.getReceiver().equals(myId) && chat.getSender().equals(userId)){
                        chats.add(chat);
                        Log.e("toString() chat object ", chat.toString()+ " !");
                        Log.e("test created_date value", chat.getCreated_date()+ " !");
                    }

                }
                messageAdapter = new MessageAdapter(ChatActivity.this , chats);
                rvChat.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }



}
