package co.ardulous.skillindia;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    //This actually serves a purpose of MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splashscreen);

        Glide.with(this).load(R.drawable.preloader).into((ImageView) findViewById(R.id.preLoader));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, PanelActivity.class));
                finish();
            }
        }, 3 * 1000);
    }
}
