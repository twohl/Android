package com.script.muhelp;

/**
 * Created by hongl on 2018/2/22.
 */

public enum ResultCode {
    /**
     * 返回状态码
     */

    REGIST_SUCCESS(200),                   //注册成功
    LOGIN_SUCCESS(201),                    //登陆成功
    GETDATA_SUCCESS(202),                  //获取列表等数据成功
    DATAUPDATE_SUCCESS(203),               //用户信息更新成功
    UPLOAD_SUCCESS(204),


    NWPUBLISH_SUCCESS(210),                 //notwork发布成功
    NWACC_SUCCESS(211),                     //notwork接取成功
    NWCANCLE_SUCCESS(212),                  //notwork取消成功
    EVALUATE_SUCCESS(213),                  //评价成功

    SHARE_SUCCESS(220),                     //分享成功

    SEND_MESSAGE_SUCCESS(230),              //发送信息成功

    /**
     * 异常信息码
     */

    NODATAINDB_EXCEPTION(300),              //未找到数据异常


    NORIGHTDATA_EXCEPTION(310),            //不正确的数据
    REPATE_THE_USERNAME(311),              //重复的用户名


    ACCNW_EXCEPTION(320),                   //不可接取notwork

    REPATE_CHATID(330),                     //重复的chatID

    ERROR_DATA_NULL(400),                    //没有相应的数据传入
    ERROR_INTEGER_FORMAT(401),              //错误的integer类型
    ERROR_DOUBLE_FORMAT(402),               //错误的double类型
    ERROR_DATE_FORMAT(403),                 //错误的时间类
    UPLOAD_FAILD(404),                      //上传失败

    PURVIEW_CER_EXCEPTION(500),             //权限验证异常
    NW_PURVIEW_CER_EXCEPTION(501),          //notWork权限验证异常













    UNKNOW_EXCEPTION(-1);                  //未定义异常

    int code;

    ResultCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

