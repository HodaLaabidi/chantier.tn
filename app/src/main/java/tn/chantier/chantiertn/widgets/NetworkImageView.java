package tn.chantier.chantiertn.widgets;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import tn.chantier.chantiertn.R;

public class NetworkImageView extends android.support.v7.widget.AppCompatImageView {

    protected String imageURL;

    public NetworkImageView(Context context) {
        super(context);
    }

    public NetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    public void setImageUrl(String imageURL, final int defaultImgRes) {
        this.imageURL = imageURL;
        if (this.imageURL != null && !this.imageURL.equals("")) {
            setImageResource(defaultImgRes);
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(8)
                    .oval(false)
                    .borderColor(Color.parseColor("#d2d7de"))
                    .borderWidthDp((float) 3.0)
                    .build();

            Picasso.with(getContext()).load(imageURL).transform(transformation).resize(400, 400).centerCrop().into(this, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    setImageResource(defaultImgRes);
                }
            });
        } else setImageResource(defaultImgRes);
    }

    public void setImageUrl(String imageURL, final int defaultImgRes, final int cornerRadius) {
        this.imageURL = imageURL;
        if (imageURL != null && !imageURL.equals("")) {
            setImageResource(defaultImgRes);
            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(cornerRadius)
                    .oval(false)
                    .borderColor(Color.parseColor("#d2d7de"))
                    .build();
            Picasso.with(getContext()).load(imageURL).transform(transformation).resize(400, 400).centerCrop().into(this, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    setImageResource(defaultImgRes);
                }
            });
        } else setImageResource(defaultImgRes);
    }

    public void setImageUrlWithCustomBorderColor(String imageURL, String BorderColor) {
        this.imageURL = imageURL;
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(8)
                .oval(false)
                .borderColor(Color.parseColor(BorderColor))
                .borderWidthDp((float) 3.0)
                .build();
        Picasso.with(getContext()).load(imageURL).transform(transformation).resize(400, 400).centerCrop().into(this);
    }

    public void setImageUrlWithCustomBorderColor(final String imageURL, final String borderColor, final int cornerRadius, final int defaultImgRes) {
        this.imageURL = imageURL;
        if (this.imageURL != null && !this.imageURL.equals("")) {

            Log.e("test networkiv", this.imageURL);
            final Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(cornerRadius)
                    .oval(false)
                    .borderColor(Color.parseColor(borderColor))
                    .borderWidthDp((float) 3.0)
                    .build();
            Picasso.with(getContext()).load(imageURL).transform(transformation).resize(400, 400).centerCrop().into(this, new Callback() {
                @Override
                public void onSuccess() {
                    Log.e("test callback", "");



                }

                @Override
                public void onError() {
                    setImageResource(defaultImgRes, borderColor, cornerRadius);
                }
            });
        } else setImageResource(defaultImgRes);
    }


    public void setImageResource(int imageRes, String BorderColor, int cornerRadius) {
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(cornerRadius)
                .oval(false)
                .borderColor(Color.parseColor(BorderColor))
                .borderWidthDp((float) 3.0)
                .build();
        Picasso.with(getContext().getApplicationContext()).load(imageRes).transform(transformation).resize(400, 400).centerCrop().into(this);
    }

    public void setImageUrl(String imageURL) {
        this.imageURL = imageURL;
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(16)
                .oval(false)
                .borderColor(Color.parseColor("#edf0f5"))
                .borderWidthDp((float) 3.0)
                .build();
        Picasso.with(getContext()).load(imageURL).transform(transformation).resize(400, 400).centerCrop().into(this);
    }
    public void  setGifImageUrl(String URL){
        this.imageURL = URL;
        Glide.with(getContext().getApplicationContext()).load(imageURL).centerCrop().into(this);

    }

    public void setImageUrlWithoutBorder(String imageURL) {
        this.imageURL = imageURL;
        if (imageURL != null && !imageURL.equals(""))
            Picasso.with(getContext()).load(imageURL).resize(400, 400).centerCrop().into(this);
    }

    public void setImageUrlWithoutBorderWithoutResizing(String imageURL) {
        this.imageURL = imageURL;
        if (imageURL != null && !imageURL.equals("") && getContext() != null)
            Glide.with(getContext().getApplicationContext()).load(imageURL).centerCrop().into(this);
    }

    public void setImageUrlWithoutBorderWithoutResizing(String imageURL, int defaultIcon) {
        this.imageURL = imageURL;
        if (imageURL != null && !imageURL.equals("") && getContext() != null)
            Glide.with(getContext().getApplicationContext()).load(imageURL).into(this);
        else if (getContext() != null)
            Glide.with(getContext().getApplicationContext()).load(defaultIcon).into(this);
        else if (imageURL == null || imageURL == ""){
            Glide.with(getContext().getApplicationContext()).load(defaultIcon).centerCrop().into(this);
        }
    }

    public void setImageUrlWithoutBorderWithoutResizing(final String imageURL, final int defaultResourse, final View v) {
        this.imageURL = imageURL;

        if (imageURL != null && !imageURL.equals("") && getContext() != null)
            Picasso.with(getContext().getApplicationContext()).load(imageURL).into(this, new Callback() {
                @Override
                public void onSuccess() {
                    v.setVisibility(GONE);
                }

                @Override
                public void onError() {
                    if (getContext() != null) {
                        v.setVisibility(GONE);
                        setImageUrlWithoutBorderWithoutResizing(imageURL,defaultResourse,v);

                    }
                }
            });
    }

    public void setImageUri(Uri imageURL) {

        if (imageURL != null && getContext() != null)
            Glide.with(getContext().getApplicationContext()).load(imageURL).centerCrop().into(this);
    }

    @Override
    public void setImageResource(int imageRes) {
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(16)
                .oval(false)
                .borderColor(Color.parseColor("#ffffff"))
                .borderWidthDp((float) 3.0)
                .build();
        Glide.with(getContext().getApplicationContext()).load(imageRes).centerCrop().into(this);
    }
    public void setGifImageResource(int imageRes) {
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(16)
                .oval(false)
                .borderColor(Color.parseColor("#ffffff"))
                .borderWidthDp((float) 3.0)
                .build();
        Glide.with(getContext().getApplicationContext()).load(imageRes).centerCrop().into(this);
    }

    public void setImageResourceRaw(int imageRes) {
        if (getContext() != null)
            Glide.with(getContext().getApplicationContext()).load(imageRes).centerCrop().into(this);
    }
}
