package com.sitechdev.vehicle.pad.module.phone.fragment;


import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;

import com.sitechdev.vehicle.lib.event.BindEventBus;
import com.sitechdev.vehicle.lib.util.ForbidClickEnable;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.MvpFragment;
import com.sitechdev.vehicle.pad.event.DialEvent;
import com.sitechdev.vehicle.pad.event.SysEvent;
import com.sitechdev.vehicle.pad.model.contract.DialContract;
import com.sitechdev.vehicle.pad.module.phone.PhoneDialWindow;
import com.sitechdev.vehicle.pad.module.phone.presenter.DialPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 拨号页面
 *
 * @author liuhe
 * @date 2019/04/03
 */
@BindEventBus
public class DialFragment extends MvpFragment<DialPresenter> implements View.OnClickListener,
        View.OnLongClickListener, DialContract.View {

    public static final String TAG = DialFragment.class.getSimpleName();

    private List<View> viewList;
    private TableLayout mDialTagLayout;
    private EditText mEdittext;
    private PhoneDialWindow mPhoneDialWindow;
    private View mDelete;

    public DialFragment() {
    }

    public static DialFragment newInstance() {
        Bundle args = new Bundle();
        DialFragment fragment = new DialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dial;
    }

    @Override
    protected DialPresenter createPresenter() {
        return new DialPresenter();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mDialTagLayout = mContentView.findViewById(R.id.tl_dial_container);
        mEdittext = mContentView.findViewById(R.id.phone_dial_edit);
        mDelete = mContentView.findViewById(R.id.dialpad_key_delete);
        mEdittext.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mEdittext.setTextIsSelectable(true);

        viewList = new ArrayList<>();
        viewList.add(mContentView.findViewById(R.id.dialpad_key_one));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_two));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_three));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_four));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_five));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_six));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_seven));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_eight));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_nine));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_star));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_hash));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_dial));
        viewList.add(mContentView.findViewById(R.id.dialpad_key_zero));
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
        mDelete.setOnClickListener(this);
        for (int i = 0; i < viewList.size(); i++) {
            viewList.get(i).setOnClickListener(this);
            if (i == viewList.size() - 1 || i == viewList.size() - 2) {
                viewList.get(i).setOnLongClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialpad_key_one) {
            mPresenter.passKey('1');
        } else if (i == R.id.dialpad_key_two) {
            mPresenter.passKey('2');
        } else if (i == R.id.dialpad_key_three) {
            mPresenter.passKey('3');
        } else if (i == R.id.dialpad_key_four) {
            mPresenter.passKey('4');
        } else if (i == R.id.dialpad_key_five) {
            mPresenter.passKey('5');
        } else if (i == R.id.dialpad_key_six) {
            mPresenter.passKey('6');
        } else if (i == R.id.dialpad_key_seven) {
            mPresenter.passKey('7');
        } else if (i == R.id.dialpad_key_eight) {
            mPresenter.passKey('8');
        } else if (i == R.id.dialpad_key_nine) {
            mPresenter.passKey('9');
        } else if (i == R.id.dialpad_key_zero) {
            mPresenter.passKey('0');
        } else if (i == R.id.dialpad_key_star) {
            mPresenter.passKey('*');
        } else if (i == R.id.dialpad_key_hash) {
            mPresenter.passKey('#');
        } else if (i == R.id.dialpad_key_dial) {
            if (ForbidClickEnable.isForbidClick(700)) {
                return;
            }
            mPresenter.dial();
            mEdittext.setText("");
        } else if (i == R.id.dialpad_key_delete) {
            mPresenter.delete();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.dialpad_key_zero) {
            mPresenter.passKey('+');
        }
        return true;
    }

    @Override
    public void onDialNumber(String number) {
//        if (null == mPhoneDialWindow) {
//            mPhoneDialWindow = new PhoneDialWindow(mContext);
//        }
//        if (!mPhoneDialWindow.isShowing()) {
//            mPhoneDialWindow.showAtLocation(mDialTagLayout, Gravity.LEFT | Gravity.TOP, 0, 123);
//        }
//        mPhoneDialWindow.refreshView(number);
        mEdittext.setText(number);
    }

    @Override
    public void hideInputWindow() {
        if (null != mPhoneDialWindow && mPhoneDialWindow.isShowing()) {
            mPhoneDialWindow.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSysEvent(SysEvent event) {
        if (null != mPresenter) {
            mPresenter.onSysEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPhoneNumStr(DialEvent dialEvent) {
        if (null != mPresenter) {
            mPresenter.refreshPhoneNumStr(dialEvent);
        }
    }

}
