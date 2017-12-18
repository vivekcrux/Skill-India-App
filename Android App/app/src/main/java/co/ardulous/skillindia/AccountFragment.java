package co.ardulous.skillindia;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AccountFragment extends android.app.Fragment {

    private Context context;

    private View.OnClickListener flipFragment;

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

        flipFragment = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() != R.id.register) {
                    getChildFragmentManager().popBackStack();
                } else {
                    getChildFragmentManager().beginTransaction().setCustomAnimations(
                            R.animator.flip_right_in, R.animator.flip_right_out,
                            R.animator.flip_left_in, R.animator.flip_left_out
                    ).replace(R.id.container, RegisterFragment.newInstance(this, context)).addToBackStack(null).commit();
                }
            }
        };

        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance(context, flipFragment)).commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
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
}
