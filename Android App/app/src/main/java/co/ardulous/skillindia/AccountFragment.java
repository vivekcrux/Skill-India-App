package co.ardulous.skillindia;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AccountFragment extends android.app.Fragment {

    private boolean loginTabOpen;
    private Context context;

    public AccountFragment() {
    }

    public static AccountFragment newInstance(Context context) {
        AccountFragment fragment = new AccountFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginTabOpen = false;
        switchFragment(LoginFragment.newInstance(context));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View itemView = inflater.inflate(R.layout.fragment_account, container, false);

        final View login = itemView.findViewById(R.id.login_button), register = itemView.findViewById(R.id.register_button);
        login.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.login_button) {
                    if(!loginTabOpen) {
                        switchFragment(LoginFragment.newInstance(context));

                        login.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        register.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }
                } else {
                    if(loginTabOpen) {
                        switchFragment(new RegisterFragment());

                        register.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        login.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                    }
                }
            }
        };
        login.setOnClickListener(onClickListener);
        register.setOnClickListener(onClickListener);

        return itemView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(((AppCompatActivity) context).getSupportActionBar() != null) {
            ((AppCompatActivity) context).getSupportActionBar().hide();
        }
    }

    @Override
    public void onStop() {
        if(((AppCompatActivity) context).getSupportActionBar() != null) {
            ((AppCompatActivity) context).getSupportActionBar().show();
        }

        super.onStop();
    }

    private void switchFragment(android.app.Fragment thisFragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.container, thisFragment).commit();
        loginTabOpen = !loginTabOpen;
    }
}
