package com.yhau.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.yhau.ad.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@Component
public class BinlogClient {
    private BinaryLogClient client;
    private final BinlogConfig binlogConfig;
    private final AggregationListener aggregationListener;

    @Autowired
    public BinlogClient(AggregationListener aggregationListener, BinlogConfig binlogConfig) {
        this.aggregationListener = aggregationListener;
        this.binlogConfig = binlogConfig;
    }

    public void connect() {
        new Thread(() -> {
            client = new BinaryLogClient(
                    binlogConfig.getHost(),
                    binlogConfig.getPort(),
                    binlogConfig.getUsername(),
                    binlogConfig.getPassword()
            );
            if (!StringUtils.isEmpty(binlogConfig.getBinlogName())
                    && !binlogConfig.getPosition().equals(-1L)) {
                client.setBinlogFilename(binlogConfig.getBinlogName());
                client.setBinlogPosition(binlogConfig.getPosition());
            }
            client.registerEventListener(aggregationListener);
            try {
                log.info("connecting to mysql start");
                client.connect();
                log.info("connecting to mysql done");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public void close() {
        try {
            client.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
