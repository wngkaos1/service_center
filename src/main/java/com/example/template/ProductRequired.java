package com.example.template;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductRequired  extends AbstractEvent{

    private String stateMessage = "추가 물량이 필요함";

    private String productName ;

    public ProductRequired(){
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
