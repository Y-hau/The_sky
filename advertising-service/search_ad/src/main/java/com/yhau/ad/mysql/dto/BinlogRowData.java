package com.yhau.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BinlogRowData {
    private TableTemplate table;
    private EventType eventType;
    private List<Map<String, String>> after;  //key操作列的名字，value操作列的值
    private List<Map<String, String>> before; //只有update操作有 before 数据
}
