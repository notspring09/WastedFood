package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class SendFeedbackSellerFragment extends Fragment {

    //ui view
    EditText editText_sendFeedback_title;
    EditText editText_sendFeedback_description;
    TextInputLayout tilTitle,tilDescription;
    Button btn_sendFeedback_send;

    String title, description;

    final int id;
    final HandleSendFeedBack handleSendFeedBack;
    Boolean bolTitle = false,bolDescription = false;

    public SendFeedbackSellerFragment(HandleSendFeedBack handleSendFeedBack, int id) {
        this.id = id;
        this.handleSendFeedBack = handleSendFeedBack;
    }

    public interface HandleSendFeedBack {
        void onSuccess();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_feedback_seller, container, false);

        //init ui view
        editText_sendFeedback_title = view.findViewById(R.id.editText_sendFeedback_title);
        editText_sendFeedback_description = view.findViewById(R.id.editText_sendFeedback_description);
        btn_sendFeedback_send = view.findViewById(R.id.btn_sendFeedback_send);
        tilDescription = view.findViewById(R.id.textInputFeedbackDescription);
        tilTitle = view.findViewById(R.id.textInputFeedbackTitle);

        //for multiline EditText
        //scroll for EditText
        editText_sendFeedback_description.setScroller(new Scroller(getActivity().getApplicationContext()));
        editText_sendFeedback_description.setVerticalScrollBarEnabled(true);

        //Edit Text Line
        editText_sendFeedback_description.setMinLines(5);
        editText_sendFeedback_description.setMaxLines(5);

        //get data from home activity
//        id = Variable.SELLER.getId();

        //click send button handle
        btn_sendFeedback_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
        editText_sendFeedback_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(CommonFunction.checkEmptyEditText(editText_sendFeedback_title)){
                       bolTitle = true;
                        tilTitle.setError(null);
                    }else{
                        bolTitle = false;
                        tilTitle.setError("Tiêu đề không được để trống");
                    }
                }
            }
        });
        editText_sendFeedback_description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(editText_sendFeedback_description.getText().toString().trim().length() > 200){
                        bolDescription = false;
                        tilDescription.setError("Nội dung không được quá 200 ký tự");
                    }else{
                        bolDescription = true;
                        tilDescription.setError(null);
                    }
                }
            }
        });
        return view;
    }

    private void clearText() {
        editText_sendFeedback_description.setText("");
        editText_sendFeedback_title.setText("");
    }

    private void inputData() {
        title = editText_sendFeedback_title.getText().toString().trim();
        description = editText_sendFeedback_description.getText().toString().trim();
        //validate data
        String urlGetData = Variable.IP_ADDRESS + "seller/sellerFeedback.php";
        editText_sendFeedback_description.clearFocus();
        editText_sendFeedback_title.clearFocus();
        btn_sendFeedback_send.requestFocus();

        if( bolDescription && bolTitle ) {
            addFeedback(urlGetData);
        }
    }


    //add feedback data
    private void addFeedback(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Succesfully update")) {
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            clearText();

                            //TODO move back to home
                            handleSendFeedBack.onSuccess();
                        } else {
                            Toast.makeText(getActivity(), "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("account_id", String.valueOf(id));
                params.put("title", title);
                params.put("description", description);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}