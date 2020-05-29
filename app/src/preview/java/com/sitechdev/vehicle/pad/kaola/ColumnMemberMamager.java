package com.sitechdev.vehicle.pad.kaola;

import com.kaolafm.opensdk.api.operation.model.column.ColumnMember;
import com.kaolafm.opensdk.api.operation.model.column.RadioDetailColumnMember;

/**
 * Description:
 *
 * @author Steve_qi
 * @date: 2019/8/19
 */
public class ColumnMemberMamager {

    private ColumnMemberMamager() {
    }


    public static class SingltonHolder {
        public static ColumnMemberMamager INSTANCE = new ColumnMemberMamager();
    }

    public ColumnMember mColumnMember;
}
