package com.xiyo.pfund.service;

import org.springframework.stereotype.Service;

import java.util.Map;


public interface SmsService {

    void send(String mobile, String templateCode, Map<String,Object> param);
}
