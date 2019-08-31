package com.yhau.ad.mysql.listener;

import com.yhau.ad.mysql.dto.BinlogRowData;

public interface Ilistener {
    void register();

    void onEvent(BinlogRowData eventData);
}
