/*
 * Decompiled with CFR 0.152.
 */
package com.zenithe.boost.sms.dtos;

import com.zenithe.boost.sms.dtos.ApiSms;
import java.io.Serializable;
import java.util.List;

public class ApiSmsRoot
implements Serializable {
    private Integer responsecode = 0;
    private String responsedescription;
    private String responsemessage;
    private List<ApiSms> sms;

    public Integer getResponsecode() {
        return this.responsecode;
    }

    public String getResponsedescription() {
        return this.responsedescription;
    }

    public String getResponsemessage() {
        return this.responsemessage;
    }

    public List<ApiSms> getSms() {
        return this.sms;
    }

    public void setResponsecode(Integer responsecode) {
        this.responsecode = responsecode;
    }

    public void setResponsedescription(String responsedescription) {
        this.responsedescription = responsedescription;
    }

    public void setResponsemessage(String responsemessage) {
        this.responsemessage = responsemessage;
    }

    public void setSms(List<ApiSms> sms) {
        this.sms = sms;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ApiSmsRoot)) {
            return false;
        }
        ApiSmsRoot other = (ApiSmsRoot)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$responsecode = this.getResponsecode();
        Integer other$responsecode = other.getResponsecode();
        if (this$responsecode == null ? other$responsecode != null : !((Object)this$responsecode).equals(other$responsecode)) {
            return false;
        }
        String this$responsedescription = this.getResponsedescription();
        String other$responsedescription = other.getResponsedescription();
        if (this$responsedescription == null ? other$responsedescription != null : !this$responsedescription.equals(other$responsedescription)) {
            return false;
        }
        String this$responsemessage = this.getResponsemessage();
        String other$responsemessage = other.getResponsemessage();
        if (this$responsemessage == null ? other$responsemessage != null : !this$responsemessage.equals(other$responsemessage)) {
            return false;
        }
        List<ApiSms> this$sms = this.getSms();
        List<ApiSms> other$sms = other.getSms();
        return !(this$sms == null ? other$sms != null : !((Object)this$sms).equals(other$sms));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ApiSmsRoot;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $responsecode = this.getResponsecode();
        result = result * 59 + ($responsecode == null ? 43 : ((Object)$responsecode).hashCode());
        String $responsedescription = this.getResponsedescription();
        result = result * 59 + ($responsedescription == null ? 43 : $responsedescription.hashCode());
        String $responsemessage = this.getResponsemessage();
        result = result * 59 + ($responsemessage == null ? 43 : $responsemessage.hashCode());
        List<ApiSms> $sms = this.getSms();
        result = result * 59 + ($sms == null ? 43 : ((Object)$sms).hashCode());
        return result;
    }

    public String toString() {
        return "ApiSmsRoot(responsecode=" + this.getResponsecode() + ", responsedescription=" + this.getResponsedescription() + ", responsemessage=" + this.getResponsemessage() + ", sms=" + this.getSms() + ")";
    }
}
