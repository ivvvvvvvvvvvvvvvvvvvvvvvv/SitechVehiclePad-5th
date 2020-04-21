package com.sitechdev.vehicle.pad.kaola;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaolafm.opensdk.api.operation.model.column.Column;
import com.kaolafm.opensdk.api.operation.model.column.ColumnMember;
import com.kaolafm.sdk.core.mediaplayer.IPlayerStateListener;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayerListManager;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.util.AppVariants;
import com.sitechdev.vehicle.pad.view.CommonToast;
import com.sitechdev.vehicle.pad.view.ScrollTextView;

import java.util.List;

import static com.sitechdev.vehicle.pad.BuildConfig.DEBUG;

public class KaolaListActivity extends BaseActivity {
    Context mContext;
    Column mCurrentColumn;
    private int deepIndex;
    List<ColumnMember> mColumnMembers;
    RecyclerView rv_kaola_list;
    KaolaListAdapter mKaolaListAdapter;

    ImageView iv_back;
    ImageView btn_pre;
    ImageView btn_pause_play;
    ImageView btn_next;
    ImageView btn_pop_list;
    ScrollTextView tv_bottom_title;
    TextView tv_title;
    LinearLayout play_bar_root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_kaola_main;
    }

    @Override
    protected void initViewBefore() {
        super.initViewBefore();
        deepIndex = getIntent().getIntExtra(Constant.KEY_COLUMN, -1);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        rv_kaola_list = findViewById(R.id.rv_kaola_list);

        iv_back = findViewById(R.id.iv_back);
        btn_pre = findViewById(R.id.btn_pre);
        btn_pause_play = findViewById(R.id.btn_pause_play);
        btn_next = findViewById(R.id.btn_next);
        btn_pop_list = findViewById(R.id.btn_pop_list);
        tv_bottom_title = findViewById(R.id.tv_bottom_title);
        tv_title = findViewById(R.id.tv_title);

        play_bar_root = findViewById(R.id.play_bar_root);
        if (PlayerManager.getInstance(mContext).isPlaying()) {
            play_bar_root.setVisibility(View.VISIBLE);
        } else {
            play_bar_root.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        deepIndex = intent.getIntExtra(Constant.KEY_COLUMN, -1);
        initData();
        initListener();
    }

    @Override
    protected void initData() {
        mCurrentColumn = KaolaPlayManager.SingletonHolder.INSTANCE.mCurrentColumn;
        if (mCurrentColumn != null) {
            tv_title.setText(mCurrentColumn.getTitle());
            mColumnMembers = (List<ColumnMember>) mCurrentColumn.getColumnMembers();
            mKaolaListAdapter = new KaolaListAdapter(this, mColumnMembers);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,3,LinearLayoutManager.VERTICAL,false);
            rv_kaola_list.setLayoutManager(gridLayoutManager);
//        rv_kaola_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rv_kaola_list.setAdapter(mKaolaListAdapter);
        }
        if (deepIndex >= 0 && null != mColumnMembers &&
                mColumnMembers.size() > deepIndex){
            ColumnMember columnMember = mColumnMembers.get(deepIndex);
            if (null != columnMember){
                Intent intent = new Intent(KaolaListActivity.this, NewsDetailsActivity.class);
                intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
                intent.putExtra(Constant.KEY_MEMBER_CODE, columnMember);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PlayerManager.getInstance(mContext).isPlaying()) {
            play_bar_root.setVisibility(View.VISIBLE);
        } else {
            play_bar_root.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_pause_play.setOnClickListener(this);
        btn_pop_list.setOnClickListener(this);

        mKaolaListAdapter.setOnItemClick(new KaolaListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                ColumnMember columnMember = mColumnMembers.get(position);
                String code = mColumnMembers.get(position).getCode();
                ColumnMember playingColumnMember = ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember;
                if (playingColumnMember != null) {
                    String playingCode = playingColumnMember.getCode();
                    if (code != null && code.equals(playingCode)) {
                        SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "=========== jump ");
                        Intent intent = new Intent(KaolaListActivity.this, NewsDetailsActivity.class);
                        intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.PLAYING);
                        startActivity(intent);
                        return;
                    }
                }

                if (code != null) {
                    Intent intent = new Intent(KaolaListActivity.this, NewsDetailsActivity.class);
                    intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
                    intent.putExtra(Constant.KEY_MEMBER_CODE, columnMember);
                    startActivity(intent);
                }

                SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "============ position =" + position + "==========" + "CODE = " + mColumnMembers.get(position).getCode());
            }
        });
        PlayerManager.getInstance(mContext).addPlayerStateListener(playerStateListener);
        if (DEBUG) SitechDevLog.e(this.getClass().getSimpleName(), mCurrentColumn.toString());

/*        if (DEBUG) {
            Button btn_to_child = findViewById(R.id.btn_to_child);
            btn_to_child.setVisibility(View.VISIBLE);
            btn_to_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KaolaPlayManager.SingletonHolder.INSTANCE.toPlayListActivity(KaolaListActivity.this, 1);
                }
            });
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_pre:
                KaolaPlayManager.SingletonHolder.INSTANCE.playPre();
                break;
            case R.id.btn_next:
                KaolaPlayManager.SingletonHolder.INSTANCE.playNext();
                break;
            case R.id.btn_pause_play:
                PlayerManager.getInstance(mContext).switchPlayerStatus();
                break;
            case R.id.btn_pop_list:
                Intent intent = new Intent(KaolaListActivity.this, NewsDetailsActivity.class);
                intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.PLAYING);
                startActivity(intent);
                break;
        }
    }

    private IPlayerStateListener playerStateListener = new IPlayerStateListener() {
        @Override
        public void onIdle(PlayItem playItem) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onIdle =======");
        }

        @Override
        public void onPlayerPreparing(PlayItem playItem) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onPlayerPreparing =======");
        }

        @Override
        public void onPlayerPlaying(PlayItem playItem) {
            if (playItem != null) {
                tv_bottom_title.setText(playItem.getTitle());
                btn_pause_play.setActivated(true);
            }

            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onPlayerPlaying =======");
        }

        @Override
        public void onPlayerPaused(PlayItem playItem) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onPlayerPaused =======");
            btn_pause_play.setActivated(false);
        }

        @Override
        public void onProgress(String s, int i, int i1, boolean b) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onProgress =======");
        }

        @Override
        public void onPlayerFailed(PlayItem playItem, int i, int i1) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onPlayerFailed =======");
        }

        @Override
        public void onPlayerEnd(PlayItem playItem) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onPlayerEnd =======");
        }

        @Override
        public void onSeekStart(String s) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onSeekStart =======");
        }

        @Override
        public void onSeekComplete(String s) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onSeekComplete =======");
        }

        @Override
        public void onBufferingStart(PlayItem playItem) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onBufferingStart =======");
        }

        @Override
        public void onBufferingEnd(PlayItem playItem) {
            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onBufferingEnd =======");
        }
    };
}
