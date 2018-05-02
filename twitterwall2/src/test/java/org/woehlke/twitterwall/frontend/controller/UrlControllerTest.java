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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.woehlke.twitterwall.Application;
import org.woehlke.twitterwall.frontend.controller.common.PrepareDataTest;
import org.woehlke.twitterwall.oodm.model.Url;
import org.woehlke.twitterwall.oodm.service.UrlService;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.woehlke.twitterwall.frontend.content.ContentFactory.FIRST_PAGE_NUMBER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class},webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UrlControllerTest {

    private static final Logger log = LoggerFactory.getLogger(UrlControllerTest.class);

    @Autowired
    private UrlController controller;

    @Autowired
    private UrlService urlService;

    @Autowired
    private PrepareDataTest prepareDataTest;

    @Autowired
    private MockMvc mockMvc;

    @Commit
    @Test
    public void test001controllerIsPresentTest(){
        log.debug("controllerIsPresentTest");
        assertThat(controller).isNotNull();
    }

    @Commit
    @Test
    public void test002setupTestData() throws Exception {
        String msg = "setupTestData: ";
        prepareDataTest.getTestDataTweets(msg);
        prepareDataTest.getTestDataUser(msg);
        Assert.assertTrue(true);
    }

    @WithMockUser
    @Commit
    @Test
    public void test003getAllTest() throws Exception {
        String msg ="test003getAllTest: ";
        log.debug(msg+"------------------------------------");
        String url = "/url/all";
        log.info(msg+url);
        MvcResult result = this.mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(view().name("url/all"))
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

    @Commit
    @WithMockUser
    @Test
    public void test004getUrlById() throws Exception {
        String msg ="test004getUrlById: ";
        log.debug(msg+"------------------------------------");
        Url oneUrl = findOneUrl();
        if(oneUrl != null) {
            long id = oneUrl.getId();
            String url = "/url/" + id;
            log.info(msg+url);
            MvcResult result = this.mockMvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(view().name("url/id"))
                    .andExpect(model().attributeExists("users"))
                    .andExpect(model().attributeExists("latestTweets"))
                    .andExpect(model().attributeExists("url"))
                    .andExpect(model().attributeExists("page"))
                    .andReturn();

            String content = result.getResponse().getContentAsString();

            log.debug(msg + "#######################################");
            log.debug(msg + "#######################################");
            log.debug(msg + content);
            log.debug(msg + "#######################################");
            log.debug(msg + "#######################################");
            Assert.assertTrue(true);
        }
    }

    private Url findOneUrl() {
        int size = 1;
        Pageable pageRequest = new PageRequest(FIRST_PAGE_NUMBER, size);
        Page<Url> urlPage = urlService.getAll(pageRequest);
        if(urlPage.getContent().size()>0){
            return urlPage.getContent().iterator().next();
        } else {
            return null;
        }
    }

}
