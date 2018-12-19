package com.wisdom.nhoa.push;

/**
 * @authorzhanglichao
 * @date2018/8/2 14:44
 * @package_name com.wisdom.nhoa.push
 */
public class MsgCountEvent {
    private String message;
    public MsgCountEvent(String message){ this.message=message; }
    public String getMessage() { return message; }
    public void setMessage(String message)
    { this.message = message; }


}
