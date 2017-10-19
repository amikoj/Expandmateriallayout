package cn.enjoytoday.expandrefresh.beans

/**
 * @date 17-10-16.
 * @className ExpandGroupInfo
 * @serial 1.0.0
 *
 * 组类别数据填充集合
 */
class ExpandGroupInfo {

    /**
     * group id
     */
    var groupId:Int?=null
    /**
     * group name
     */
    var groupName:String?=null
    /**
     * detail description of group.
     */
    var groupDescription:String?=null
    /**
     * child item count.
     */
    var childCount:Int=0

    /**
     * 其他扩展参数
     */
    var params= mutableMapOf<String,Any>()

}
