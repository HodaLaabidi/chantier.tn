package tn.chantier.chantiertn.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.utils.Utils;

import static tn.chantier.chantiertn.utils.Utils.CHANTIER_TEL_NUMBER;

public class ContactActivity extends AppCompatActivity {

    @BindView(R.id.call_chantiertn)
    LinearLayout callUs ;
    @BindView(R.id.iv_arrow_back_contact_activity)
    LinearLayout arrowBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ButterKnife.bind(this);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        callUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null ;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Utils.hasPermissions(ContactActivity.this, Utils.MY_PERMISSIONS_REQUEST_CALL_PHONE, Utils.PERMISSIONS)) {
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse(
                                "tel:" + CHANTIER_TEL_NUMBER
                        ));
                        startActivity(intent);
                    }
                }
                else {
                    intent = new Intent(Intent.ACTION_CALL, Uri.parse(
                            "tel:" + CHANTIER_TEL_NUMBER
                    ));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
