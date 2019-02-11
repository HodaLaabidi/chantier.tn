package tn.chantier.chantiertn.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.factories.RetrofitServiceFactory;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;

public class ElitePackFragment extends android.support.v4.app.Fragment {
    
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    
    private String mParam1;
    private String mParam2;
    private static final int ELITE_PACK_TIME_OUT = 3000;
    View rootView;
    @BindView(R.id.btn_send_pack_elite)
    LinearLayout btnSendPAckElite ;


    public ElitePackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ElitePackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ElitePackFragment newInstance(String param1, String param2) {
        ElitePackFragment fragment = new ElitePackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ButterKnife.bind(getActivity());
        
        //setButtonSubscibeView();
        //setClickableButtonSubscribe();
    }

    private void setClickableButtonSubscribe() {
    }

    private void setButtonSubscibeView() {
        
        if (SharedPreferencesFactory.retrieveUserData().getPack() == 0){
            
           // buttonElitePack.setText("est déja abonné");
            
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_elite_pack, container, false);
        initialiseFragment();
        return rootView;
    }


    private void initialiseFragment() {

        ButterKnife.bind(this , rootView);
        btnSendPAckElite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject postPArams = new JsonObject();
                postPArams.addProperty("pack","elite" );
                Call<ResponseBody> call = RetrofitServiceFactory.getChantierService().sendPackRequest(postPArams);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200){
                            Log.e (" packPro" , response.code() + response.body().toString()+ "! ");
                            final LayoutInflater inflater = LayoutInflater.from(getActivity());
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyleSuggest);
                            final View customDialog = inflater.inflate(R.layout.pop_up_reset_password, null , false);
                            RalewayTextView textCongratulations = customDialog.findViewById(R.id.text_congratulations);
                            textCongratulations.setText(" Votre demande de pack a été enregistrée, notre service commercial vous contactera le plus tôt possible");
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alertDialog.setView(customDialog);
                            LinearLayout btnGoToConnexion = customDialog.findViewById(R.id.btn_go_to_connexion);
                            btnGoToConnexion.setVisibility(View.GONE);
                            alertDialog.show();
                            LinearLayout goToConnexionButton = customDialog.findViewById(R.id.btn_go_to_connexion);
                            goToConnexionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();


                                }
                            });

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    alertDialog.dismiss();



                                }
                            }, ELITE_PACK_TIME_OUT);

                        } else {
                            Log.e (" packPro" , response.code() + response.body().toString()+ "! ");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

}
