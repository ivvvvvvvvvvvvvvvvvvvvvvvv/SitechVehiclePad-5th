package com.sitechdev.vehicle.pad.module.online_audio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kaolafm.opensdk.api.operation.model.column.Column;
import com.kaolafm.opensdk.api.operation.model.column.ColumnMember;
import com.kaolafm.opensdk.api.operation.model.column.RadioDetailColumnMember;
import com.kaolafm.sdk.core.mediaplayer.PlayerListManager;
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

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/20
 * </pre>
 */
//听伴  AI电台页
public class KaolaAudioSubPageFrag extends BaseFragment {
    private RecyclerView recyclerView;
    private ListIndicatorRecycview indecator;
    private KaolaAIListAdapter adapter;
    private ColumnMember ready2playColumn;
    private int defaultIndex;
    private int defaultSubIndex = 0;

    public KaolaAudioSubPageFrag() {

    }

    @SuppressLint("ValidFragment")
    public KaolaAudioSubPageFrag(int defaultIndex, int subIndex) {
        this.defaultIndex = defaultIndex;
        this.defaultSubIndex = subIndex;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.audio_kaola_sub_frame;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        indecator = mContentView.findViewById(R.id.indicator);
        recyclerView = mContentView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(60));
        adapter = new KaolaAIListAdapter(mContext, new ArrayList());
        recyclerView.setAdapter(adapter);
        indecator.setupWithRecycler(recyclerView);
        adapter.setOnItemClick(new KaolaAIListAdapter.OnItemClick() {
            @Override
            public void onClick(KaolaDataWarpper warpper) {
                toPlaylist(warpper.column);
            }
        });
    }

    private void toPlaylist(ColumnMember column){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
        bundle.putLong(Constant.KEY_MEMBER_CODE, ((RadioDetailColumnMember) column).getRadioId());
        if(column.getImageFiles() != null){
            if(column.getImageFiles().containsKey("cover")){
                bundle.putString(Constant.KEY_IMG_URL, column.getImageFiles().get("cover").getUrl());
            }
            if(column.getImageFiles().containsKey("icon")){
                bundle.putString(Constant.KEY_IMG_URL, column.getImageFiles().get("icon").getUrl());
            }
        }

        bundle.putString(Constant.KEY_TITLE, column.getTitle());
        RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_ONLINE, bundle);
    }

    List<KaolaDataWarpper> kaolaDataWarpperList = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        if (AppVariants.activeSuccess) {
            KaolaPlayManager.SingletonHolder.INSTANCE.setCallback(new KaolaPlayManager.Callback() {
                @Override
                public void onSuccess(int index, String textContent) {
                }

                @Override
                public void onDataGot(List<Column> data) {
                    if (!KaolaPlayManager.SingletonHolder.INSTANCE.isPlaying(getActivity())) {
                        //当前未播放资源   播放数据中defaultIndex 下面defaultSubIndex 内容
                        final int index = defaultIndex;
                        for (int i = 0; i < data.size(); i++) {
                            if (i == index) {
                                if (data.get(i) != null && data.get(i).getColumnMembers() != null && defaultSubIndex < data.get(i).getColumnMembers().size()) {
                                    ready2playColumn = data.get(i).getColumnMembers().get(defaultSubIndex);
                                    if (null != ready2playColumn && ready2playColumn instanceof RadioDetailColumnMember) {
                                        PlayerListManager.getInstance().clearPlayList();
                                        KaolaPlayManager.SingletonHolder.INSTANCE.playPgc(getActivity(), ((RadioDetailColumnMember) ready2playColumn).getRadioId());
                                        KaolaPlayManager.SingletonHolder.INSTANCE.setCurPlayingAlbumTitle(ready2playColumn.getTitle());
                                        KaolaPlayManager.SingletonHolder.INSTANCE.setCurPlayingAlbumCover(ready2playColumn.getImageFiles());
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    Observable.fromIterable(data).map(new Function<Column, List<KaolaDataWarpper>>() {
                        @Override
                        public List<KaolaDataWarpper> apply(Column column) throws Exception {
                            List<KaolaDataWarpper> kaolaDataWarpperList = new ArrayList<>();
                            for (int i = 0; i < column.getColumnMembers().size(); i++) {
                                KaolaDataWarpper warpper = new KaolaDataWarpper();
                                warpper.column = column.getColumnMembers().get(i);
                                warpper.tag = column.getTitle();
                                kaolaDataWarpperList.add(warpper);
                            }
                            return kaolaDataWarpperList;
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<KaolaDataWarpper>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            kaolaDataWarpperList.clear();
                        }

                        @Override
                        public void onNext(List<KaolaDataWarpper> kaolaDataWarppers) {
                            kaolaDataWarpperList.addAll(kaolaDataWarppers);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            adapter.setDataAndNotify(kaolaDataWarpperList);
                            indecator.initChoose(defaultIndex);

                        }
                    });
                }
            });
            KaolaPlayManager.SingletonHolder.INSTANCE.acquireKaolaData();
        } else {
            KaolaPlayManager.SingletonHolder.INSTANCE.activeKaola();
        }
    }

    private List<Indexable> initMock(){
        List<Indexable> list = new ArrayList<>();
        Indexable indexable = new Indexable() {
            @Override
            public String getIndex() {
                return "推荐";
            }
        };
        list.add(indexable);
        list.add(indexable);
        list.add(indexable);
        Indexable indexable1 = new Indexable() {
            @Override
            public String getIndex() {
                return "新特速报";
            }
        };
        list.add(indexable1);
        list.add(indexable1);
        list.add(indexable1);
        list.add(indexable1);
        list.add(indexable1);
        Indexable indexable2 = new Indexable() {
            @Override
            public String getIndex() {
                return "少儿读物";
            }
        };
        list.add(indexable2);
        list.add(indexable2);
        list.add(indexable2);
        list.add(indexable2);
        list.add(indexable2);
        Indexable indexable3 = new Indexable() {
            @Override
            public String getIndex() {
                return "车嗨娱乐";
            }
        };
        list.add(indexable3);
        list.add(indexable3);
        list.add(indexable3);
        list.add(indexable3);
        list.add(indexable3);
        Indexable indexable4 = new Indexable() {
            @Override
            public String getIndex() {
                return "生活一点通";
            }
        };
        list.add(indexable4);
        list.add(indexable4);
        list.add(indexable4);
        list.add(indexable4);
        list.add(indexable4);
        return list;
    }

}
