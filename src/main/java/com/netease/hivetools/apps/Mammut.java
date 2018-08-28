package com.netease.hivetools.apps;

import com.netease.hivetools.mammut.PfHiveSite;
import com.netease.hivetools.mappers.MammutMapper;
import com.netease.hivetools.service.MyBatisUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.List;

/**
 * Created by hzliuxun on 16/11/11.
 */
public class Mammut {
  private static final Logger logger = Logger.getLogger(Mammut.class.getName());

  public static void main(String[] args) {
    PropertyConfigurator.configure("log4j.properties");

    MyBatisUtil.sourceName = "mammut";
    MammutMapper mammutMapper = new MammutMapper("mammut");
    List<PfHiveSite> list = mammutMapper.getPfHivesite();
  }
}
