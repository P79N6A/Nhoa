package com.wisdom.nhoa;

import com.wisdom.nhoa.homepage.model.UrlModel;
import com.wisdom.nhoa.sendreceive.model.FileProcessingModel;
import com.wisdom.nhoa.widget.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa
 * @class describe：
 * @time 2018/1/30 15:11
 * @change
 */

public class ConstantString {
    public static String FORM_ID = "";
    public static Boolean LOGIN_STATE = true;
    //token过期后返回的code
    public static final int CODE_TOKEN_ILLEGAL = 20013;
    public static final int CODE_NO_FILE = 20013;
    public static final int CODE_NO_DATA = 30001;
    public static final int CODE_ACTIVITY_FINISH = 0x18;
    //判断是否是第一次进入适配器页面
    public static Boolean IS_ADAPTER_FIRST = true;
    //    群管理页面请求码
    public static final int MANAGMENT_REQUEST_CODE = 0x133;
    //    选择抄送人页面请求码
    public static final int COPY_TO_REQUEST_CODE = 0x134;
    public static final int COPY_TO_RESULT_CODE = 0x124;
    //刷新常用联系人
    public static final int CONTRACT_REFRESH = 0x19;

    //待办详情页面-文件处理单  没有公文正文，或者没有附件时候，返回的code
    public static final String FILE_NO_CONTENT_CODE = "20402";
    //拍照后图片缓存的地址
    public static String PIC_LOCATE = "";//拍照后图片缓存的地址
    //保存下载文件的本地地址
    public static final String SAVE_FILE_ADDR = "nhoa1/download";
    //扫码请求回调参数
    public static final int QRCODE_SCAN_REQUEST_CODE = 104;//扫描二维码授权回调标识
    //百度推送 key
    public static final String BAIDU_API_KEY = "NeFwaNTcNHpLOeZUkR8ra6Vy";//百度推送 key

    public static List<UrlModel> filelist;
    //上传签名图片
    public static final String UPLOAD_SIGN_FILE = "http://192.168.1.191:8081/hz_oa/tzxm/acceptOrder";

    public static String USERID = "";///推送用的用户Id，暂时新版没有啥用
    //本地存储sp文件的名称
    public static final String SHARE_PER_INFO = "nahe_oa_sp";//本地sp文件的存储名称
    //sp文件存储用户名
    public static final String USER_NAME = "userName";
    //sp文件存储密码
    public static final String PASS_WORD = "psw";
    //sp文件存储用户信息的key
    public static final String USER_INFO = "userInfo";
    //sp文件存储用户权限信息的key
    public static final String USER_PERMISSION = "userPermission";
    //    发送广播时候的insid过滤标识
    public static final String BROADCAST_INSID_TAG = "insid";
    //    发送广播时候是否显示签到按钮
    public static final String BROADCAST_SIGN_TAG = "sign";
    //    发送广播时候的通知刷新数据的过滤标识 refresh
    public static final String BROADCAST_REFRESH_TAG = "refresh";
    //    发送广播时候的关闭activity过滤标识
    public static final String BROADCAST_ACTIVITY_FINISH = "activityfinish";
    //    发送广播时候的更新最近联系人过滤标识
    public static final String BROADCAST_REFRESH_CONTRACT_LATELY = "refreshContract";
    //    发送广播时候的启动登录页过滤标识
    public static final String BROADCAST_START_ACTIVITY_LOGIN = "login";

    //    发送广播时候的关闭群设置activity过滤标识
    public static final String BROADCAST_GROUP_FINISH = "groupfinish";
    //    刷新左侧督办列表
    public static final String BROADCAST_REFRESH_SUPERVISION_LEFT = "refreshSupervisionLeft";
    //刷新右侧督办列表
    public static final String BROADCAST_REFRESH_SUPERVISION_RIGHT = "refreshSupervisionRight";

    //app key
    public static final String APP_KEY = "78A6EB180388D196363D872FDB1421A2";
    //会议签到状态-未签到
    public static final String METTING_UNSIGN = "0";
    //会议签到状态-已签到
    public static final String METTING_SIGN = "1";
    //群列表页面进行刷新的请求码
    public static final int REQUEST_CODE_REFRESH_DATA = 0x12;
    //公文传阅有关于群的内容，判断是否是创建者
    public static final String IS_CREATER_TRUE = "0";//是创建者
    public static final String IS_CREATER_FALSE = "1";//不是创建者
    //调取相册相机文件方面的参数常量
    public static final int REQUEST_CAMERA = 101;//调起相机
    public static final int ALBUM_SELECT_CODE = 102;//调起相册
    public static final int FILE_SELECT_CODE = 103;//调起文件选择
    //群管理界面判断是否已经勾选该成员
    public static final String MEMBER_IS_SELECTED_TRUE = "1";
    public static final String MEMBER_IS_SELECTED_FALSE = "0";
    //公文传阅详情页面 下载标识（1：已下载、0：未下载）
    public static final String DOCUMENT_DOWNLOAD_TRUE = "1";
    public static final String DOCUMENT_DOWNLOAD_FALSE = "0";
    //收发文管理列表 列表类型 我发起的
    public static final String SEND_RECEIVE_LIST_TYPE_MANAGE = "manage";
    //收发文管理列表 列表类型 我审批的
    public static final String SEND_RECEIVE_LIST_TYPE_HANDLE = "handle";
    //收发文管理列表 列表类型 收发文待办
    public static final String SEND_RECEIVE_LIST_TYPE_SEND = "send";
    //收发文管理列表 列表类型 收发文查阅
    public static final String SEND_RECEIVE_LIST_TYPE_COMPLETE = "complete";
    // 发文管理列表  公文类型  发文
    public static final String SEND_RECEIVE_DOC_TYPE_SEND = "send";//
    // 发文管理列表  公文类型  收文
    public static final String SEND_RECEIVE_DOC_TYPE_RECEIVE = "receive";//


    //待办详情页面-文件处理单   签字可编辑
    public static final String FILE_SIGN_TYPE_URLW = "urlw";
    //待办详情页面-文件处理单   签字只读
    public static final String FILE_SIGN_TYPE_URLR = "urlr";
    //待办详情页面-文件处理单   文本可编辑
    public static final String FILE_SIGN_TYPE_TEXTW = "textw";
    //待办详情页面-文件处理单   文本只读
    public static final String FILE_SIGN_TYPE_TEXTR = "textr";


    //首页待办详情页面     在办环节
    public static final String FILE_TYPE_UNDER_DO = "0";
    //首页待办详情页面    办结环节
    public static final String FILE_TYPE_COMPLETE = "1";
    //会议  是否是创建人  是
    public static final String METTING_IS_CREATER_TRUE = "1";
    //会议  是否是创建人  否
    public static final String METTING_IS_CREATER_FALSE = "0";

    //群组成员数量
    public static String GROUP_TOTAL_COUNT = "";//
    //发起申请的类型
    public static final String APPROVAL_TYPE_XJ = "xj";//休假
    public static final String APPROVAL_TYPE_CC = "cc";//出差
    public static final String APPROVAL_TYPE_YP = "yp";//办公用品
    //   发起申请的  0待审核 1审核通过 2审核退回
    public static final String APPROVAL_STATUS_CHECK = "0";//
    public static final String APPROVAL_STATUS_PASS = "1";//
    public static final String APPROVAL_STATUS_BACK = "2";//
    //统计用户一共选中了多少个抄送人的变量
    public static int COPY_TO_PERSON_TOTAL_COUNT = 0;
    //推送推送过来的消息类型代码
    public static final String PUSH_DETAIL_TAG_GWCY = "GWCY";//公文传阅
    public static final String PUSH_DETAIL_TAG_HYGL = "HYGL";//会议管理
    public static final String PUSH_DETAIL_TAG_GGGL = "GGGL";//公告管理
    public static final String PUSH_DETAIL_TAG_DBXX = "DBXX";//督办信息
    public static final String PUSH_DETAIL_TAG_XJSP = "XJSP";//休假审批
    public static final String PUSH_DETAIL_TAG_CCSP = "CCSP";//出差审批
    public static final String PUSH_DETAIL_TAG_YPSP = "YPSP";//用品审批
    //会议状态
    public static final String MEETING_STATE_READY = "0";//未开始
    public static final String MEETING_STATE_DOING = "1";//进行中
    public static final String MEETING_STATE_OVER = "2";//已结束
    //检查到版本信息后是否强制升级
    public static final String FORCE_UPDATE_TRUE = "1";
    public static final String FORCE_UPDATE_FALSE = "0";
    public static ListViewForScrollView listViewForScrollView;
    public static List<FileProcessingModel> fileProcessingModelList = new ArrayList<>();
    //用来唤起的Activity名字的key
    public static final String CALL_TO_ACTIVITY = "callToActivity";//
    public static final String FORM_NOTICE_OPEN = "fromNoticOpen";//
    public static final String FORM_NOTICE_OPEN_DATA = "formNoticeOpenData";//
    //申请会议室数据的状态
    //为空或null：等待审批；0：审批退回；1：审批通过；2：已发布
    public static final String APPLY_MEETING_ROOM_STATUS_RETURN = "0";//退回
    public static final String APPLY_MEETING_ROOM_STATUS_PASS = "1";//通过
    public static final String APPLY_MEETING_ROOM_STATUS_PUBLISH = "2";//发布
    //会议发布列表的状态
    public static final String MEETING_PUBLISH_STATUS_PASS = "1";//审批通过
    public static final String MEETING_PUBLISH_STATUS_PUBLISHED = "2";//已发布
    //判断是那个界面跳转的发布会议界面
    public static final String MEETING_PUBLISH_TAG_MEETING_LIST = "1";//由会议列表跳转到发布会议
    public static final String MEETING_PUBLISH_TAG_MEETING_ROOM_LIST = "0";//由会议室申请列表跳转到发布会议
    //用户权限查询常量（用以下常量进行匹配，查询服务器返回的权限状态）
    public static final String PERMISSION_BUSINESS_APPLY = "出差申请";
    public static final String PERMISSION_MEETING_PUBLISH = "发布会议";
    public static final String PERMISSION_LEADER_SCHEDULE = "领导日程";
    public static final String PERMISSION_MINE_SCHEDULE = "我的日程";
    public static final String PERMISSION_MEETING_ROOM_APPLY = "会议室申请";
    public static final String PERMISSION_BE_SUPERVISED = "被督办";
    public static final String PERMISSION_OFFICE_SUPPLIES_APPLY = "办公用品申请";
    public static final String PERMISSION_SPONSOR_SUPERVISE = "发起督办";
    public static final String PERMISSION_LEAVE_APPLY = "请假申请";
}
