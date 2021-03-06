package com.ashlikun.utils.other.file;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;


/**
 * 作者　　: 李坤
 * 创建时间: 13:49 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：对文件操作的类
 */

public class FileUtils {
    /**
     * 文件是否存在
     *
     * @param file
     * @return
     */
    public static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    /**
     * 方法功能：获取清单文件的mate值
     */

    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 方法功能：读取输入流
     *
     * @return 文本
     */
    public static byte[] readByte(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败" .getBytes();
        }
    }

    public static byte[] readByte(String path) {
        try {
            return readByte(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "获取失败" .getBytes();
        }
    }


    /**
     * 判断文件夹是否为空
     */
    public static boolean isEmptyFolder(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            if (file.list().length > 0) {
                return false;
            }
        }
        return true;
    }

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return formetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return autoFormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private static long getFileSizes(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFileSizes(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double formetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("0.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 自动格式化单位
     *
     * @param size size
     * @return size
     */
    public static String autoFormetFileSize(double size) {
        //# 一个数字，不包括 0 , 0 一个数字
        DecimalFormat df = new DecimalFormat("0.#");
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return df.format(size) + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            return df.format(kiloByte) + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            return df.format(megaByte) + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            return df.format(gigaByte) + "GB";
        }
        return df.format(teraBytes) + "TB";
    }

    /**
     * 删除文件或者目录
     */
    public static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }

    /**
     * 删除文件或者目录
     */
    public static void deleteFile(String strFile) {
        deleteFile(new File(strFile));
    }
}