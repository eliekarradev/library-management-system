package com.libraryMS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import libraryMS.ApplicationRunSuccessfullyEvent;
import libraryMS.Starter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * @author Hossam Al-masto
 * Created on 2022-03-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public abstract class AbstractTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationContext;
//
//    @BeforeEach
//    protected void setUp() {
////        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }

    protected <T> T strToObj(String obj, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(obj, clazz);
    }
}
