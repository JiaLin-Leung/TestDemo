package jiangsu.tbkt.teacher.utils;

import android.util.Log;

import jiangsu.tbkt.teacher.application.MyApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 日志类
 * @Description: 输入日志信息
 * @FileName: LogUtil.java
 * @Package com.tbkt.teacher_eng.utils
 * @Author zhangzl
 * @Date 2015-3-27
 * @Version V1.0
 */
public class LogUtil {

    /**
     * 显示日志
     * @param log
     */
    public static void showPrint(String log) {
        System.out.println(log);
    }

    /**
     * 显示错误日志
     * @param tag
     * @param log
     */
    public static void showError(Class<?> tag, String log) {
        Log.e(tag.getName(), log);
    }


    /**
     * 保存到文件中
     * @param html
     * @param fileName
     */
    public static void saveHtml(String html, String fileName) {
        FileWriter mFileWriter = null;
        try {
            File outFile = new File(MyApplication.getInstance().CACHE_HTML+fileName+".html");
            if (outFile.exists()) {
                outFile.delete();
            }
            outFile.createNewFile();

            mFileWriter = new FileWriter(outFile,true);
            mFileWriter.write(html);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (mFileWriter != null) {
                try {
                    mFileWriter.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
