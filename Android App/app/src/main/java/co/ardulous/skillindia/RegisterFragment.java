package co.ardulous.skillindia;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Signature;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


import static android.content.Context.CONNECTIVITY_SERVICE;

public class RegisterFragment extends android.app.Fragment implements View.OnClickListener{

    private String LOG_TAG="RegisterFragment";

    FirebaseAuth mFirebaseAuth;
    //FirebaseAuth.AuthStateListener authStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private LinearLayout CodeSentLayout;
    private ConnectivityManager connectivityManager;
    private TextView RegisterButton;
    private Button retryButton;
    private TextView resendButton;
    private TextView verifyButton;
    private NetworkInfo networkInfo;
    private LinearLayout signUpWidget;
    private LinearLayout internetWarn;
    private TextInputEditText firstNameView, lastNameView, phoneView;
    private TextInputEditText passwordView, confirmPasswordView;
    private ArrayList<ItemMap> Items = new ArrayList<>();
    private SparseIntArray map = new SparseIntArray();
    private RadioButton MentorButton, StudentButton;
    private boolean statusFlag;
    private View.OnClickListener flipFragment;
    private Context context;
    private boolean verificationStatus = false;
    private TextView phoneCodeSentView;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private String mVerificationID;
    private EditText mVerificationField;

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance(View.OnClickListener flipFragment, Context context) {
        RegisterFragment fragment = new RegisterFragment();

        fragment.context = context;
        fragment.flipFragment = flipFragment;

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
            networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            mFirebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("users/PhoneUsers");

            mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    Log.d(LOG_TAG,"Verification Completed"+phoneAuthCredential);
                    verificationStatus=false;
                    updateUI(STATE_VERIFY_SUCCESS,phoneAuthCredential);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Log.w(LOG_TAG,"Verification Failed",e);
                    verificationStatus=false;
                    if(e instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(context,"Error:Verification Failed",Toast.LENGTH_SHORT).show();
                    }
                    if(e instanceof FirebaseTooManyRequestsException)
                    {
                        Toast.makeText(context,"Error:Server Busy.Try Again after sometime",Toast.LENGTH_SHORT).show();
                    }
                    updateUI(STATE_VERIFY_FAILED);
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    //s above is the verificationID
                    Log.d(LOG_TAG,"On code sent:"+s);
                    mVerificationID=s;
                    resendingToken=forceResendingToken;
                    updateUI(STATE_CODE_SENT);
                }
            };
        }
    }

    private void signOut() {
        mFirebaseAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mFirebaseAuth.getCurrentUser(), null);
    }
    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }
    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
                //This is the default state of RegisterFragment
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
                CodeSentLayout.setVisibility(View.VISIBLE);
                signUpWidget.setVisibility(View.GONE);
                phoneCodeSentView.setText(phoneView.getText().toString());
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
                /*CodeSentLayout.setVisibility(View.VISIBLE);
                signUpWidget.setVisibility(View.GONE);
                */
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
                Snackbar.make(getActivity().findViewById(R.id.framelayout),"Verification Succeeded",Snackbar.LENGTH_SHORT).show();

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
                Toast.makeText(context,"Sign In Failed",Toast.LENGTH_SHORT).show();
                break;
            case STATE_SIGNIN_SUCCESS:
                // Np-op, handled by sign-in check
                Toast.makeText(context,"Signed In",Toast.LENGTH_SHORT).show();
                break;
        }

        /*if (user == null) {
            // Signed out
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            mSignedInViews.setVisibility(View.GONE);

            mStatusText.setText(R.string.signed_out);
        } else {
            // Signed in
            mPhoneNumberViews.setVisibility(View.GONE);
            mSignedInViews.setVisibility(View.VISIBLE);

            enableViews(mPhoneNumberField, mVerificationField);
            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);

            mStatusText.setText(R.string.signed_in);
            mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
        }*/
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential)
    {
        /////////////////////////////////////////////////////////////Remaining..i have to copy more code here
        mFirebaseAuth
                .signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(LOG_TAG,"Sign In with credential success!!");
                            FirebaseUser firebaseUser=task.getResult().getUser();
                            updateUI(STATE_SIGNIN_SUCCESS,firebaseUser);
                        }
                        else
                        {
                            Log.w(LOG_TAG,"SignInFailure",task.getException());
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(context,"Invalid Code!!",Toast.LENGTH_SHORT).show();
                            }
                            updateUI(STATE_SIGNIN_FAILED);
                        }
                    }
        });
    }
    /*@Override
    public void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }*/

    private void Instantiator() {
        Items.add(new ItemMap(R.id.firstName, R.id.firstNameRequired));
        Items.add(new ItemMap(R.id.lastName, R.id.lastNameRequired));
        Items.add(new ItemMap(R.id.phone, R.id.phoneRequired));
        Items.add(new ItemMap(R.id.Password, R.id.passwordRequired));
        Items.add(new ItemMap(R.id.confirmPassword, R.id.confirmRequired));
        for (int i = 0; i < Items.size(); ++i) {
            int id = Items.get(i).getID();
            map.put(id, i);
        }
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_register, container, false);

        CodeSentLayout=itemView.findViewById(R.id.code_sent_layout);
        mVerificationField=itemView.findViewById(R.id.code_sent_verification);
        phoneCodeSentView=itemView.findViewById(R.id.phone_code_sent);
        resendButton=itemView.findViewById(R.id.resend_button);
        verifyButton=itemView.findViewById(R.id.verify_button);

        signUpWidget = itemView.findViewById(R.id.signUpWidget);
        retryButton = itemView.findViewById(R.id.retry);
        internetWarn = itemView.findViewById(R.id.internetWarn);
        firstNameView = itemView.findViewById(R.id.firstName);
        lastNameView = itemView.findViewById(R.id.lastName);
        phoneView = itemView.findViewById(R.id.phone);
        passwordView = itemView.findViewById(R.id.Password);
        confirmPasswordView = itemView.findViewById(R.id.confirmPassword);
        RegisterButton = itemView.findViewById(R.id.register_button);
        MentorButton = itemView.findViewById(R.id.mentor);
        StudentButton = itemView.findViewById(R.id.student);

        TextView login = itemView.findViewById(R.id.login);

        login.setText(Html.fromHtml("Already have an Account ? <u>Login</u>"));
        login.setOnClickListener(flipFragment);

        Instantiator();

        checkNetworkInfo();

        MentorButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && StudentButton.isChecked()) {
                    StudentButton.setChecked(false);
                }
            }
        });
        StudentButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && MentorButton.isChecked()) {
                    MentorButton.setChecked(false);
                }
            }
        });
        RegisterButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
        resendButton.setOnClickListener(this);


        return itemView;
    }

    private void startPhoneVerification(String phoneNumber) {
        Log.e(LOG_TAG,phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);
        verificationStatus = true;
    }

    private void AddToFireBase(String fname, String lname, String phno, String password, int registrationType) {
        try {
            password = AESCrypt.encrypt(password);
            PhoneUsers user = new PhoneUsers(fname, lname, phno, password, registrationType);
            databaseReference.child(phno).setValue(user);
        } catch (Exception e) {
            Log.e("RegisterFragment", e.toString());
            Toast.makeText(getActivity(), "Some unexpected error occurred!", Toast.LENGTH_SHORT).show();
        }
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
    private String createPhoneNumber(String ph){
        String s="+91";
        s=s.concat(ph);
        return s;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                statusFlag = false;

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        for (int i = 0; i < Items.size(); ++i) {
                            int id = Items.get(i).getID();

                            TextInputEditText editText = getActivity().findViewById(id);
                            String text = editText.getText().toString().trim();
                            if (text.isEmpty()) {
                                Toast.makeText(context, "The fields can't be blank", Toast.LENGTH_SHORT).show();
                                statusFlag = true;
                                break;
                            }
                        }
                    }
                }.run();

                if (!statusFlag) {
                    final String phno = phoneView.getText().toString().trim();
                    final String fname = firstNameView.getText().toString().trim();
                    final String lname = lastNameView.getText().toString().trim();
                    int phoneLength = phno.length();
                    final int registrationType;
                    final String password = passwordView.getText().toString().trim();
                    String confirmPassword = confirmPasswordView.getText().toString().trim();
                    if (StudentButton.isChecked()) {
                        //Log.e("RegisterFragment", "1");
                        registrationType = 1;
                    } else {
                        registrationType = 2;
                    }
                    if (phoneLength != 10) {
                        Toast.makeText(context, "The phone number that you entered is invalid", Toast.LENGTH_SHORT).show();
                    } else if (password.length() < 8) {
                        Toast.makeText(context, "The password should be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(confirmPassword)) {
                        //Log.e("RegisterFragment",password);
                        //Log.e("RegisterFragment",confirmPassword);
                        Toast.makeText(context, "The two passwords do not match", Toast.LENGTH_SHORT).show();
                    } else {
                        networkInfo = connectivityManager.getActiveNetworkInfo();
                        if (networkInfo == null) {
                            Toast.makeText(context, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final String phnoWithCountryCode = createPhoneNumber(phno);
                        databaseReference.child(phnoWithCountryCode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(context, "Error:This phone number is already registered with us!", Toast.LENGTH_SHORT).show();
                                } else {
                                    startPhoneVerification(phnoWithCountryCode);
                                    //AddToFireBase(fname, lname, phnoWithCountryCode, password, registrationType);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(context, "Some unexpected error occurred!!", Toast.LENGTH_SHORT).show();
                            }

                        });

                    }
                }
                break;
            case R.id.verify_button:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(context,"Field can not be empty!",Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationID, code);
                break;
            case R.id.resend_button:
                EditText phoneText=getActivity().findViewById(R.id.phone_code_sent);
                String phnoWithCountryCode=createPhoneNumber(phoneText.getText().toString());
                resendVerificationCode(phnoWithCountryCode, resendingToken);
                break;
            /*case R.id.sign_out_button:
                signOut();
                break;*/
        }
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
