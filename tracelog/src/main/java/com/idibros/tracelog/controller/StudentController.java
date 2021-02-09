package com.idibros.tracelog.controller;

import com.idibros.tracelog.event.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("students")
public class StudentController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MessageSender messageSender;

    @GetMapping("{studentId}")
    public ResponseEntity<String> getStudent(@PathVariable("studentId") Long studentId) throws IOException {

        messageSender.send("test_zipkin", "foo");

        String studentClass = restTemplate.exchange("http://studentclass/studentclass/{studentClassId}", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        }, 1).getBody();

        return ResponseEntity.of(Optional.ofNullable("Hello, idibros, " + studentClass));
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
