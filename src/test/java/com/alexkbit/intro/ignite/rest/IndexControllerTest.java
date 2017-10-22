package com.alexkbit.intro.ignite.rest;

import com.alexkbit.intro.ignite.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for {@link IndexController}.
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class IndexControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldGetIndexOk() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk());
    }
    @Test
    public void shouldGetStatOk() throws Exception {
        mockMvc.perform(get("/stat"))
               .andExpect(status().isOk());
    }

}