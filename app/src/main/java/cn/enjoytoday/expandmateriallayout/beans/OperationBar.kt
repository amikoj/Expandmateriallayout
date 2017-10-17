package cn.enjoytoday.expandmateriallayout.beans

import android.support.annotation.IdRes
import android.view.Gravity
import java.text.FieldPosition

/**
 * @date 17-10-16.
 * @className OperationBar
 * @serial 1.0.0
 *
 * 左右滑动操作bar的基础信息
 */
class OperationBar {

    constructor(idRes: Int,gravity: Int,position: Int,id:Int){
        this.idRes=idRes
        this.gravity=gravity
        this.position=position
        this.id=id
    }


    constructor(text:String,gravity: Int,position: Int,id:Int){
        this.text=text
        this.gravity=gravity
        this.position=position
        this.id=id
    }
    /**
     * 图片资源
     */
    var idRes:Int=Int.MIN_VALUE

    /**
     * 文字
     */
    var text:String?=null


    /**
     * 位置方位信息
     */
    var gravity:Int=Gravity.RIGHT

    /**
     * 位置顺序信息
     */
    var position:Int=0


    /**
     * 标记信息
     */
    var id:Int=0

    /**
     * 组别,默认是使用所有,可以指定
     */
    var groupId:Int= Int.MAX_VALUE

    /**
     * 子item位置,默认是使用所有,可以指定
     */
    var childId:Int= Int.MAX_VALUE




}