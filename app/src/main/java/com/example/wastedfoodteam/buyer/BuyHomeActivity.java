package com.example.wastedfoodteam.buyer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.LoginActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.FragmentListProduct;
import com.example.wastedfoodteam.buyer.followseller.FragmentListSellerFollow;
import com.example.wastedfoodteam.buyer.infomation.FragmentEditInformationBuyer;
import com.example.wastedfoodteam.buyer.order.FragmentOrderHistory;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.seller.notification.NotificationFragment;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.example.wastedfoodteam.seller.sellerFragment.SendFeedbackSellerFragment;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.GPSTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class BuyHomeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final String TAG_LIST_FRAGMENT = "ListProduct";
    ImageButton ibUserInfo;
    FragmentListProduct fragmentListProduct;
    NotificationUtil notificationUtil;
    private BottomNavigationView navigation;
    TextView navHeaderTextViewUsername;
    ImageView navHeaderImageViewUser;
    private DrawerLayout drawerLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);


        if (checkGPSPermission()) {
            getGPSLocation();
        }
        //mapping
        ibUserInfo = findViewById(R.id.ibUserInfo);
        //Variable.BUYER = new Buyer();

        Variable.CURRENT_USER = "BUYER";
        notificationUtil = new NotificationUtil();
        checkIsActive();

        //get the header view
        NavigationView navigationView = findViewById(R.id.nav_view_buyer);
        View headerView = navigationView.getHeaderView(0);

        navHeaderTextViewUsername = headerView.findViewById(R.id.navHeaderTextViewUsername);
        navHeaderImageViewUser = headerView.findViewById(R.id.navHeaderImageViewUser);
        navHeaderTextViewUsername.setText(Variable.BUYER.getName() + "");

        CommonFunction.setImageViewSrc(this, Variable.BUYER.getImage(), navHeaderImageViewUser);

        // Find our drawer view
        drawerLayout = findViewById(R.id.drawer_layout_buyer);

        //or maybe u can use button instead
        ibUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //init navigation view
        navigationView = findViewById(R.id.nav_view_buyer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int id = item.getItemId();
                switch (id) {
                    case R.id.itemNavMenuBuyerInfor:
                        FragmentEditInformationBuyer fragmentEditInformationBuyer = new FragmentEditInformationBuyer();
                        clearBackStackAndOpenFragment(fragmentEditInformationBuyer);
                        break;
                    case R.id.itemNavLogout:
                        Variable.BUYER = null;
                        switch (Variable.CHECK_LOGIN) {
                            case 2:
                                finish();
                                signOutFacebook();
                                break;
                            case 1:
                                finish();
                                signOutGoogle();
                                break;
                        }
                        break;
                    case R.id.itemNavFeedBack:
                        SendFeedbackSellerFragment sendFeedbackSellerFragment = new SendFeedbackSellerFragment(new SendFeedbackSellerFragment.HandleSendFeedBack() {
                            @Override
                            public void onSuccess() {

                            }
                        }, Variable.BUYER.getId());
                        clearBackStackAndOpenFragment(sendFeedbackSellerFragment);
                        break;
                    case R.id.itemNavFollowSeller:
                        FragmentListSellerFollow fragmentListSellerFollow = new FragmentListSellerFollow();
                        clearBackStackAndOpenFragment(fragmentListSellerFollow);
                        break;
                    case R.id.itemNavOrderHistory:
                        FragmentOrderHistory fragmentOrderHistory = new FragmentOrderHistory();
                        clearBackStackAndOpenFragment(fragmentOrderHistory);
                        break;
                    case R.id.itemNavNotification:
                        NotificationFragment notificationFragment = new NotificationFragment(Variable.BUYER.getId() + "");
                        clearBackStackAndOpenFragment(notificationFragment);
                        notificationUtil.updateNotificationSeen(getApplicationContext(), Variable.BUYER.getId(), navigation);
                        break;
                    case R.id.itemNavMenuBuyerHome:
                        addFragmentListProduct();
                        break;

                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout_buyer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //bottom navigation
        navigation = findViewById(R.id.bottom_nav_buyer);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        notificationUtil.getTotalNotification(getApplicationContext(), Variable.BUYER.getId(), navigation);
        //notification badge
        if (Variable.TOTAL_NOTIFICATION > 0) {
            BadgeDrawable badge = navigation.getOrCreateBadge(R.id.item_bottom_nav_menu_notification);
            badge.setVisible(true);
            badge.setNumber(Variable.TOTAL_NOTIFICATION);
        }
        String type = getIntent().getStringExtra("From");
        if (type != null) {
            if ("notifyFrag".equals(type)) {
                NotificationFragment notificationFragment = new NotificationFragment(Variable.BUYER.getId() + "");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flSearchResultAH, notificationFragment, "")
                        .addToBackStack(null)
                        .commit();
            }
        }

    }

    private void getGPSLocation() {
        GPSTracker gps = new GPSTracker(this, new GPSTracker.HandleGetLastKnowLocation() {
            @Override
            public void onSuccess(Location location) {
                Variable.gps = location;
                addFragmentListProduct();
            }

            @Override
            public void onFailure() {

            }
        });
        if (gps.canGetLocation()) {
            Variable.gps = gps.getLocation();
            if (Variable.gps != null)
                addFragmentListProduct();
        } else {
            gps.showSettingAlert();
        }

    }

    private boolean checkGPSPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != MockPackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
            return false;
        }
        return true;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_bottom_nav_menu_buyer_home:
                    addFragmentListProduct();
                    return true;
                case R.id.item_bottom_nav_menu_buyer_notification:
                    NotificationFragment notificationFragment = new NotificationFragment(Variable.BUYER.getId() + "");
                    clearBackStackAndOpenFragment(notificationFragment);
                    notificationUtil.updateNotificationSeen(getApplicationContext(), Variable.BUYER.getId(), navigation);
                    return true;
                case R.id.item_bottom_nav_menu_buyer_history:
                    FragmentOrderHistory fragmentOrderHistory = new FragmentOrderHistory();
                    clearBackStackAndOpenFragment(fragmentOrderHistory);
                    return true;
                case R.id.item_bottom_nav_menu_buyer_follow:
                    FragmentListSellerFollow fragmentListSellerFollow = new FragmentListSellerFollow();
                    clearBackStackAndOpenFragment(fragmentListSellerFollow);
                    return true;
            }
            return false;
        }
    };

    private void clearBackStackAndOpenFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flSearchResultAH, fragment, "")
                .addToBackStack(null)
                .commit();
    }


    private void addFragmentListProduct() {
        fragmentListProduct = new FragmentListProduct();
        //add fragment search result
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flSearchResultAH, fragmentListProduct, TAG_LIST_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void signOutFacebook() {
        LoginManager.getInstance().logOut();
        SharedPreferences sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        sharedpreferences.edit().clear().apply();
        finishAndRemoveTask();
        Toast.makeText(BuyHomeActivity.this, "Sign out Success", Toast.LENGTH_LONG).show();
        startActivity(new Intent(BuyHomeActivity.this, LoginActivity.class));
    }

    private void signOutGoogle() {
        try {
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(
                    getApplicationContext(),
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut();
            SharedPreferences sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().apply();
            finishAndRemoveTask();
            startActivity(new Intent(BuyHomeActivity.this, LoginActivity.class));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGPSLocation();
            } else {
                setRequestCodeResultDialog();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setRequestCodeResultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Ứng dụng chỉ hoạt động được khi sử dụng GPS. Cấp quyền lại cho ứng dụng?")
                .setTitle("Thông báo");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (checkGPSPermission()) getGPSLocation();
            }
        });
        builder.setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isListFragment()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else super.onBackPressed();
    }

    private boolean isListFragment() {
        FragmentListProduct fragmentListProduct = (FragmentListProduct) getSupportFragmentManager().findFragmentByTag(TAG_LIST_FRAGMENT);
        return fragmentListProduct != null && fragmentListProduct.isVisible();
    }

    private void checkIsActive() {

        try {
            String accountId = Variable.BUYER.getId() + "";
            //String accountId = "201";
            String url = Variable.IP_ADDRESS + "login/checkIsActive.php?account_id=" + accountId;
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if ("account is locked".equals(response)) {
                        Toast.makeText(BuyHomeActivity.this, "Tài khoản của bạn đã bị khóa", Toast.LENGTH_LONG).show();
                        switch (Variable.CHECK_LOGIN) {
                            case 2:
                                signOutFacebook();
                                break;
                            case 1:
                                signOutGoogle();
                                break;
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(BuyHomeActivity.this, "Lỗi kết nỗi", Toast.LENGTH_LONG).show();

                }
            }
            );
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(BuyHomeActivity.this, "Vui lòng thử lại", Toast.LENGTH_LONG).show();
        }
    }
}