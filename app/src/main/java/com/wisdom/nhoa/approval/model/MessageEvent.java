package com.wisdom.nhoa.approval.model;

/**
 * @authorzhanglichao
 * @date2018/7/16 16:31
 * @package_name com.wisdom.nhoa.approval.model
 */
public class MessageEvent {
    private String message;
    public MessageEvent(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}

