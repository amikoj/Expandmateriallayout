package cn.enjoytoday.expandmateriallayout.test

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.enjoytoday.expandmateriallayout.*
import cn.enjoytoday.expandmateriallayout.adapter.ExpandBasicAdapter
import cn.enjoytoday.expandmateriallayout.beans.ExpandChildInfo
import cn.enjoytoday.expandmateriallayout.beans.ExpandGroupInfo
import cn.enjoytoday.expandmateriallayout.beans.OperationBar
import cn.enjoytoday.expandmateriallayout.callbacks.OperationBarCallback
import kotlinx.android.synthetic.main.activity_drag_test.*
import kotlinx.android.synthetic.main.child_item.view.*
import kotlinx.android.synthetic.main.footer_view.*
import kotlinx.android.synthetic.main.footer_view.view.*
import kotlinx.android.synthetic.main.group_item.view.*
import kotlinx.android.synthetic.main.header_view.view.*


class DragTestActivity : Activity() {

    /**
     * 组信息
     */
    var groupList= mutableListOf<ExpandGroupInfo>()

    /**
     * 子信息
     */
    var childList= mutableListOf<ExpandChildInfo>()

    var adapter: ExpandBasicAdapter?=null

    val handler=Handler()

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
            for (y in 0..2){
                val info=ExpandChildInfo()
                info.childDescription="child info basic"
                info.groupId=x
                info.childPosition=y
                info.childName="child "+y
                info.iconId=R.drawable.book_icon
                childList.add(info)
            }
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

            override fun addChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?):View =
                    View.inflate(this@DragTestActivity,R.layout.child_item,null)
        }



        adapter!!.addOperationBar(OperationBar("Del",Gravity.RIGHT,0,1))
//                .addOperationBar(OperationBar("Test",Gravity.LEFT,0,2))
                .addOperationBar(OperationBar("Add",Gravity.RIGHT,1,2))
                .addOperationCallback(object : OperationBarCallback {
                    override fun onClick(view: View, parent: View?, operationBar: OperationBar, groupPosition: Int, childPosition: Int) {
                        toast(message = "operation bar onClick ${operationBar.text}")
                    }

                })


        refresh_layout.setHeaderViewBackgroundColor(resources.getColor(R.color.header_view_background_color))
        refresh_layout.setFooterViewBackgroundColor(resources.getColor(R.color.footer_view_background_color))
        refresh_layout.setHeaderView(createHeaderView())
        refresh_layout.setFooterView(createFooterView())
        refresh_layout.isTargetScrollWithLayout=true

        refresh_layout.setOnPullRefreshListener(object : ExpandRefreshLayout.OnPullRefreshListener{
            override fun onRefresh() {
                log(message = "refresh_layout onRefresh")
                refresh_layout.image_view.visibility = View.GONE
                refresh_layout.pb_view.visibility=View.VISIBLE
                refresh_layout.text_view.text=resources.getString(R.string.loading_text)
                handler.postDelayed({
                    refresh_layout.text_view.text=resources.getString(R.string.already_update_times,formatDate(System.currentTimeMillis()))
                    handler.postDelayed({
                        refresh_layout.setRefreshing(false)
                    },1000)

                },1500)
            }

            override fun onPullDistance(distance: Int) {
            }

            override fun onPullEnable(enable: Boolean) {
                refresh_layout.text_view.text= resources.getString(if (enable) R.string.release_after_loading else R.string.pull_down_loading)
                refresh_layout.image_view!!.visibility = View.VISIBLE
                refresh_layout.pb_view.visibility=View.GONE
                refresh_layout.image_view!!.rotation = (if (enable) 180 else 0).toFloat()

            }

        }).setOnPushLoadMoreListener(object :ExpandRefreshLayout.OnPushLoadMoreListener{
            override fun onLoadMore() {

            }

            override fun onPushDistance(distance: Int) {

            }

            override fun onPushEnable(enable: Boolean) {
                refresh_layout.footer_text_view!!.text = resources.getString(if (enable) R.string.release_after_loading else R.string.pull_up_loading)
                footer_image_view!!.visibility = View.VISIBLE
                footer_image_view!!.rotation = (if (enable) 0 else 180).toFloat()

            }

        })


        expandable_list_view.setAdapter(adapter)

    }



    private fun createHeaderView(): View {
        return LayoutInflater.from(this).inflate(R.layout.header_view, null)
    }




    private fun createFooterView():View{
        return LayoutInflater.from(this).inflate(R.layout.footer_view, null)
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


