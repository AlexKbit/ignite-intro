package com.alexkbit.intro.ignite.rest;

import com.alexkbit.intro.ignite.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Integration test for {@link StatResource}.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StatResourceTest {

    private static final String PATH = "/stat";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldGetNodeStat() throws Exception {
        mvc.perform(
                get(PATH + "/node")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetStatusStat() throws Exception {
        mvc.perform(
                get(PATH + "/status")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}