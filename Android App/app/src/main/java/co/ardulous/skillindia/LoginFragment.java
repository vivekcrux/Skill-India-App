package co.ardulous.skillindia;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends android.app.Fragment {

    private String LOG_TAG="LoginFragment";

    private LinearLayout SignInContainer;
    private LinearLayout InternetWarn;
    private Button retryButton;
    private CardView GoogleSignInContainer;
    private TextView LoginButton;
    private TextInputEditText phoneNumberView;
    private TextInputEditText passwordView;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private TextView orTextView;
    private TextView registerButton;
    private LinearLayout myaccount;
    private LinearLayout loginForm;

    private Context context;
    private View.OnClickListener flipFragment;

    public LoginFragment() {
    }

    public static LoginFragment newInstance(Context context, View.OnClickListener flipFragment) {
        LoginFragment fragment = new LoginFragment();

        fragment.context = context;
        fragment.flipFragment = flipFragment;

        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        SignInContainer=rootView.findViewById(R.id.SignInWidget);
        InternetWarn=rootView.findViewById(R.id.internetWarn);
        retryButton=rootView.findViewById(R.id.retry);
        LoginButton = rootView.findViewById(R.id.login_button);
        GoogleSignInContainer = rootView.findViewById(R.id.google_login);
        orTextView=rootView.findViewById(R.id.or);
        connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        registerButton=rootView.findViewById(R.id.register);
        loginForm=rootView.findViewById(R.id.loginForm);

        myaccount=rootView.findViewById(R.id.myaccount);
        FirebaseAuthContent firebaseAuthContent=new FirebaseAuthContent();
        if(firebaseAuthContent.mAuth!=null)
        {
            //Log.e(LOG_TAG,"Auth is not null");
            networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null)
            {
                if(firebaseAuthContent.mAuth.getCurrentUser()!=null) {
                    //Log.e(LOG_TAG,"Hey!Here I am");

                    toggleFormOut();


                }
                else
                {
                    toggleFormIn();
                }
            }
            else
            {
                toggleFormIn();
            }

        }
        else
        {
            toggleFormIn();
        }
        TextView register = rootView.findViewById(R.id.register);
        register.setText(Html.fromHtml("Don't have an Account ? <u>Register</u>"));
        register.setOnClickListener(flipFragment);
        if(connectivityManager != null)
            networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null) {
            registerButton.setVisibility(View.VISIBLE);
            orTextView.setVisibility(View.VISIBLE);
            SignInContainer.setVisibility(View.VISIBLE);
            GoogleSignInContainer.setVisibility(View.VISIBLE);
            InternetWarn.setVisibility(View.GONE);
            phoneNumberView = rootView.findViewById(R.id.phone);
            passwordView = rootView.findViewById(R.id.password);
            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phoneNumber = phoneNumberView.getText().toString();
                    String password = passwordView.getText().toString();
                    if (phoneNumber.isEmpty() || password.isEmpty()) {
                        Toast.makeText(rootView.getContext(), "Error: None of the fields can be blank", Toast.LENGTH_SHORT).show();
                    } else {
                        //compare with database
                    }
                }
            });
        }
        else
        {
            registerButton.setVisibility(View.GONE);
            SignInContainer.setVisibility(View.GONE);
            orTextView.setVisibility(View.GONE);
            LoginButton.setVisibility(View.GONE);
            GoogleSignInContainer.setVisibility(View.GONE);
            InternetWarn.setVisibility(View.VISIBLE);
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reloadFragment();
                }
            });
        }
        return rootView;
    }
    private void toggleFormIn()
    {
        loginForm.setVisibility(View.VISIBLE);
        myaccount.setVisibility(View.GONE);
    }
    private void toggleFormOut()
    {
        loginForm.setVisibility(View.GONE);
        myaccount.setVisibility(View.VISIBLE);
    }
    private void reloadFragment()
    {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}
