package com.csdn.reader.controller;

import com.csdn.reader.service.CsdnTopNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author szz
 * @since 2020/1/16 14:59
 */
@Controller
public class CsdnTopNController {

    @Autowired
    private CsdnTopNService csdnTopNService;

    @GetMapping("index")
    public String index() {
        return "/index";
    }

    //日增投票数量柱状图
    @PostMapping("getRankingBoard")
    @ResponseBody
    public List<Map<String, Object>> getDailyIncrRankingBoard() throws Exception{
        return csdnTopNService.getDailyIncrRankingBoard();
    }

    //投票情况
    @PostMapping("getScrollBoard")
    @ResponseBody
    public List<Collection<Object>> getScrollBoard() throws Exception{
        return csdnTopNService.getScrollBoard();
    }

    //整体情况
    @PostMapping("DigitalFlop")
    @ResponseBody
    public Map<String ,Object> DigitalFlop() throws Exception{
        return csdnTopNService.DigitalFlop();
    }

    //增量
    @PostMapping("doPostCsdnTopNIncrement")
    @ResponseBody
    public Object doPostCsdnTopNIncrement() throws Exception{
        return csdnTopNService.doPostCsdnTopNIncrement();
    }

    //趋势
    @PostMapping("doPostCsdnTop20")
    @ResponseBody
    public Object doPostCsdnTop20() throws Exception{
        return csdnTopNService.doPostCsdnTop20();
    }
}
