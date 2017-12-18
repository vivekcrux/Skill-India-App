package co.ardulous.skillindia;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class RegisterFragment extends android.app.Fragment {

    private ConnectivityManager connectivityManager;
    private TextView RegisterButton;
    private Button retryButton;
    private NetworkInfo networkInfo;
    private LinearLayout signUpWidget;
    private LinearLayout internetWarn;
    private TextInputEditText firstNameView, lastNameView, phoneView;
    private TextInputEditText passwordView, confirmPasswordView;
    private ArrayList<ItemMap> Items = new ArrayList<>();
    private SparseIntArray map = new SparseIntArray();

    private boolean statusFlag;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
            networkInfo = connectivityManager.getActiveNetworkInfo();
    }

    private void Instantiator() {
        Items.add(new ItemMap(R.id.firstName, R.id.firstNameRequired));
        Items.add(new ItemMap(R.id.lastName, R.id.lastNameRequired));
        Items.add(new ItemMap(R.id.phone, R.id.phoneRequired));
        Items.add(new ItemMap(R.id.Password, R.id.passwordRequired));
        Items.add(new ItemMap(R.id.confirmPassword, R.id.confirmRequired));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_register, container, false);

        signUpWidget = itemView.findViewById(R.id.signUpWidget);
        retryButton = itemView.findViewById(R.id.retry);
        internetWarn = itemView.findViewById(R.id.internetWarn);
        firstNameView = itemView.findViewById(R.id.firstName);
        lastNameView = itemView.findViewById(R.id.lastName);
        phoneView = itemView.findViewById(R.id.phone);
        passwordView = itemView.findViewById(R.id.Password);
        confirmPasswordView = itemView.findViewById(R.id.confirmPassword);
        RegisterButton = itemView.findViewById(R.id.register_button);

        TextView login = itemView.findViewById(R.id.login);
        login.setText(Html.fromHtml("Already have an Account ? <u>Login</u>"));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Instantiator();

        checkNetworkInfo();

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusFlag = false;

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        for (int i = 0; i < Items.size(); ++i) {
                            int id = Items.get(i).getID();

                            map.put(id, i);

                            TextInputEditText editText = getActivity().findViewById(id);
                            String text = editText.getText().toString().trim();
                            if (text.isEmpty()) {
                                Toast.makeText(getContext(), "The fields can't be blank", Toast.LENGTH_SHORT).show();
                                statusFlag = true;
                                break;
                            }
                        }
                    }
                }.run();

                if (!statusFlag) {
                    int phoneLength=phoneView.getText().toString().trim().length();
                    String password=passwordView.getText().toString().trim();
                    String confirmPassword=confirmPasswordView.getText().toString().trim();
                    if(phoneLength!=10)
                    {
                        Toast.makeText(getContext(), "The phone number that you entered is invalid", Toast.LENGTH_SHORT).show();
                    }
                    else if(password.length()<8)
                    {
                        Toast.makeText(getContext(), "The password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    }
                    else if(!password.equals(confirmPassword))
                    {
                        //Log.e("RegisterFragment",password);
                        //Log.e("RegisterFragment",confirmPassword);
                        Toast.makeText(getContext(), "The two passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //send OTP
                    }
                }
            }
        });

        return itemView;
    }

    private void checkNetworkInfo() {
        if (networkInfo == null) {

            signUpWidget.setVisibility(View.GONE);
            internetWarn.setVisibility(View.VISIBLE);
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getFragmentManager().beginTransaction().replace(R.id.container, new RegisterFragment()).commit();
                }
            });

        } else {
            signUpWidget.setVisibility(View.VISIBLE);
            internetWarn.setVisibility(View.GONE);
            notifier(firstNameView);
            notifier(lastNameView);
            notifier(phoneView);
            notifier(passwordView);
            notifier(confirmPasswordView);
        }
    }

    private void notifier(final TextInputEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() != 0) {
                    int fieldID = editText.getId();
                    //Log.e("RegisterFragment",String.valueOf(fieldID));
                    final int pos = map.get(fieldID);
                    int currPromptID = Items.get(pos).getErrID();
                    TextView currPromptTextView = getActivity().findViewById(currPromptID);
                    if (currPromptTextView.getVisibility() == View.VISIBLE) {
                        currPromptTextView.setVisibility(View.GONE);
                    }

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();

                            for (int it = 0; it < pos; ++it) {
                                int checkFieldID = Items.get(it).getID();
                                int checkPromptID = Items.get(it).getErrID();
                                TextInputEditText tempEditField = getActivity().findViewById(checkFieldID);
                                int length = tempEditField.getText().toString().trim().length();
                                TextView messageView = getActivity().findViewById(checkPromptID);
                                if (length == 0) {
                                    if (messageView.getVisibility() != View.VISIBLE) {
                                        messageView.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    }.run();
                } else {
                    int fieldID = editText.getId();
                    final int pos = map.get(fieldID);
                    int currPromptID = Items.get(pos).getErrID();
                    TextView currPromptTextView = getActivity().findViewById(currPromptID);
                    if (currPromptTextView.getVisibility() == View.VISIBLE) {
                        currPromptTextView.setVisibility(View.GONE);
                    }

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();

                            for (int it = 0; it < pos; ++it) {
                                int checkPromptID = Items.get(it).getErrID();
                                TextView messageView = getActivity().findViewById(checkPromptID);
                                if (messageView.getVisibility() == View.VISIBLE) {
                                    messageView.setVisibility(View.GONE);
                                }
                            }
                        }
                    }.run();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
    }

    private class ItemMap {
        private int ID;
        private int errID;

        ItemMap(int tmpID, int tmpErrID) {
            ID = tmpID;
            errID = tmpErrID;
        }

        int getID() {
            return ID;
        }

        int getErrID() {
            return errID;
        }
    }
}
