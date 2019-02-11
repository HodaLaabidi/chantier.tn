package tn.chantier.chantiertn.utils.typefaces;

import android.content.Context;
import android.graphics.Typeface;

public class FontRaleway {
    Context context;

    public FontRaleway(Context context) {
        this.context = context;
    }

    public Typeface getLight() {
        return TypeFacesUtils.obtainTypeface(context, "raleway-light.ttf");
    }

    public Typeface getBlack() {
        return TypeFacesUtils.obtainTypeface(context, "raleway-Black.ttf");
    }

    public Typeface getRegular() {
        return TypeFacesUtils.obtainTypeface(context, "raleway-regular.ttf");
    }


    public Typeface getBold() {
        return TypeFacesUtils.obtainTypeface(context, "raleway-bold.ttf");
    }


}
