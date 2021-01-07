package com.example.wastedfoodteam.seller.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wastedfoodteam.LoginActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.editSeller.EditSellerFragment;
import com.example.wastedfoodteam.seller.notification.NotificationFragment;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.example.wastedfoodteam.seller.product.AddProductFragment;
import com.example.wastedfoodteam.seller.product.ListProductSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.ChangePasswordSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.SendFeedbackSellerFragment;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.notification.SendNotif;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerHomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private NotificationUtil notificationUtil;

    private BottomNavigationView navigation;

    Seller seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        seller = new Seller();
        Intent intent = new Intent();
        notificationUtil = new NotificationUtil();
        Variable.CURRENT_USER = "SELLER";
        final SendNotif sendNotif = new SendNotif();
        sendNotif.updateToken();

        //get the header view
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        //add to tab navigation header
        CircleImageView iv_nav_header_profile_image = headerView.findViewById(R.id.iv_nav_header_profile_image);
        TextView tv_nav_header_user_name = headerView.findViewById(R.id.tv_nav_header_user_name);

        ImageButton information_tab_seller = findViewById(R.id.information_tab_seller);

        //button for open drawer layout
        information_tab_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        CommonFunction.setImageViewSrc(this,Variable.SELLER.getImage(), iv_nav_header_profile_image);
        tv_nav_header_user_name.setText(Variable.SELLER.getName());

        // Find our drawer view
        drawerLayout = findViewById(R.id.drawer_layout);

        //init navigation view
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.home){
                    SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main, sellerHomeFragment, sellerHomeFragment.getTag()).commit();
                } else if(id == R.id.item_nav_drawer_menu_information){
                    EditSellerFragment editSellerFragment = new EditSellerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,editSellerFragment,editSellerFragment.getTag()).commit();
                } else if(id == R.id.item_nav_drawer_menu_change_password){
                    ChangePasswordSellerFragment changePasswordSellerFragment = new ChangePasswordSellerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,changePasswordSellerFragment,changePasswordSellerFragment.getTag()).commit();
                }else if (id == R.id.item_nav_drawer_menu_feedback) {
                    SendFeedbackSellerFragment sendFeedbackSellerFragment = new SendFeedbackSellerFragment(new SendFeedbackSellerFragment.HandleSendFeedBack() {
                        @Override
                        public void onSuccess() {

                        }
                    }, Variable.SELLER.getId());
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main, sendFeedbackSellerFragment, sendFeedbackSellerFragment.getTag()).commit();
                }else if(id == R.id.item_nav_drawer_menu_manager){
                    ListProductSellerFragment listProductSellerFragment = new ListProductSellerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,listProductSellerFragment,listProductSellerFragment.getTag()).commit();
                }else if(id == R.id.item_nav_drawer_menu_alert){
                    /*RegisterSellerLocationFragment registerSellerLocationFragment = new RegisterSellerLocationFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,registerSellerLocationFragment,registerSellerLocationFragment.getTag()).commit();*/
                    NotificationFragment notificationFragment = new NotificationFragment(Variable.SELLER.getId()+"");
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,notificationFragment,notificationFragment.getTag()).commit();
                    /*RegisterSellerPhoneFragment registerSellerLocationFragment = new RegisterSellerPhoneFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,registerSellerLocationFragment,registerSellerLocationFragment.getTag()).commit();*/
                }else if(id == R.id.item_nav_drawer_menu_logout){
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(SellerHomeActivity.this, LoginActivity.class));
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //bottom navigation
        navigation = findViewById(R.id.bottom_nav_seller);
        Variable.bottomNavigationViewSeller = navigation;
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //set activity to home fragment
        SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main, sellerHomeFragment, sellerHomeFragment.getTag()).commit();

        notificationUtil.getTotalNotification(getApplicationContext(), Variable.SELLER.getId(),navigation);

        //notification badge
        if(Variable.TOTAL_NOTIFICATION > 0) {
            BadgeDrawable badge = navigation.getOrCreateBadge(R.id.item_bottom_nav_menu_notification);
            badge.setVisible(true);
            badge.setNumber(Variable.TOTAL_NOTIFICATION);
        }
        String type = getIntent().getStringExtra("From");
        if (type != null) {
            if ("notifyFrag".equals(type)) {
                NotificationFragment notificationFragment = new NotificationFragment(Variable.SELLER.getId() + "");
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, notificationFragment, notificationFragment.getTag()).commit();
            }
        }
    }



    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            FragmentManager manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.item_bottom_nav_menu_home:
                    SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                    manager.beginTransaction().replace(R.id.content_main, sellerHomeFragment, sellerHomeFragment.getTag()).commit();
                    return true;
                case R.id.item_bottom_nav_menu_add:
                    AddProductFragment addProductFragment = new AddProductFragment();
                    manager.beginTransaction().replace(R.id.content_main, addProductFragment, addProductFragment.getTag()).commit();
                    return true;
                case R.id.item_bottom_nav_menu_notification:
                    NotificationFragment notificationFragment = new NotificationFragment(Variable.SELLER.getId()+"");
                    manager.beginTransaction().replace(R.id.content_main,notificationFragment,notificationFragment.getTag()).commit();
                    notificationUtil.updateNotificationSeen(getApplicationContext(),Variable.SELLER.getId(),navigation);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}