/*
 * Decompiled with CFR 0.152.
 */
package com.zenithe.boost.sms.dtos;

import java.io.Serializable;

public class ApiSms
implements Serializable {
    private String status;
    private String smsclientid;
    private String messageid;
    private String mobileno;
    private String errorcode;
    private String errordescription;
    private Integer total_sms_unit = 0;
    private Integer balance = 0;

    public String getStatus() {
        return this.status;
    }

    public String getSmsclientid() {
        return this.smsclientid;
    }

    public String getMessageid() {
        return this.messageid;
    }

    public String getMobileno() {
        return this.mobileno;
    }

    public String getErrorcode() {
        return this.errorcode;
    }

    public String getErrordescription() {
        return this.errordescription;
    }

    public Integer getTotal_sms_unit() {
        return this.total_sms_unit;
    }

    public Integer getBalance() {
        return this.balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSmsclientid(String smsclientid) {
        this.smsclientid = smsclientid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public void setErrordescription(String errordescription) {
        this.errordescription = errordescription;
    }

    public void setTotal_sms_unit(Integer total_sms_unit) {
        this.total_sms_unit = total_sms_unit;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ApiSms)) {
            return false;
        }
        ApiSms other = (ApiSms)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$total_sms_unit = this.getTotal_sms_unit();
        Integer other$total_sms_unit = other.getTotal_sms_unit();
        if (this$total_sms_unit == null ? other$total_sms_unit != null : !((Object)this$total_sms_unit).equals(other$total_sms_unit)) {
            return false;
        }
        Integer this$balance = this.getBalance();
        Integer other$balance = other.getBalance();
        if (this$balance == null ? other$balance != null : !((Object)this$balance).equals(other$balance)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$smsclientid = this.getSmsclientid();
        String other$smsclientid = other.getSmsclientid();
        if (this$smsclientid == null ? other$smsclientid != null : !this$smsclientid.equals(other$smsclientid)) {
            return false;
        }
        String this$messageid = this.getMessageid();
        String other$messageid = other.getMessageid();
        if (this$messageid == null ? other$messageid != null : !this$messageid.equals(other$messageid)) {
            return false;
        }
        String this$mobileno = this.getMobileno();
        String other$mobileno = other.getMobileno();
        if (this$mobileno == null ? other$mobileno != null : !this$mobileno.equals(other$mobileno)) {
            return false;
        }
        String this$errorcode = this.getErrorcode();
        String other$errorcode = other.getErrorcode();
        if (this$errorcode == null ? other$errorcode != null : !this$errorcode.equals(other$errorcode)) {
            return false;
        }
        String this$errordescription = this.getErrordescription();
        String other$errordescription = other.getErrordescription();
        return !(this$errordescription == null ? other$errordescription != null : !this$errordescription.equals(other$errordescription));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ApiSms;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $total_sms_unit = this.getTotal_sms_unit();
        result = result * 59 + ($total_sms_unit == null ? 43 : ((Object)$total_sms_unit).hashCode());
        Integer $balance = this.getBalance();
        result = result * 59 + ($balance == null ? 43 : ((Object)$balance).hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $smsclientid = this.getSmsclientid();
        result = result * 59 + ($smsclientid == null ? 43 : $smsclientid.hashCode());
        String $messageid = this.getMessageid();
        result = result * 59 + ($messageid == null ? 43 : $messageid.hashCode());
        String $mobileno = this.getMobileno();
        result = result * 59 + ($mobileno == null ? 43 : $mobileno.hashCode());
        String $errorcode = this.getErrorcode();
        result = result * 59 + ($errorcode == null ? 43 : $errorcode.hashCode());
        String $errordescription = this.getErrordescription();
        result = result * 59 + ($errordescription == null ? 43 : $errordescription.hashCode());
        return result;
    }

    public String toString() {
        return "ApiSms(status=" + this.getStatus() + ", smsclientid=" + this.getSmsclientid() + ", messageid=" + this.getMessageid() + ", mobileno=" + this.getMobileno() + ", errorcode=" + this.getErrorcode() + ", errordescription=" + this.getErrordescription() + ", total_sms_unit=" + this.getTotal_sms_unit() + ", balance=" + this.getBalance() + ")";
    }
}
