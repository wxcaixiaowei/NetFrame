package com.wedo.client.netframe.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.wedo.client.netframe.R;
import com.wedo.client.netframe.activities.common.BaseActivity;
import com.wedo.client.netframe.activities.common.ThreadAid;
import com.wedo.client.netframe.activities.common.ThreadHelper;
import com.wedo.client.netframe.activities.common.ThreadListener;
import com.wedo.client.netframe.androidext.ChenApplication;
import com.wedo.client.netframe.remote.RemoteManager;
import com.wedo.client.netframe.remote.Request;
import com.wedo.client.netframe.remote.Response;
import com.wedo.client.netframe.util.JsonUtil;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MainActivity extends BaseActivity {

    private Future<Response> indexResponseFuture;
    private TextView reportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        shenApplication = (ChenApplication) this.getApplication();
        setContentView(R.layout.activity_main);

        reportText = (TextView) findViewById(R.id.reportText);

        requestSynchronization();
        //requestAsynchronous();
    }

    /**
     * 同步请求
     */
    public void requestSynchronization(){
        RemoteManager manager = RemoteManager.getPostOnceRemoteManager();
        Request request = manager.createQueryRequest("你的网络请求地址");

        request.addParameter("请求参数1", "参数1");
        request.addParameter("请求参数2", "参数2");

        ProgressDialog progress = showProgressDialog(R.string.app_up_data);
        progress.setOnDismissListener(new SynchronizationReport());
        indexResponseFuture = shenApplication.asyInvoke(new ThreadHelper(progress, request));

    }

    /**
     * 异步请求
     */
    public void requestAsynchronous(){
        RemoteManager manager = RemoteManager.getPostOnceRemoteManager();
        Request request = manager.createQueryRequest("你的网络请求地址");

        request.addParameter("请求参数1", "参数1");
        request.addParameter("请求参数2", "参数2");

        shenApplication.asyInvoke(new ThreadAid(new AsynchronousListener(), request));

    }


    /**
     * 同步请求的网络回调
     */
    class SynchronizationReport implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface arg0) {
            Response response;

            if (indexResponseFuture == null) {
                return;
            }
            try {
                response = indexResponseFuture.get();
                if (response == null) {
                    //失败时用户自定义
                }

                if (response.isSuccess()) {
                    final JSONObject json = JsonUtil.getJsonObject(response.getModel());
                    //纯测试 这种方法请勿再用
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reportText.setText(json.toString());
                        }
                    });

                } else {
                    toastShort(response.getMessage());
                }
            } catch (InterruptedException e) {
                logError(e.getMessage());
            } catch (ExecutionException e) {
                logError(e.getMessage());
            }
        }
    }

    /**
     * 异步请求网络回调监听
     */
        class AsynchronousListener implements ThreadListener {

            @Override
            public void onPostExecute(Response response) {

                if (response == null) {
                    //失败时用户自定义
                } else if (response.isSuccess()) {
                    final JSONObject json = JsonUtil.getJsonObject(response.getModel());
                    //纯测试 这种方法请勿再用
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reportText.setText(json.toString());
                        }
                    });
                } else {
                    toastLong(response.getMessage());
                }
            }
        }


    }




