package com.fb.apps.mobilesafe.bean;

/**
 * Created by FB on 2015/5/25.
 */
public class BlackNumberInfo {
    /**
     * 黑名单的电话号码
     */
    private String number;
    /**
     * 黑名单的拦截模式
     */
    private String mode; //1:全部拦截  2：电话  3 短信

    public void setNumber(String number) {
        this.number = number;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNumber() {
        return number;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "number='" + number + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }
}
