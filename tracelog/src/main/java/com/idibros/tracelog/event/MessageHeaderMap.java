package com.idibros.tracelog.event;

import brave.propagation.Propagation;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import lombok.Builder;

import java.util.Map;

@Builder
public class MessageHeaderMap implements Propagation.Getter<Map<String, MessageAttributeValue>, String>, Propagation.Setter<Map<String, MessageAttributeValue>, String> {

    public MessageHeaderMap() {
    }

    @Override public void put(Map<String, MessageAttributeValue> request, String key, String value) {
        MessageAttributeValue messageAttributeValue = new MessageAttributeValue();
        request.put(key, messageAttributeValue.withDataType("String").withStringValue(value));
    }

    @Override public String get(Map<String, MessageAttributeValue> request, String key) {
        return String.valueOf(request.get(key));
    }

}
