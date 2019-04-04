package pw.androidthanatos.lib.utils.core;


import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.*;
import androidx.annotation.NonNull;
import pw.androidthanatos.lib.utils.LibUtils;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

/**
 *  @desc:  webView 工具类
 *  @className: WebViewDelegate
 *  @author: thanatos
 *  @createTime: 2018/12/20
 *  @updateTime: 2018/12/20 4:28 PM
 */
public class WebViewDelegate {
  private WeakReference<WebView> viewWeakReference;
  private WebSettings mSetting;

  private WebViewDelegate() {
  }

  public static WebViewDelegate getInstance() {
    return new WebViewDelegate();
  }

  public WebViewDelegate init(@NonNull WebView webView) {
    this.viewWeakReference = new WeakReference<>(webView);
    return this;
  }

  public WebViewDelegate baseConfig(){
    baseConfig(false);
    return this;
  }

  public WebViewDelegate baseConfig(boolean zoom) {
    if (this.viewWeakReference != null && this.viewWeakReference.get() != null) {
      WebSettings webSettings = (this.viewWeakReference.get()).getSettings();
      this.mSetting = webSettings;
      webSettings.setSupportZoom(zoom);
      webSettings.setBuiltInZoomControls(true);
      webSettings.setDisplayZoomControls(false);
      webSettings.setUseWideViewPort(true);
      webSettings.setLoadWithOverviewMode(true);
      webSettings.setDefaultTextEncodingName("UTF-8");
      webSettings.setLoadsImagesAutomatically(true);
      webSettings.setLoadWithOverviewMode(true);
      //webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
      webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
      this.mSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

      return this;
    } else {
      return this;
    }
  }

  public WebViewDelegate javaScriptEnable(boolean enable) {
    this.mSetting.setJavaScriptEnabled(enable);
    this.mSetting.setJavaScriptCanOpenWindowsAutomatically(enable);
    this.mSetting.setBlockNetworkImage(false);
    //可以访问文件
    this.mSetting.setAllowFileAccess(true);
    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
      this.mSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }
    return this;
  }

  public WebViewDelegate canInput() {
    if (this.viewWeakReference != null && this.viewWeakReference.get() != null) {
      this.viewWeakReference.get().requestFocusFromTouch();
      return this;
    } else {
      return this;
    }
  }


  /**
   * 加载文本
   * @param html
   */
  public WebViewDelegate load(String html){
    load(html, new ProgressCallBack() {
      @Override
      public void progress(int i) {

      }

      @Override
      public void finish() {

      }

      @Override
      public void error() {

      }

      @Override
      public void getTitle(String s) {

      }

      @Override
      public List<String> loadAssetsFiles() {
        return Collections.emptyList();
      }

      @Override
      public void loadUrl(WebViewDelegate delegate, WebView webView, String s) {
        loadUrlBySystemBrowser(s);
      }
    });
    return this;
  }

  public WebViewDelegate load(@NonNull final String url, @NonNull final ProgressCallBack callBack) {

    if (this.viewWeakReference != null && this.viewWeakReference.get() != null) {
      if (url.startsWith("http:") || url.startsWith("https:")
              || url.startsWith("file:") || url.startsWith("content:")){
        this.viewWeakReference.get().loadUrl(url);
      }else {
        this.viewWeakReference.get().loadDataWithBaseURL(null, url,
                "text/html", "utf-8", null);
      }

      this.viewWeakReference.get().setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, final String s) {
          callBack.loadUrl(WebViewDelegate.this, webView, s);
          return true;
        }


        public void onPageFinished(WebView view, String url) {
          super.onPageFinished(view, url);
          try {
            List<String> files = callBack.loadAssetsFiles();
            if (files != null){
              for (String file : files) {
                //"css/detail.js"
                String js = LibUtils.INSTANCE.fileUtil().getFromAssets(file);
                view.loadUrl("javascript:" + js);
              }
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
          super.onReceivedError(view, request, error);
          callBack.error();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
          //super.onReceivedSslError(view, handler, error);
          //接受所有证书
          handler.proceed();
        }

      });

      this.viewWeakReference.get().setWebChromeClient(new WebChromeClient(){

          @Override
          public void onReceivedTitle(WebView webView, String s) {
              super.onReceivedTitle(webView, s);
              callBack.getTitle(s);
          }

          @Override
        public void onProgressChanged(WebView webView, int i) {
          super.onProgressChanged(webView, i);

          if (i >= 90){
            callBack.finish();
          }else{
            callBack.progress(i);
          }
        }
      });
    }
    return this;
  }


  /**
   * 系统浏览器打开当前url
   * @param url
   */
  public WebViewDelegate loadUrlBySystemBrowser(String url){
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      viewWeakReference.get().getContext().startActivity(intent);
      return this;
  }


  public interface ProgressCallBack {

        void progress(int i);

        void finish();

        void error();

        void getTitle(String s);


        void loadUrl(WebViewDelegate delegate, WebView webView, String url);

        /**
         * asset 文件名称
         * @return {@link List<String>}
         */
        List<String> loadAssetsFiles();
  }
}