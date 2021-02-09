package com.idibros.studentclass.event;

import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class MessageReceiver {

    @SqsListener(value = "test_zipkin")
    public void receive() {

    }

}
