package com.sitechdev.vehicle.pad.kaola;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.kaolafm.opensdk.api.operation.model.column.Column;
import com.kaolafm.opensdk.api.operation.model.column.ColumnMember;
import com.kaolafm.sdk.core.mediaplayer.IPlayerStateListener;
import com.kaolafm.sdk.core.mediaplayer.PlayItem;
import com.kaolafm.sdk.core.mediaplayer.PlayerManager;
import com.sitechdev.vehicle.lib.imageloader.GlideApp;
import com.sitechdev.vehicle.lib.util.Constant;
import com.sitechdev.vehicle.lib.util.SitechDevLog;
import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.app.BaseActivity;
import com.sitechdev.vehicle.pad.module.forshow.MusicKaolaForShowActivity;
import com.sitechdev.vehicle.pad.module.music.fragment.LocalMusicFragment;
import com.sitechdev.vehicle.pad.router.RouterConstants;
import com.sitechdev.vehicle.pad.router.RouterUtils;
import com.sitechdev.vehicle.pad.util.AppUtil;
import com.sitechdev.vehicle.pad.util.FontUtil;
import com.sitechdev.vehicle.pad.view.ScrollTextView;

import java.util.List;

import static com.sitechdev.vehicle.pad.BuildConfig.DEBUG;
@Route(path = RouterConstants.KAOLA_RADIO_LIST)
public class KaolaListActivity extends BaseActivity {
    Context mContext;
    Column mCurrentColumn;
//    @Autowired(name = Constant.KEY_COLUMN)
    private int deepIndex;
//    @Autowired(name = Constant.KEY_TYPE_INDEX)
    private int pageIndex;
    List<ColumnMember> mColumnMembers;
//    RecyclerView rv_kaola_list;
//    KaolaListAdapter mKaolaListAdapter;

    ImageView iv_back;
    ImageView btn_pre;
    ImageView btn_pause_play;
    ImageView btn_next;
    ImageView btn_pop_list;
    ScrollTextView tv_bottom_title;
    TextView tv_title;
    LinearLayout play_bar_root;

    private static final int[] IMG_CHYL_ICONS = {R.drawable.icon_chyl_top1,
            R.drawable.icon_chyl_top2,
            R.drawable.icon_chyl_bottom1,
            R.drawable.icon_chyl_bottom2,
            R.drawable.icon_chyl_bottom3,
            R.drawable.icon_chyl_bottom4};
    private static final int[] IMG_XTSB_ICONS = {R.drawable.icon_xtsb_top1,
            R.drawable.icon_xtsb_top2,
            R.drawable.icon_xtsb_bottom1,
            R.drawable.icon_xtsb_bottom2,
            R.drawable.icon_xtsb_bottom3,
            R.drawable.icon_xtsb_bottom4};
    private static final int[] IMG_SHYDT_ICONS = {R.drawable.icon_shydt_top1,
            R.drawable.icon_shydt_top2,
            R.drawable.icon_shydt_bottom1,
            R.drawable.icon_shydt_bottom2,
            R.drawable.icon_shydt_bottom3,
            R.drawable.icon_shydt_bottom4};
    private static final int[] IMG_ETDW_ICONS = {R.drawable.icon_etdw_top1,
            R.drawable.icon_etdw_top2,
            R.drawable.icon_etdw_bottom1,
            R.drawable.icon_etdw_bottom2,
            R.drawable.icon_etdw_bottom3,
            R.drawable.icon_etdw_bottom4};

    //default
    private static final int[] IMG_CHYL_ICONS_DEFAULT = {R.drawable.icon_chyl_top1_default,
            R.drawable.icon_chyl_top2_default,
            R.drawable.icon_chyl_bottom1_default,
            R.drawable.icon_chyl_bottom2_default,
            R.drawable.icon_chyl_bottom3_default,
            R.drawable.icon_chyl_bottom4_default};
    private static final int[] IMG_XTSB_ICONS_DEFAULT = {R.drawable.icon_xtsb_top1_default,
            R.drawable.icon_xtsb_top2_default,
            R.drawable.icon_xtsb_bottom1_default,
            R.drawable.icon_xtsb_bottom2_default,
            R.drawable.icon_xtsb_bottom3_default,
            R.drawable.icon_xtsb_bottom4_default};
    private static final int[] IMG_SHYDT_ICONS_DEFAULT = {R.drawable.icon_shydt_top1_default,
            R.drawable.icon_shydt_top2_default,
            R.drawable.icon_shydt_bottom1_default,
            R.drawable.icon_shydt_bottom2_default,
            R.drawable.icon_shydt_bottom3_default,
            R.drawable.icon_shydt_bottom4_default};
    private static final int[] IMG_ETDW_ICONS_DEFAULT = {R.drawable.icon_etdw_top1_default,
            R.drawable.icon_etdw_top2_default,
            R.drawable.icon_etdw_bottom1_default,
            R.drawable.icon_etdw_bottom2_default,
            R.drawable.icon_etdw_bottom3_default,
            R.drawable.icon_etdw_bottom4_default};

    private int[] currentImgArray = null, currentDefaultImgArray = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ARouter.getInstance().inject(this);
        mContext = this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_kaola_content;
    }

    @Override
    protected void initViewBefore() {
        super.initViewBefore();
        parseIntent(getIntent());
    }

    private void parseIntent(Intent intent ){
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                deepIndex = bundle.getInt(Constant.KEY_COLUMN, -1);
                pageIndex = bundle.getInt(Constant.KEY_TYPE_INDEX, 0);
            } else {
                deepIndex = intent.getIntExtra(Constant.KEY_COLUMN, -1);
                pageIndex = intent.getIntExtra(Constant.KEY_TYPE_INDEX, 0);
            }
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
//        rv_kaola_list = findViewById(R.id.rv_kaola_list);

        iv_back = findViewById(R.id.iv_sub_back);
        btn_pre = findViewById(R.id.btn_pre);
        btn_pause_play = findViewById(R.id.btn_pause_play);
        btn_next = findViewById(R.id.btn_next);
        btn_pop_list = findViewById(R.id.btn_pop_list);
        tv_bottom_title = findViewById(R.id.tv_bottom_title);
        tv_bottom_title.setTypeface(FontUtil.getInstance().getMainFont_i());
        tv_title = findViewById(R.id.tv_sub_title);

        play_bar_root = findViewById(R.id.play_bar_root);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
        initData();
        initListener();
    }

    @Override
    protected void initData() {
        mCurrentColumn = KaolaPlayManager.SingletonHolder.INSTANCE.mCurrentColumn;
        if (mCurrentColumn != null) {
            tv_title.setText(mCurrentColumn.getTitle());
            mColumnMembers = (List<ColumnMember>) mCurrentColumn.getColumnMembers();
            switch (pageIndex) {
                case 1://儿童读物
                    currentImgArray = IMG_ETDW_ICONS;
                    currentDefaultImgArray = IMG_ETDW_ICONS_DEFAULT;
                    break;
                case 2://车嗨娱乐
                    currentImgArray = IMG_CHYL_ICONS;
                    currentDefaultImgArray = IMG_CHYL_ICONS_DEFAULT;
                    if (mColumnMembers.size() < 6) {
                        ColumnMember columnMember = (ColumnMember) AppUtil.copyObject(mColumnMembers.get(0));
                        columnMember.setTitle("情景喜剧");
                        mColumnMembers.add(columnMember);
                    }
                    break;
                case 3://生活一点通
                    currentImgArray = IMG_SHYDT_ICONS;
                    currentDefaultImgArray = IMG_SHYDT_ICONS_DEFAULT;
                    break;
                default://新特速报
                    currentImgArray = IMG_XTSB_ICONS;
                    currentDefaultImgArray = IMG_XTSB_ICONS_DEFAULT;
                    break;
            }
//            mKaolaListAdapter = new KaolaListAdapter(this, mColumnMembers);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
//            rv_kaola_list.setLayoutManager(gridLayoutManager);
////        rv_kaola_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//            rv_kaola_list.setAdapter(mKaolaListAdapter);
            initContentViewAndData(mColumnMembers);
        }
        if (deepIndex >= 0 && null != mColumnMembers && mColumnMembers.size() > deepIndex) {
            ColumnMember columnMember = mColumnMembers.get(deepIndex);
            if (null != columnMember) {
                Intent intent = new Intent(KaolaListActivity.this, MusicKaolaForShowActivity.class);
                intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
                intent.putExtra(Constant.KEY_MEMBER_CODE, columnMember);
                startActivity(intent);
            }
        }
    }

    private void resetCardTextView(TextView textView) {
        if (pageIndex == 0 || pageIndex == 3) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.gravity = Gravity.TOP;
            textView.setLayoutParams(layoutParams);
        }
    }

    /**
     * 展示中间内容
     *
     * @param mColumnMembers
     */
    private void initContentViewAndData(List<ColumnMember> mColumnMembers) {

        if (mColumnMembers.size() > 0) {
            View card1 = findViewById(R.id.card1);

            card1.setOnClickListener(this);

            GlideApp.with(this).load(currentImgArray[0]).into((ImageView) card1.findViewById(R.id.image));

            TextView card1TextView = ((TextView) card1.findViewById(R.id.title));
            resetCardTextView(card1TextView);
            card1TextView.setText(mColumnMembers.get(0).getTitle());
        }
        if (mColumnMembers.size() > 1) {
            View card2 = findViewById(R.id.card2);

            card2.setOnClickListener(this);

            GlideApp.with(this).load(currentImgArray[1]).into((ImageView) card2.findViewById(R.id.image));

            TextView card2TextView = ((TextView) card2.findViewById(R.id.title));
            resetCardTextView(card2TextView);
            card2TextView.setText(mColumnMembers.get(1).getTitle());
        }
        if (mColumnMembers.size() > 2) {
            View card3 = findViewById(R.id.card3);

            card3.setOnClickListener(this);

            GlideApp.with(this).load(currentImgArray[2]).into((ImageView) card3.findViewById(R.id.image));

//            ((TextView) card3.findViewById(R.id.title)).setText(mColumnMembers.get(2).getTitle());

            TextView card3TextView = ((TextView) card3.findViewById(R.id.title));
            resetCardTextView(card3TextView);
            card3TextView.setText(mColumnMembers.get(2).getTitle());
        }
        if (mColumnMembers.size() > 3) {
            View card4 = findViewById(R.id.card4);

            card4.setOnClickListener(this);

            GlideApp.with(this).load(currentImgArray[3]).into((ImageView) card4.findViewById(R.id.image));

//            ((TextView) card4.findViewById(R.id.title)).setText(mColumnMembers.get(3).getTitle());

            TextView card4TextView = ((TextView) card4.findViewById(R.id.title));

            resetCardTextView(card4TextView);
            card4TextView.setText(mColumnMembers.get(3).getTitle());
        }
        if (mColumnMembers.size() > 4) {
            View card5 = findViewById(R.id.card5);

            card5.setOnClickListener(this);

            GlideApp.with(this).load(currentImgArray[4]).into((ImageView) card5.findViewById(R.id.image));

//            ((TextView) card5.findViewById(R.id.title)).setText(mColumnMembers.get(4).getTitle());

            TextView card5TextView = ((TextView) card5.findViewById(R.id.title));
            resetCardTextView(card5TextView);
            card5TextView.setText(mColumnMembers.get(4).getTitle());
        }
        if (mColumnMembers.size() > 5) {
            View card6 = findViewById(R.id.card6);

            card6.setOnClickListener(this);

            GlideApp.with(this).load(currentImgArray[5]).into((ImageView) card6.findViewById(R.id.image));

            ((TextView) card6.findViewById(R.id.title)).setText(mColumnMembers.get(5).getTitle());

            TextView card6TextView = ((TextView) card6.findViewById(R.id.title));
            resetCardTextView(card6TextView);
            card6TextView.setText(mColumnMembers.get(5).getTitle());
        } else {
            View card6 = findViewById(R.id.card6);

            card6.setOnClickListener(this);

            GlideApp.with(this).load(currentImgArray[5]).into((ImageView) card6.findViewById(R.id.image));

            TextView card6TextView = ((TextView) card6.findViewById(R.id.title));
            resetCardTextView(card6TextView);
            card6TextView.setText(mColumnMembers.get(0).getTitle());
        }

//        ((TextView) card3.findViewById(R.id.subtitle)).setText(mColumnMembers.get(2).getSubtitle());
//        ((TextView) card4.findViewById(R.id.subtitle)).setText(mColumnMembers.get(3).getSubtitle());
//        ((TextView) card5.findViewById(R.id.subtitle)).setText(mColumnMembers.get(4).getSubtitle());
//        ((TextView) card6.findViewById(R.id.subtitle)).setText(mColumnMembers.get(5).getSubtitle());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (PlayerManager.getInstance(mContext).isPlaying()) {
//            play_bar_root.setVisibility(View.GONE);
//        } else {
//            play_bar_root.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_back.setOnClickListener(this);
        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_pause_play.setOnClickListener(this);
        btn_pop_list.setOnClickListener(this);

//        mKaolaListAdapter.setOnItemClick(new KaolaListAdapter.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(int position) {
//                ColumnMember columnMember = mColumnMembers.get(position);
//                String code = mColumnMembers.get(position).getCode();
//                ColumnMember playingColumnMember = ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember;
//                if (playingColumnMember != null) {
//                    String playingCode = playingColumnMember.getCode();
//                    if (code != null && code.equals(playingCode)) {
//                        SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "=========== jump ");
//                        Intent intent = new Intent(KaolaListActivity.this, NewsDetailsActivity.class);
//                        intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.PLAYING);
//                        startActivity(intent);
//                        return;
//                    }
//                }
//
//                if (code != null) {
//                    Intent intent = new Intent(KaolaListActivity.this, NewsDetailsActivity.class);
//                    intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
//                    intent.putExtra(Constant.KEY_MEMBER_CODE, columnMember);
//                    startActivity(intent);
//                }
//
//                SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "============ position =" + position + "==========" + "CODE = " + mColumnMembers.get(position).getCode());
//            }
//        });
//        PlayerManager.getInstance(mContext).addPlayerStateListener(playerStateListener);
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
            case R.id.iv_sub_back:
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
                Intent intent = new Intent(KaolaListActivity.this, MusicKaolaForShowActivity.class);
                intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.PLAYING);
                startActivity(intent);
                break;
            case R.id.card1:
                onClickItemView(0);
                break;
            case R.id.card2:
                onClickItemView(1);
                break;
            case R.id.card3:
                onClickItemView(2);
                break;
            case R.id.card4:
                onClickItemView(3);
                break;
            case R.id.card5:
                onClickItemView(4);
                break;
            case R.id.card6:
                onClickItemView(5);
                break;
        }
    }

    private void onClickItemView(int position) {
        ColumnMember columnMember = mColumnMembers.get(position);
        String code = mColumnMembers.get(position).getCode();
        ColumnMember playingColumnMember = ColumnMemberMamager.SingltonHolder.INSTANCE.mColumnMember;
        if (playingColumnMember != null) {
            String playingCode = playingColumnMember.getCode();
            if (code != null && code.equals(playingCode)) {
                SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "=========== jump ");
//                Intent intent = new Intent(KaolaListActivity.this, NewsDetailsActivity.class);
//                intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.PLAYING);
//                startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_TYPE_KEY, Constant.TYPE.PLAYING);
                playingColumnMember.setTitle(columnMember.getTitle());
                bundle.putInt(Constant.KEY_DEFAULT_IMG_RESID, currentDefaultImgArray[position]);
                RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_SHOW, bundle);
                return;
            }
        }

        if (code != null) {
//            Intent intent = new Intent(KaolaListActivity.this, NewsDetailsActivity.class);
//            intent.putExtra(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
//            intent.putExtra(Constant.KEY_MEMBER_CODE, columnMember);
//            startActivity(intent);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.KEY_TYPE_KEY, Constant.TYPE.FIRST_ENTERED);
            bundle.putSerializable(Constant.KEY_MEMBER_CODE, columnMember);
            bundle.putInt(Constant.KEY_DEFAULT_IMG_RESID, currentDefaultImgArray[position]);
            RouterUtils.getInstance().navigation(RouterConstants.MUSIC_PLAY_SHOW, bundle);
        }

        SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "============ position =" + position + "==========" + "CODE = " + mColumnMembers.get(position).getCode());
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
//            SitechDevLog.e(KaolaListActivity.class.getSimpleName(), "====== onProgress =======");
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
