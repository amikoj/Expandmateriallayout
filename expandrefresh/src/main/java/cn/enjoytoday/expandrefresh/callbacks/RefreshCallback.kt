package cn.enjoytoday.expandrefresh.callbacks

/**
 * @date 17-10-16.
 * @className RefreshCallback
 * @serial 1.0.0
 */
interface RefreshCallback:Callback{

    /**
     * 更新下一个回调
     */
    fun refreshPre()


    /**
     * 更新上一个回调
     */
    fun refreshNext()


}