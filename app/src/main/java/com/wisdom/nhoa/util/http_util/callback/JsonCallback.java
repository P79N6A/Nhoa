package com.wisdom.nhoa.util.http_util.callback;

import android.content.Intent;
import android.util.Log;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.BaseRequest;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.base.ActivityManager;
import com.wisdom.nhoa.mine.activity.LoginActivity;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

public abstract class JsonCallback<T> extends AbsCallback<T> {
    private static final String TAG = JsonCallback.class.getSimpleName();

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现

//        request.headers("header1", "HeaderValue1")//
//                .params("params1", "ParamsValue1")//
//                .params("token", "3215sdf13ad1f65asd4f3ads1f");

    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        U.closeLoadingDialog();
        Log.i(TAG, "onError001: " + e.getMessage());
        if (StrUtils.isHasChinese(e.getLocalizedMessage())) {//如果返回错误是中文
            if (!"".equals(e.getMessage())
                    && !"文件不存在".equals(e.getMessage())) {
                ToastUtil.showToast(e.getMessage());
            }
        } else {
            ToastUtil.showToast("服务器异常");
        }


    }


    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     * <pre>
     * OkGo.get(Urls.URL_METHOD)//
     *     .tag(this)//
     *     .execute(new DialogCallback<BaseModel<ServerModel>>(this) {
     *          @Override
     *          public void onSuccess(BaseModel<ServerModel> responseData, Call call, Response response) {
     *              handleResponse(responseData.results, call, response);
     *          }
     *     });
     * </pre>
     */
    @Override
    public T convertSuccess(Response response) throws Exception {
        //以下代码是通过泛型解析实际参数,泛型必须传
        //这里为了方便理解，假如请求的代码按照上述注释文档中的请求来写，那么下面分别得到是

        //com.lzy.demo.callback.DialogCallback<com.lzy.demo.model.BaseModel<com.lzy.demo.model.ServerModel>> 得到类的泛型，包括了泛型参数
        Type genType = getClass().getGenericSuperclass();
        //从上述的类中取出真实的泛型参数，有些类可能有多个泛型，所以是数值
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        //我们的示例代码中，只有一个泛型，所以取出第一个，得到如下结果
        //com.lzy.demo.model.BaseModel<com.lzy.demo.model.ServerModel>
        Type type = params[0];

        // 这里这么写的原因是，我们需要保证上面我解析到的type泛型，仍然还具有一层参数化的泛型，也就是两层泛型
        // 如果你不喜欢这么写，不喜欢传递两层泛型，那么以下两行代码不用写，并且javabean按照
        // https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md 这里的第一种方式定义就可以实现
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
        //如果确实还有泛型，那么我们需要取出真实的泛型，得到如下结果
        //class com.lzy.demo.model.BaseModel
        //此时，rawType的类型实际上是 class，但 Class 实现了 Type 接口，所以我们用 Type 接收没有问题
        Type rawType = ((ParameterizedType) type).getRawType();
        //这里获取最终内部泛型的类型 com.lzy.demo.model.ServerModel
        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];

        //这里我们既然都已经拿到了泛型的真实类型，即对应的 class ，那么当然可以开始解析数据了，我们采用 Gson 解析
        //以下代码是根据泛型解析数据，返回对象，返回的对象自动以参数的形式传递到 onSuccess 中，可以直接使用
//        JsonReader jsonReader = new JsonReader(response.body().charStream());
        String responseStr = response.body().string();

        if (typeArgument == Void.class) {
            SimpleResponse simpleResponse = Convert.fromJson(responseStr, SimpleResponse.class);
            //无数据类型,表示没有data数据的情况（以  new DialogCallback<BaseModel<Void>>(this)  以这种形式传递的泛型)
            response.close();
            //noinspection unchecked
            return (T) simpleResponse.toBaseModel();
        } else if (rawType == BaseModel.class) {
            //有数据类型，表示有data
            int code = getJsonCode(responseStr);
            //这里的0是以下意思
            //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
            if (code == 0) {/**********************2017/5/10 根据服务器状态码进行更改****************************************************************/
                //noinspection unchecked
                BaseModel baseModel = Convert.fromJson(responseStr, type);
                response.close();
                return (T) baseModel;
            } else if (StrUtils.getFirstChar(code + "").equals("1")) {
                //服务器错误
                Log.i(TAG, "错误输出1：" + responseStr);
                throw new IllegalStateException("服务器错误");
            } else if (code == ConstantString.CODE_TOKEN_ILLEGAL) {
                Log.i(TAG, "错误输出2：" + responseStr);
                ConstantString.LOGIN_STATE = false;
                ActivityManager.getActivityManagerInstance().clearAllActivity();
                Intent intent = new Intent(AppApplication.getInstance(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppApplication.getInstance().startActivity(intent);
                throw new IllegalStateException("登录过期");
            } else if (code == ConstantString.CODE_NO_FILE) {
//                找不到文件的错误。不toast
                throw new IllegalStateException("");
            } else if (ConstantString.FILE_NO_CONTENT_CODE.equals(code)) {
//                找不到文件的错误。不toast
                throw new IllegalStateException("文件不存在");
            } else if (StrUtils.getFirstChar(code + "").equals("2")) {
                Log.i(TAG, "错误输出3：" + responseStr);
                throw new IllegalStateException(getErrorMsg(responseStr));
            } else if (code == ConstantString.CODE_NO_DATA) {//暂时没查到数据的提示
                throw new IllegalStateException("暂无数据");
            } else {
                Log.i(TAG, "错误代码：" + code + "，错误信息：" + getErrorMsg(responseStr));
                throw new IllegalStateException("服务器连接错误");
            }
        } else {
            response.close();
            throw new IllegalStateException("基类错误无法解析!");
        }
    }


    /**
     * 获得JSON串的code值
     *
     * @param data
     * @return
     */
    public static int getJsonCode(String data) {
        int error_code = 3;
        try {
            JSONObject jsonObject = new JSONObject(data);
            error_code = jsonObject.getInt("error_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return error_code;
    }

    /**
     * 获得JSON串的code值
     *
     * @param data
     * @return
     */
    public static String getErrorMsg(String data) {
        String error_msg = "";
        try {
            JSONObject jsonObject = new JSONObject(data);
            error_msg = jsonObject.getString("error_msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return error_msg;
    }


}
