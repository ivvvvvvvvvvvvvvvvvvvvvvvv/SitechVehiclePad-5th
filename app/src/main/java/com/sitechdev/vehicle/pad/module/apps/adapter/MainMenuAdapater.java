package com.sitechdev.vehicle.pad.module.apps.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sitechdev.pad.lib.aoplibrary.annotation.DebugTrace;
import com.sitechdev.vehicle.lib.imageloader.GlideUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.bean.AllModuleBean;
import com.sitechdev.vehicle.pad.module.apps.model.AllModuleUtils;
import com.sitechdev.vehicle.pad.module.apps.util.AppsMenuConfig;
import com.sitechdev.vehicle.pad.module.apps.util.MoveMenuCallback;

import java.util.ArrayList;
import java.util.List;

public class MainMenuAdapater extends BaseAdapter implements MoveMenuCallback {

    private int index = 0;
    private ArrayList<AllModuleBean.ModuleBean> mList = null;
    private int startIndex = 0;
    private int mBigListIndex = 0;
    private Context mContext = null;
    private AllModuleBean.ModuleBean menuElement = null;
    private int mTempIndex = -1;

    public MainMenuAdapater(Context context, int startIndex, int endIndex) {
        mContext = context;
        this.startIndex = startIndex;
        setListContent(startIndex, endIndex);
    }

    public void setListContent(int startIndex, int endIndex) {
        if (mList != null && !mList.isEmpty()) {
            mList.clear();
        }
        this.startIndex = startIndex;
        mList = (ArrayList<AllModuleBean.ModuleBean>) AppsMenuConfig.getNewSubList(startIndex, endIndex);
    }

    public void setListContent(int startIndex, List<AllModuleBean.ModuleBean> tempList) {
        if (mList != null && !mList.isEmpty()) {
            mList.clear();
        }
        this.startIndex = startIndex;
        mList = (ArrayList<AllModuleBean.ModuleBean>) tempList;
    }

    public List<AllModuleBean.ModuleBean> getList() {
        return (mList != null) ? mList : null;
    }

    @Override
    public int getCount() {
        return (mList != null) ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (mList != null) ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        mBigListIndex = startIndex + position;
        return mBigListIndex;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        index = position;
        menuElement = (AllModuleBean.ModuleBean) mList.get(position);

        convertView = LinearLayout.inflate(mContext, R.layout.single_grid_view, null);

        ImageView imgMenuIcon = (ImageView) convertView.findViewById(R.id.menu_icon);
        TextView menuName = (TextView) convertView.findViewById(R.id.menu_name);
        menuName.setText(menuElement.appName);
        GlideUtils.getInstance().loadImage(AllModuleUtils.getSrcId(menuElement.appIcon), imgMenuIcon);

        if (AppsMenuConfig.isAppsEditStatus) {// 是否进入编辑状态
            if (index == mTempIndex) {
                // 点击的item不可见
                convertView.setVisibility(View.INVISIBLE);
            }
            //是编辑状态，出现抖动动画
            startAnmation(imgMenuIcon);
        } else {
            stopAnmation(imgMenuIcon);
        }

        return convertView;
    }

    /**
     * 动画开始
     *
     * @param view
     */
    private void startAnmation(View view) {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.snake);
        view.startAnimation(anim);

    }

    /**
     * 动画停止
     *
     * @param view
     */
    private void stopAnmation(View view) {
        view.clearAnimation();
    }

    @Override
    @DebugTrace
    public void reorderItems(int oldPosition, int newPosition) {
        SitechDevLog.w(AppConst.TAG_APP, "reorderItems    开始位置=" + (startIndex));
        SitechDevLog.w(AppConst.TAG_APP, "reorderItems   oldPosition=" + oldPosition + ",newPosition=" + newPosition);
        if (oldPosition == AdapterView.INVALID_POSITION || newPosition == AdapterView.INVALID_POSITION) {
            return;
        }
        AllModuleBean.ModuleBean temp = (AllModuleBean.ModuleBean) mList.get(oldPosition);
        AllModuleBean.ModuleBean move = (AllModuleBean.ModuleBean) mList.get(newPosition);
        SitechDevLog.w(AppConst.TAG_APP, "temp.name==" + temp.appName);
        SitechDevLog.w(AppConst.TAG_APP, "move.name=" + move.appName);

        mList.remove(oldPosition);
        SitechDevLog.w(AppConst.TAG_APP, "reorderItems  总索引=" + (startIndex + oldPosition + 1));
        AppsMenuConfig.removeElementPosition(startIndex + oldPosition);
        if (newPosition != AdapterView.INVALID_POSITION) {
            mList.add(newPosition, temp);
            AppsMenuConfig.insertElement2Position(startIndex + newPosition, temp);
        }
    }

    @Override
    public void setHideItem(int hidePosition) {
        // if (hidePosition == AdapterView.INVALID_POSITION) {
        // return;
        // }
        this.mTempIndex = hidePosition;
        notifyDataSetChanged();
    }
}
