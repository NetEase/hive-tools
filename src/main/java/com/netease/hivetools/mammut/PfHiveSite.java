package com.netease.hivetools.mammut;

/**
 * Created by hzliuxun on 16/11/11.
 */
public class PfHiveSite {
  public String id = "";
  public String name = "";
  public String email = "";
  public String cluster = "";
  public String xml = "";
  public String connectionURL = "";
  public String connectionUserName = "";
  public String connectionPassword = "";
  public String product = "";

  public String toString() {
    return "id=" + this.id + ", cluster=" + this.cluster + ", product=" + this.product + ", name=" + this.name
         + ", email=" + this.email + ", connectionURL=" + this.connectionURL
        + ", connectionUserName=" + this.connectionUserName + ", connectionPassword=" + this.connectionPassword;
  }
}
