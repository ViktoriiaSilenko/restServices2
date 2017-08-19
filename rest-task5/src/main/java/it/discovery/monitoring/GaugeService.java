package it.discovery.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

@Service
public class GaugeService {

    @Autowired
    private CounterService counterService;


    public CounterService getCounterService() {
        return counterService;
    }
}
