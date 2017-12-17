package co.ardulous.skillindia;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AccountFragment extends Fragment {

    private boolean loginTabOpen;

    public AccountFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginTabOpen = false;
        switchFragment(new LoginFragment());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View itemView = inflater.inflate(R.layout.fragment_account, container, false);

        final View login = itemView.findViewById(R.id.login_button), register = itemView.findViewById(R.id.register_button);
        login.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.login_button) {
                    if(!loginTabOpen) {
                        switchFragment(new LoginFragment());

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


    private void switchFragment(Fragment thisFragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.container, thisFragment).commit();
        loginTabOpen = !loginTabOpen;
    }
}
