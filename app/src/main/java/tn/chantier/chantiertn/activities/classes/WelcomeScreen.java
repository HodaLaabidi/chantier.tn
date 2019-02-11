package tn.chantier.chantiertn.activities.classes;

import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.FragmentWelcomePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

import tn.chantier.chantiertn.R;
import tn.chantier.chantiertn.fragments.ConsultationFragment;
import tn.chantier.chantiertn.fragments.QualFragment;
import tn.chantier.chantiertn.fragments.RelationFragment;
import tn.chantier.chantiertn.fragments.SignalFragment;

public class WelcomeScreen  extends WelcomeActivity {
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .page(new FragmentWelcomePage() {
                          @Override
                          protected android.support.v4.app.Fragment fragment() {
                              return new SignalFragment();
                          }
                      }
                )
                .page(new FragmentWelcomePage() {
                          @Override
                          protected android.support.v4.app.Fragment fragment() {
                              return new QualFragment();
                          }
                      }
                )

                .page(new FragmentWelcomePage() {
                          @Override
                          protected android.support.v4.app.Fragment fragment() {
                              return new ConsultationFragment();
                          }
                      }
                )

                .page(new FragmentWelcomePage() {
                    @Override
                    protected android.support.v4.app.Fragment fragment() {
                        return new RelationFragment();
                    }
                }
        )
                .swipeToDismiss(true)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }
    public static String welcomeKey(){
        return "WelcomeScreen";
    }

}
