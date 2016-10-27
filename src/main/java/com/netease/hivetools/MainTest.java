package com.netease.hivetools;


import com.netease.hivetools.service.MyBatisUtil;

public class MainTest {
  public static void main(String[] args) {
    MyBatisUtil.sourceName = "hive_music";
    MyBatisUtil.destName = "hive_edu";



//    PartitionService partitionService  = new PartitionService();
//    Partition p = new Partition("/tmp", 1, "/tmp1:||:tmp2");
//    partitionService.insertPartition(p);
//    Partition po = partitionService.getPartitionByDir("/tmp");
//    System.out.println(po.getDir());
//    partitionService.deletePartition("/tmp");

    System.out.println();
  }
}
