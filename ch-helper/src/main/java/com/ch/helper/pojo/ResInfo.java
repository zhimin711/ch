package com.ch.helper.pojo;

/**
 * 执行结果
 *
 * @author zhimin
 * @version 1.0
 */
public class ResInfo {
    private int exitStatus;//返回状态码 （在linux中可以通过 echo $? 可知每步执行令执行的状态码）
    private String outRes;//标准正确输出流内容
    private String errRes;//标准错误输出流内容


    public ResInfo(int exitStatus, String outRes, String errRes) {
        super();
        this.exitStatus = exitStatus;
        this.outRes = outRes;
        this.errRes = errRes;
    }

    public ResInfo() {
        super();
    }

    public int getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(int exitStatus) {
        this.exitStatus = exitStatus;
    }

    public String getOutRes() {
        return outRes;
    }

    public void setOutRes(String outRes) {
        this.outRes = outRes;
    }

    public String getErrRes() {
        return errRes;
    }

    public void setErrRes(String errRes) {
        this.errRes = errRes;
    }

    /**
     * 当exitStuts=0 && errRes="" &&outREs=""返回true
     *
     * @return
     */
    public boolean isEmptySuccess() {
        if (this.getExitStatus() == 0 && "".equals(this.getErrRes()) && "".equals(this.getOutRes())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String tmp = outRes;
        if (outRes != null) {
            String[] strArr = outRes.split("\n");
            tmp = strArr[0];
            if(strArr.length>1) tmp+= " .....";
        }
        return "ResInfo [exitStatus=" + exitStatus + ", outRes=" + tmp + ", errRes=" + errRes + "]";
    }

    public void clear() {
        exitStatus = 0;
        outRes = errRes = null;
    }
}
