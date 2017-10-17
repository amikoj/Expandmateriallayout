package cn.enjoytoday.expandmateriallayout.beans

/**
 * @date 17-10-16.
 * @className ExpandChildInfo
 * @serial 1.0.0
 *
 * 子项数据填充集合
 */
class ExpandChildInfo {
    /**
     * group id
     */
    var groupId:Int?=null

    /**
     * name of group
     */
    var childPosition:Int=0

    /**
     * resource id of icon drawable
     */
    var iconId:Int?=null

    /**
     * name of child
     */
    var childName:String?= null


    /**
     * detail description of child
     */
    var childDescription:String?=null


    /**
     * extend parameters of child.
     */
    var expandParams= mutableMapOf<String,Any>()

}