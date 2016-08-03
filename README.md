# NetFrame
简易的安卓网络请求框架

#这三年的开发客户端一直都是用的自己封装的基于“阿帕奇”httpclient4.0的回调网络框架.其中用到FutureTask+ThreadpoolExcutor+Callback等.Android6.0发布之后应该会弃用这一套，不过对于本人来说还是比较有纪念意义的，整个demo包是在AS上的一个可运行的源码工程，里面还有一些乱七八糟的工具类，也可供初学者参考整体的代码封装过程，以下是介绍。


    比较重要的是remote包下的所有封装类，以及activity包下面的三个线程监听及实现类。
    
    具体的调用：
    
      在类里面声明变量 Future<Response> indexResponseFuture;
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
        
        
  
  以上。
