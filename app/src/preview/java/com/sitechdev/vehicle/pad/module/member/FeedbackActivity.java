package com.sitechdev.vehicle.pad.module.member;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.sitechdev.net.HttpCode;
import com.sitechdev.vehicle.lib.util.NetworkUtils;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.AppConst;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.bean.BaseResponseBean;
import com.sitechdev.vehicle.pad.callback.BaseBribery;
import com.sitechdev.vehicle.pad.module.login.bean.util.FeedBackHttpUtils;
import com.sitechdev.vehicle.pad.util.PermissionHelper;
import com.sitechdev.vehicle.pad.view.CommonToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 意见反馈
 *
 * @author bijingshuai
 * @date 2019/8/19
 */
public class FeedbackActivity extends BaseActivity implements TextWatcher {

    private static final int REQUEST_CODE_CHOOSE = 201;
    private int num = 0;
    public int mMaxNum = 200;
    private CharSequence wordNum;
    private int selectionStart;
    private int selectionEnd;
    public List<LocalMedia> mSelected;
    private LinearLayout llPicList;
    private ImageView ivAddPic;
    private Button btnCommit;
    private EditText etFeedBack;
    private TextView tvFeedBack;
    private String feedBackType = "3";

    String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private List<String> rPermissionsList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBarView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        llPicList = (LinearLayout) findViewById(R.id.ll_add_pic_list);
        ivAddPic = (ImageView) findViewById(R.id.iv_add_pic);
        btnCommit = (Button) findViewById(R.id.btn_commit);
        etFeedBack = (EditText) findViewById(R.id.et_feed_back);
        tvFeedBack = (TextView) findViewById(R.id.tv_feed_back);

        mToolBarView.setMainTitle(getResources().getString(R.string.string_feed_back_title));
        String str = 0 + " / " + "<font color='#00FF00'>" + mMaxNum + "</font>";
        tvFeedBack.setText(Html.fromHtml(str));
    }

    @Override
    protected void initData() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            FeedBackHttpUtils.requestFeedBackType(new BaseBribery() {
                @Override
                public void onSuccess(Object successObj) {
                    feedBackType = (String) successObj;
                }

                @Override
                public void onFailure(Object failObj) {
                    feedBackType = (String) failObj;
                }
            });
        }
    }

    @Override
    protected void initListener() {
        super.initListener();

        ivAddPic.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        etFeedBack.addTextChangedListener(this);

        mToolBarView.setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 检查权限是否OK
     */
    protected void checkPermissionOk() {
        rPermissionsList = PermissionHelper.getNeedRequestPermissionNameList(this, permissions);
        if (rPermissionsList == null || rPermissionsList.isEmpty()) {
            openPhotoPage();
        } else {
            //请求权限
            PermissionHelper.requestPermissons(this, rPermissionsList, AppConst.PERMISSION_REQ_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_add_pic:
                checkPermissionOk();
                break;
            case R.id.btn_commit:
                String msg = etFeedBack.getText().toString().trim();
                commitImg(msg);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && rPermissionsList.size() == grantResults.length) {
            boolean isAllPermissionOK = true;
            for (int i = 0; i < grantResults.length; i++) {
                int result = grantResults[i];
                if (result != PackageManager.PERMISSION_GRANTED) {
                    //权限允许
                    isAllPermissionOK = false;
                    break;
                }
            }
            if (isAllPermissionOK) {
                openPhotoPage();
            } else {
                rPermissionsList.clear();
                rPermissionsList = PermissionHelper.getNeedRequestPermissionNameList(this, permissions);
                //请求权限
                PermissionHelper.requestPermissons(this, rPermissionsList, AppConst.PERMISSION_REQ_CODE);
            }
        }
    }

    private void openPhotoPage() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .openCamera(PictureMimeType.ofImage())
                .theme(R.style.picture_select_white_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageSpanCount(3)// 每行显示个数 int
                .maxSelectNum(5)// 最大图片选择数量 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .selectionMedia(mSelected)
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enableCrop(false)
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .compress(true)// 是否压缩 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .cropCompressQuality(85)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .hideBottomControls(false)
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .forResult(REQUEST_CODE_CHOOSE);//结果回调onActivityResult code
    }

    private void commitImg(String msg) {
        new CompositeDisposable().add(Observable.create(emitter -> {
            if (!checkInputText(msg)) {
                emitter.onError(new Exception("问题内容必须超过3个字"));
                return;
            }
            if (!NetworkUtils.isNetworkAvailable(this)) {
                emitter.onError(new Exception(getString(R.string.tip_no_net)));
                return;
            }
            ArrayList<File> list = new ArrayList<>();
            try {
                if (mSelected != null) {
                    for (int i = 0; i < mSelected.size(); i++) {
                        list.add(new File(mSelected.get(i).getCompressPath()));
//                        list.add(BitmapAndStringUtils.uriToFile(Uri.parse(mSelected.get(i).getCompressPath())));
                    }
                }
                emitter.onNext(list);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                            showProgressDialog();
                            if (((List<File>) data).size() > 0) {
                                FeedBackHttpUtils.commitFeedBackUploadImg((ArrayList<File>) data, new BaseBribery() {
                                    @Override
                                    public void onSuccess(Object successObj) {
                                        List<String> imgUrlList = (List<String>) successObj;
                                        commitInfo(imgUrlList, msg);
                                    }


                                    @Override
                                    public void onFailure(Object failObj) {
                                        super.onFailure(failObj);
                                        ThreadUtils.runOnUIThread(() -> {
                                            CommonToast.showToast("上传失败！");
                                            cancelProgressDialog();
                                        });
                                    }
                                });
                            } else {
                                commitInfo(null,msg);
                            }


                        }, throwable -> CommonToast.makeText(FeedbackActivity.this, throwable.getMessage())
                ));
    }


    private void commitInfo(List<String> imgUrlList, String msg) {
        FeedBackHttpUtils.commitFeedBack(feedBackType, msg, imgUrlList, new BaseBribery() {
            @Override
            public void onSuccess(Object successObj) {
                BaseResponseBean responseBean = (BaseResponseBean) successObj;
                try {
                    if (responseBean != null && HttpCode.HTTP_OK.equals(responseBean.getCode())) {
                        ThreadUtils.runOnUIThread(() -> {
                            CommonToast.showToast(responseBean.getData());
                            cancelProgressDialog();
                            finish();
                        });
                        return;
                    }
                } catch (Exception e) {
                    SitechDevLog.exception(e);
                }
                ThreadUtils.runOnUIThread(() -> {
                    CommonToast.showToast(responseBean.getData());
                    cancelProgressDialog();
                });
            }

            @Override
            public void onFailure(Object failObj) {
                super.onFailure(failObj);
                ThreadUtils.runOnUIThread(() -> {
                    CommonToast.showToast("反馈信息提交失败");
                    cancelProgressDialog();
                });
            }
        });
    }

    private boolean checkInputText(String msg) {
        return !TextUtils.isEmpty(msg) && msg.length() >= 3;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = PictureSelector.obtainMultipleResult(data);

            if (mSelected != null && mSelected.size() > 0) {
                //设置图片文本
                refreshView();
            }
        }
    }

    private void refreshView() {
        if (mSelected.size() >= 5) {
            ivAddPic.setVisibility(View.GONE);
        } else {
            ivAddPic.setVisibility(View.VISIBLE);
        }

        llPicList.removeAllViews();
        for (int i = 0; i < mSelected.size(); i++) {
            View v = View.inflate(this, R.layout.item_single_imageview_feedback, null);
            ImageView ivPic = v.findViewById(R.id.pic);
            ivPic.setImageURI(Uri.parse(mSelected.get(i).getCompressPath()));
            ImageView ivDel = v.findViewById(R.id.iv_del);
            ivDel.setTag(mSelected.get(i));

            ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelected.remove(v.getTag());
                    refreshView();
                }
            });

            llPicList.addView(v);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        wordNum = s;
    }

    @Override
    public void afterTextChanged(Editable s) {
        int number = num + s.length();
        String str = number + " / " + "<font color='#00FF00'>" + mMaxNum + "</font>";
        tvFeedBack.setText(Html.fromHtml(str));
        selectionStart = etFeedBack.getSelectionStart();
        selectionEnd = etFeedBack.getSelectionEnd();

        if (wordNum.length() == mMaxNum) {
            String str1 = "<font color='#FF0000'>" + mMaxNum + "</font>" + " / " + "<font color='#00FF00'>" + mMaxNum + "</font>";
            tvFeedBack.setText(Html.fromHtml(str1));
        }

        if (wordNum.length() > mMaxNum) {
            s.delete(selectionStart - 1, selectionEnd);
            int tempSelection = selectionEnd;
            etFeedBack.setText(s);
            etFeedBack.setSelection(tempSelection);
            CommonToast.makeText(FeedbackActivity.this, "已达到文本输入上限！");
        }
    }
}
