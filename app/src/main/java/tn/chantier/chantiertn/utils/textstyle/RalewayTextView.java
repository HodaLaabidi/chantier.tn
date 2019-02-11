package tn.chantier.chantiertn.utils.textstyle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.utils.typefaces.FontRaleway;


@SuppressLint("AppCompatCustomView")
public class RalewayTextView extends TextView {
    public RalewayTextView(Context context) {
        super(context);
    }

    public RalewayTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RalewayTextView,
                0, 0);

        try {
            int font = a.getInt(R.styleable.RalewayTextView_fonttype, 0);
            FontRaleway fontRaleway = new FontRaleway(context);
            switch (font) {
                case 0:
                    setTypeface(fontRaleway.getRegular());
                    break;
                case 2:
                    setTypeface(fontRaleway.getLight());
                    break;
                case 3:
                    setTypeface(fontRaleway.getBlack());
                    break;
                case 4:
                    setTypeface(fontRaleway.getBold());
                    break;
            }
        } finally {
            a.recycle();
        }
    }

    public RalewayTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RalewayTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
