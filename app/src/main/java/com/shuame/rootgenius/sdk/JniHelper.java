package com.shuame.rootgenius.sdk;


public class JniHelper {
    private static final String TAG = JniHelper.class.getSimpleName();


    static {
        System.loadLibrary("rgsdk");
    }


    public static native byte[] decrypt(byte[] bArr);


    public static native byte[] encrypt(byte[] bArr);


}
