package com.sitechdev.vehicle.pad.module.online_audio;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.bean.BaseFragment;
import com.sitechdev.vehicle.pad.view.Indexable;
import com.sitechdev.vehicle.pad.view.ZSideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/20
 * </pre>
 */
public class KaolaAudioSubPageFrag extends BaseFragment {
    private ZSideBar zbar;
    private RecyclerView recyclerView;
    private KaolaAIListAdapter adapter ;
    @Override
    protected int getLayoutId() {
        return R.layout.audio_kaola_sub_frame;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        zbar = mContentView.findViewById(R.id.zbar);
        recyclerView = mContentView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new KaolaAIListAdapter(mContext, new ArrayList());
        recyclerView.setAdapter(adapter);
        zbar.setChoose(0);
        zbar.setupWithRecycler(recyclerView);
    }

    @Override
    protected void initData() {
        super.initData();
        List<Indexable> list = initMock();
        adapter.setDataAndNotify(list);
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
