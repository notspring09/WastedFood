package com.example.wastedfoodteam.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.BuyHomeActivity;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.utils.service.BuyerResponseCallback;
import com.example.wastedfoodteam.utils.service.BuyerVolley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FragmentLoginBuyer extends Fragment {
    SharedPreferences sharedpreferences;
    GoogleSignInClient mGoogleSignInClient;
    final int RC_SIGN_IN = 10002;
    Button btnPartnerOption, btnSignInGoogle;
    //    SignInButton btnSignInGoogle;
    LoginButton btnSignInFacebook;
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    public static final String mPreference = "mypref";
    public static final String BUYER_JSON = "BUYER_JSON";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_buyer, container, false);
        btnSignInGoogle = view.findViewById(R.id.btnGoogleSignInFLB);
        btnSignInFacebook = view.findViewById(R.id.btnFacebookSignInFLB);
        btnPartnerOption = view.findViewById(R.id.btnPartnerOptionFLB);
        mAuth = FirebaseAuth.getInstance();
        sharedpreferences = requireActivity().getSharedPreferences(mPreference, Context.MODE_PRIVATE);

        getBuyerFromPreference();
        handleSignInFacebook();

        callbackManager = CallbackManager.Factory.create();
        //btnSignInFacebook.setPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends","user_gender"));
        btnSignInFacebook.setPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        btnSignInFacebook.setFragment(this);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(), "Kiểm tra lại kết nối Internet", Toast.LENGTH_LONG).show();
            }
        });
        //google option
        addGoogleSignInOption();
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To Do Check Phone
                signInGoogle();
            }
        });

        btnPartnerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentLoginPartner();
            }
        });

        return view;
    }

    public void saveUserInformation(Buyer buyer) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String buyerJson = gson.toJson(buyer);
        editor.putString(BUYER_JSON, buyerJson);
        editor.apply();
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("fb", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("fb", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            resultFacebook(user.getUid());
                            //Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
                            Variable.CHECK_LOGIN = 2;
                            //getActivity().finishAndRemoveTask();
                            //startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fb", "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("gg", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            resultGoogle(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("gg", "signInWithCredential:failure", task.getException());
                        }

                    }
                });

    }

    /**
     * google sign in option
     */
    private void addGoogleSignInOption() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    /**
     * Start Sign in Google flow
     */
    private void signInGoogle() {
        Date currentTime = Calendar.getInstance().getTime();
        Log.d("date:", currentTime.toString());
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        Variable.CHECK_LOGIN = 1;
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * keep Sign In Google
     *
     * @param completedTask
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Variable.CHECK_LOGIN = 1;
    }

    private void resultGoogle(String firebase_UID) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if (acct != null) {
            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String thirdPartyId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            String urlImage = personPhoto.toString();
            String gender = "1";
            String dob = "0000-00-00";
            String urlInsert = Variable.IP_ADDRESS + "login/register3rdParty.php";
            checkDataAndInsert3rdParty(urlInsert, email, thirdPartyId, name, urlImage, dob, gender, firebase_UID);
        }
    }

    private void getUserInformationBy3rdPartyId(String thirdPartyId) {

        BuyerVolley buyerVolley = new BuyerVolley(getActivity(), Variable.IP_ADDRESS + "information/getBuyerBy3rdPartyId.php");
        buyerVolley.setRequestGetBuyerBy3rdId(new BuyerResponseCallback() {
            @Override
            public void onSuccess(Buyer result) {
                Variable.BUYER = result;
                saveUserInformation(Variable.BUYER);

                Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
                requireActivity().finishAndRemoveTask();
                startActivity(intent);
            }

        }, thirdPartyId);
    }

    /**
     * Check account
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                Variable.CHECK_LOGIN = 1;
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(task);
                firebaseAuthWithGoogle(account.getIdToken());
            }
        } catch (Exception e) {
            Log.e("SignIn", "Code" + Arrays.toString(e.getStackTrace()));
        }
    }


    @Override
    public void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if (account != null) {
            if (isBuyerInformationExist()) {
                requireActivity().finishAndRemoveTask();
                startActivity(new Intent(requireActivity(), BuyHomeActivity.class));
            } else
                getUserInformationBy3rdPartyId(account.getId());
        }
        super.onStart();
    }

    private boolean isBuyerInformationExist() {
        if (Variable.BUYER == null)
            getBuyerFromPreference();
        return Variable.BUYER != null;
    }

    private void getBuyerFromPreference() {
        if (sharedpreferences.contains(BUYER_JSON)) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            String buyerJson = sharedpreferences.getString(BUYER_JSON, "");
            Variable.BUYER = gson.fromJson(buyerJson, Buyer.class);
        }
    }

    /**
     * Keep Sign In Facebook
     */
    private void handleSignInFacebook() {
        //check loginFB
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
            Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
            Variable.CHECK_LOGIN = 2;
            requireActivity().finishAndRemoveTask();
            startActivity(intent);

        }
    }

    /**
     * add fragment login for seller
     */
    public void addFragmentLoginPartner() {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentLoginPartner fragmentLoginPartner = new FragmentLoginPartner();
        fragmentTransaction.replace(R.id.flFragmentLayoutAM, fragmentLoginPartner);
        fragmentTransaction.commit();
    }


    private void resultFacebook(final String firebase_UID) {

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("Json", response.getJSONObject().toString());
                try {
                    String email = object.getString("email");
                    String name = object.getString("name");
                    String thirdPartyId = object.getString("id");
                    String dob = object.getString("birthday");
                    //object.getString("gender");
                    String gender = "1";
                    String urlImage = "https://graph.facebook.com/" + thirdPartyId + "/picture?type=large";
                    String urlInsert = Variable.IP_ADDRESS + "login/register3rdParty.php";

                    checkDataAndInsert3rdParty(urlInsert, email, thirdPartyId, name, urlImage, dob, gender, firebase_UID);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameter = new Bundle();
        parameter.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameter);
        graphRequest.executeAsync();
        Log.d("Tag: ", "failed");
    }

    // checking register 3rdparty
    private void checkDataAndInsert3rdParty(String url, final String emailFB,
                                            final String thirdPartyIdFB, final String nameFB, final String urlImageFB,
                                            final String dobFB, final String genderFB, final String firebase_UID) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if ("account is locked".equals(response)) {
                        Toast.makeText(getActivity(), "Tài khoản của bạn đã bị khóa", Toast.LENGTH_LONG).show();
                        switch (Variable.CHECK_LOGIN) {
                            case 2:
                                signOutFacebook();
                                break;
                            case 1:
                                signOutGoogle();
                                break;
                        }

                    }
                    else {
                        JSONArray object = new JSONArray(response);

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                        Variable.BUYER = gson.fromJson(object.getString(0), Buyer.class);
                        saveUserInformation(Variable.BUYER);

                        Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
                        requireActivity().finishAndRemoveTask();
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("StringResponse", response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "lỗi kết nỗi", Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("thirdPartyId", thirdPartyIdFB);
                params.put("name", nameFB);
                params.put("urlImage", urlImageFB);
                params.put("dob", dobFB);
                params.put("gender", genderFB);
                params.put("email", emailFB);
                params.put("firebase_UID", firebase_UID);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void signOutGoogle() {
        try {
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(
                    requireActivity().getApplicationContext(),
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void signOutFacebook() {
        LoginManager.getInstance().logOut();
    }


}
