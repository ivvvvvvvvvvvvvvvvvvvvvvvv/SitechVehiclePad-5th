package com.sitechdev.vehicle.pad.vui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.sitechdev.vehicle.pad.R;

import org.json.JSONObject;

/**
 * @author zhubaoqiang
 * @date 2019/8/20
 */
public class StockHolder extends VUIHolder {

    private TextView stockName;

    private TextView stockCode;

    private ImageView riseImg;

    private TextView riseRate;

    public StockHolder(@NonNull Context context){
        super(context, R.layout.holder_stock);
    }

    public StockHolder(@NonNull LayoutInflater inflater){
        super(inflater, R.layout.holder_stock);
    }

    @Override
    protected void getView() {
        stockName = findViewById(R.id.stock_holder_name);
        stockCode = findViewById(R.id.stock_holder_stockCode);
        riseImg = findViewById(R.id.stock_holder_riseImg);
        riseRate = findViewById(R.id.stock_holder_riseRate);
    }

    public void adapter(JSONObject stock){
        stockName.setText(stock.optString("name"));
        stockCode.setText(stock.optString("stockCode"));
        String sRiseRate = stock.optString("riseRate");
        String temp = sRiseRate.replace("-", "");
        riseRate.setText(temp);
        float value = Float.valueOf(sRiseRate.replace("%", ""));
        if (value > 0){
            riseImg.setImageResource(R.mipmap.ic_stock_up);
            riseRate.setTextColor(riseRate.getContext().getResources().getColor(
                    R.color.stock_holder_up));
        }else if (value < 0){
            riseImg.setImageResource(R.mipmap.ic_stock_down);
            riseRate.setTextColor(riseRate.getContext().getResources().getColor(
                    R.color.stock_holder_down));
        }else {
            /**
             * 涨平 没图标
             */
            riseImg.setImageDrawable(null);
            riseRate.setTextColor(riseRate.getContext().getResources().getColor(
                    R.color.white));
        }
    }
}
