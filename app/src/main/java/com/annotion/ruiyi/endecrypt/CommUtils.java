package com.annotion.ruiyi.endecrypt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;


import com.shuame.rootgenius.sdk.proto.ProtoData;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;



public class CommUtils {
    private static final String CHECK_CMD_END_TEXT = "rg_cmd_end_magic";
    private static final String CHECK_CMD_START_TEXT = "rg_cmd_start_magic";
    private static final String TAG = CommUtils.class.getSimpleName();
    protected static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static Map StringSplitToHash(String str, char c, char c2) {
        int i = 0;
        Map hashMap = new HashMap();
        String str2 = "";
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == c) {
                str2 = str.substring(i, i2).trim();
                i = i2 + 1;
            } else if (str.charAt(i2) == c2) {
                hashMap.put(str2, str.substring(i, i2).trim());
                str2 = "";
                i = i2 + 1;
            }
        }
        if (!str2.isEmpty()) {
            hashMap.put(str2, str.substring(i).trim());
        }
        return hashMap;
    }

    public static boolean aysnDownload(String str, String str2, ExecutorService executorService, Runnable runnable) {
        Executors.newSingleThreadExecutor();
        return true;
    }

    public static String bytesToHex(byte[] bArr) {
        char[] cArr = new char[(bArr.length << 1)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            cArr[i << 1] = hexArray[i2 >>> 4];
            cArr[(i << 1) + 1] = hexArray[i2 & 15];
        }
        return new String(cArr);
    }

    public static String calcFileMd5(InputStream inputStream) {
        String str = "";
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] bArr = new byte[8192];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    return bytesToHex(instance.digest());
                }
                instance.update(bArr, 0, read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return str;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return str;
        } catch (IOException e3) {
            e3.printStackTrace();
            return str;
        }
    }

    public static String calcFileMd5(String str) {
        String str2 = "";
        try {
            if (!new File(str).exists()) {
                return str2;
            }
            InputStream fileInputStream = new FileInputStream(str);
            str2 = calcFileMd5(fileInputStream);
            fileInputStream.close();
            return str2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return str2;
        } catch (IOException e2) {
            e2.printStackTrace();
            return str2;
        }
    }

    public static String calcMd5(byte[] bArr) {
        String str = "";
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bArr, 0, bArr.length);
            return bytesToHex(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static boolean checkActiveNetworkConnected(Context context, int i) {
        @SuppressLint("WrongConstant") ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        while (true) {
            if (activeNetworkInfo != null) {
                try {
                    if (activeNetworkInfo.isConnected()) {
                        break;
                    }
                } catch (NullPointerException e2) {
                    e2.printStackTrace();
                }
            }
            if (i <= 0) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            i -= 1000;
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static boolean checkFileMd5(InputStream inputStream, String str) {
        return (str == null || str.isEmpty() || !calcFileMd5(inputStream).equalsIgnoreCase(str)) ? false : true;
    }

    public static boolean checkFileMd5(String str, String str2) {
        boolean checkFileMd5;
        FileNotFoundException e;
        IOException e2;
        try {
            InputStream fileInputStream = new FileInputStream(str);
            checkFileMd5 = checkFileMd5(fileInputStream, str2);
            try {
                fileInputStream.close();
            } catch (FileNotFoundException e3) {
                e = e3;
                e.printStackTrace();
                return checkFileMd5;
            } catch (IOException e4) {
                e2 = e4;
                e2.printStackTrace();
                return checkFileMd5;
            }
        } catch (FileNotFoundException e5) {
            FileNotFoundException fileNotFoundException = e5;
            checkFileMd5 = false;
            e = fileNotFoundException;
        } catch (IOException e6) {
            IOException iOException = e6;
            checkFileMd5 = false;
            e2 = iOException;
            e2.printStackTrace();
            return checkFileMd5;
        }
        return checkFileMd5;
    }

    private static String checkRid(Context context, String str) {
        String group;
        int i = 0;
        ArrayList arrayList = new ArrayList();
        arrayList.add("/sdcard/.rid");
        arrayList.add(context.getFilesDir().getAbsolutePath() + "/.rid");
        Pattern compile = Pattern.compile("rid:(\\d+)");
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            Matcher matcher = compile.matcher(readFrom((String) arrayList.get(i2)));
            if (matcher.find()) {
                group = matcher.group(1);
                break;
            }
        }
        group = null;
        if (group == null || group.isEmpty()) {
            CRC32 crc32 = new CRC32();
            if (str == null || str.isEmpty()) {
                str = String.valueOf(System.currentTimeMillis());
            }
            crc32.update(str.getBytes());
            group = String.format("%d", new Object[]{Long.valueOf(crc32.getValue())});
        }
        if (group == null || group.isEmpty()) {
            return "0";
        }
        while (i < arrayList.size()) {
            writeTo("rid:" + group, new File((String) arrayList.get(i)).getAbsolutePath());
            i++;
        }
        return group;
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                file.delete();
                return;
            }
            for (File delete : listFiles) {
                delete(delete);
            }
            file.delete();
        }
    }

    public static String exec(String str) {
        String readErrInStream;
        if (str == null || str.isEmpty()) {
            return "";
        }
        try {
            Process exec = Runtime.getRuntime().exec(str);
            readErrInStream = readErrInStream(exec);
            exec.waitFor();
            return readErrInStream;

        } catch (Exception e) {
            return "";
        }
    }

    public static String exec(String[] strArr) {
        String readErrInStream;
        if (strArr == null || strArr.length == 0) {
            return "";
        }
        try {
            Process exec = Runtime.getRuntime().exec(strArr);
            readErrInStream = readErrInStream(exec);
            exec.waitFor();
            return readErrInStream;

        } catch (Exception e) {
            return "";
        }
    }

    public static String execCmd(String str) {
        return exeCmd(str);
    }



    private static void getAdbInfo(ProtoData.AdbInfo adbInfo) {
        if (VERSION.SDK_INT >= 14) {
            String str = "/sys/class/android_usb/android0/";
            adbInfo.serial = readFrom(str + "iSerial").replaceAll("\n", "");
            adbInfo.pid = readFrom(str + "idProduct").replaceAll("\n", "");
            adbInfo.vid = readFrom(str + "idVendor").replaceAll("\n", "");
        }
    }

    public static String getFileNameFromUrl(String str) {
        return str.substring(str.lastIndexOf(47) + 1).split("\\?")[0].split("#")[0];
    }

    public static String getImei(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
    }

    public static String getImsi(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
    }

    public static int getNumCores() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new C1466a()).length;
        } catch (Exception e) {
            return 1;
        }
    }




    @SuppressLint({"NewApi"})
    public static String getResolution(Context context) {
        int i;
        int i2;
        Point point = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (VERSION.SDK_INT >= 13) {
            windowManager.getDefaultDisplay().getSize(point);
            i = point.x;
            i2 = point.y;
        } else {
            Display defaultDisplay = windowManager.getDefaultDisplay();
            i = defaultDisplay.getWidth();
            i2 = defaultDisplay.getHeight();
        }
        return String.valueOf(i) + "x" + String.valueOf(i2);
    }



    public static int getVersionCode(Context context) {
        int i = 0;
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }

    public static String getVersionName(Context context) {
        String str = "";
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String getWifiMacAddr(Context context) {
        String readPrefWifiMacAddr = readPrefWifiMacAddr(context);
        if (readPrefWifiMacAddr.isEmpty()) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager.isWifiEnabled()) {
                readPrefWifiMacAddr = wifiManager.getConnectionInfo().getMacAddress();
            } else {
                wifiManager.setWifiEnabled(true);
                String macAddress = wifiManager.getConnectionInfo().getMacAddress();
                wifiManager.setWifiEnabled(false);
                readPrefWifiMacAddr = macAddress;
            }
        }
        writePrefWifiMacAddr(context, readPrefWifiMacAddr);
        return readPrefWifiMacAddr == null ? "" : readPrefWifiMacAddr;
    }

    public static int parseInt(String str) {
        int i = 0;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return i;
        }
    }

    public static ProtoData.RootingDev parseRootingDev(Context context) {
        ProtoData.RootingDev rootingDev = new ProtoData.RootingDev();
        try {
            char[] cArr = new char[1024];
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            Properties properties = new Properties();
            properties.load(new StringReader(String.valueOf(cArr, 0, fileReader.read(cArr)).replaceAll("CPU\\s+", "CPU_")));
            fileReader.close();
            rootingDev.phoneInfo.hardware = properties.getProperty("Hardware", "");
            rootingDev.phoneInfo.cpuInfo.hardware = properties.getProperty("Hardware", "");
            rootingDev.phoneInfo.cpuInfo.processor = properties.getProperty("Processor", "");
            rootingDev.phoneInfo.cpuInfo.arch = properties.getProperty("CPU_architecture", "");
            rootingDev.phoneInfo.cpuInfo.cores = getNumCores();
            fileReader = new FileReader("/proc/version");
            int read = fileReader.read(cArr);
            fileReader.close();
            rootingDev.phoneInfo.kernel = String.valueOf(cArr, 0, read).trim();
            Reader stringReader = new StringReader(execCmd("getprop").replaceAll("[\\[\\]]", ""));
            properties.clear();
            properties.load(stringReader);
            stringReader.close();
            rootingDev.phoneInfo.buildId = properties.getProperty("ro.build.id", "");
            rootingDev.phoneInfo.productDevice = properties.getProperty("ro.product.device", "");
            rootingDev.phoneInfo.productModel = properties.getProperty("ro.product.model", "");
            rootingDev.phoneInfo.phoneHardware = properties.getProperty("ro.hardware", "");
            rootingDev.phoneInfo.productBoard = properties.getProperty("ro.product.board", "");
            rootingDev.phoneInfo.productBrand = properties.getProperty("ro.product.brand", "");
            rootingDev.phoneInfo.buildFingerPrint = properties.getProperty("ro.build.fingerprint");
            rootingDev.phoneInfo.productManufacturer = properties.getProperty("ro.product.manufacturer", "");
            rootingDev.phoneInfo.androidVersion = properties.getProperty("ro.build.version.release", "");
            rootingDev.phoneInfo.buildDescription = properties.getProperty("ro.build.description", "");
            rootingDev.phoneInfo.buildVersionSdk = properties.getProperty("ro.build.version.sdk", "");
            rootingDev.phoneInfo.region = properties.getProperty("ro.csc.sales_code", "");
            if (rootingDev.phoneInfo.region.isEmpty()) {
                rootingDev.phoneInfo.region = properties.getProperty("rli.sales_code", "");
            }
            rootingDev.phoneId.mac = getWifiMacAddr(context);
            rootingDev.phoneId.packageName = context.getPackageName();
            if (rootingDev.phoneId.phimei.isEmpty()) {
                rootingDev.phoneId.phimei = getImei(context);
            }
            if (rootingDev.phoneId.imsi.isEmpty()) {
                rootingDev.phoneId.imsi = getImsi(context);
            }
            getAdbInfo(rootingDev.phoneInfo.adb);
            rootingDev.phoneId.phsn = rootingDev.phoneInfo.adb.serial;
            rootingDev.phoneId.rid = checkRid(context, rootingDev.phoneId.phsn + rootingDev.phoneInfo.productModel + String.valueOf(System.currentTimeMillis()));
            rootingDev.phoneInfo.resolution = getResolution(context);
            String obj = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            rootingDev.phoneInfo.phoneId = TextUtils.isEmpty(obj) ? null : m4052a(obj.toCharArray(), 16);
            rootingDev.inited = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return rootingDev;
    }


    public static String m4052a(char[] cArr, int i) {
        if (cArr == null || cArr.length == 0 || i < 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(i);
        Random random = new Random();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append(cArr[random.nextInt(cArr.length)]);
        }
        return stringBuilder.toString();
    }

    public static String readErrInStream(Process process) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String readLine;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while (true) {
                readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuffer.append(readLine);
                stringBuffer.append("\n");
            }
            bufferedReader.close();
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (true) {
                readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuffer.append(readLine);
                stringBuffer.append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public static String readFrom(String str) {
        String str2 = "";
        try {
            InputStream fileInputStream = new FileInputStream(str);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                str2 = str2 + new String(bArr, 0, read);
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e2) {
        }
        return str2;
    }
    private static String readPrefWifiMacAddr(Context context) {
        return context != null ? context.getSharedPreferences("rgpref", 0).getString("wifi_mac_addr", "") : "";
    }

    public static void unzip(InputStream inputStream, String str) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            byte[] bArr = new byte[4096];
            File file = new File(str);
            file.mkdirs();
            file.setExecutable(true, false);
            file.setReadable(true, false);
            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    File file2 = new File(str + nextEntry.getName());
                    if (nextEntry.isDirectory()) {
                        file2.mkdirs();
                    } else {
                        file = file2.getParentFile();
                        if (!file.exists()) {
                            file.mkdirs();
                            file.setExecutable(true, false);
                            file.setReadable(true, false);
                        }
                        OutputStream fileOutputStream = new FileOutputStream(file2);
                        while (true) {
                            int read = zipInputStream.read(bArr);
                            if (read <= 0) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        fileOutputStream.close();
                    }
                    file2.setExecutable(true, false);
                    file2.setReadable(true, false);
                } else {
                    zipInputStream.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void unzip(InputStream inputStream, String str, String str2) {
        try {
            ZipEntry nextEntry;
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            byte[] bArr = new byte[4096];
            do {
                nextEntry = zipInputStream.getNextEntry();
                if (nextEntry == null) {
                    break;
                }
            } while (!nextEntry.getName().equals(str));
            File file = new File(str2);
            if (nextEntry.isDirectory()) {
                file.mkdirs();
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                    parentFile.setExecutable(true, false);
                    parentFile.setReadable(true, false);
                }
                OutputStream fileOutputStream = new FileOutputStream(file);
                while (true) {
                    int read = zipInputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                fileOutputStream.close();
            }
            file.setExecutable(true, false);
            file.setReadable(true, false);
            zipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean unzip(String str, String str2) {
        try {
            unzip(new FileInputStream(str), str2);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 执行命令并且输出结果
     */
    public static String exeCmd(String cmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("sh");
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while ((line = dis.readLine()) != null) {

                result += line;
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.e("MainActivityCMD执行结果", result);
        return result;
    }

//    public static boolean unzip(String str, String str2, String str3) {
//        try {
//            C0001b c0001b = new C0001b(str);
//            if (c0001b.mo5a()) {
//                if (C1468c.m4056a(str3)) {
//                    c0001b.mo4a(str3.toCharArray());
//                } else {
//                    throw new NullPointerException();
//                }
//            }
//            c0001b.mo3a(str2);
//            return true;
//        } catch (C0010a e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    private static void writePrefWifiMacAddr(Context context, String str) {
        if (context != null) {
            Editor edit = context.getSharedPreferences("rgpref", 0).edit();
            edit.putString("wifi_mac_addr", str);
            edit.commit();
        }
    }

    public static String writeTo(String str, String str2) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        try {
            OutputStream fileOutputStream = new FileOutputStream(str2);
            fileOutputStream.write(str.getBytes(), 0, str.length());
            fileOutputStream.close();
            return str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return str;
        } catch (IOException e2) {
            e2.printStackTrace();
            return str;
        }
    }
}
