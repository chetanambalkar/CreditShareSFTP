package com.smicc.CreditShare;

import java.util.List;

public class Configuration {
    private String rootPath;
    private String tempPath;
    private int deletionFrequencyDays;
    private List<String> folderNames;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public int getDeletionFrequencyDays() {
        return deletionFrequencyDays;
    }

    public void setDeletionFrequencyDays(int deletionFrequencyDays) {
        this.deletionFrequencyDays = deletionFrequencyDays;
    }

    public List<String> getFolderNames() {
        return folderNames;
    }

    public void setFolderNames(List<String> folderNames) {
        this.folderNames = folderNames;
    }
}