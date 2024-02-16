package com.meesho.notification.controller;


import com.meesho.notification.models.dto.SmsRequestDTO;
import com.meesho.notification.models.entities.SmsRequest;
import com.meesho.notification.producers.SmsEventsProducer;
import com.meesho.notification.service.sms.BlacklistService;
import com.meesho.notification.service.sms.SmsService;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@TestPropertySource(locations = "classpath:test.env")
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SmsService smsService;

    @MockBean
    private SmsEventsProducer smsEventsProducer;

    @MockBean
    private BlacklistService blacklistService;

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void sendSmsEmptyBodyTest() throws Exception {
        mockMvc.perform(post("/api/v1/sms/send").with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body not found."));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void sendSmsWithNullDataTest() throws Exception {
        mockMvc.perform(post("/api/v1/sms/send")
                        .with(csrf().asHeader())
                        .contentType(ContentType.APPLICATION_JSON.toString())
                        .content("{}")
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Phone Number should not be empty."));
    }


    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void sendSmsMalformedBodyTest() throws Exception {
        SmsRequestDTO smsRequestDTO = new SmsRequestDTO();
        smsRequestDTO.setPhoneNumber("+919571805234s");
        smsRequestDTO.setMessage("Hey, there!!");
        mockMvc.perform(post("/api/v1/sms/send")
                        .with(csrf().asHeader())
                        .content(smsRequestDTO.toBytesArray())
                        .contentType("application/json")
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value(String.format("Invalid Indian mobile number pattern: %s", smsRequestDTO.getPhoneNumber())));
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    public void sendSmsTest() throws Exception {

        SmsRequestDTO smsRequestDTO = new SmsRequestDTO();
        smsRequestDTO.setPhoneNumber("+919571805234");
        smsRequestDTO.setMessage("Hey, there!!");

        SmsRequest smsRequest = new SmsRequest(smsRequestDTO.getPhoneNumber(), smsRequestDTO.getMessage());

        Answer<SmsRequest> answer = invocation -> {
            smsRequest.setId(1L);
            smsRequest.setCreatedAt(LocalDateTime.now());
            smsRequest.setUpdatedAt(LocalDateTime.now());
            return smsRequest;
        };

        given(smsService.saveSmsRequest(any(SmsRequest.class))).willAnswer(answer);

        mockMvc.perform(post("/api/v1/sms/send")
                        .with(csrf().asHeader())
                        .content(smsRequestDTO.toBytesArray())
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1L));

        verify(smsEventsProducer).sendSmsEvent(smsRequest);
    }
}
