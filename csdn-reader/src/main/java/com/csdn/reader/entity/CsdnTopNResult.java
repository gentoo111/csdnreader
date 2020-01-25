package com.csdn.reader.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author szz
 * @since 2020/1/20 16:45
 */
public class CsdnTopNResult {
    private List<String> date;
    private List<String> name;
    private Boolean res;
    private String msg;
    private BigDecimal maxcount;
    private BigDecimal mincount;
    private List<Ticket> ticket;

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public Boolean getRes() {
        return res;
    }

    public void setRes(Boolean res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BigDecimal getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(BigDecimal maxcount) {
        this.maxcount = maxcount;
    }

    public BigDecimal getMincount() {
        return mincount;
    }

    public void setMincount(BigDecimal mincount) {
        this.mincount = mincount;
    }

    public List<Ticket> getTicket() {
        return ticket;
    }

    public void setTicket(List<Ticket> ticket) {
        this.ticket = ticket;
    }

    public class Ticket {
        private String name;
        private List<Integer> data;
        private String type = "line";
        /*private Map<String, Object> lineArea;
        private Map<String, Object> lineStyle;
        private Map<String, Object> linePoint;*/

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }
}
