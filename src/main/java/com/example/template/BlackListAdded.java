package com.example.template;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BlackListAdded extends AbstractEvent{

    private String stateMessage = "블랙리스트로 추가됨";
    private String customerName;

    public BlackListAdded(){
        this.setEventType(this.getClass().getSimpleName());
        SimpleDateFormat defaultSimpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
        this.timestamp = defaultSimpleDateFormat.format(new Date());
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
