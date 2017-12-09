package co.ardulous.skillindia;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    //This actually serves a purpose of MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Thread delayLoad = new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        try {
                            sleep(3000);
                        } catch (InterruptedException ex) {
                            Log.e("Splash Screen", "Interrupt");
                        } finally {
                            startActivity(new Intent(MainActivity.this, PanelActivity.class));
                            finish();
                        }
                    }
                };
                delayLoad.run();
            }
        }, 3*1000);
    }
}
