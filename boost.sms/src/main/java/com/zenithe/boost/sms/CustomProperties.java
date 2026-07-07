/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.boot.context.properties.ConfigurationProperties
 *  org.springframework.context.annotation.Configuration
 */
package com.zenithe.boost.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="sms.api")
public class CustomProperties {
    private String hostname;
    private String expirations;
    private String expirations04;
    private String expirations30;
    private String serveSms;
    private String user;
    private String password;
    private String senderid;
    private String sms_production;
    private String temoin1;
    private String temoin2;

    public String getHostname() {
        return this.hostname;
    }

    public String getExpirations() {
        return this.expirations;
    }

    public String getExpirations04() {
        return this.expirations04;
    }

    public String getExpirations30() {
        return this.expirations30;
    }

    public String getServeSms() {
        return this.serveSms;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSenderid() {
        return this.senderid;
    }

    public String getSms_production() {
        return this.sms_production;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setExpirations(String expirations) {
        this.expirations = expirations;
    }

    public void setExpirations04(String expirations04) {
        this.expirations04 = expirations04;
    }

    public void setExpirations30(String expirations30) {
        this.expirations30 = expirations30;
    }

    public void setServeSms(String serveSms) {
        this.serveSms = serveSms;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public void setSms_production(String sms_production) {
        this.sms_production = sms_production;
    }

    public String getTemoin1() { return this.temoin1; }
    public String getTemoin2() { return this.temoin2; }
    public void setTemoin1(String temoin1) { this.temoin1 = temoin1; }
    public void setTemoin2(String temoin2) { this.temoin2 = temoin2; }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CustomProperties)) {
            return false;
        }
        CustomProperties other = (CustomProperties)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$hostname = this.getHostname();
        String other$hostname = other.getHostname();
        if (this$hostname == null ? other$hostname != null : !this$hostname.equals(other$hostname)) {
            return false;
        }
        String this$expirations = this.getExpirations();
        String other$expirations = other.getExpirations();
        if (this$expirations == null ? other$expirations != null : !this$expirations.equals(other$expirations)) {
            return false;
        }
        String this$expirations04 = this.getExpirations04();
        String other$expirations04 = other.getExpirations04();
        if (this$expirations04 == null ? other$expirations04 != null : !this$expirations04.equals(other$expirations04)) {
            return false;
        }
        String this$expirations30 = this.getExpirations30();
        String other$expirations30 = other.getExpirations30();
        if (this$expirations30 == null ? other$expirations30 != null : !this$expirations30.equals(other$expirations30)) {
            return false;
        }
        String this$serveSms = this.getServeSms();
        String other$serveSms = other.getServeSms();
        if (this$serveSms == null ? other$serveSms != null : !this$serveSms.equals(other$serveSms)) {
            return false;
        }
        String this$user = this.getUser();
        String other$user = other.getUser();
        if (this$user == null ? other$user != null : !this$user.equals(other$user)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$senderid = this.getSenderid();
        String other$senderid = other.getSenderid();
        if (this$senderid == null ? other$senderid != null : !this$senderid.equals(other$senderid)) {
            return false;
        }
        String this$sms_production = this.getSms_production();
        String other$sms_production = other.getSms_production();
        return !(this$sms_production == null ? other$sms_production != null : !this$sms_production.equals(other$sms_production));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CustomProperties;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $hostname = this.getHostname();
        result = result * 59 + ($hostname == null ? 43 : $hostname.hashCode());
        String $expirations = this.getExpirations();
        result = result * 59 + ($expirations == null ? 43 : $expirations.hashCode());
        String $expirations04 = this.getExpirations04();
        result = result * 59 + ($expirations04 == null ? 43 : $expirations04.hashCode());
        String $expirations30 = this.getExpirations30();
        result = result * 59 + ($expirations30 == null ? 43 : $expirations30.hashCode());
        String $serveSms = this.getServeSms();
        result = result * 59 + ($serveSms == null ? 43 : $serveSms.hashCode());
        String $user = this.getUser();
        result = result * 59 + ($user == null ? 43 : $user.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $senderid = this.getSenderid();
        result = result * 59 + ($senderid == null ? 43 : $senderid.hashCode());
        String $sms_production = this.getSms_production();
        result = result * 59 + ($sms_production == null ? 43 : $sms_production.hashCode());
        return result;
    }

    public String toString() {
        return "CustomProperties(hostname=" + this.getHostname() + ", expirations=" + this.getExpirations() + ", expirations04=" + this.getExpirations04() + ", expirations30=" + this.getExpirations30() + ", serveSms=" + this.getServeSms() + ", user=" + this.getUser() + ", password=" + this.getPassword() + ", senderid=" + this.getSenderid() + ", sms_production=" + this.getSms_production() + ")";
    }
}
