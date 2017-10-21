package com.alexkbit.intro.ignite.rest;

import com.alexkbit.intro.ignite.model.TaskStatus;
import com.alexkbit.intro.ignite.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for statistics.
 */
@Slf4j
@Transactional
@RestController
@RequestMapping("stat")
public class StatResource {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * Statistics about nodes.
     */
    @RequestMapping(value = "/node", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, Integer> nodes() {
        return statisticsService.getNodeStat();
    }

    /**
     * Statistics about status.
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<TaskStatus, Integer> status() {
        return statisticsService.getStatusStat();
    }

}
