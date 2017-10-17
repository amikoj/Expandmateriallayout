package cn.enjoytoday.expandmateriallayout.test

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.enjoytoday.expandmateriallayout.R
import cn.enjoytoday.expandmateriallayout.adapter.ExpandBasicAdapter
import cn.enjoytoday.expandmateriallayout.beans.ExpandChildInfo
import cn.enjoytoday.expandmateriallayout.beans.ExpandGroupInfo
import cn.enjoytoday.expandmateriallayout.beans.OperationBar
import cn.enjoytoday.expandmateriallayout.callbacks.OperationBarCallback
import cn.enjoytoday.expandmateriallayout.toast
import kotlinx.android.synthetic.main.activity_drag_test.*
import kotlinx.android.synthetic.main.child_item.view.*
import kotlinx.android.synthetic.main.group_item.view.*


class DragTestActivity : Activity() {

    /**
     * 组信息
     */
    var groupList= mutableListOf<ExpandGroupInfo>()

    /**
     * 子信息
     */
//    var childList= mutableListOf<MutableList<ExpandChildInfo>>()

    var childList= mutableListOf<ExpandChildInfo>()

    var adapter: ExpandBasicAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_test)


        /**
         * 初始化数据
         */

        var expandGropInfo: ExpandGroupInfo

        for (x in 0..5){
            expandGropInfo=ExpandGroupInfo()
            expandGropInfo.childCount=3
            expandGropInfo.groupId=x
            expandGropInfo.groupName="test"+x

            expandGropInfo.groupDescription="basic test of group"
            groupList.add(expandGropInfo)

//            val billInfos= mutableListOf<ExpandChildInfo>()
            for (y in 0..2){
                val info=ExpandChildInfo()
                info.childDescription="child info basic"
                info.groupId=x
                info.childPosition=y
                info.childName="child "+y
                info.iconId=R.drawable.book_icon
                childList.add(info)
            }
//            childList.add(billInfos)
        }


        adapter=object :ExpandBasicAdapter(this,childList,groupList){


            override fun setData(parent: ViewGroup?, groupPosition: Int, childPosition: Int) {
                val childInfo=getChild(groupPosition,childPosition)
                parent!!.child_name.text=childInfo.childName
                parent.child_des.text="test description"
                parent.child_icon.setImageDrawable(this@DragTestActivity.resources.getDrawable(childInfo.iconId!!))

            }

            override fun setGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
                /**
                 * 组类别显示
                 */
                var view:View?
                var gropholder: GroupHolder?
                val groupInfo=getGroup(groupPosition)
                if (convertView==null) {
                    view= View.inflate(this@DragTestActivity,R.layout.group_item,null)
                    gropholder= GroupHolder(view)
                    view.tag = gropholder
                } else{
                    view=convertView
                    gropholder= view.tag as GroupHolder?
                }
                gropholder?.group_name?.text = groupInfo.groupName
                gropholder?.group_desc?.text= groupInfo.groupDescription
                gropholder?.numbers?.text= groupInfo.childCount.toString()
                return  view!!
            }

            override fun addChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?) {
                /**
                 * child 条目信息
                 */
                val view:View=View.inflate(this@DragTestActivity,R.layout.child_item,null)
                parent!!.addView(view,0, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))


            }
        }



        adapter!!.addOperationBar(OperationBar("Del",Gravity.RIGHT,0,1))
//                .addOperationBar(OperationBar("Test",Gravity.LEFT,0,2))
                .addOperationBar(OperationBar("Add",Gravity.RIGHT,1,2))
                .addOperationCallback(object : OperationBarCallback {
                    override fun onClick(view: View, parent: View?, operationBar: OperationBar, groupPosition: Int, childPosition: Int) {
                        toast(message = "operation bar onClick ${operationBar.text}")
                        expandable_list_view.smoothScrollToPositionFromTop(1,0)
                    }

                })




        expandable_list_view.setAdapter(adapter)
//        expandable_list_view.setIsPaging(true)



    }



    class GroupHolder(view:View){

        var expansion_view:ImageView=view.expansion_view
        var group_name:TextView=view.group_name
        var group_desc:TextView=view.group_des
        var numbers:TextView=view.numbers



    }


    class ChildHolder(view: View){
        var del:TextView=view.del
        var item_info=view.item_info
        var child_icon=view.child_icon
        var child_name=view.child_name
        var child_des=view.child_des


    }


}


