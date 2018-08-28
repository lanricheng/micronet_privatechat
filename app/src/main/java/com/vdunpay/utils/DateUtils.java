package com.vdunpay.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by HY on 2018/8/10.
 */

public class DateUtils {

    public static List<String> getData() {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");//日期格式化 年月日
        SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE");//日期格式化 星期几
        SimpleDateFormat twentyFourMinuteTimeFormat = new SimpleDateFormat("HH:mm:ss");//日期格式化 小时：分：秒 HH代表24小时制
        SimpleDateFormat twentyFourTimeFormat = new SimpleDateFormat("HH:mm");//日期格式化 小时：分
        SimpleDateFormat twelveMinuteTimeFormat = new SimpleDateFormat("hh:mm:ss");//日期格式化 小时：分：秒  hh代表12小时制
        SimpleDateFormat twelveTimeFormat = new SimpleDateFormat("hh:mm");//日期格式化 小时：分
        return null;
    }

    public static String getNowDate() {
        Date date = new Date();
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss");//日期格式化 年月日
        return mFormat.format(date);
    }


    public static Boolean compareTime(Date updateDate) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();
        c1.setTime(updateDate);//要判断的日期
        c2.setTime(new Date());//初始日期
        c3.setTime(new Date());//也给初始日期 把分钟加五
        c3.add(Calendar.MINUTE, 1);
        c2.add(Calendar.MINUTE, -1);//减去五分钟
//        System.out.println("c1"+c1.getTime());
//        System.out.println("c2"+c2.getTime());
//        System.out.println("c3"+c3.getTime());
        if (c1.after(c2) && c1.before(c3)) {
            return true;
//            System.out.println("五分钟之内");
        } else {
            return false;
//            System.out.println("五分钟之外");
        }
    }

    public static Date formatData(String strData) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            strData = strData.substring(0, 4) + "-" + strData.substring(4, 6) + "-" + strData.substring(6, 8) + " " + strData.substring(8, 10) + ":" + strData.substring(10, 12) + ":" + strData.substring(12, 14);
            LogUtils.d("strData---" + strData);
            return myFormatter.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDataForDisplay(String strData) {
        Date date = new Date();
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date issueDate = null;
        try {
            issueDate = myFormatter.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = getDate();
        long currTime = date.getTime();
        long issueTime = issueDate.getTime();
        long diff = currTime - issueTime;
        diff = diff / 1000;//秒
        if (diff / 60 < 1) {
            return "刚刚";
        }

        if (diff / 60 > 1 && diff / 3600 <= 24) {
            return time + " " + formatter.format(issueDate);
        }

//        if (diff / 60 >= 1 && diff / 60 <= 60) {
////            return diff / 60 + "分钟前";
//        }
//        if (diff / 3600 > 0 && diff / 3600 <= 24) {
////            return diff / 3600 + "小时前";
////            return formatter.format(issueDate);
//        }
        if (diff / (3600 * 24) > 0 && diff / (3600 * 24) < 2) {
            return "昨天";
        }
        if (diff / (3600 * 24) > 1 && diff / (3600 * 24) < 3) {
            return "前天";
        }
        if (diff / (3600 * 24) > 2) {
            return formatter.format(issueDate);
        }
        return "";
    }


    private static String getDate() {
        Date d = new Date();
        if (d.getHours() < 11) {
            return "早上";
        } else if (d.getHours() < 13) {
            return "中午";
        } else if (d.getHours() < 18) {
            return "下午";
        } else if (d.getHours() < 24) {
            return "晚上";
        }
        return "";
    }

}
