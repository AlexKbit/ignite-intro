package com.alexkbit.intro.ignite.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.core.Is.is;

import com.alexkbit.intro.ignite.IntegrationTest;
import com.alexkbit.intro.ignite.model.Task;
import com.alexkbit.intro.ignite.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

/**
 * Integration test for {@link IndexController}.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TaskResourceTest {

    private static final String PATH = "/tasks";
    private static final String EXPRESSION = "1 + 1";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TaskService taskService;

    @Before
    public void setup() throws Exception {
        mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldStart() throws Exception {
        mvc.perform(
                post(PATH + "/start")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EXPRESSION))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.expression", is(EXPRESSION)));
    }

    @Test
    public void shouldStop() throws Exception {
        Task task = taskService.start(EXPRESSION);
        mvc.perform(
                get(PATH + "/stop/" + task.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void shouldNotFoundForStop() throws Exception {
        mvc.perform(
                get(PATH + "/stop/" + UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    public void shouldRemove() throws Exception {
        Task task = taskService.start(EXPRESSION);
        mvc.perform(
                delete(PATH + "/" + task.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotFoundForRemove() throws Exception {
        mvc.perform(
                delete(PATH + "/" + UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldLoad() throws Exception {
        mvc.perform(
                get(PATH + "?page=0&count=10")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}