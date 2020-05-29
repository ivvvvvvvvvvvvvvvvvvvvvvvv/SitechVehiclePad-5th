package com.sitechdev.vehicle.pad.module.apps.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 系统名称: 异度支付应用<br />
 * 模块名称: viewpager自定义适配器<br />
 * 软件版权: Copyright (c) 2012-2013 CHINA CITIC BANK<br />
 * 功能说明: viewpager自定义,滑动展现图片页面<br />
 * .<br />
 * <b>修订记录</b>
 * <table>
 * <tr>
 * <td>日期</td>
 * <td>编号</td>
 * <td>修改人</td>
 * <td>备注</td>
 * </tr>
 * <tr>
 * <td>2013-10-10</td>
 * <td>0000</td>
 * <td>shaozhi</td>
 * <td>创建</td>
 * </tr>
 * </table>
 * 
 * @author shaozhi
 * @version 1.0
 * @since 1.0
 */
public class MainViewPagerAdapter extends PagerAdapter {

	List<View> list;

	public MainViewPagerAdapter(List<View> list) {
		this.list = list;
	}

	public void setList(List<View> list) {
		this.list = list;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// LoggerUtil.warn("instantiateItem", "position==" + position +
		// ",list.size()==" + list.size());
		try {
			((ViewPager) container).addView(list.get(position));
		} catch (Exception e) {
			// LoggerUtil.exception(e);
		}
		return list.get(position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// LoggerUtil.warn("destroyItem============", "position==" + position +
		// ",list.size()==" + list.size());
		// 若是removeview，则不能跨越页移动item
		// 要跨页移动，需要注释
		// ((ViewPager) container).removeView(list.get(position));
	}
}
