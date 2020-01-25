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

    //设置一个今天常量
    private final Integer TODAY = 0;
    //设置一个昨天常量
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
        //查询今天最新201个博主投票数据
        List<Map<String, Object>> todayResult = csdnTopNMapper.getScrollBoard(TODAY);
        //查询昨天最后一次采集201个博主投票数据
        List<Map<String, Object>> yesTodayResult = csdnTopNMapper.getScrollBoard(YESTERDAY);
        //构造最终返回的集合
        List<Collection<Object>> arrayResult = new ArrayList<>();
        //对今天的数据进行遍历
        todayResult.forEach(today->{
            //因为前端最终需要的每个博主数据是个这样的数组：['168', '天元浪子', '-', '11456', '1686']
            //所以用一个集合来装每一个博主的投票数据
            Collection<Object> values = new ArrayList<>();
            //values.add(today.get("ranking"));
            //因为采集来的博客名字的格式为：168.天元浪子，需要进行处理，将序号和名字分开
            String name = today.get("name").toString();
            values.add(name.substring(0,name.indexOf(".")));
            values.add(name.substring(name.indexOf(".")+1));

            //遍历昨天最后一次采集的数据
            yesTodayResult.forEach(yesToday->{
                //拿到昨天数据的博客主名字
                String yesTodayName = yesToday.get("name").toString();
                //对比今天和昨天博客主名字，如果相等，说明是同一个博主，那就进行两天名次的相减
                if (name.equals(yesTodayName)) {
                    Integer todayRanking=Integer.parseInt(today.get("ranking").toString());
                    Integer yesTodayRanking=Integer.parseInt(yesToday.get("ranking").toString());
                    Integer i = todayRanking - yesTodayRanking;
                    //对名次的差进行处理，↑代表上升，↓代表下降，-代表名次未变
                    if (i != 0) {
                        values.add(i > 0 ? "↑" + i : "↓" + Math.abs(i));
                    } else {
                        values.add("-");
                    }
                }
            });

            //装该博主的实时票数和最近采集时间
            values.add(today.get("nowVotes"));
            values.add(today.get("createDate"));
            //用集合来装每一个博主的集合数据
            arrayResult.add(values);
        });

        //获取前20名博主数据返回给前端
        return arrayResult.subList(0, 20);
    }

    @Override
    public Map<String, Object> DigitalFlop() throws Exception{
        return schedulingTest.doGetCsdnTopNUrl();
    }

    @Override
    public Object doPostCsdnTopNIncrement() throws Exception {
        //获取72小时增量投票数据
        Object o = schedulingTest.doPostCsdnTopNIncrement();
        //使用fastjson转换为CsdnTopNResult对象
        CsdnTopNResult result = JSONObject.parseObject((String) o, CsdnTopNResult.class);
        //获取结果对象中的433个采集时间点
        List<String> date = result.getDate();
        //因为数据量巨大，我们取最近的20个时间点，相当于近200分钟
        result.setDate(date.subList(date.size()-20,date.size()));
        date = result.getDate();
        //将时间2020-01-20 21:53:28处理为21:53，x轴显示不了太多内容，所以需要进行截取
        List<String> dateFinal = new ArrayList<>();
        for (String s : date) {
            String[] split = s.split(" ");
            String substring = split[1].substring(0, 5);
            dateFinal.add(substring);
        }
        //将处理好的时间点设置到返回值中
        result.setDate(dateFinal);

        //得到前20个博主的得票数据
        List<CsdnTopNResult.Ticket> ticket = result.getTicket();
        //取前十名博主数据
        ticket = ticket.subList(0, 10);
        //我们页面需要博主的名字数组，所以这里处理一下，直接将名字放在一个集合里，这样前端就可以直接使用了
        List<String> name = new ArrayList<>();
        //与上面的时间点对应，取每个博主最近的20个时间点的增量数据
        for (CsdnTopNResult.Ticket t : ticket) {
            List<Integer> data = t.getData();
            t.setData(data.subList(data.size()-20,data.size()));
            name.add(t.getName());
        }
        //设置名字集合
        result.setName(name);
        //设置投票集合
        result.setTicket(ticket);
        return result;
    }

    @Override
    public Object doPostCsdnTop20() throws Exception {
        //爬取投票数据
        Object o = schedulingTest.doPostCsdnTop20();
        //封装到结果集对象中
        CsdnTopNResult result = JSONObject.parseObject((String) o, CsdnTopNResult.class);

        //获取采集时间点
        List<String> date = result.getDate();
        //获取近24小时的时间点，因为是10分钟一次，所以我们要144个值
        date = date.subList(date.size() - 144, date.size());
        //2020-01-20 21:53:28
        //对于144个数据值，我们每隔6个值取一个，处理成小时格式，正好1个小时取一个
        List<String> dateFinal = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            if (i%6==0) {
                String[] split = date.get(i).split(" ");
                String substring = split[1].substring(0, 2);
                dateFinal.add(substring);
            }
        }
        //将处理完的时间值处理完后重新设置到结果集中
        result.setDate(dateFinal);

        //获取TOP20投票对象集合
        List<CsdnTopNResult.Ticket> ticket = result.getTicket();
        //只取前十名
        ticket = ticket.subList(0, 10);
        //新建集合，封装前十名的博主名字
        List<String> name = new ArrayList<>();
        //遍历博主的投票对象
        for (int i = 0; i < ticket.size(); i++) {
            CsdnTopNResult.Ticket t = ticket.get(i);
            //获取博主的投票数集合
            List<Integer> data = t.getData();
            //取最近的144个数值，跟上面时间的处理一样，我们每隔6个取一个值，这样也正好能跟上面的时间对应上
            data = data.subList(data.size() - 144, data.size());
            List<Integer> dataFinal = new ArrayList<>();
            for (int j = 0; j < data.size(); j++) {
                if (j%6==0) {
                    dataFinal.add(data.get(j));
                }
            }
            //将处理好的数值重新设置到投票数结果中
            t.setData(dataFinal);
            //封装博主姓名到集合中
            name.add(t.getName());
        }

        result.setName(name);
        result.setTicket(ticket);
        return result;
    }

}
