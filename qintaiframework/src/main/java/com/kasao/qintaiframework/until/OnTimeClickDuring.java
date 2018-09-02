package com.kasao.qintaiframework.until;


/**
 * Created by suochunming
 * <p>
 * on 2017/11/1.
 * des: 防止连续点击 多次出现 对话框
 */

public class OnTimeClickDuring {
    private  long currentTime;
    private  long durning=1500;
    private long dueingChange=1000;

    private static OnTimeClickDuring mOnTimeClick;
    private OnTimeClickDuring(){}
    public static OnTimeClickDuring getInstance(){
        if(mOnTimeClick==null){
            mOnTimeClick=new OnTimeClickDuring();
        }
        return mOnTimeClick;
    }
    public  boolean onTickTime(long time){
      boolean ableClick= time-currentTime>durning;
        if(ableClick){
            currentTime=time;
            return true;
        }else{
            currentTime=time;
        }
        return false;
    }
    public  boolean onTickTimeChange(long time){
        boolean ableClick= time-currentTime>dueingChange;
        if(ableClick){
            currentTime=time;
            return true;
        }else{
            currentTime=time;
        }
        return false;
    }
    public  boolean onTickTimeChange(long time,long wait){
        boolean ableClick= time-currentTime>wait;
        if(ableClick){
            currentTime=time;
            return true;
        }else{
            currentTime=time;
        }
        return false;
    }
    public  boolean onTickTimeLoad(long time){
        boolean ableClick= time-currentTime>1000;
        if(ableClick){
            currentTime=time;
            return true;
        }else{
            currentTime=time;
        }
        return false;
    }
}
