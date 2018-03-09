package jiangsu.tbkt.teacher.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 日期工具类
 * @FileName: DateUtils.java
 * @Package: com.tbkt.studentpad.utils
 * @Author: zhangzl
 * @Date: 2015/5/11
 * @Version V1.0
 */
public class DateUtils {
    public static String YYYYMMDDHHMM = "yyyyMMddHHmm";

    /**
     * 格式化日期
     * @param format
     * @return
     */
    public static String getNowTimeWithFormat(String format){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }

    /**
     * 格式化日期
     * @param date
     * @param format
     * @return
     */
    public static String parseDate2Str(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 日期字符串转为日期类型
     * @param date
     * @return
     */
    public static long stringToDate(String date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long mTime = new Date().getTime();
        try {
            Date dBefore = sf.parse(date);
            if (dBefore != null) {
                mTime = dBefore.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mTime;
    }
}
