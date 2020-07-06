package com.sitechdev.vehicle.pad.module.music.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;
import com.sitechdev.vehicle.pad.module.music.MusicManager;
import com.sitechdev.vehicle.pad.module.music.service.MusicInfo;
import com.sitechdev.vehicle.pad.view.CommonToast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhubaoqiang
 * @date 2019/8/24
 */
public class LocalMusicAdapter extends
        RecyclerView.Adapter<LocalMusicAdapter.LocalMusicHolder>
        implements MusicManager.OnMusicChangeListener,
        MusicManager.OnMusicListUpdateListener {

    private Context context;

    private List<MusicInfo> musicInfos;

    private int checkedPositon = -1;

    private int noramlColor;

    private int checkedColor;

    private OnCheckEmptyListener onCheckEmptyListener;

    public LocalMusicAdapter(Context context) {
        this.context = context;
        this.musicInfos = new ArrayList<>();
        noramlColor = context.getResources().getColor(R.color.white);
        checkedColor = context.getResources().getColor(
                R.color.item_local_music_checked_color);
    }

    public int getCheckedPositon() {
        return checkedPositon;
    }

    @NonNull
    @Override
    public LocalMusicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = inflater.inflate(R.layout.item_local_music, viewGroup, false);
        return new LocalMusicHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicHolder holder, int position) {
        boolean validData = position < musicInfos.size() && null != musicInfos.get(position);
        if (!validData) {
            return;
        }
        MusicInfo musicInfo = musicInfos.get(position);
        if (checkedPositon == position){
            holder.getIndex().setTextColor(checkedColor);
            holder.getName().setTextColor(checkedColor);
        }else {
            holder.getIndex().setTextColor(noramlColor);
            holder.getName().setTextColor(noramlColor);
        }
        int pos = position + 1;
        String index = pos <= 9 ? "0" + pos : String.valueOf(pos);
        holder.getIndex().setText(index);
        holder.getName().setText("");
        holder.getName().append(musicInfo.musicName);
        holder.getName().append(" - ");
        holder.getName().append(musicInfo.artist);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedPositon == position){
                    MusicManager.getInstance().toggle(new MusicManager.CallBack<String>() {
                        @Override
                        public void onCallBack(int code, String msg) {
                            if (code != 0){
                                CommonToast.showToast(msg);
                            }
                        }
                    });
                }else {
                    MusicManager.getInstance().play(musicInfo.songId, new MusicManager.CallBack<String>() {
                        @Override
                        public void onCallBack(int code, String msg) {
                            if (code != 0){
                                CommonToast.showToast(msg);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicInfos.size();
    }



    public void addDatas(List<MusicInfo> muscics){
        if (null == muscics || muscics.size() == 0){
            if (null != onCheckEmptyListener){
                onCheckEmptyListener.onCheckEmpty(true);
            }
        }else {
            if (null != onCheckEmptyListener){
                onCheckEmptyListener.onCheckEmpty(false);
            }
            musicInfos.addAll(muscics);
            notifyDataSetChanged();
        }
    }

    public void setOnCheckEmptyListener(OnCheckEmptyListener onCheckEmptyListener) {
        this.onCheckEmptyListener = onCheckEmptyListener;
    }

    @Override
    public void onMusicChange(MusicInfo current, int status) {
        checkedPositon = -1;
        if (null != musicInfos && null != current){
            int len = musicInfos.size();
            for (int i = 0; i < len; i++){
                if (musicInfos.get(i).songId == current.songId){
                    checkedPositon = i;
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onMusicListUpdate(List<MusicInfo> infos, MusicInfo info,
                                  int status, int postion) {
        if (null != info){
            int len = infos.size();
            for (int i = 0; i < len; i++){
                if (info.songId == infos.get(i).songId){
                    checkedPositon = i;
                    break;
                }
            }
        }else {
            checkedPositon = -1;
        }
        musicInfos.clear();
        notifyDataSetChanged();
        addDatas(infos);
    }

    public static class LocalMusicHolder extends RecyclerView.ViewHolder{

        private TextView vIndex;
        private TextView vName;

        public LocalMusicHolder(@NonNull View itemView) {
            super(itemView);
            vIndex = itemView.findViewById(R.id.item_locl_music_index);
            vName = itemView.findViewById(R.id.item_locl_music_name);
        }

        public TextView getIndex() {
            return vIndex;
        }

        public TextView getName() {
            return vName;
        }
    }

    public interface OnCheckEmptyListener{
        void onCheckEmpty(boolean isEmpty);
    }
}
