package com.sitechdev.vehicle.pad.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;

import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.StringUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.callback.BaseBribery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Android M权限申请封装类。可在Activity或Fragment中调用。
 * <p/>
 * <code>
 * //获得需要获取的所有权限的列表<br/>
 * List<String> mPermissionList=XTPermissionHelper.getNeedApplyPermissonsName();<br/>
 * //依次请求所有权限<br/>
 * XTPermissionHelper.needPermissons(Context,mPermissionList);<br/>
 * </code>
 * <p/>
 *
 * @author shaozhi
 */
public class PermissionHelper {
    private static final String TAG = "XTPermissionHelper";
    /**
     * 权限请求返回码
     */
    private static final int REQUESTPERMISSIONS_CODE = 100;

    /**
     * 私有化构造，防止外部直接New对象
     */
    private PermissionHelper() {
    }

    /**
     * 是否需要进行权限检查
     *
     * @return true=6.0以上系统，需要进行权限检查。false=6.0以下系统，不需要权限检查。
     */
    private static boolean isNeedCheckPermisson() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    /**
     * 判断某个权限是否已经被授权
     *
     * @param context    上下文
     * @param permission 需要判断的权限名称
     * @return 已授权返回true, 未授权返回false
     */
    public static synchronized boolean isAllowPermission(Context context, String permission) {
        if (!isNeedCheckPermisson() || context == null || StringUtils.isEmpty(permission)) {
            return true;
        }
        int result = ContextCompat.checkSelfPermission(context, permission);
        SitechDevLog.i(TAG, permission + ":" + result);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 获得需要弹窗申请的权限名称，自动过滤已经允许的权限
     *
     * @param context         上下文
     * @param permissionArray 要批量申请的权限数组
     * @return 需要弹窗让用户允许的权限列表
     */
    public static synchronized List<String> getNeedRequestPermissionNameList(Context context, String[] permissionArray) {
        List<String> permissionList = new ArrayList<String>();
        if (permissionArray == null || permissionArray.length == 0) {
            return permissionList;
        }
        for (String permission : permissionArray) {
            if (!isAllowPermission(context, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }

    /**
     * 获得需要申请的权限，自动过滤已经允许的权限
     * 【针对Leui 6.0修改】
     *
     * @param context         上下文
     * @param permissionArray 要批量申请的权限数组
     * @return 需要弹窗让用户允许的权限列表
     */
    public static synchronized List<PermissionInfo> getNeedRequestPermissonList(Context context, String[] permissionArray) {
        List<PermissionInfo> permissionInfoList = new ArrayList<>();
        if (permissionArray == null || permissionArray.length == 0) {
            return permissionInfoList;
        }
        PackageManager pm = context.getPackageManager();

        for (String permission : permissionArray) {
            if (!isAllowPermission(context, permission)) {
                try {
                    permissionInfoList.add(pm.getPermissionInfo(permission, 0));
                } catch (PackageManager.NameNotFoundException e) {
                    SitechDevLog.exception(e);
                }
            }
        }
        return permissionInfoList;
    }

    /**
     * 申请所请求的权限
     *
     * @param mObject        Activity或Fragment对象
     * @param permissionList 需要获取的全部权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static synchronized void requestPermissons(Object mObject, List<String> permissionList, int requestCode) {
        if (permissionList == null || permissionList.isEmpty()) {
            return;
        }
        requestPermissons(mObject, permissionList.toArray(new String[permissionList.size()]), requestCode);
    }

    /**
     * 申请所请求的权限
     *
     * @param mObject         Activity或Fragment对象
     * @param permissionArray 需要获取的全部权限名称数组
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static synchronized void requestPermissons(Object mObject, String[] permissionArray) {
        requestPermissons(mObject, permissionArray, REQUESTPERMISSIONS_CODE);
    }

    /**
     * 申请所请求的权限
     *
     * @param mObject    Activity或Fragment对象
     * @param permission 需要获取的全部权限名称数组
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static synchronized void requestPermissons(Object mObject, String permission) {
        requestPermissons(mObject, new String[]{permission}, REQUESTPERMISSIONS_CODE);
    }

    /**
     * 申请所请求的权限
     *
     * @param mObject         Activity或Fragment对象
     * @param permissionArray 需要获取的全部权限名称数组
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static synchronized void requestPermissons(Object mObject, String[] permissionArray, int requestCode) {
        if (permissionArray == null || permissionArray.length <= 0) {
            return;
        }
        if (mObject instanceof Fragment) {
            SitechDevLog.i(TAG, "Fragment request permission");
            Fragment mFragment = (Fragment) mObject;
            mFragment.requestPermissions(permissionArray, requestCode);
        } else {
            SitechDevLog.i(TAG, "Activity request permission");
            Activity mActivity = (Activity) mObject;
            ActivityCompat.requestPermissions(mActivity, permissionArray, requestCode);
        }
    }

    private static AlertDialog neverPermissionRequestDialog = null;

    public static synchronized void showPermissionDialog(Activity mActivity, String[] applyPermissions, BaseBribery mBribery) {
        showPermissionDialogImpl(mActivity, applyPermissions, mBribery);
    }

    private static synchronized void showPermissionDialogImpl(final Activity activity, String[] applyPermissions, final BaseBribery mBribery) {
        List<PermissionInfo> permissionInfoList = getNeedRequestPermissonList(activity, applyPermissions);
        if (permissionInfoList.isEmpty()) {
            return;
        }
        neverPermissionRequestDialog = new AlertDialog.Builder(activity)
                .setMessage(Html.fromHtml(getNeedPermissonsContentMessage(activity, permissionInfoList)))
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelDialog();
                        mBribery.onFailure("cancel");
                    }
                })
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelDialog();
                        Intent in = new Intent().setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
                                .setData(Uri.fromParts("package", activity.getPackageName(), null))
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        activity.startActivityForResult(in, 1000);
                        mBribery.onSuccess("goset");
                    }
                })
                .show();
    }

    private static synchronized void cancelDialog() {
        if (neverPermissionRequestDialog != null && neverPermissionRequestDialog.isShowing()) {
            neverPermissionRequestDialog.cancel();
        }
    }

    private static synchronized String getNeedPermissonsContentMessage(Activity activity, List<PermissionInfo> permissionInfoList) {
        PackageManager tPackageManager = activity.getPackageManager();

        StringBuilder tStringBuilder = new StringBuilder();
        tStringBuilder.append(
                String.format(
                        activity.getString(R.string.permission_des_head),
                        tPackageManager.getApplicationLabel(activity.getApplicationInfo())
                )
        );

        Set<String> permissionGroupSet = new HashSet<>();
        for (PermissionInfo permissionInfo : permissionInfoList) {
            try {
                permissionGroupSet.add(
                        tPackageManager.getPermissionGroupInfo(permissionInfo.group, 0).loadLabel(tPackageManager).toString()
                );
            } catch (Exception e) {
                SitechDevLog.exception(e);
            }
        }

        for (Iterator<String> iterator = permissionGroupSet.iterator(); iterator.hasNext(); ) {
            String permissionGroupLabel = iterator.next();
            tStringBuilder.append(
                    String.format(
                            activity.getString(R.string.permission_high_light),
                            permissionGroupLabel
                    )
            );

            tStringBuilder.append("、");
        }

        tStringBuilder.deleteCharAt(tStringBuilder.length() - 1);
        tStringBuilder.append(activity.getString(R.string.permission_des_tail));
        return tStringBuilder.toString();
    }
}
