package com.sitechdev.vehicle.pad.model.feedback.utils;

import java.util.Map;

public class FeedbackConfig {
    private static FeedbackConfig INSTANCE;

    private FeedbackConfig() {
    }

    public static FeedbackConfig getInstance() {
        if (null == INSTANCE) {
            synchronized (FeedbackConfig.class) {
                if (null == INSTANCE) {
                    INSTANCE = new FeedbackConfig();
                }
            }
        }
        return INSTANCE;
    }


    private Map<String, String> fileMap = null;

    public Map<String, String> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, String> fileMap) {
        this.fileMap = fileMap;
    }
}
