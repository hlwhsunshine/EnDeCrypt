package com.annotion.ruiyi.endecrypt;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/* renamed from: com.shuame.rootgenius.sdk.a */
final class C1466a implements FileFilter {


    public final boolean accept(File file) {
        return Pattern.matches("cpu[0-9]", file.getName());
    }
}
