package consultas.apps.py.com.atomcontrolmultas.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import consultas.apps.py.com.atomcontrolmultas.R;

/**
 * Created by Andres on 29/1/2016.
 */
public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.animacion_entrada_derecha, R.anim.animacion_salida_izquierda);

                // close this activity
                finish();
                overridePendingTransition(R.anim.animacion_entrada_derecha, R.anim.animacion_salida_izquierda);
            }
        }, SPLASH_TIME_OUT);
    }
}
