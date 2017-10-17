package cn.enjoytoday.expandmateriallayout.callbacks

import android.telecom.Call
import android.view.View
import cn.enjoytoday.expandmateriallayout.beans.OperationBar
import java.text.FieldPosition

/**
 * @date 17-10-17.
 * @className OperationBarCallback
 * @serial 1.0.0
 */
interface OperationBarCallback :Callback {

    /**
     * 点击操作回调
     */
    fun onClick(view: View,parent:View?,operationBar: OperationBar,groupPosition: Int,childPosition:Int)

//    fun scrollItem(view: View,groupPosition: Int,childPosition:Int)


}