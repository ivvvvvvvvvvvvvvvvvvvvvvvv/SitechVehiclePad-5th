package com.sitechdev.vehicle.pad.view.dialog;

import com.sitechdev.vehicle.pad.R;

/**
 * @author zhubaoqiang
 * @date 2019/5/9
 *
 */
public class LeftCancelDialogClock extends DialogClick {

    @Override
    public void onDialogClick(int id) {
        if (id == R.id.dialog_t_left){
            adapter.getDialogWrapper().dismiss();
        }
    }
}
