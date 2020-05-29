package com.sitechdev.vehicle.pad.module.apps.util;

import com.sitechdev.vehicle.pad.bean.AllModuleBean;

import java.util.List;

/**
 * 菜单增、删 方法接口
 * 
 * <pre>
 *  使用ViewPagerBundle类调用该接口的实现：<br/>
 *  
 * 增加一个新菜单对象newDOMElement：
 *  ViewPagerBundle.getInstance().getListener().onAddMenu(Element);
 *  
 * 增加一个新菜单对象list：
 *  ViewPagerBundle.getInstance().getListener().onAddMenuList(ElementList);
 *  
 * 删除一个菜单，索引为position:
 *  ViewPagerBundle.getInstance().getListener().onDeleteMenu(position);
 *  
 * 长按时刷新主界面:
 *  ViewPagerBundle.getInstance().getListener().refreshMainView();
 * 
 * </pre>
 * 
 * @author shaozhi
 * 
 */
public interface MainViewMenuListener {

	/**
	 * 添加一个菜单到list中
	 * 
	 * @param menuElement
	 *            需要添加的菜单对象
	 */
	void onAddMenu(AllModuleBean.ModuleBean menuElement);

	/**
	 * 添加一个菜单list到list中
	 * 
	 * @param menuElementlist
	 *            需要添加的菜单list
	 */
	void onAddMenuList(List<AllModuleBean.ModuleBean> menuElementlist);

	/**
	 * 删除指定索引一个菜单
	 * 
	 * @param position
	 *            需要删除的菜单的索引
	 */
	void onDeleteMenu(int position);

	/**
	 * 删除指定一个菜单
	 * 
	 * @param menuElement
	 *            需要删除的菜单对象
	 */
	void onDeleteMenu(AllModuleBean.ModuleBean menuElement);

	/**
	 * 长按时刷新主界面
	 * 
	 */
	void refreshMainViewLongClick();

	/**
	 * 刷新主界面
	 * 
	 */
	void refreshMainView();

	/**
	 * 滑动切换页面时刷新下标
	 * 
	 */
	void refreshMainTagInChangePager();

	/**
	 * 移动item时viewpager滑动界面
	 * 
	 */
	void dragViewPager();

	/**
	 * 移动item时viewpager滑动界面
	 *
	 */
	void setCountDownTimeRunnable(boolean enable);

}
