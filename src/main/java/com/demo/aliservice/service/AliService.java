package com.demo.aliservice.service;

import java.util.Map;

public interface AliService {
    boolean send(Map<String, Object> param, String phone);
}
