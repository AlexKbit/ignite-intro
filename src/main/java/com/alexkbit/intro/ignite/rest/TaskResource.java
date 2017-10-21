package com.alexkbit.intro.ignite.rest;

import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for tasks.
 */
@Slf4j
@Transactional
@RestController
@RequestMapping("tasks")
public class TaskResource {

    @Autowired
    private TaskService taskService;

    /**
     * Start new task.
     * @param expression math expression
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Task start(@RequestBody String expression) {
        log.info("Start new task for expression: {}", expression);
        return taskService.start(expression);
    }

    /**
     * Stop task.
     * @param taskId taskId
     */
    @RequestMapping(value = "/stop/{taskId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean stop(@PathVariable("taskId") String taskId){
        log.info("Stop task with id: {}", taskId);
        return taskService.stop(taskId);
    }

    /**
     * Remove task.
     * @param taskId taskId
     */
    @RequestMapping(value = "/{taskId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("taskId") String taskId){
        log.info("Remove task with id: {}", taskId);
        taskService.remove(taskId);
    }

    /**
     * Load list of task from page
     * @param page page number
     * @param count count tasks on page
     * @return {@link Page} of {@link Task}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Page<Task> load(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "count", defaultValue = "10") int count){
        Page<Task> loadedPage = taskService.load(page, count);
        log.debug("Load {} tasks from page: {} with count {}", loadedPage.getSize(), page, count);
        return loadedPage;
    }

}
