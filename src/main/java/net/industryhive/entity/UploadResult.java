package net.industryhive.entity;

import java.util.ArrayList;

/**
 * @Author 未央
 * @Create 2020-11-01 22:31
 */
public class UploadResult {
    private int errno;
    private ArrayList<String> data;

    public UploadResult(int errno, ArrayList<String> data) {
        this.errno=errno;
        this.data=data;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
