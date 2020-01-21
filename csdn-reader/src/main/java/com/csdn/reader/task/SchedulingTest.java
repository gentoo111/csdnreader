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
        String body = Tool.doGet(csdnTopNVoteUrl);
        Document document = Jsoup.parse(body);
        Elements tr = document.select("tr");
        List<CsdnTopN> csdnTopNList = new ArrayList<>();
        Elements trs = document.select("tr");
        Date date = new Date();
        for (int i = 0; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            CsdnTopN csdnTopN = new CsdnTopN();
            csdnTopN.setRanking(tds.get(0).text().substring(1,tds.get(0).text().length()-1));
            csdnTopN.setName(tds.get(1).text());
            csdnTopN.setNowVotes(tds.get(2).text().substring(0,tds.get(2).text().length()-1));
            csdnTopN.setCreateDate(date);
            csdnTopNList.add(csdnTopN);
        }
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
