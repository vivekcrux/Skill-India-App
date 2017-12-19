package co.ardulous.skillindia;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends android.app.Fragment {
    private LinearLayout SignInContainer;
    private LinearLayout InternetWarn;
    private Button retryButton;
    private LinearLayout GoogleSignInContainer;
    private TextView LoginButton;
    private TextInputEditText phoneNumberView;
    private TextInputEditText passwordView;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

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

        TextView register = rootView.findViewById(R.id.register);
        register.setText(Html.fromHtml("Don't have an Account ? <u>Register</u>"));
        register.setOnClickListener(flipFragment);
        connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null)
            networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null) {
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
            SignInContainer.setVisibility(View.GONE);
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
    private void reloadFragment()
    {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}
