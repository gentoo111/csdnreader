package com.csdn.reader.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author 爱尔眼科 szz
 * @since 2020/1/14 9:48
 */
@Data
@TableName("t_csdn_topn")
public class CsdnTopN {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;
    @TableField(value="ranking")
    private String ranking;
    @TableField(value="name")
    private String name;
    @TableField(value="nowVotes")
    private String nowVotes;
    @TableField(value="createDate")
    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNowVotes() {
        return nowVotes;
    }

    public void setNowVotes(String nowVotes) {
        this.nowVotes = nowVotes;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
