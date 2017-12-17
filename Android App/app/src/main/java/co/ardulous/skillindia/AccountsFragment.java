package co.ardulous.skillindia;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class AccountsFragment extends Fragment {
    private LinearLayout SignInContainer;
    private LinearLayout InternetWarn;
    private LinearLayout buttonPanel;
    private Button retryButton;
    private LinearLayout GoogleSignInContainer;
    private TextView LoginButton;
    private TextInputEditText phoneNumberView;
    private TextInputEditText passwordView;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    public AccountsFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_accounts, container, false);
        SignInContainer=rootView.findViewById(R.id.SignInWidget);
        InternetWarn=rootView.findViewById(R.id.internetWarn);
        retryButton=rootView.findViewById(R.id.retry);
        LoginButton = rootView.findViewById(R.id.login_button);
        GoogleSignInContainer = rootView.findViewById(R.id.google_login);
        connectivityManager=(ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                    if (phoneNumber.isEmpty() == true || password.isEmpty() == true) {
                        Toast.makeText(rootView.getContext(), "The fields can not be blank", Toast.LENGTH_SHORT).show();
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
