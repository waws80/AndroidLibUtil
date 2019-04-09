package pw.androidthanatos.lib.utils.core

import android.os.CountDownTimer


/**
 *  @desc: 倒计时工具类
 *  @className: TimerUtils.kt
 *  @author: thanatos
 *  @createTime: 18-12-6 上午11:43
 */
class TimerUtils private constructor(){


    companion object {
        private const val ONE_SECOND = 1000L
        /**
         * 获取 CountDownTimerUtils
         * @return CountDownTimerUtils
         */
        val invoke = TimerUtils()


        /**
         * 一行代码调用
         */
        @JvmName("start")
        fun start(totalMillis: Long = 60 * 1000L,
                  intervalMillis: Long = 60 * 1000L,
                  interval: (Long)->Unit,
                  finish:()->Unit){
            invoke.setTotalMillis(totalMillis)
                .setInterval(intervalMillis)
                .setFinishDelegate {
                    finish.invoke()
                }
                .setTickDelegate {
                    interval.invoke(it)
                }.start()
        }

        /**
         * 取消
         */
        @JvmName("cancel")
        fun cancel(){
            invoke.cancel()
        }
    }

    /**
     * 总倒计时时间
     */
    private var mTotalMillis: Long = 60 * 1000L
    /**
     * 定期回调的时间 必须大于0 否则会出现ANR
     */
    private var mCountDownInterval: Long = 1000L
    private var mCountDownTimer: MyCountDownTimer? = null

    /**
     * 倒计时结束的回调接口
     */
    var finishDelegate:()->Unit = {}
    /**
     * 定期回调的接口
     */
    var tickDelegate: (Long) ->Unit = {}

    /**
     * 设置定期回调的时间 调用[.setTickDelegate]
     * @param interval 定期回调的时间 必须大于0
     * @return CountDownTimerUtils
     */
    fun setInterval(interval: Long = 1000L): TimerUtils {
        this.mCountDownInterval = interval
        return this
    }

    /**
     * 设置倒计时结束的回调
     * @param finishDelegate 倒计时结束的回调接口
     * @return CountDownTimerUtils
     */
    fun setFinishDelegate(finishDelegate:()->Unit): TimerUtils {
        this.finishDelegate = finishDelegate
        return this
    }

    /**
     * 设置总倒计时时间
     * @param totalMillis 总倒计时时间
     * @return CountDownTimerUtils
     */
    fun setTotalMillis(totalMillis: Long = 60 * 1000L): TimerUtils {
        this.mTotalMillis = totalMillis
        return this
    }

    /**
     * 设置定期回调
     * @param tickDelegate 定期回调接口
     * @return CountDownTimerUtils
     */
    fun setTickDelegate(tickDelegate: (Long) ->Unit): TimerUtils {
        this.tickDelegate = tickDelegate
        return this
    }

    private fun create() {
        if (mCountDownTimer != null) {
            mCountDownTimer!!.cancel()
            mCountDownTimer = null
        }
        if (mCountDownInterval <= 0) {
            mCountDownInterval = mTotalMillis + ONE_SECOND
        }
        mCountDownTimer = MyCountDownTimer(mTotalMillis, mCountDownInterval)
        mCountDownTimer!!.setTickDelegate(tickDelegate)
        mCountDownTimer!!.setFinishDelegate(finishDelegate)
    }

    /**
     * 开始倒计时
     */
    fun start() {
        if (mCountDownTimer == null) {
            create()
        }
        mCountDownTimer!!.start()
    }

    /**
     * 取消倒计时
     */
    fun cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer!!.cancel()
        }
    }

    private class MyCountDownTimer
    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to [.start] until the countdown is done and [.onFinish]
     * is called.
     * @param countDownInterval The interval along the way to receive
     * [.onTick] callbacks.
     */
        (millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        /**
         * 倒计时结束的回调接口
         */
        var finishDelegate:()->Unit = {}
        /**
         * 定期回调的接口
         */
        var tickDelegate: (Long) ->Unit = {}
        override fun onTick(millisUntilFinished: Long) {
            tickDelegate.invoke(millisUntilFinished)
        }

        override fun onFinish() {
            finishDelegate.invoke()
        }

        internal fun setFinishDelegate(finishDelegate:()->Unit) {
            this.finishDelegate = finishDelegate
        }

        internal fun setTickDelegate(tickDelegate: (Long) ->Unit) {
            this.tickDelegate = tickDelegate
        }
    }

}
