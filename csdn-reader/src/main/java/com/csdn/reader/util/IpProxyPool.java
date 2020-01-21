package com.csdn.reader.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: szz
 * @Date: 2019/9/15 下午8:07
 * @Version 1.0
 */
public class IpProxyPool {
    public static void main(String[] args)  throws Exception {
        getIPPool();
    }

    public static void getIPPool() throws Exception {
        List<String> Urls = new ArrayList<>();
        List<DatabaseMessage> databaseMessages = new ArrayList<>();
        List<IPMessage> list = new ArrayList<>();
        List<IPMessage> ipMessages = new ArrayList<>();
        String url = "http://www.xicidaili.com/nn/1";
        String IPAddress;
        String IPPort;
        int k, j;

        //首先使用本机ip进行爬取
        list = URLFecter.urlParse(url, list);

        //对得到的IP进行筛选，选取链接速度前100名的
        list = IPFilter.Filter(list);

        //构造种子Url
        for (int i = 1; i <= 5; i++) {
            Urls.add("http://www.xicidaili.com/nn/" + i);
        }

        //得到所需要的数据
        for (k = 0, j = 0; j < Urls.size(); k++) {
            url = Urls.get(j);

            IPAddress = list.get(k).getIPAddress();
            IPPort = list.get(k).getIPPort();
            //每次爬取前的大小
            int preIPMessSize = ipMessages.size();
            ipMessages = URLFecter.urlParse(url, IPAddress, IPPort, ipMessages);
            //每次爬取后的大小
            int lastIPMessSize = ipMessages.size();
            if (preIPMessSize != lastIPMessSize) {
                j++;
            }

            //对IP进行轮寻调用
            if (k >= list.size()) {
                k = 0;
            }
        }

        //对得到的IP进行筛选，选取链接速度前100名的
        ipMessages = IPFilter.Filter(ipMessages);

        //对ip进行测试，不可用的从数组中删除
        ipMessages = IPUtils.IPIsable(ipMessages);

        for (IPMessage ipMessage : ipMessages) {
            System.out.println(ipMessage.getIPAddress());
            System.out.println(ipMessage.getIPPort());
            System.out.println(ipMessage.getServerAddress());
            System.out.println(ipMessage.getIPType());
            System.out.println(ipMessage.getIPSpeed());
        }

        //将得到的IP存储在数据库中(每次先清空数据库)
        DataBaseDemo.delete();
        DataBaseDemo.add(ipMessages);

        //从数据库中将IP取到
        databaseMessages = DataBaseDemo.query();

        for (DatabaseMessage databaseMessage : databaseMessages) {
            System.out.println(databaseMessage.getId());
            System.out.println(databaseMessage.getIPAddress());
            System.out.println(databaseMessage.getIPPort());
            System.out.println(databaseMessage.getServerAddress());
            System.out.println(databaseMessage.getIPType());
            System.out.println(databaseMessage.getIPSpeed());
        }
    }
}
