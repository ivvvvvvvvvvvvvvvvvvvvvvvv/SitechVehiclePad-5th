package com.sitechdev.vehicle.pad.module.online_audio;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kaolafm.opensdk.api.BasePageResult;
import com.kaolafm.opensdk.api.operation.OperationRequest;
import com.kaolafm.opensdk.api.operation.model.category.AlbumCategoryMember;
import com.kaolafm.opensdk.api.operation.model.category.Category;
import com.kaolafm.opensdk.api.operation.model.category.CategoryMember;
import com.kaolafm.opensdk.http.core.HttpCallback;
import com.kaolafm.opensdk.http.error.ApiException;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.BaseFragment;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.model.kaola.KaolaDataWarpper;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.view.Indexable;
import com.sitechdev.vehicle.pad.view.ListIndicatorRecycview;
import com.sitechdev.vehicle.pad.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/20
 * </pre>
 */
public class KaolaAudioCategoryPageFrag extends BaseFragment {
    private RecyclerView recyclerView;
    private ListIndicatorRecycview indecator;
    private KaolaAICategoryListAdapter adapter ;
    @Override
    protected int getLayoutId() {
        return R.layout.audio_kaola_sub_frame;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        indecator = mContentView.findViewById(R.id.indicator);
        recyclerView = mContentView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(60));
    }

    List<KaolaDataWarpper> kaolaDataWarpperList = new ArrayList<>();
    private List<Category> mCategories = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        if (AppVariants.activeSuccess) {
            //获取分类数据
            KaolaPlayManager.SingletonHolder.INSTANCE.getkaolaCategory(new HttpCallback<List<Category>>() {
                @Override
                public void onSuccess(List<Category> categories) {
                    mCategories = categories;
                    List<Indexable> indexs = new ArrayList<>();
                    for (int i = 0; i < categories.size(); i++) {
                        int finalI = i;
                        Indexable indexable = new Indexable() {
                            @Override
                            public String getIndex() {
                                return categories.get(finalI).getName();
                            }
                        };
                        indexs.add(indexable);
                    }
                    indecator.setChoose(0);
                    indecator.setupWithData(indexs, new ListIndicatorRecycview.OnItemClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                int pos = (Integer) v.getTag();
                                indecator.setChoose(pos);
                                getCategoryAblum(mCategories.get((Integer) v.getTag()));
                            } catch (Exception e) {

                            }
                        }
                    });
                    //初始化分类专辑数据
                    getCategoryAblum(mCategories.get(0));
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
        new OperationRequest().getCategoryMemberList(category.getCode(), 1, 50, new HttpCallback<BasePageResult<List<CategoryMember>>>() {
            @Override
            public void onSuccess(BasePageResult<List<CategoryMember>> listBasePageResult) {
                adapter = new KaolaAICategoryListAdapter(mContext, listBasePageResult.getDataList());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClick(new KaolaAICategoryListAdapter.OnItemClick() {
                    @Override
                    public void onClick(CategoryMember cm) {
                        if (cm instanceof AlbumCategoryMember) {
                            AlbumCategoryMember albumCategoryMember = (AlbumCategoryMember) cm;
                            jump2Play(albumCategoryMember.getAlbumId(), albumCategoryMember.getTitle(), albumCategoryMember.getImageFiles().get("cover").getUrl());
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
        bundle.putBoolean(Constant.KEY_IS_ALBUM, true);
        bundle.putString(Constant.KEY_TITLE, title);
        RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_ONLINE, bundle);
    }

}
