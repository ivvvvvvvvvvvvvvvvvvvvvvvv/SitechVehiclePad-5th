package com.sitechdev.vehicle.pad.module.online_audio;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kaolafm.opensdk.ResType;
import com.kaolafm.opensdk.api.search.model.SearchProgramBean;
import com.kaolafm.opensdk.http.core.HttpCallback;
import com.kaolafm.opensdk.http.error.ApiException;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.BaseFragment;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.view.SpaceItemDecoration;

import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/20
 * </pre>
 */
public class KaolaAudioSearchPageFrag extends BaseFragment {
    private RecyclerView recyclerView;
    private KaolaSearchAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.audio_kaola_search_frame;
    }

    ViewAllCategoryDialog dialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        recyclerView = mContentView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(60));
        search("新闻");
    }

    @Override
    protected void initData() {
        super.initData();

    }

    private void search(String key) {
        if (AppVariants.activeSuccess) {
            //获取分类数据
            KaolaPlayManager.SingletonHolder.INSTANCE.searchByKeyword(key, new HttpCallback<List<SearchProgramBean>>() {
                @Override
                public void onSuccess(List<SearchProgramBean> searchProgramBeans) {
                    if (adapter == null) {
                        adapter = new KaolaSearchAdapter(getActivity(), searchProgramBeans);
                        adapter.setOnItemClick(new KaolaSearchAdapter.OnItemClick() {
                            @Override
                            public void onClick(SearchProgramBean warpper) {
                                if (warpper.getType() == ResType.TYPE_BROADCAST) {
                                    jump2PlayBroadcast(warpper.getId(), warpper.getName(), warpper.getImg());
                                }
                                if (warpper.getType() == ResType.TYPE_RADIO || warpper.getType() == ResType.TYPE_AUDIO) {
                                    jump2Play(warpper.getId(), warpper.getName(), warpper.getImg(), false);
                                }
                                if (warpper.getType() == ResType.TYPE_ALBUM) {
                                    jump2Play(warpper.getId(), warpper.getName(), warpper.getImg(), true);
                                }
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.setDataAndNotify(searchProgramBeans);
                    }
                    if (searchProgramBeans == null || searchProgramBeans.size() == 0) {
                        //todo 文字提示
                    }
                }

                @Override
                public void onError(ApiException e) {

                }
            });
        } else {
            KaolaPlayManager.SingletonHolder.INSTANCE.activeKaola();
        }
    }


    private void jump2PlayBroadcast(long id, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
        bundle.putLong(Constant.KEY_MEMBER_CODE, id);
        bundle.putString(Constant.KEY_IMG_URL, url);
        bundle.putString(Constant.KEY_TITLE, title);
        RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_ONLINE_BROADCAST, bundle);
    }

    private void jump2Play(long id, String title, String url, boolean isAlbum) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
        bundle.putLong(Constant.KEY_MEMBER_CODE, id);
        bundle.putString(Constant.KEY_IMG_URL, url);
        bundle.putBoolean(Constant.KEY_IS_ALBUM, isAlbum);
        bundle.putString(Constant.KEY_TITLE, title);
        RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_ONLINE, bundle);
    }
}
