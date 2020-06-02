package com.sitechdev.vehicle.pad.module.online_audio;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kaolafm.opensdk.ResType;
import com.kaolafm.opensdk.api.operation.model.category.Category;
import com.kaolafm.opensdk.http.core.HttpCallback;
import com.kaolafm.opensdk.http.error.ApiException;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.kaola.KaolaPlayManager;
import com.sitechdev.vehicle.pad.model.kaola.KaolaCategoryDataWarpper;
import com.sitechdev.vehicle.pad.view.SpaceItemKaolaDialogItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/6/2
 * </pre>
 */
class ViewAllCategoryDialog extends Dialog {
    public ViewAllCategoryDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public ViewAllCategoryDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected ViewAllCategoryDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    @Override
    public void show() {
        super.show();
        android.view.WindowManager.LayoutParams lpp = getWindow().getAttributes();
        lpp.width = 1525;
        lpp.height = 728;
        getWindow().setBackgroundDrawable(new ColorDrawable(0xff223D75));
        getWindow().setAttributes(lpp);
    }

    RecyclerView recyclerView;

    private void init() {
        View root = getLayoutInflater().inflate(R.layout.dialog_kaola_category, null);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new SpaceItemKaolaDialogItemDecoration(40));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        ImageView close = root.findViewById(R.id.close);
        setContentView(root);
    }

    List<KaolaCategoryDataWarpper> categories = new ArrayList<>();
    KaolaCategoryDialogAdapter.OnItemClick clickListener ;

    public void getCategoryData(HttpCallback<List<Category>> callback, KaolaCategoryDialogAdapter.OnItemClick clickListener) {
        getData(ResType.TYPE_ALBUM, callback, clickListener);
    }

    public void getBroadcastData(HttpCallback<List<Category>> callback, KaolaCategoryDialogAdapter.OnItemClick clickListener) {
        getData(ResType.TYPE_BROADCAST, callback, clickListener);
    }

    public void getData(int type, HttpCallback<List<Category>> callback, KaolaCategoryDialogAdapter.OnItemClick clickListener) {
        this.clickListener = clickListener;
        if (categories != null && categories.size() > 0) {
            callback.onSuccess(new ArrayList<>());
            setData();
            return;
        }
        KaolaPlayManager.SingletonHolder.INSTANCE.getkaolagetCategoryTree(type, new HttpCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> c) {
                ThreadUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != callback) {
                            callback.onSuccess(c);
                        }
                        categories.clear();
                        categories = KaolaBroadcastUtil.dealdata(c);
                        setData();
                    }
                });
            }

            @Override
            public void onError(ApiException e) {
                if (null != callback) {
                    callback.onError(e);
                }
            }
        });
    }

    KaolaCategoryDialogAdapter adapter;

    private void setData() {
        adapter = new KaolaCategoryDialogAdapter(getContext(), categories);
        recyclerView.setAdapter(adapter);
        if (clickListener != null) {
            adapter.setOnItemClick(clickListener);
        }
    }
}
