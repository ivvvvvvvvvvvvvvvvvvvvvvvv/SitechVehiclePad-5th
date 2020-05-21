package com.sitechdev.vehicle.pad.module.apps.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.amap.api.services.route.TruckStep;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.bean.AllModuleBean;
import com.sitechdev.vehicle.pad.module.apps.adapter.MainMenuAdapater;
import com.sitechdev.vehicle.pad.module.apps.util.MenuBundle;
import com.sitechdev.vehicle.pad.module.apps.util.AppsMenuConfig;

/**
 *
 */
public class AppPagerMenuGridView extends GridView implements OnItemLongClickListener, OnItemClickListener {
    public Context mContext = null;
    /**
     * 移动的x坐标
     */
    public int moveX = 0;
    /**
     * 移动的y坐标
     */
    public int moveY = 0;
    /**
     * 顶部状态栏的高度
     */
    public int mStatusHeight;
    /**
     * 要拖拽的item的position
     */
    public int mMovePosition = -1;
    /**
     * 要拖拽的item的在总list中的position
     */
    public static int mMovePositionInList = -1;
    /**
     * 要拖拽的item
     */
    public LinearLayout mMoveLinearLayout = null;
    /**
     * 要拖拽的item的DOMElement对象
     */
    public static AllModuleBean.ModuleBean mMoveBussInfo = null;
    /**
     * 要拖拽的item的bitmap
     */
    public Bitmap mMoveBitmap = null;
    /**
     * 长按的间隔时间 ms
     */
    public long mMoveMillis = 1000;
    /**
     * item变换响应的间隔时间 ms
     */
    public long mMoveItemMillis = 500;
    /**
     * pager变换响应的间隔时间 ms
     */
    public long mChangePagerMillis = 1000;
    /**
     * 镜像文件所依赖的参数
     */
    public WindowManager.LayoutParams mWindowLayoutParams = null;
    /**
     * 移动的item所处的adapter
     */
    public MainMenuAdapater mMoveAdapter = null;
    /**
     * 是否点击的item
     */
    public static boolean isTouchItem = false;
    /**
     * 当前屏幕的宽
     */
    public static int mScreenWidth = 0;
    /**
     * 当前屏幕的高
     */
    public static int mScreenHeight = 0;
    /**
     * 点击的x坐标
     */
    public int mDownX = 0;
    /**
     * 点击的y坐标
     */
    public int mDownY = 0;
    /**
     * 手指点击的点距要移动的item左的距离
     */
    private int mPoint2ItemLeft = 0;
    /**
     * 手指点击的点距要移动的item顶的距离
     */
    private int mPoint2ItemTop = 0;
    /**
     * GridView的上边缘到屏幕上边缘的距离
     */
    private int mOffset2Top;
    /**
     * GridView的左边缘到屏幕左边缘的距离
     */
    private int mOffset2Left;
    /**
     * 手指滑动到的item
     */
    private int mTempPosition = -1;
    int tmpMovePosition = 0;
    int dragTims;

    public AppPagerMenuGridView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public AppPagerMenuGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public AppPagerMenuGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    /**
     * 初始化一些原始属性
     */
    private void init() {
        AppsMenuConfig.windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = 0;// BarUtils.getStatusBarHeight();// CyberMainMenuUtil.getStatusHeight(mContext); // 获取状态栏的高度

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();

        AppsMenuConfig.windowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        SitechDevLog.w(AppConst.TAG_APP, "width=" + dm.widthPixels);
        SitechDevLog.w(AppConst.TAG_APP, "height=" + dm.heightPixels);

        this.setOnItemClickListener(this);
        this.setOnItemLongClickListener(this);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof MainMenuAdapater) {
            mMoveAdapter = (MainMenuAdapater) adapter;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                SitechDevLog.w(AppConst.TAG_APP, "onTouchEvent  ACTION_DOWN ");
                SitechDevLog.w(AppConst.TAG_APP, "onTouchEvent ====>ACTION_DOWN isItemTouching===>" + AppsMenuConfig.isItemTouching);

                if (AppsMenuConfig.isAppsEditStatus && AppsMenuConfig.mMoveImageView != null) {
                    AppsMenuConfig.isItemTouchCount++;
                }
                //退出编辑倒计时取消
                if (AppsMenuConfig.isAppsEditStatus && MenuBundle.getInstance().getMainViewMenuListener() != null) {
                    SitechDevLog.w(AppConst.TAG_APP, "setCountDownTimeRunnable ====>onTouchEvent ACTION_DOWN ==取消 退出编辑倒计时 线程===>");
                    MenuBundle.getInstance().getMainViewMenuListener().setCountDownTimeRunnable(false);
                }
                if (AppsMenuConfig.mMoveImageView == null) {
                    initDragImageView(ev);
                } else {
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // LoggerUtil.warn("onTouchEvent", "ACTION_MOVE");
                if (AppsMenuConfig.mLongClick && AppsMenuConfig.mMoveImageView != null) {
                    if (mMovePosition != AdapterView.INVALID_POSITION) {
                        // 因在左右滑动翻页过程中，moveX会累加，故此处对屏宽取余
                        moveX = (int) ev.getX() % mScreenWidth;
                        moveY = (int) ev.getY();

                        if (moveX < 0) {
                            moveX = mScreenWidth + moveX;
                        }
                        // 取余操作，单屏坐标
                        // moveX = moveX ;
                        // 合法的item，开始拖动item
                        onDragItem((int) ev.getRawX(), (int) ev.getRawY());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                SitechDevLog.w(AppConst.TAG_APP, "onTouchEvent  ACTION_UP");
                SitechDevLog.w(AppConst.TAG_APP, "onTouchEvent  ACTION_UP isItemTouching===>" + AppsMenuConfig.isItemTouching);
//                LoggerUtil.warn("onTouchEvent", "ACTION_UP isItemTouchCount===>" + AppViewPagerUtil.isItemTouchCount);
                //退出编辑倒计时开始
                if (AppsMenuConfig.isAppsEditStatus && MenuBundle.getInstance().getMainViewMenuListener() != null) {
                    SitechDevLog.w(AppConst.TAG_APP, "setCountDownTimeRunnable ====>onTouchEvent ACTION_UP ==开始 退出编辑倒计时 线程===>");
                    MenuBundle.getInstance().getMainViewMenuListener().setCountDownTimeRunnable(true);
                }

                if (AppsMenuConfig.isItemTouchCount > 0 && AppsMenuConfig.isItemTouching) {
//                    LoggerUtil.warn("onTouchEvent", "ACTION_UP if isItemTouchCount===>" + CyberMainMenuUtil.isItemTouchCount);
                    SitechDevLog.w(AppConst.TAG_APP, "onTouchEvent  ACTION_UP isItemTouching");
                    AppsMenuConfig.isItemTouchCount = 0;
                    return true;
                }
                if (AppsMenuConfig.mLongClick && AppsMenuConfig.mMoveImageView != null) {
                    SitechDevLog.w(AppConst.TAG_APP, "onTouchEvent  ACTION_UP onStopMove");
                    // 移除监听事件
                    AppsMenuConfig.onStopMove();
                    return true;
                }
                break;
            default:
                break;
        }
        // LoggerUtil.warn("onTouchEvent", "return super " +
        // super.onTouchEvent(ev));
        return super.onTouchEvent(ev);
    }

    /**
     * item的单击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 移除监听事件
        AppsMenuConfig.onStopMove();
        SitechDevLog.w(AppConst.TAG_APP, "CyberMenuGridView onItemClick position=" + position);
        try {
            AllModuleBean.ModuleBean menuElement = (AllModuleBean.ModuleBean) parent.getItemAtPosition(position);
            //TODO 点击事件增加
//            Util.clickItemEvent(mContext, menuElement);
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    /**
     * item的长按事件
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        SitechDevLog.w(AppConst.TAG_APP, "onItemLongClick   长按长按长按");
        if (!AppsMenuConfig.isAppsEditStatus) {
            AllModuleBean.ModuleBean menuElement = (AllModuleBean.ModuleBean) parent.getItemAtPosition(position);
            String menuId = menuElement.appId;

            AppsMenuConfig.updateViewPager = false;
            SitechDevLog.w(AppConst.TAG_APP, "onItemLongClick   进入编辑");
            AppsMenuConfig.isAppsEditStatus = true;
            if (MenuBundle.getInstance().getMainViewMenuListener() != null) {
                SitechDevLog.w(AppConst.TAG_APP, "setCountDownTimeRunnable ====> onItemLongClick ==开始 退出编辑倒计时 线程===>");
                MenuBundle.getInstance().getMainViewMenuListener().setCountDownTimeRunnable(true);
            }
            AppsMenuConfig.setLongClick(true);
            AppViewPager.isTouchResponse = false;
            MenuBundle.getInstance().getMainViewMenuListener().refreshMainViewLongClick();
            // 当viewpager界面重绘时，长按将不再建立镜像文件
            if (AppsMenuConfig.updateViewPager || AppsMenuConfig.isItemTouching) {
                return true;
            } else {
                AppsMenuConfig.mHandler.removeCallbacksAndMessages(null);
                AppsMenuConfig.mHandler.post(mCreateDragImageRunnable);
            }
        }
        return false;
    }

    /**
     * 交换item线程
     */
    private Runnable mSwapRunnable = new Runnable() {
        @Override
        public void run() {
            AppsMenuConfig.moveRunnableLists.add(this);
            AppsMenuConfig.mHandler.removeCallbacks(this);
            onMoveItem(mTempPosition, moveX, moveY);
        }
    };

    /**
     * viewpager 换页线程
     */
    private Runnable mNextPagerRunnable = new Runnable() {
        @Override
        public void run() {
            AppsMenuConfig.moveRunnableLists.add(this);
            AppsMenuConfig.mHandler.removeCallbacks(this);
            goNextPager();
        }
    };

    /**
     * 创建windows窗口镜像
     */
    public Runnable mCreateDragImageRunnable = new Runnable() {
        @Override
        public void run() {
            AppsMenuConfig.moveRunnableLists.add(this);
            mMoveAdapter = AppsMenuConfig.mAllMenuAdapterList.get(AppViewPager.mCurrentPagerIndex);
            // adapter元素刷新
            mMoveAdapter.setHideItem(mMovePosition);

            // 根据我们按下的点显示item镜像
            createDragImage(mMoveBitmap, mDownX, mDownY);
        }
    };

    /**
     * 初始化item镜像数据，创建镜像对象
     *
     * @param ev
     */
    private void initDragImageView(MotionEvent ev) {
        SitechDevLog.w(AppConst.TAG_APP, "initDragImageView  initDragImageView");
        AppsMenuConfig.removeDragImage();

        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();

        // 根据按下的X,Y坐标获取所点击item的position
        mMovePosition = pointToPosition(mDownX, mDownY);
        SitechDevLog.w(AppConst.TAG_APP, "initDragImageView  mMovePosition=" + mMovePosition);
        // 点击Gridview的空白部分
        if (mMovePosition == AdapterView.INVALID_POSITION) {
            return;
        }

        // 根据mMovePosition获取所点击的item
        mMoveLinearLayout = (LinearLayout) getChildAt(mMovePosition - getFirstVisiblePosition());
        // 得到在总DOM树中的索引位置
        mMovePositionInList = AppViewPager.mCurrentPagerIndex * AppsMenuConfig.MAX_GRIDVIEW_SIZE + mMovePosition;
        SitechDevLog.w(AppConst.TAG_APP, "initDragImageView  mMovePositionInList=" + mMovePositionInList);
        // 根据mMovePosition获取所点击的item
        mMoveBussInfo = AppsMenuConfig.getModuleBeanMenuByPosition(mMovePositionInList);

        // 手指按下的点到item左边缘的距离
        mPoint2ItemLeft = mDownX - mMoveLinearLayout.getLeft();
        // 手指按下的点到item顶部的距离
        mPoint2ItemTop = mDownY - mMoveLinearLayout.getTop();

        // gridview到屏幕顶部的距离
        mOffset2Top = (int) (ev.getRawY() - mDownY);
        // gridview到屏幕左边缘的距离
        mOffset2Left = (int) (ev.getRawX() - mDownX);

        if (AppsMenuConfig.isAppsEditStatus) {
            AppsMenuConfig.updateViewPager = false;
            AppViewPager.isTouchResponse = false;
            AppsMenuConfig.mHandler.post(mCreateDragImageRunnable);
        }

        // 开启mDragItemView绘图缓存
        mMoveLinearLayout.setDrawingCacheEnabled(true);
        // 获取mDragItemView在缓存中的Bitmap对象
        mMoveBitmap = Bitmap.createBitmap(mMoveLinearLayout.getDrawingCache());
        // 这一步很关键，释放绘图缓存，避免出现重复的镜像
        mMoveLinearLayout.destroyDrawingCache();
    }

    /**
     * 创建拖动的镜像
     *
     * @param bitmap
     * @param downX  按下的点相对父控件的X坐标
     * @param downY  按下的点相对父控件的X坐标
     */
    private void createDragImage(Bitmap bitmap, int downX, int downY) {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; // 图片之外的其他地方透明
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        mWindowLayoutParams.alpha = 0.55f; // 透明度
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        AppsMenuConfig.removeDragImage();

        if (!AppsMenuConfig.isItemTouching) {
            AppsMenuConfig.isItemTouching = true;
        }

        // windows窗口赋值
        AppsMenuConfig.mMoveImageView = new ImageView(mContext);
        AppsMenuConfig.mMoveImageView.setImageBitmap(bitmap);
        AppsMenuConfig.windowManager.addView(AppsMenuConfig.mMoveImageView, mWindowLayoutParams);
    }

    /**
     * 拖动item，在里面实现了item镜像的位置更新，item的相互交换以及GridView的自行滚动
     *
     * @param mRawX mRawX
     * @param mRawY mRawY
     */
    private void onDragItem(int mRawX, int mRawY) {
        try {
            mWindowLayoutParams.x = mRawX - mMoveLinearLayout.getWidth() / 2;
            mWindowLayoutParams.y = mRawY - mMoveLinearLayout.getHeight() / 2 - mStatusHeight;
            // if (mWindowLayoutParams.y <= (mOffset2Top - mStatusHeight)) {
            // mWindowLayoutParams.y = mOffset2Top - mStatusHeight;
            // }

            // 更新镜像的位置
            AppsMenuConfig.windowManager.updateViewLayout(AppsMenuConfig.mMoveImageView, mWindowLayoutParams);

            // 获取手指移动到的那个item的position
            mTempPosition = ((AppPagerMenuGridView) AppViewPager.mAllMenuGridViewList.get(AppViewPager.mCurrentViewIndex)).pointToPosition(moveX, moveY);

            // 如果已滑到左右边缘，则进入换页线程
            if (isPagerMargin(mWindowLayoutParams.x, mWindowLayoutParams.y)) {
                // 延迟mChangePagerMillis再切换页面。
                AppsMenuConfig.mHandler.removeCallbacks(mSwapRunnable);
                AppsMenuConfig.mHandler.postDelayed(mNextPagerRunnable, mChangePagerMillis);
            } else {
                // 进入交换item事件处理
                AppsMenuConfig.mHandler.removeCallbacks(mNextPagerRunnable);
                // 为了防止在移动过程中不停的刷新位置，此处加上延迟mMoveItemMillis再判断刷新item位置。
                // CyberMainMenuUtil.mHandler.postDelayed(mSwapRunnable,
                // mMoveItemMillis);
                AppsMenuConfig.mHandler.post(mSwapRunnable);
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    /**
     * 交换item,并且控制item之间的显示与隐藏效果
     *
     * @param moveX
     * @param moveY
     */
    private void onMoveItem(final int tempPosition, int moveX, int moveY) {
        try {
            SitechDevLog.w(AppConst.TAG_APP, "onMoveItem" + (tempPosition == mMovePosition) + ",tempPosition=" + tempPosition + ",mMovePosition=" + mMovePosition);

            mMoveAdapter = AppsMenuConfig.mAllMenuAdapterList.get(AppViewPager.mCurrentPagerIndex);
            if (tempPosition >= mMoveAdapter.getCount()) {
                return;
            }
            if (tempPosition == AdapterView.INVALID_POSITION) {
                //目标位置无效
                return;
            }

            int startIndex = 0;
            int size = AppsMenuConfig.MAX_GRIDVIEW_SIZE;
            // 假如tempPosition 改变了,则进行移动或补位
            // 跨页移动了
            if (AppsMenuConfig.mPagerChanged) {
                /**
                 * 重组要移动的item对象的索引位置。
                 * 1.若0==>1,1==>2,item右移动，页面索引增加。先将原先索引位置的item从list中删除
                 * ，再在新页面的第0位增加该item
                 * 同时mMovePosition=0，进行mMovePosition和tempPosition的交换
                 *
                 * 2. 若左移动，索引减少。先将原先索引位置的item从list中删除，再在新页面的最后一位增加该item
                 * 同时mMovePosition=最后一位，进行mMovePosition和tempPosition的交换
                 */
                SitechDevLog.w(AppConst.TAG_APP, "mCurrentPagerIndex==  #" + AppViewPager.mCurrentPagerIndex);
                SitechDevLog.w(AppConst.TAG_APP, "item在list中索引== #" + mMovePositionInList);
                if (AppsMenuConfig.mPagerDirection > 0) {
                    AppsMenuConfig.removeElementPosition(mMovePositionInList);
                    // 在当前页面的第一个添加上
                    SitechDevLog.w(AppConst.TAG_APP, "当前页面的第一个Index== #" + (AppViewPager.mCurrentPagerIndex * AppsMenuConfig.MAX_GRIDVIEW_SIZE));
                    AppsMenuConfig.insertElement2Position(AppViewPager.mCurrentPagerIndex * AppsMenuConfig.MAX_GRIDVIEW_SIZE, mMoveBussInfo);
                    mMovePosition = 0;
                } else if (AppsMenuConfig.mPagerDirection < 0) {
                    // 往前一页进行的滑动,在当前页面的item索引位置加上
                    if ((AppViewPager.mCurrentPagerIndex == AppsMenuConfig.mAllMenuAdapterList.size() - 1)
                            && (AppsMenuConfig.mAllMenuBeanList.size() % AppsMenuConfig.MAX_GRIDVIEW_SIZE != 0)) {
                        size = AppsMenuConfig.mAllMenuBeanList.size() % AppsMenuConfig.MAX_GRIDVIEW_SIZE;
                    }
                    AppsMenuConfig.removeElementPosition(mMovePositionInList);
                    AppsMenuConfig.insertElement2Position(AppViewPager.mCurrentPagerIndex * AppsMenuConfig.MAX_GRIDVIEW_SIZE + size - 1, mMoveBussInfo);
                    mMovePosition = size - 1;
                    SitechDevLog.w(AppConst.TAG_APP, "mMovePosition== #" + mMovePosition);
                    mMovePositionInList = AppViewPager.mCurrentPagerIndex * AppsMenuConfig.MAX_GRIDVIEW_SIZE + size;
                }
                SitechDevLog.w(AppConst.TAG_APP, "onMoveItem  mPagerChanged==" + (tempPosition == mMovePosition) + ",tempPosition=" + tempPosition + ",mMovePosition="
                        + mMovePosition + ",mMovePositionInList=" + mMovePositionInList);

                // 全部界面重组，并且全部取消不可见
                int hidePos = AdapterView.INVALID_POSITION;
                size = AppsMenuConfig.MAX_GRIDVIEW_SIZE;
                for (int i = 0; i < AppsMenuConfig.mAllMenuAdapterList.size(); i++) {
                    // 重置隐藏item的索引
                    hidePos = AdapterView.INVALID_POSITION;
                    MainMenuAdapater mTempAdapater = AppsMenuConfig.mAllMenuAdapterList.get(i);
                    startIndex = i * AppsMenuConfig.MAX_GRIDVIEW_SIZE;
                    if ((i == AppsMenuConfig.mAllMenuAdapterList.size() - 1)
                            && (AppsMenuConfig.mAllMenuBeanList.size() % AppsMenuConfig.MAX_GRIDVIEW_SIZE != 0)) {
                        size = AppsMenuConfig.mAllMenuBeanList.size() % AppsMenuConfig.MAX_GRIDVIEW_SIZE;
                    }
                    mTempAdapater.setListContent(startIndex, startIndex + size);
                    if (i == AppViewPager.mCurrentPagerIndex) {
                        // 设置当前gridview的数据隐藏的item索引
                        if (AppsMenuConfig.mPagerDirection > 0) {
                            hidePos = 0;
                        } else if (AppsMenuConfig.mPagerDirection < 0) {
                            hidePos = size - 1;
                        }
                        SitechDevLog.w(AppConst.TAG_APP, "onMoveItem  hidePos=" + (hidePos) + ", tempPosition=" + tempPosition + " mMovePosition=" + mMovePosition);
                    }
                    mTempAdapater.setHideItem(hidePos);
                }
                AppsMenuConfig.mPagerChanged = false;
            }

            SitechDevLog.w(AppConst.TAG_APP, "onMoveItem   mAnimationEnd" + AppsMenuConfig.mAnimationEnd);
            // 单页内移动的
            if (tempPosition != mMovePosition && AppsMenuConfig.mAnimationEnd) {
                // 新移位动画
                int fromPosition = tempPosition;
                int toPosition = mMovePosition;
                int moveNum = fromPosition - toPosition;

                if (moveNum != 0) {
                    dragTims = Math.abs(moveNum);
                    tmpMovePosition = toPosition;
                    for (int index = 0; index < Math.abs(moveNum); index++) {
                        View toView = ((AppPagerMenuGridView) AppViewPager.mAllMenuGridViewList.get(AppViewPager.mCurrentViewIndex))
                                .getChildAt(tmpMovePosition);
                        if (moveNum > 0) {
                            tmpMovePosition = tmpMovePosition + 1;
                        } else {
                            tmpMovePosition = tmpMovePosition - 1;
                        }
                        View fromView = ((AppPagerMenuGridView) AppViewPager.mAllMenuGridViewList.get(AppViewPager.mCurrentViewIndex))
                                .getChildAt(tmpMovePosition);
                        int toXValue = toView.getLeft() - fromView.getLeft();
                        int toYValue = toView.getTop() - fromView.getTop();

                        // tempPosition = tmpMovePosition;
                        Animation moveAnimation = getMoveAnimation(toXValue, toYValue);
                        fromView.startAnimation(moveAnimation);
                        moveAnimation.setAnimationListener(new AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                                AppsMenuConfig.mAnimationEnd = false;
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                dragTims--;
                                if (tempPosition == tmpMovePosition && dragTims == 0) {
                                    // 动画结束后更新数据
                                    SitechDevLog.w(AppConst.TAG_APP, "开始移动  =====>" + " tempPosition=" + tempPosition + ", mMovePosition=" + mMovePosition);
                                    mMoveAdapter.reorderItems(mMovePosition, tempPosition);
                                    mMoveAdapter.setHideItem(tempPosition);
                                    mMovePosition = tempPosition;
                                    mMovePositionInList = AppViewPager.mCurrentPagerIndex * AppsMenuConfig.MAX_GRIDVIEW_SIZE + mMovePosition;
//                                    mMoveAdapter.notifyDataSetChanged();
                                    SitechDevLog.w(AppConst.TAG_APP, "位置完成  =====>" + " tempPosition=" + tempPosition + ", mMovePosition=" + mMovePosition);
                                }
                                AppsMenuConfig.mAnimationEnd = true;
                            }
                        });
                    }
                }
                // ===========================================================
            }
        } catch (Exception e) {
            SitechDevLog.exception(e);
        }
    }

    public Animation getMoveAnimation(float toXDelta, float toYDelta) {
        TranslateAnimation ta = new TranslateAnimation(0, toXDelta, 0, toYDelta);
        ta.setDuration(100);
        ta.setFillAfter(true);
        return ta;
    }

    /**
     * 页面跳转操作
     */
    public void goNextPager() {
        MenuBundle.getInstance().getMainViewMenuListener().dragViewPager();
    }

    /**
     * 是否到达屏幕左右边缘
     *
     * @return true=到达,false=未到达
     */
    private boolean isPagerMargin(int wx, int wy) {
        // LoggerUtil.warn("isPagerMargin", "wx=" + wx);
        // LoggerUtil.warn("isPagerMargin", "" + ((wx +
        // mMoveImageView.getWidth()) >= mScreenWidth));
        if (wx <= 0 || ((wx + AppsMenuConfig.mMoveImageView.getWidth()) >= mScreenWidth)) {
            AppsMenuConfig.mPagerDirection = wx;
            // LoggerUtil.warn("移动方向", (CyberMainMenuUtil.mPagerDirection > 0) ?
            // "加1" : "减1");
            return true;
        }
        return false;
    }
}
