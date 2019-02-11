package tn.chantier.chantiertn.widgets.UI;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.utils.textstyle.RalewayTextView;


public class CustomToast {

    public final static int INFO = 0;
    public final static int WARNING = 1;
    public final static int ERROR = 2;
    public final static int SUCCESS = 3;
    Toast toast;
    ImageView ivClose, ivToast;
    RalewayTextView tvToastMessage, tvToastTitle;
    String title, message;
    LinearLayout layoutBorder;
    private int mode;
    private Context context;
    private int icon;

    public CustomToast(Context context, View rootView, int mode) {
        this.context = context;
        this.mode = mode;

    }

    public CustomToast(Context context, String title, String message, int icon, int mode) {
        this.context = context;
        this.mode = mode;
        this.icon = icon;
        this.message = message;
        this.title = title;
        build();
    }

    public CustomToast(Context context, String title, String message, int icon, int length, int mode) {
        this.context = context;
        this.mode = mode;
        this.icon = icon;
        this.message = message;
        this.title = title;
        build();
        toast.setDuration(length);
    }

    public CustomToast(Context context, int mode) {
        this.context = context;
        this.mode = mode;
        build();
    }

    public void build() {
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        // Inflate the Layout
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) ((AppCompatActivity) context).findViewById(R.id.custom_toast_layout));

        toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 400);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        layoutBorder = layout.findViewById(R.id.layout_border);
        ivClose = layout.findViewById(R.id.iv_icon_close);
        ivToast = layout.findViewById(R.id.iv_toast);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast.cancel();
            }
        });


        tvToastMessage = layout.findViewById(R.id.tv_toast_message);
        tvToastTitle = layout.findViewById(R.id.tv_toast_title);

        if (message != null && !message.equals("")) {
            tvToastMessage.setText(message);
            tvToastMessage.setVisibility(View.VISIBLE);
        }

        if (title != null && !title.equals("")) {
            tvToastTitle.setText(title);
            tvToastTitle.setVisibility(View.VISIBLE);
        }
        if (icon != 0) {
            ivToast.setImageResource(icon);
            ivToast.setVisibility(View.VISIBLE);
        }

        switch (mode) {
            case 0:
                layoutBorder.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                tvToastTitle.setTextColor(context.getResources().getColor(R.color.colorGrey));
                break;
            case 1:
                layoutBorder.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                tvToastTitle.setTextColor(context.getResources().getColor(R.color.colorDarkGrey));
                tvToastMessage.setTextColor(context.getResources().getColor(R.color.colorDarkGrey));
                break;
            case 2:
                layoutBorder.setBackgroundColor(context.getResources().getColor(R.color.red_color));
                tvToastTitle.setTextColor(context.getResources().getColor(R.color.red_color));
                break;
            case 3:
                layoutBorder.setBackgroundColor(context.getResources().getColor(R.color.colorDarkGrey));
                tvToastTitle.setTextColor(context.getResources().getColor(R.color.colorWhite));
                break;
        }
    }

    public void setTitle(String title) {
        if (title != null && !title.equals("")) {
            tvToastTitle.setText(title);
            tvToastTitle.setVisibility(View.VISIBLE);
        }
    }

    public void setMessage(String message) {
        if (message != null && !message.equals("")) {
            tvToastMessage.setText(message);
            tvToastMessage.setVisibility(View.VISIBLE);
        }
    }

    public void setIcon(int icon) {
        ivToast.setImageResource(icon);
        ivToast.setVisibility(View.VISIBLE);
    }

    public void show() {
        toast.show();
    }
}
