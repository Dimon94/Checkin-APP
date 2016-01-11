package com.ak47.checkin_app.forgetpassword;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ak47.checkin_app.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;

public class ForgetByEmailFragment extends Fragment {
    private View view;
    private Button btnSendEmail;
    private EditText etEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forget_by_email, container, false);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
//获取输入框内容
        btnSendEmail = (Button) view.findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2015/11/23 发送邮件到注册邮箱
                sendVerifyEmail();

            }
        });
        return view;
    }

    private void sendVerifyEmail() {
        final String email = etEmail.getText().toString();
        Log.i("email","email = "+email);
        BmobUser.resetPasswordByEmail(getActivity(), email, new ResetPasswordByEmailListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                toast("重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
            }

            @Override
            public void onFailure(int code, String e) {
                // TODO Auto-generated method stub
                toast("重置密码失败:" + e);
            }
        });
    }

    //Toast显示
    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


}
