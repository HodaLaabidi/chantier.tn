package tn.chantier.chantiertn.utils.textstyle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.utils.typefaces.FontRaleway;
import tn.chantier.chantiertn.utils.typefaces.FontRaleway;


@SuppressLint("AppCompatCustomView")
public class RalewayEditText extends EditText {
    public RalewayEditText(Context context) {
        super(context);
    }

    public RalewayEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RalewayTextView,
                0, 0);

        try {
            int font = a.getInt(R.styleable.RalewayTextView_fonttype, 0);
            FontRaleway raleway = new FontRaleway(context);
            switch (font) {
                case 0:
                    setTypeface(raleway.getRegular());
                    break;
                case 2:
                    setTypeface(raleway.getLight());
                    break;
                case 3:
                    setTypeface(raleway.getBlack());
                    break;
                case 4:
                    setTypeface(raleway.getBold());
                    break;
            }
        } finally {
            a.recycle();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RalewayEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
