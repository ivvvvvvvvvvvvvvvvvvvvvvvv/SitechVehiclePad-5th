package com.sitechdev.vehicle.pad.module.online_audio;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kaolafm.opensdk.ResType;
import com.kaolafm.opensdk.api.operation.model.category.Category;
import com.kaolafm.opensdk.http.core.HttpCallback;
import com.kaolafm.opensdk.http.error.ApiException;
import com.sitechdev.vehicle.lib.util.ThreadUtils;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
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
    Context context;
    public ViewAllCategoryDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ViewAllCategoryDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    protected ViewAllCategoryDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        init();
    }

    @Override
    public void show() {
        super.show();
        initSize();
    }

    private void initSize(){
        android.view.WindowManager.LayoutParams lpp = getWindow().getAttributes();
        if (isLand) {
            lpp.width = context.getResources().getInteger(R.integer.kaola_all_category_dialog_w_land);
            lpp.height = context.getResources().getInteger(R.integer.kaola_all_category_dialog_h_land);
        } else {
            lpp.width = context.getResources().getInteger(R.integer.kaola_all_category_dialog_w);
            lpp.height = context.getResources().getInteger(R.integer.kaola_all_category_dialog_h);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(0xff223D75));
        getWindow().setAttributes(lpp);
    }

    RecyclerView recyclerView;
    boolean isLand = true;
    private void init() {
        if (context instanceof BaseActivity) {
            isLand = ((BaseActivity) context).isLandscape();
        }
        View root = getLayoutInflater().inflate(R.layout.dialog_kaola_category, null);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new SpaceItemKaolaDialogItemDecoration(context.getResources().getInteger(R.integer.kaola_all_category_dialog_item_space)));
        int spanCount = isLand ? 5 : 3;
        recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount));
        root.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
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
            initAdapter();
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
                        initAdapter();
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
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            if (clickListener != null) {
                adapter.setOnItemClick(clickListener);
            }
        }
    }
    private void initAdapter() {
        adapter = new KaolaCategoryDialogAdapter(context, categories);
        recyclerView.setAdapter(adapter);
        if (clickListener != null) {
            adapter.setOnItemClick(clickListener);
        }
    }

}
