package com.ak47.checkin_app.view.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ak47.checkin_app.Constants;
import com.ak47.checkin_app.GloableParams;
import com.ak47.checkin_app.R;
import com.ak47.checkin_app.common.UserUtils;
import com.ak47.checkin_app.common.Utils;
import com.ak47.checkin_app.view.activity.ImageActivity;
import com.ak47.checkin_app.view.activity.PublicActivity;
import com.ak47.checkin_app.view.activity.SettingActivity;
import com.ak47.checkin_app.view.activity.UserActivity;

/**
 * Created by Dombi on 2015/11/2.
 * The Fragment must from v4 lib.
 */
public class UserFragment extends Fragment implements View.OnClickListener {
    private Activity ctx;
    private View layout;
    private TextView tvname, tv_accout;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_user, container, false);
//        //return super.onCreateView(inflater, container, savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (layout == null) {
            ctx = this.getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.fragment_profile,
                    null);
            initViews();
            initData();
            setOnListener();
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        return layout;
    }

    private void initViews() {
        tvname = (TextView) layout.findViewById(R.id.tvname);
        tv_accout = (TextView) layout.findViewById(R.id.tvmsg);
        String  id = Utils.getValue(getActivity(), Constants.User_ID);
        tv_accout.setText(getString(R.string.Position) + "：" + id);
        if (GloableParams.UserInfos != null) {
            String name = UserUtils.getUserName(ctx);
            if (name != null && !TextUtils.isEmpty(name))
                tvname.setText(name);
        }
    }

    private void setOnListener() {
        layout.findViewById(R.id.view_user).setOnClickListener(this);
        layout.findViewById(R.id.txt_album).setOnClickListener(this);
        layout.findViewById(R.id.txt_collect).setOnClickListener(this);
        layout.findViewById(R.id.txt_setting).setOnClickListener(this);
    }

    private void initData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_user:
                Utils.start_Activity(getActivity(), UserActivity.class);
                break;
            case R.id.txt_album:// 相册
                ContentValues values = new ContentValues();
                values.put( Constants.NAME, getString(R.string.my_posts));
                Utils.start_Activity(getActivity(), ImageActivity.class,
                         values);
                break;
            case R.id.txt_collect:// 收藏
                ContentValues values1 = new ContentValues();
                values1.put(Constants.NAME, getString(R.string.assignment));
                Utils.start_Activity(getActivity(), PublicActivity.class,
                        values1);
                break;
            case R.id.txt_setting:// 设置
                Utils.start_Activity(getActivity(), SettingActivity.class);
                break;
            default:
                break;
        }
    }

}
