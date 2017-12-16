package co.ardulous.skillindia;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class RegisterActivity extends AppCompatActivity {
    private ConnectivityManager connectivityManager;
    private Button retryButton;
    private NetworkInfo networkInfo;
    private LinearLayout signUpWidget;
    private LinearLayout internetWarn;
    private EditText firstNameView,lastNameView,phoneView;
    private TextInputEditText passwordView,confirmPasswordView;
    private Map<Integer,Integer> PriorityMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PriorityMap.put(R.id.firstName,0);
        PriorityMap.put(R.id.lastName,1);
        PriorityMap.put(R.id.phone,2);
        PriorityMap.put(R.id.password,3);
        PriorityMap.put(R.id.confirmPassword,4);

        signUpWidget=findViewById(R.id.signUpWidget);
        retryButton=findViewById(R.id.retry);
        internetWarn=findViewById(R.id.internetWarn);
        firstNameView=findViewById(R.id.firstName);
        lastNameView=findViewById(R.id.lastName);
        phoneView=findViewById(R.id.phone);
        passwordView=findViewById(R.id.password);
        confirmPasswordView=findViewById(R.id.confirmPassword);


        connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null)
        {
            String firstName=firstNameView.getText().toString();
            String lastName=lastNameView.getText().toString();
            String phone=phoneView.getText().toString();
            String password=passwordView.getText().toString();
            String confirmPassword=confirmPasswordView.getText().toString();

            signUpWidget.setVisibility(View.GONE);
            internetWarn.setVisibility(View.VISIBLE);
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(RegisterActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
        else
        {
            signUpWidget.setVisibility(View.VISIBLE);
            internetWarn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
