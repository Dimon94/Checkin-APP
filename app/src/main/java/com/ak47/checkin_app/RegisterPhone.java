package com.ak47.checkin_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ak47.checkin_app.bean.MyUser;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

public class RegisterPhone extends AppCompatActivity {


    private Button bnGetPhoneHashcode;
    private Button bnRegPhoneView;
    private EditText etRegisterPhoneNum;
    private EditText etPhoneHashcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        // TODO: 2015/11/17 ui初始化，手机验证码一键注册
        bnGetPhoneHashcode = (Button) findViewById(R.id.bn_get_phone_hashcode);
        bnRegPhoneView = (Button) findViewById(R.id.bn_reg_phoneview);
        etRegisterPhoneNum = (EditText) findViewById(R.id.et_register_phonenum);
        etPhoneHashcode = (EditText) findViewById(R.id.et_register_phone_hashcode);

        //发送验证码
        bnGetPhoneHashcode.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               requestPhoneHashcode();
           }
       });
        //点击注册。判断验证码有没有输入正确。如果正确，可以登录。
        bnRegPhoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpByHashcode();

            }
        });
    }

    private void signUpByHashcode() {
//        获取用户验证码，手机，密码
        String mHashcode = etPhoneHashcode.getText().toString();
        String mPhoneNum = etRegisterPhoneNum.getText().toString();

        BmobUser.signOrLoginByMobilePhone(this, mPhoneNum, mHashcode, new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                // TODO Auto-generated method stub
                if (user != null) {
                    Log.i("smile", "用户登陆成功");
                    startActivity(new Intent(RegisterPhone.this,MainActivity.class));
                    finish();
                }
            }
        });

    }

    private void requestPhoneHashcode() {
        String mPhoneNum = etRegisterPhoneNum.getText().toString();
        BmobSMS.requestSMSCode(RegisterPhone.this, mPhoneNum, "移动签到验证码", new RequestSMSCodeListener() {

            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {//验证码发送成功
                    Log.i("smile", "短信id：" + integer);//用于查询本次短信发送详情
                }
            }
        });

    }
}
