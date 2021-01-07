package com.example.wastedfoodteam.buyer.buy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.followseller.FragmentSellerDetail;
import com.example.wastedfoodteam.buyer.order.BuyerOrder;
import com.example.wastedfoodteam.buyer.order.FragmentOrderDetail;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.notification.SendNotif;
import com.example.wastedfoodteam.utils.service.FollowResponseCallback;
import com.example.wastedfoodteam.utils.service.FollowVolley;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentDetailProduct extends Fragment {
    private BuyerProduct product;
    private final String UPDATE_ORDER_URL = Variable.IP_ADDRESS + Variable.INSERT_NEW_ORDER;
    private int orderQuantity;
    ImageButton ibFollow;
    ImageView ivProduct;
    CircleImageView civSeller;
    TextView tvPriceDiscount,
            tvOpenTime, tvPriceOriginal,
            tvDirect, tvDescription,
            tvBuyQuantity, tvQuantity, tvDistance,
            tvDiscount;
    Button btnIncrease, btnDecrease, btnBuy;
    private FollowVolley followVolley;

    NotificationUtil util;
    SendNotif sendNotif;

    public FragmentDetailProduct() {
        orderQuantity = 1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_product_detail, container, false);
        //mapping view
        mappingViewWithVariable(view);

        //get bundle values
        assert getArguments() != null;
        product = (BuyerProduct) getArguments().get("PRODUCT");

        setViewContent();

        //Set button follow
        followVolley = new FollowVolley(requireActivity().getApplicationContext(), ibFollow);
        String GET_FOLLOW_INFORMATION_URL = Variable.IP_ADDRESS + Variable.GET_FOLLOW;
        followVolley.setRequestGetFollow(GET_FOLLOW_INFORMATION_URL, Variable.BUYER.getId(), product.getSeller_id());

        //set image from url

        //set event
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDecreaseOnclick();
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnIncreaseOnClick();
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogConfirmBuy();
            }
        });


        ibFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followVolley.onIbFollowClick();
            }
        });

        civSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSellerDetail fragmentSellerDetail = new FragmentSellerDetail();

                //put product to next screen
                Bundle bundle = new Bundle();
                bundle.putSerializable("SELLER", product.getSeller());
                fragmentSellerDetail.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flSearchResultAH, fragmentSellerDetail, "")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    private void setViewContent() {
        CommonFunction.setImageViewSrc(requireActivity().getApplicationContext(), product.getSeller().getImage(), civSeller);
        CommonFunction.setImageViewSrc(requireActivity().getApplicationContext(), product.getImage(), ivProduct);
        tvQuantity.setText("Còn: " + product.getRemain_quantity() + "/" + product.getOriginal_quantity());
        tvDirect.setText(product.getSeller().getAddress());
        tvPriceDiscount.setText(CommonFunction.getCurrency(product.getSell_price()));
        tvPriceOriginal.setText(CommonFunction.getCurrency(product.getOriginal_price()));
        tvOpenTime.setText("Mở cửa từ: " + CommonFunction.getOpenClose(product.getStart_time(), product.getEnd_time()));
        tvDescription.setText(product.getDescription());
        tvBuyQuantity.setText(orderQuantity + "");
        tvDistance.setText(CommonFunction.getStringDistance(product.getSeller(), Variable.gps));
        tvDiscount.setText(CommonFunction.getDiscount(product.getSell_price(), product.getOriginal_price()));
    }

    @SuppressLint("SetTextI18n")
    private void btnIncreaseOnClick() {
        if (orderQuantity < product.getRemain_quantity()) {
            orderQuantity++;
            tvBuyQuantity.setText(orderQuantity + "");
        }
    }

    @SuppressLint("SetTextI18n")
    private void btnDecreaseOnclick() {
        if (orderQuantity > 1) {
            orderQuantity--;
            tvBuyQuantity.setText(orderQuantity + "");
        }
    }

    private boolean isImageButtonIsFollowed(Object tag) {
        return tag.equals(R.drawable.followed);
    }

    @SuppressLint("SetTextI18n")
    private void setDialogConfirmBuy() {
        final Dialog confirmDialog = new Dialog(requireActivity());
        confirmDialog.setTitle("Xác nhận mua hàng");
        confirmDialog.setContentView(R.layout.dialog_buyer_confirm_buy);
        confirmDialog.show();

        final EditText etMessage = confirmDialog.findViewById(R.id.etMessage);
        final Button btnConfirm = confirmDialog.findViewById(R.id.btnConfirm);
        final Button btnCancel = confirmDialog.findViewById(R.id.btnCancel);
        final TextView tvNotice = confirmDialog.findViewById(R.id.tvNotice);

        tvNotice.setText("Xác nhận mua " + orderQuantity + " sản phẩm?");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBuyOnClick(etMessage.getText().toString(), product);
                confirmDialog.cancel();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.cancel();
            }
        });

    }

    /**
     * buy
     */
    private void btnBuyOnClick(final String message, final BuyerProduct product) {
        RequestQueue requestInsertOrder = Volley.newRequestQueue(requireActivity().getApplicationContext());
        StringRequest stringRequestInsert = new StringRequest(Request.Method.POST, UPDATE_ORDER_URL, new Response.Listener<String>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("ERROR")) {
                    Toast.makeText(requireActivity().getApplicationContext(), "Có lỗi bất thường xảy ra", Toast.LENGTH_LONG);
                } else if (response.equalsIgnoreCase("NOT ENOUGH QUANTITY")) {
                    Toast.makeText(requireActivity().getApplicationContext(), "Số lượng còn lại không đủ", Toast.LENGTH_LONG);
                } else {
                    int order_id = 0;
                    try{
                        order_id = Integer.parseInt(response);
                    }catch (Exception e){

                    }
                    Toast.makeText(requireActivity().getApplicationContext(), "Thành công", Toast.LENGTH_LONG);
                    util = new NotificationUtil();
                    sendNotif = new SendNotif();

                    String sendMessage = setUpMessageForSend(product, message);

                    util.addNotification(getContext(), Variable.BUYER.getId(), product.getSeller_id(), sendMessage, order_id);
                    sendNotif.notificationHandle(product.getSeller().getFirebase_UID(), "Wasted food app", sendMessage);

                    moveToFragmentOrderDetail();
                    Log.i("Firebase_UID", product.getSeller().getFirebase_UID());
                }
            }
        }, new Response.ErrorListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireActivity().getApplicationContext(), "Lỗi hệ thống: " + error.getMessage(), Toast.LENGTH_LONG);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("buyer", Variable.BUYER.getId() + "");
                params.put("product", product.getId() + "");
                params.put("quantity", orderQuantity + "");
                params.put("status", Order.Status.BUYING + "");
                params.put("total_cost", (orderQuantity * product.getSell_price()) + "");
                return params;
            }
        };
        requestInsertOrder.add(stringRequestInsert);
    }

    private String setUpMessageForSend(@NotNull BuyerProduct product, String message) {
        String sendMessage = "Khách hàng " + Variable.BUYER.getName() + " đã đặt hàng sản phẩm " + product.getName() + " của bạn";
        if (message != null)
            if (!message.isEmpty())
                sendMessage = sendMessage + "\nKèm với lời nhắn: " + message;
        return sendMessage;
    }


    /**
     * Open after buy
     */
    private void moveToFragmentOrderDetail() {
        BuyerOrder order = new BuyerOrder(Variable.BUYER.getId(), product.getId(), orderQuantity, Order.Status.BUYING, orderQuantity * product.getSell_price());
        order.setProduct(product);
        FragmentOrderDetail fragmentOrderDetail = new FragmentOrderDetail(order);

        //put product to next screen
        Bundle bundle = new Bundle();
        bundle.putSerializable("PRODUCT", product);
        fragmentOrderDetail.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, fragmentOrderDetail, "")
                .addToBackStack(null)
                .commit();

    }

    /**
     * Set value for view variable
     *
     * @param view
     */
    private void mappingViewWithVariable(View view) {
        ivProduct = view.findViewById(R.id.ivProduct);

        civSeller = view.findViewById(R.id.civSeller);

        tvQuantity = view.findViewById(R.id.tvQuantity);
        tvPriceDiscount = view.findViewById(R.id.tvPriceDiscount);
        tvPriceOriginal = view.findViewById(R.id.tvPriceOriginal);
        tvDirect = view.findViewById(R.id.tvDirect);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvBuyQuantity = view.findViewById(R.id.tvBuyQuantity);
        tvOpenTime = view.findViewById(R.id.tvOpenTime);
        tvDistance = view.findViewById(R.id.tvDistance);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        btnIncrease = view.findViewById(R.id.btnIncrease);
        btnDecrease = view.findViewById(R.id.btnDecrease);
        btnBuy = view.findViewById(R.id.btnBuy);
        ibFollow = view.findViewById(R.id.iBtnFollow);
    }

    @Override
    public void onPause() {
        String UPDATE_FOLLOW_URL = Variable.IP_ADDRESS + Variable.UPDATE_FOLLOW;
        try {
            followVolley.setRequestUpdateFollow(new FollowResponseCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.w("FollowResult", result);
                }
            }, UPDATE_FOLLOW_URL, Variable.BUYER.getId(), product.getSeller_id(), isImageButtonIsFollowed(ibFollow.getTag()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }
}
