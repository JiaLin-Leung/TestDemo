package jiangsu.tbkt.teacher.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import jiangsu.tbkt.teacher.application.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Description:
 * @FileName: FileUtils.java
 * @Package: com.tbkt.teacher_eng.util
 * @Author: zhangzl
 * @Date: 2015/4/13
 * @Version V1.0
 */
public class FileUtils {


    private static final String FILE_ROOT = Environment.getExternalStorageDirectory() + "/RCB";//根目录
    public static File getFile(String filename) {
        File file = new File(FILE_ROOT, filename);
        file.getParentFile().mkdirs();
        return file;
    }


    /**
     * 判断SD卡是否存在
     * @return
     */
    public static boolean isSDExist() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 创建目录或文件
     * @param path 文件路径
     */
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 解压assets的zip压缩文件到指定目录
     * @param context 上下文对象
     * @param assetName 压缩文件名
     * @param outputDirectory 输出目录
     * @param isReWrite 是否覆盖
     * @throws IOException
     */
    public static void unZip(Context context, String assetName, String outputDirectory, boolean isReWrite) throws IOException {
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 打开压缩文件
        InputStream inputStream = context.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    /**
     * SD卡的空闲大小是否大于sizeMb
     *
     * @param sizeMb
     *            指定的大小（M）
     * @return
     */
    public static boolean isAvaiableSpace(int sizeMb) {

        boolean ishasSpace = false;

        if (!TextUtils.isEmpty(MyApplication.getInstance().ROOT_PATH)) {
            String sdcard = MyApplication.getInstance().ROOT_PATH;// Environment.getExternalStorageDirectory().getPath();

            StatFs statFs = new StatFs(sdcard);

            long blockSize = statFs.getBlockSize();

            long blocks = statFs.getAvailableBlocks();

            long availableSpare = (blocks * blockSize) / (1024 * 1024);
            if (availableSpare >= sizeMb) {

                ishasSpace = true;
            }
        }
        return ishasSpace;
    }

    /**
     * 删除文件
     * @param path 目录路径
     */
    public static void deleteFiles(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (int i=0;i<fileList.length;i++) {
                File f = fileList[i];
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    public static void saveBitmap2File(Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap scaleBitmap(Bitmap bm, int width, int height) {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / bmWidth, (float) height / bmHeight);
        return Bitmap.createBitmap(bm, 0, 0, bmWidth, bmHeight, matrix, true);
    }
}
