package com.wisdom.nhoa;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa
 * @class describe：
 * @time 2018/1/30 13:49
 * @change
 */

public class ConstantUrl {
    //    public static final String BASE_URL = "http://192.168.1.191:8081/hz_oa/v1/";//杜邵鹏
//    public static final String BASE_URL = "http://192.168.123.79:8080/hz_oa/";//丁曙光
    public static final String BASE_URL = "http://60.219.169.77:9012/hz_oa/";//线上
    //    public static final String BASE_URL = "http://192.168.8.8:8080/hz_oa/";//王越
//        public static final String BASE_URL = "http://192.168.1.95:8090/hz_oa/";//国君
    public static final String PUSH = "http://192.168.1.191:8081/hz_oa/user/changeChannel";
    public static final String SIGNIN = "http://192.168.1.191:8081/hz_oa/dsp/applySignShouji";

    //登录
    public static final String LOGIN_URL = "v1/account/login";
    //首页上四个统计数据接口
    public static final String HOME_PAGE_STATIC_DATA_URL = "v1/homepage/getrapidnum";
    //公告接口
//    public static final String NOTICE_URL = "v1/notice/all_list";
    public static final String NOTICE_URL = "v2/notice/all_list";
    //首页待阅接口
    public static final String TOBE_READ_URL = "v2/homepage/noticelist";
    //公告详细接口
//    public static final String NOTICE_DETIALED_URL = "v1/notice/details";
    public static final String NOTICE_DETIALED_URL = "v2/notice/details";
    //通讯录的厅局列表接口
    public static final String DEPARTMENT_LIST_URL = "v1/address/dep_list";//
    //通讯录的部门列表接口
    public static final String SECTION_LIST_URL = "v1/address/office_list";//
    //人员列表
    public static final String STAFF_LIST_URL = "v1/address/user_list";
    //获取用户名搜索列表
    public static final String GETCONTACTLIST = "v1/address/searchuser";
    //公文传阅列表
//    public static final String DOCUMENT_CIRCLATED_LIST_URL = "v1/doctransfer/group_list";//
    public static final String DOCUMENT_CIRCLATED_LIST_URL = "v2/doctransfer/group_list";//
    //公文传阅-查询所有人员列表页面
//    public static final String CIRCLATED_SEARCH_ALL_STAFF = "v1/doctransfer/member_list";//
    public static final String CIRCLATED_SEARCH_ALL_STAFF = "v2/doctransfer/member_list";//
    //公文传阅-创建群组
//    public static final String CIRCLATED_CREATE_GROUP = "v1/doctransfer/group_create";//
    public static final String CIRCLATED_CREATE_GROUP = "v2/doctransfer/group_create";//
    //公文传阅-对话列表
//    public static final String CIRCLATED_CONVERSATION = "v1/doctransfer/group_session";//
    public static final String CIRCLATED_CONVERSATION = "v2/doctransfer/group_session";//
    //    公文传阅保存附件下载记录
//    public static final String RECORD_DOWNLOAD_URL = "v1/doctransfer/group_file_log_save";
    public static final String RECORD_DOWNLOAD_URL = "v2/doctransfer/group_file_log_save";
    //公文传阅群详情
//    public static final String GET_GROUP_DETAIL_URL = "v1/doctransfer/group_details";//
    public static final String GET_GROUP_DETAIL_URL = "v2/doctransfer/group_details";//
    //公文传阅管理群成员页面
//    public static final String GET_GROUP_MANAGMENT_URL = "v1/doctransfer/member_upd";//
    public static final String GET_GROUP_MANAGMENT_URL = "v2/doctransfer/member_upd";//
    //公文传阅退出群
//    public static final String QUIT_GROUP_URL = "v1/doctransfer/group_quit";//
    public static final String QUIT_GROUP_URL = "v2/doctransfer/group_quit";//
    //公文传阅管理群成员保存修改
//    public static final String ALTER_GROUP_SUBMIT = "v1/doctransfer/member_upd_save";//
    public static final String ALTER_GROUP_SUBMIT = "v2/doctransfer/member_upd_save";//
    //公文传阅上传附件
//    public static final String GROUP_UPLOAD_FILE = "v1/doctransfer/group_uploadfile";//
    public static final String GROUP_UPLOAD_FILE = "v2/doctransfer/group_uploadfile";//
    //公文传阅附件下载日志
//    public static final String DOCUMENT_DOWNLOAD_LOG = "v1/doctransfer/group_file_log";//
    public static final String DOCUMENT_DOWNLOAD_LOG = "v2/doctransfer/group_file_log";//
    //收发文管理列表
    public static final String RECEIVE_SEND_ISSUE_MANAGMENT = "v1/homepage/doclist_rs";//
    //收发文 公文正文
    public static final String RECEIVE_SEND_ISSUE_FILE_CONTEXT = "v1/homepage/backlogdetails_doccontext";//


    // <-- 会议-->
    //会议列表
//    public static final String MEETING_LIST = "v1/meeting/meeting_list";
    public static final String MEETING_LIST = "v2/meeting/meeting_list";
    //查询可参会人员
//    public static final String MANAGE_USER = "v1/meeting/meeting_user";
    public static final String MANAGE_USER = "v2/meeting/meeting_user";
    //创建会议
//    public static final String MEETING_SEND = "v1/meeting/meeting_create";
    public static final String MEETING_SEND = "v2/meeting/meeting_create";
    //会议详情
//    public static final String MEETING_DETAILS = "v1/meeting/meeting_details";
    public static final String MEETING_DETAILS = "v2/meeting/meeting_details";
    //查询签到人员
//    public static final String MEETING_SIGNUSER = "v1/meeting/meeting_signuser";
    public static final String MEETING_SIGNUSER = "v2/meeting/meeting_signuser";
    //会议签到
//    public static final String MEETING_SIGNING = "v1/meeting/meeting_signing";
    public static final String MEETING_SIGNING = "v2/meeting /meeting_signing";
    //结束会议
//    public static final String MEETING_OVER = "v2/meeting/meeting_over";
    public static final String MEETING_OVER = "v2/meeting/meeting_over";

    //修改手机号
    public static final String CHANGE_TELEPHONE = "v1/account/upd_phone";
    //添加日程
//    public static final String SUBMITCALENDAREVENT = "v1/scheduleplan/add_schedule";
    public static final String SUBMITCALENDAREVENT = "v2/scheduleplan/add_scheduleNew";
    //修改日程
    public static final String UPDATACALENDAREVENT = "v2/scheduleplan/update_scheduleNew";
    //删除日程
    public static final String DELETECALENDAREVENT = "v2/scheduleplan/delete_scheduleNew";
    //修改密码
    public static final String CHANGE_PASSWORD = "v1/account/upd_password";
    //查询日程内容
//    public static final String GETCALENDAREVENT = "v1/scheduleplan/query_schedule";
    public static final String GETCALENDAREVENT = "v2/scheduleplan/query_scheduleNew";
    //查询领导日程内容
    public static final String GETLEADERSCHEDULE = "v2/scheduleplan/queryLeaders_scheduleNew";
    // <-- 发文-->
    //发文代办
    public static final String BACKLOGLIST = "v1/homepage/backloglist";
    // 待办详情
    public static final String BACKLOGDETAILS = "v1/homepage/backlogdetails";
    //文件处理签字
    public static final String FILEHANDINGSHEET_SIGN = "v1/homepage/backlogdetails_filehandingsheet_sign";
    //文件处理单
    public static final String FILEHANDINGSHEET = "v1/homepage/backlogdetails_filehandingsheet";
    //发文代办附件接口
    public static final String BACKLOGDETAILS_FILE = "v1/homepage/backlogdetails_file";
    //文件处理单-发送接口-环节请求
    public static final String BACKLOGDETAILS_NEXTNODE = "v1/homepage/backlogdetails_nextnode";
    //文件处理单-发送接口-人员请求
    public static final String BACKLOGDETAILS_NODEPERSON = "v1/homepage/backlogdetails_nodeperson";
    //首页待办详情页面-文件处理单-驳回接口
    public static final String REFUSE_PROJECT_URL = "v1/homepage/backlogdetails_reject";//
    //首页待办详情页面-文件处理单-修正接口
    public static final String REFUSE_SUGGESTION_URL = "v1/homepage/backlogdetails_revise";//
    //首页待办详情页面-文件处理单-提交接口
    public static final String BACKLOGDETAILS_SUBMIT = "v1/homepage/backlogdetails_submit";//

    //首页待办详情页面-文件处理单-办结接口
    public static final String FILE_COMPLETE_URL = "v1/homepage/backlogdetails_complete";
    //录常用联系人
    public static final String SAVE_DAIL_CONTRACT = "v1/address/lately_person";
    //获取常用联系人列表
    public static final String GET_LATELY_PERSON = "v1/address/list_lately_person";
    //首页草稿箱按钮
    public static final String GET_DRAFTLIST = "v1/homepage/draftlist";
    //收发文查阅详情接口
    public static final String GET_COMPLETE_ISSUE_DETAILS = "v1/homepage/backlogdetails_log";
    //我的申请（请假等）列表页面
    public static final String GET_MY_APPROVAL_LIST_URL = "v2/examine/examine_list";
    //添加请假
    public static final String ADD_LEAVE = "v2/examine/add_leave";
    //添加出差
    public static final String ADD_BUSINESS_TRIP = "/v2/examine/add_businesstrip";
    //需要我审批的列表页面(待审页面)
    public static final String NEEDTOAPPROVAL_LIST_URL = "v2/examine/checkMyExamine_list";
    //请假类型
    public static final String LEAVE_TYPE = "v2/examine/leave_type";
    //获取办公用品列表接口
    public static final String GET_OFFICE_STUFF_LIST = "v2/examine/officeSupplies_list";//
    //添加生活用品接口
    public static final String SUBMIT_OFFICE_STUFF_URL = "v2/examine /add_officeSupplies";//
    //提交待审审批接口
    public static final String SUBMIT_APPROVAL = "v2/examine/updateStatus";//

    //获取审批人列表数据接口
    public static final String GET_PEOPLE_TREE_URL = "v2/examine/people_tree";
    //我抄送的
    public static final String MY_COPY_TO_URL = "v2/examine/myReminderExamine_list";//
    //审批 ------  我审批的列表
    public static final String MY_APPROVAL_LIST = "v2/examine/checkExamine_list";
    //督办列表
    public static final String SUPERVISUON_LIST_URL = "v2/supervision/ supervision_list";
    //被督办人反馈信息详细列表
    public static final String BE_SUPERVISED_PERSON_FEED_BACK_LIST = "v2/supervision/feedback_details";//
    //添加反馈信息页面
    public static final String ADD_FEED_BACK_INFO_URL = "v2/supervision/add_supervisionFeedback";
    //添加督办
    public static final String ADD_SUPERVISION = "v2/supervision/add_supervision";
    //修改督办
    public static final String EDIT_SUPERVISION = "v2/supervision/update_supervision";
    //被督办列表
    public static final String SUPERVISED_LIST_URL = "v2/supervision/ supervision_list";
    //删除督办信息
    public static final String DELETE_SUPERVERSION_INFO = "v2/supervision/delete_supervision";
    //    通讯链路信息
    public static final String DATA_CHANGE_URL = "user/changeChannel";
    //    查询某个会议的自身签到状态
    public static final String CHECK_MEETING_SIGN_STATE = "v2/meeting/qyMySignStateForTheMeet";
    //   2.	通过通知消息进入消息详情
    public static final String GET_PUSH_MSG_DETAIL_URL = "v2/homepage/messContent";
    //获得版本信息以及是否自动更新操作
    public static final String GET_VERSION_INFO = "v2/doctransfer/versionChecked";//
    //会议-我发起的列表
    public static final String MEETING_MY_SPONSOR_LIST_URL = "v2/meeting/myMeetList_publish";
    //会议-我发起的列表
    public static final String MEETING_MY_PARTICIPATION_LIST_URL = "v2/meeting/myMeetList_join";
    //申请会议室列表
    public static final String APPLY_MEETING_ROOM_LIST_URL = "v2/meeting/bdrmDemandList";
    //申请会议室信息详情
    public static final String APPLY_MEETING_ROOM_DETAIL_URL = "v2/meeting/bdrmApplyDetail";
    //获取会议室信息列表
    public static final String GET_MEETING_ROOM_INFO_URL = "v2/meeting/bdrmList";
    //修改会议室信息接口
    public static final String ALTER_MEETING_ROOM_INFO_URL = "v2/meeting/editBdrmApplyMsg";
    //删除会议室信息接口
    public static final String DELETE_MEETING_ROOM_INFO_BT_DATA_ID = "v2/meeting/delBdrmApplyMsg";
    //会议签到列表
    public static final String GET_MEETING_LIST_URL = "v2/meeting/meetList_abSign";
    //    上传附件（返还文件名称和路径，用于异步业务使用）
    public static final String UPLOAD_FILE_URL = "v2/meeting/uploadFile";
    //会议发布页面  列表请求地址
    public static final String GET_MEETING_PUBLISH_LIST = "v2/meeting /meetingListPub";
    //我审核过的会议室申请信息
    public static final String MY_CHECKED_MEETING_ROOM_APPLY = "v2/meeting/meetingListMyAudited";
    //申请会议室接口
    public static final String APPLY_MEETING_ROOM_APPLY = "v2/meeting/applyBdrm";
    //获得用户相应的权限
    public static final String GET_USER_PERMISSION_URL = "v2/permission/userMenuPermission";//
    //    发布会议接口
    public static final String PUBLISH_MEETING_URL = "v2/meeting/meeting_create";
    //网页扫码登录接口
    public static final String WEB_SCAN_QR_CODE_LOGIN = "v2/meeting/sureLoginByQrCode";//
}
