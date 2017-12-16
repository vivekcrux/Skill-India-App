package co.ardulous.skillindia;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Map;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class RegisterFragment extends Fragment {

    private ConnectivityManager connectivityManager;
    private Button retryButton;
    private NetworkInfo networkInfo;
    private LinearLayout signUpWidget;
    private LinearLayout internetWarn;
    private EditText firstNameView,lastNameView,phoneView;
    private EditText passwordView,confirmPasswordView;
    private Map<Integer,Integer> PriorityMap;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*PriorityMap.put(R.id.firstName,0);
        PriorityMap.put(R.id.lastName,1);
        PriorityMap.put(R.id.phone,2);
        PriorityMap.put(R.id.password,3);
        PriorityMap.put(R.id.confirmPassword,4);*/

        connectivityManager=(ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager != null)
            networkInfo=connectivityManager.getActiveNetworkInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.activity_register, container, false);

        signUpWidget=itemView.findViewById(R.id.signUpWidget);
        retryButton=itemView.findViewById(R.id.retry);
        internetWarn=itemView.findViewById(R.id.internetWarn);
        firstNameView=itemView.findViewById(R.id.firstName);
        lastNameView=itemView.findViewById(R.id.lastName);
        phoneView=itemView.findViewById(R.id.phone);
        passwordView=itemView.findViewById(R.id.password);
        confirmPasswordView=itemView.findViewById(R.id.confirmPassword);

        checkNetworkInfo();

        return itemView;
    }

    private void checkNetworkInfo() {
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
                    /*Intent intent=new Intent(getContext(),RegisterActivity.class);
                    startActivity(intent);*/

                    getFragmentManager().beginTransaction().replace(R.id.container, new RegisterFragment()).commit();
                }
            });

        }
        else
        {
            signUpWidget.setVisibility(View.VISIBLE);
            internetWarn.setVisibility(View.GONE);
        }
    }
}
