package com.idibros.tracelog.event;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class MessageListener {

    @Autowired
    private Tracing tracing;

    @SqsListener(value = "test_zipkin", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void doAction(String body, @Headers Map<String, Object> headers) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        Map injected = om.readValue(headers.get("injected").toString(), Map.class);

        MapEntry mapEntry = MapEntry.builder().build();
        Span nextSpan = tracing.tracer().newChild(tracing.propagation().extractor(mapEntry).extract(injected).context());
        try (Tracer.SpanInScope ws = tracing.tracer().withSpanInScope(nextSpan.start())) {

            nextSpan.name("doAction");
            nextSpan.kind(Span.Kind.CONSUMER)
                    .remoteServiceName("aws sqs")
                    .start();

            //TODO doAction
        } finally {

            nextSpan.finish();
        }
    }

}
