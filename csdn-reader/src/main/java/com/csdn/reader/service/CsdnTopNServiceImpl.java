package com.csdn.reader.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csdn.reader.dao.CsdnTopNMapper;
import com.csdn.reader.entity.CsdnTopN;
import com.csdn.reader.entity.CsdnTopNResult;
import com.csdn.reader.task.SchedulingTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Service("csdnTopNService")
public class CsdnTopNServiceImpl extends ServiceImpl<CsdnTopNMapper, CsdnTopN> implements CsdnTopNService {

    private final Integer TODAY = 0;
    private final Integer YESTERDAY = 1;
    private final Integer DAY_BEFORE_YESTERDAY = 2;

    @Autowired
    SchedulingTest schedulingTest;

    @Resource
    private CsdnTopNMapper csdnTopNMapper;

    @Override
    public List<Map<String, Object>> getDailyIncrRankingBoard() throws Exception{

        List<Map<String, Object>> yestodayResult = csdnTopNMapper.getDailyIncrRankingBoard(YESTERDAY);
        List<Map<String, Object>> dayBeforeYesterdayResult = csdnTopNMapper.getDailyIncrRankingBoard(DAY_BEFORE_YESTERDAY);

        int i = 0;

        for (Map<String, Object> yestodayMap : yestodayResult) {
            for (Map<String, Object> dayBeforeYesterdayMap : dayBeforeYesterdayResult) {
                if (yestodayMap.get("name").equals(dayBeforeYesterdayMap.get("name"))) {
                    yestodayMap.put("value", Integer.parseInt((String) yestodayMap.get("value")) - Integer.parseInt((String) dayBeforeYesterdayMap.get("value")));
                }
            }
        }
        yestodayResult = yestodayResult.subList(0, 20);
        return yestodayResult;
    }

    //['168', '天元浪子', '-', '11456', '1686']
    //'编号', '博主', '升降', '总票数', '日增票数'
    @Override
    public List<Collection<Object>> getScrollBoard() {
        List<Map<String, Object>> todayResult = csdnTopNMapper.getScrollBoard(TODAY);
        List<Map<String, Object>> yesTodayResult = csdnTopNMapper.getScrollBoard(YESTERDAY);
        List<Collection<Object>> arrayResult = new ArrayList<>();
        todayResult.forEach(today->{
            Collection<Object> values = new ArrayList<>();
            String name = today.get("name").toString();

            //values.add(today.get("ranking"));
            values.add(name.substring(0,name.indexOf(".")));
            values.add(name.substring(name.indexOf(".")+1));

            yesTodayResult.forEach(yesToday->{
                String yesTodayName = yesToday.get("name").toString();
                if (name.equals(yesTodayName)) {
                    Integer todayRanking=Integer.parseInt(today.get("ranking").toString());
                    Integer yesTodayRanking=Integer.parseInt(yesToday.get("ranking").toString());
                    Integer i = todayRanking - yesTodayRanking;
                    if (i != 0) {
                        values.add(i > 0 ? "↑" + i : "↓" + Math.abs(i));
                    } else {
                        values.add("-");
                    }
                }
            });

            values.add(today.get("nowVotes"));
            values.add(today.get("createDate"));
            arrayResult.add(values);
        });

        return arrayResult.subList(0, 20);
    }

    @Override
    public Map<String, Object> DigitalFlop() throws Exception{
        return schedulingTest.doGetCsdnTopNUrl();
    }

    @Override
    public Object doPostCsdnTopNIncrement() throws Exception {
        Object o = schedulingTest.doPostCsdnTopNIncrement();
        CsdnTopNResult result = JSONObject.parseObject((String) o, CsdnTopNResult.class);
        List<String> date = result.getDate();
        result.setDate(date.subList(date.size()-20,date.size()));
        date = result.getDate();
        //2020-01-20 21:53:28
        List<String> dateFinal = new ArrayList<>();
        for (String s : date) {
            String[] split = s.split(" ");
            String substring = split[1].substring(0, 5);
            dateFinal.add(substring);
        }
        result.setDate(dateFinal);

        List<CsdnTopNResult.Ticket> ticket = result.getTicket();
        ticket = ticket.subList(0, 10);
        List<String> name = new ArrayList<>();
        for (CsdnTopNResult.Ticket t : ticket) {
            List<Integer> data = t.getData();
            t.setData(data.subList(data.size()-20,data.size()));
            name.add(t.getName());
        }
        result.setName(name);
        result.setTicket(ticket);
        return result;
    }

    @Override
    public Object doPostCsdnTop20() throws Exception {
        Object o = schedulingTest.doPostCsdnTop20();
        CsdnTopNResult result = JSONObject.parseObject((String) o, CsdnTopNResult.class);

        List<String> date = result.getDate();
        date = date.subList(date.size() - 144, date.size());
        //2020-01-20 21:53:28
        List<String> dateFinal = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            if (i%6==0) {
                String[] split = date.get(i).split(" ");
                String substring = split[1].substring(0, 2);
                dateFinal.add(substring);
            }
        }
        result.setDate(dateFinal);

        List<CsdnTopNResult.Ticket> ticket = result.getTicket();
        ticket = ticket.subList(0, 10);
        List<String> name = new ArrayList<>();
        for (int i = 0; i < ticket.size(); i++) {
            CsdnTopNResult.Ticket t = ticket.get(i);
            List<Integer> data = t.getData();
            data = data.subList(data.size() - 144, data.size());
            List<Integer> dataFinal = new ArrayList<>();
            for (int j = 0; j < data.size(); j++) {
                if (j%6==0) {
                    dataFinal.add(data.get(j));
                }
            }
            t.setData(dataFinal);
            name.add(t.getName());
        }

        result.setName(name);
        result.setTicket(ticket);
        return result;
    }

}
