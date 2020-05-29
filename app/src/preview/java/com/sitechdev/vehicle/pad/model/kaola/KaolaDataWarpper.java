package com.sitechdev.vehicle.pad.model.kaola;

import com.kaolafm.opensdk.api.operation.model.column.Column;
import com.kaolafm.opensdk.api.operation.model.column.ColumnMember;
import com.sitechdev.vehicle.pad.view.Indexable;

import java.io.Serializable;

/**
 * <pre>
 *      author : zyf
 *      time   : 2020/5/25
 * </pre>
 */
public class KaolaDataWarpper implements Serializable , Indexable {
    public String tag = "";
    public ColumnMember column;

    @Override
    public String getIndex() {
        return tag;
    }
}
