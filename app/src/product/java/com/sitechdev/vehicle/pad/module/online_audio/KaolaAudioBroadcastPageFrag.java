package com.sitechdev.vehicle.pad.module.online_audio;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kaolafm.opensdk.api.BasePageResult;
import com.kaolafm.opensdk.api.operation.OperationRequest;
import com.kaolafm.opensdk.api.operation.model.category.BroadcastCategoryMember;
import com.kaolafm.opensdk.api.operation.model.category.Category;
import com.kaolafm.opensdk.api.operation.model.category.CategoryMember;
import com.kaolafm.opensdk.http.core.HttpCallback;
import com.kaolafm.opensdk.http.error.ApiException;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.bean.BaseFragment;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.model.kaola.KaolaDataWarpper;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.view.Indexable;
import com.sitechdev.vehicle.pad.view.KaolaCategorySpaceItemDecoration;
import com.sitechdev.vehicle.pad.view.ListIndicatorRecycview;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/20
 * </pre>
 */
public class KaolaAudioBroadcastPageFrag extends BaseFragment {
    private RecyclerView recyclerView;
    private ListIndicatorRecycview indecator;
    private KaolaAICategoryListAdapter adapter;
    private TextView curSelectChannel;
    private List<Category> mCategories = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.audio_kaola_category_frame;
    }
    ViewAllCategoryDialog dialog;
    @Override
    protected void initView(Bundle savedInstanceState) {
        indecator = mContentView.findViewById(R.id.indicator);
        curSelectChannel = mContentView.findViewById(R.id.cur_select_channel);
        recyclerView = mContentView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        gridLayoutManager.setOrientation(isLandscape() ? GridLayoutManager.HORIZONTAL : GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new KaolaCategorySpaceItemDecoration());
        mContentView.findViewById(R.id.btn_all_categroy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).showProgressDialog();
                dialog = new ViewAllCategoryDialog(getActivity());
                dialog.getBroadcastData(new HttpCallback<List<Category>>() {
                    @Override
                    public void onSuccess(List<Category> categories) {
                        ((BaseActivity) getActivity()).cancelProgressDialog();
                        dialog.show();
                    }


                    @Override
                    public void onError(ApiException e) {
                        ((BaseActivity) getActivity()).cancelProgressDialog();
                    }
                }, warpper -> {
                    dialog.cancel();
                    getCategoryAblum(warpper);
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != dialog) {
            dialog.cancel();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (AppVariants.activeSuccess) {
            //获取分类数据
            KaolaPlayManager.SingletonHolder.INSTANCE.getkaolaBroadcast(new HttpCallback<List<Category>>() {
                @Override
                public void onSuccess(List<Category> categories) {
                    mCategories.clear();
                    List<Indexable> indexs = KaolaBroadcastUtil.getIndexableData(categories, isLandscape() ? 4 : 3);
                    mCategories = KaolaBroadcastUtil.getList(categories);
                    indecator.setupWithData(indexs, new ListIndicatorRecycview.OnItemClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                int pos = (Integer) v.getTag();
                                curSelectChannel.setText(mCategories.get((Integer) v.getTag()).getName());
                                getCategoryAblum(mCategories.get((Integer) v.getTag()));
                            } catch (Exception e) {

                            }
                        }
                    });
                    //初始化分类专辑数据
                    getCategoryAblum(mCategories.get(0));
                    curSelectChannel.setText(mCategories.get(0).getName());
                }

                @Override
                public void onError(ApiException e) {
                }
            });
        } else {
            KaolaPlayManager.SingletonHolder.INSTANCE.activeKaola();
        }
    }

    private void getCategoryAblum(Category category) {
        String name = category.getName();
        new OperationRequest().getCategoryMemberList(category.getCode(), 1, 50, new HttpCallback<BasePageResult<List<CategoryMember>>>() {
            @Override
            public void onSuccess(BasePageResult<List<CategoryMember>> listBasePageResult) {
                adapter = new KaolaAICategoryListAdapter(mContext, listBasePageResult.getDataList());
                recyclerView.setAdapter(adapter);
                curSelectChannel.setText(name);
                adapter.setOnItemClick(new KaolaAICategoryListAdapter.OnItemClick() {
                    @Override
                    public void onClick(CategoryMember cm) {
                        if (cm instanceof BroadcastCategoryMember) {
                            BroadcastCategoryMember broadcastCategoryMember = (BroadcastCategoryMember) cm;
                            String url = "";
                            if (broadcastCategoryMember.getImageFiles() != null && broadcastCategoryMember.getImageFiles().get("cover") != null) {
                                url = broadcastCategoryMember.getImageFiles().get("cover").getUrl();
                            }
                            jump2Play(broadcastCategoryMember.getBroadcastId(), broadcastCategoryMember.getTitle(), url);
                        }
                    }
                });
            }

            @Override
            public void onError(ApiException e) {

            }
        });
    }

    private void jump2Play(long id, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
        bundle.putLong(Constant.KEY_MEMBER_CODE, id);
        bundle.putString(Constant.KEY_IMG_URL, url);
        bundle.putString(Constant.KEY_TITLE, title);
        RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_ONLINE_BROADCAST, bundle);
    }

}
