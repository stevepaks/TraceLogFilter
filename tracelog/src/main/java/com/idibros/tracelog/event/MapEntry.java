package com.idibros.tracelog.event;

import brave.propagation.Propagation;
import lombok.Builder;

import java.util.Map;

@Builder
public class MapEntry implements Propagation.Getter<Map<String, String>, String>, Propagation.Setter<Map<String, String>, String> {

    public MapEntry() {
    }

    @Override public void put(Map<String, String> request, String key, String value) {
        request.put(key, value);
    }

    @Override public String get(Map<String, String> request, String key) {
        return request.get(key);
    }

}
