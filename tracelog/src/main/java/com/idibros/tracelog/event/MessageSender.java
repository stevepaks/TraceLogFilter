package com.idibros.tracelog.event;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MessageSender {

    @Autowired
    private Tracing tracing;

    @Autowired
    private Tracer tracer;

    @Autowired
    private AmazonSQSAsync amazonSQSAsync;

    public void send(String destination, String message) throws JsonProcessingException {
        Map<String, String> r = new HashMap<>();

        Span nextSpan = tracer.nextSpan();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(nextSpan.start())) {

            nextSpan.name("send");
            nextSpan.tag("destination", destination);
            nextSpan.tag("message", message);
            SendMessageRequest sendMessageRequest = new SendMessageRequest();
            sendMessageRequest.setQueueUrl(destination);
            sendMessageRequest.setMessageBody(message);

            MessageHeaderMap messageHeaderMap = MessageHeaderMap.builder().build();
            Map<String, MessageAttributeValue> messageAttributeMap = new HashMap<>();
            tracing.propagation().injector(messageHeaderMap).inject(tracer.currentSpan().context(), messageAttributeMap);
            sendMessageRequest.setMessageAttributes(messageAttributeMap);

            nextSpan.kind(Span.Kind.PRODUCER)
                    .remoteServiceName("aws sqs")
                    .start();

            amazonSQSAsync.sendMessage(sendMessageRequest);
        } finally {

            nextSpan.finish();
        }
    }

}
