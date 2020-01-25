package com.csdn.reader.task;

import com.csdn.reader.entity.CsdnTopN;
import com.csdn.reader.service.CsdnTopNService;
import com.csdn.reader.util.Tool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author:
 * @description:528
 * @date: 2019-05-30 23:36
 */
@Component
public class SchedulingTest {
    private int i = 0;
    public static Set<String> allBlogUrls = new HashSet<>();
    @Autowired
    private CsdnTopNService csdnTopNService;

    private static final String csdnTopNVoteUrl = "http://m234140.nofollow.ax.mvote.cn/action/viewvotewxorderlist.html?voteguid=43ced329-3a4b-0a5d-a13c-f088cf8eafef";

    private static final String csdnTopNUrl = "http://m234140.nofollow.ax.mvote.cn/wxvote/43ced329-3a4b-0a5d-a13c-f088cf8eafef.html";

    private static final String csdnTopNIncrement="http://csdn.sdysit.com/Increment";
    private static final String csdnTop20="http://csdn.sdysit.com/top20";

    //拉取72小时10分钟投票增量
    public Object doPostCsdnTopNIncrement() throws Exception {
        String post = Tool.doPost(csdnTopNIncrement, null);
        return post;
    }

    //拉取72小时10分钟投票走势
    public Object doPostCsdnTop20() throws Exception {
        return Tool.doPost(csdnTop20, null);
    }

    //总体概况
    public Map<String, Object> doGetCsdnTopNUrl()throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        String body = Tool.doGet(csdnTopNUrl);
        Document document = Jsoup.parse(body);
        Elements votetitleEle = document.select(".votetitle");

        //博客主数
        Element blogernumEle = document.select(".wxvoteinfo .d2").first();
        //总投票数
        Elements allvotenumEle = document.select(".wxvoteinfo #wxallvotenum");
        //访问次数
        Elements allviewnumEle = document.select(".wxvoteinfo #wxallviewnum");

        resultMap.put("blogernum",blogernumEle.text());
        resultMap.put("allvotenum",allvotenumEle.text());
        resultMap.put("allviewnum",allviewnumEle.text());

        System.out.println("统计---采集完成");
        return resultMap;
    }

    //具体时间间隔，3600*1000也就是1小时执行一次
    //@Scheduled(fixedRate = 3600 * 1000)
    void getAllBlogUrl() throws Exception {
        allBlogUrls = Tool.getAllBlogUrl("m0_37609579");
    }

    //具体时间间隔，60*10000也就是10分钟执行一次
    //博主投票数，定时拉取存库
    @Scheduled(fixedRate = 300000)
    void doGetCsdnTopNVoteUrl() throws Exception {
        //模拟get请求
        String body = Tool.doGet(csdnTopNVoteUrl);
        //对获取到的页面字符串进行解析
        Document document = Jsoup.parse(body);
        //使用选择器获取table下的所有tr标签
        Elements trs = document.select("tr");
        //使用一个集合来装每一个博主的实时投票数据
        List<CsdnTopN> csdnTopNList = new ArrayList<>();
        //构建当次爬取的时间
        Date date = new Date();
        //对获取到的tr节点集合进行遍历
        for (int i = 0; i < trs.size(); i++) {
            //获取每个tr节点下的所有td节点
            Elements tds = trs.get(i).select("td");
            CsdnTopN csdnTopN = new CsdnTopN();
            //获取每个博主的名次信息，将第几名中的第和名这两个字符去掉，数据库只保留名次数字
            csdnTopN.setRanking(tds.get(0).text().substring(1,tds.get(0).text().length()-1));
            //获取博主名字
            csdnTopN.setName(tds.get(1).text());
            //获取实时票数并将票字去掉，只保留票数的数字
            csdnTopN.setNowVotes(tds.get(2).text().substring(0,tds.get(2).text().length()-1));
            csdnTopN.setCreateDate(date);
            //将博主的投票数据添加到集合中
            csdnTopNList.add(csdnTopN);
        }
        //使用MybatisPlus的方法批量保存210个博主的投票数字
        csdnTopNService.saveBatch(csdnTopNList);
        System.out.println(date.toString()+"投票数据---采集完成");
    }

    //具体时间间隔，60*10000也就是10分钟执行一次
    //@Scheduled(fixedRate = 1000)
    void doAddAllBlogReader() throws Exception {
        if (i > 10) {
            i = 10 / 0;
        }
        for (String url : allBlogUrls) {
            Tool.doGet(url);
        }
        i++;
        System.out.println("第" + i + "次访问");
    }
}
