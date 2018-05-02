package org.woehlke.twitterwall.frontend.controller;


import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.woehlke.twitterwall.Application;
import org.woehlke.twitterwall.oodm.service.UserListService;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class},webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserListControllerTest {

    private static final Logger log = LoggerFactory.getLogger(UserListControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserListController controller;

    @Autowired
    private UserListService userListService;


    @Commit
    @Test
    public void test001controllerIsPresentTest(){
        log.debug("controllerIsPresentTest");
        assertThat(controller).isNotNull();
    }

    @WithMockUser
    @Commit
    @Test
    public void test002getAllTest() throws Exception {
        String msg ="test002getAllTest: ";
        log.debug(msg+"------------------------------------");
        String url = "/userlist/all";
        log.info(msg+url);
        MvcResult result = this.mockMvc.perform(get(url))
            .andExpect(status().isOk())
            .andExpect(view().name("userlist/all"))
            .andExpect(model().attributeExists("myPageContent"))
            .andExpect(model().attributeExists("page"))
            .andReturn();

        String content = result.getResponse().getContentAsString();

        log.debug(msg+"#######################################");
        log.debug(msg+"#######################################");
        log.debug(msg+content);
        log.debug(msg+"#######################################");
        log.debug(msg+"#######################################");
        Assert.assertTrue(true);
    }

    //TODO: #252 https://github.com/phasenraum2010/twitterwall2/issues/252
    @WithMockUser
    @Commit
    @Test
    public void test003getUserListForIdTest() throws Exception {
        String msg ="test003getUserListForIdTest: ";
        log.debug(msg+"------------------------------------");
        //String url = "/userlist/all";
        //log.info(msg+url);
        Assert.assertTrue(true);
    }


}
