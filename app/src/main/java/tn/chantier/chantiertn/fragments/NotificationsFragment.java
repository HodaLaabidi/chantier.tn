package tn.chantier.chantiertn.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.adapters.MainHomeFragmentAdapter;
import tn.chantier.chantiertn.adapters.NotificationsFragmentAdapter;
import tn.chantier.chantiertn.factories.SharedPreferencesFactory;
import tn.chantier.chantiertn.notifications.Notification;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView ;
    ArrayList<Notification> listNotifications ;
    @BindView(R.id.rv_notifications_fragment)
    RecyclerView rvNotificationsFragment;

    private OnFragmentInteractionListener mListener;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter img1.
     * @param param2 Parameter img2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this , rootView);
        initialiseFragment();
        return rootView;
    }

    private void initialiseFragment() {



        listNotifications = SharedPreferencesFactory.getListOfNotifications(getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvNotificationsFragment.setLayoutManager(mLayoutManager);
        if (listNotifications != null ) {
            Log.e("test listNotification", listNotifications.size()+"!");
            for (int i = 0 ; i < listNotifications.size(); i++){
                Log.e("test listNotification", listNotifications.get(i).getContent()+"!");
                Log.e("test listNotification", listNotifications.get(i).getDate()+"!");
                Log.e("test listNotification", listNotifications.get(i).getTitle()+"!");
                Log.e("test listNotification", listNotifications.get(i).getType()+"!");

            }
            NotificationsFragmentAdapter notificationsFragmentAdapter = new NotificationsFragmentAdapter(getContext(), listNotifications);
            rvNotificationsFragment.setAdapter(notificationsFragmentAdapter);
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
