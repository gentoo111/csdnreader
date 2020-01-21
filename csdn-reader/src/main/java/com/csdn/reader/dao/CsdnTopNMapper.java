package com.csdn.reader.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csdn.reader.entity.CsdnTopN;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface CsdnTopNMapper extends BaseMapper<CsdnTopN> {

    List<Map<String, Object>> getDailyIncrRankingBoard(@Param("day") Integer day);
    List<Map<String, Object>> getScrollBoard(@Param("day") Integer day);
}
