package com.ak47.checkin_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ak47.checkin_app.bean.MyUser;

import cn.bmob.v3.listener.SaveListener;

public class RegisterEmail extends AppCompatActivity {

    private EditText etEmail;
    private Button bnRegisterEmail;
    private EditText etRegisterPassword;
    private EditText etRegisterPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        //ui
        bnRegisterEmail = (Button) findViewById(R.id.bn_reg_emailview);
        etEmail = (EditText) findViewById(R.id.et_register_email);
        etRegisterPassword = (EditText) findViewById(R.id.et_password_email);
        etRegisterPassword2 = (EditText) findViewById(R.id.et_password2_email);
        //点击事件
        bnRegisterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击判断是否符合规范（写判断函数）,是就启动注册
                if (checkIsLegal()) {
                    //启动注册
                    attemptRegister();
                }
                else {
                    toast("两次输入的密码不同,请重新输入");
                }

            }
        });


    }

    private boolean checkIsLegal() {
//两次输入的密码一致,而且输入的是邮箱
        // TODO: 2015/11/17 增加密码复杂性判断逻辑

        return etRegisterPassword.getText().toString().equals(etRegisterPassword2.getText().toString());
    }

    public void attemptRegister() {

        //将edittext中的数据拿出来
        String mEmail = etEmail.getText().toString();
        String mPassword = etRegisterPassword.getText().toString();

        MyUser mUser = new MyUser();
        mUser.setUsername(mEmail);
        mUser.setPassword(mPassword);
        mUser.setEmail(mEmail);
//注意：不能用save方法进行注册
        mUser.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                toast("注册成功:请返回登录");
            }

            @Override
            public void onFailure(int code, String msg) {
                toast("注册失败:" + msg);
            }
        });
    }
    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
