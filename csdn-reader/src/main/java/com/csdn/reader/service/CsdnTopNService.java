package com.csdn.reader.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.csdn.reader.entity.CsdnTopN;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface CsdnTopNService extends IService<CsdnTopN> {

    List<Map<String, Object>> getDailyIncrRankingBoard() throws Exception;

    List<Collection<Object>> getScrollBoard()throws Exception;

    Map<String, Object> DigitalFlop()throws Exception;

    Object doPostCsdnTopNIncrement()throws Exception;

    Object doPostCsdnTop20()throws Exception;
}
