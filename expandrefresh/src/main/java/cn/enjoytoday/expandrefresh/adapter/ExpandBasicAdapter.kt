package cn.enjoytoday.expandrefresh.adapter

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.*
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import cn.enjoytoday.expandrefresh.ChildViewHolder
import cn.enjoytoday.expandrefresh.R
import cn.enjoytoday.expandrefresh.beans.ExpandChildInfo
import cn.enjoytoday.expandrefresh.beans.ExpandGroupInfo
import cn.enjoytoday.expandrefresh.beans.OperationBar
import cn.enjoytoday.expandrefresh.callbacks.OperationBarCallback
import cn.enjoytoday.expandrefresh.dip2px
import cn.enjoytoday.expandrefresh.log
import kotlinx.android.synthetic.main.child_item_parent.view.*

/**
 * @date 17-10-16.
 * @className ExpandBasicAdapter
 * @serial 1.0.0
 *
 * 定制修改adapter适配器
 */
abstract class  ExpandBasicAdapter(private var context:Context,private var childList:MutableList<ExpandChildInfo>): BaseExpandableListAdapter(){
    private var groupList:MutableList<ExpandGroupInfo> = mutableListOf()  //组类别数据清单
    private var operation_bars:MutableList<OperationBar> = mutableListOf() //操作bar集合


    private var canScrollToLeft=false     //标准左滑删除
    private var canScrollToRight=false    //右滑触发
    private var leftItemCount:Int=0       //左边操作Bar个数
    private var rightItemCount:Int=0      //右边操作Bar个数
    private var itemWidth:Int=dip2px(context,60f).toInt()           //单个action的宽度

    private var operationCallback: OperationBarCallback?=null


    private var lastOpenView:View?=null


    private fun openOperation(view:View){
        if (lastOpenView!=null && lastOpenView!!.scrollX!=0){
            lastOpenView!!.scrollTo(0,0)
        }
        lastOpenView=view
    }


    /**
     * 关闭所有的
     */
    fun closeOperationBar(){
        if (lastOpenView!=null && lastOpenView!!.scrollX!=0){
            lastOpenView!!.scrollTo(0,0)
        }

    }

    fun addOperationCallback(operationCallback: OperationBarCallback){
        this.operationCallback=operationCallback
    }




    constructor(context: Context, childList: MutableList<ExpandChildInfo>,groupList:MutableList<ExpandGroupInfo>):this(context,childList){
        this.groupList=groupList
    }


    override fun getGroup(groupPosition: Int): ExpandGroupInfo = groupList[groupPosition]

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean =false

    override fun hasStableIds(): Boolean =true

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        return  setGroupView(groupPosition,isExpanded,convertView,parent)
    }


    abstract fun  setGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View

    override fun getChildrenCount(groupPosition: Int): Int =groupList[groupPosition].childCount

    override fun getChild(groupPosition: Int, childPosition: Int): ExpandChildInfo {
        return  childList.filter {
            it.groupId==groupPosition && it.childPosition==childPosition
        }[0]
    }

    override fun getGroupId(groupPosition: Int): Long =groupPosition.toLong()


    /**
     * 添加操作按钮,提供两种方式,一种显示图片,一种显示文字
     */
    fun addOperationBar(operationBar: OperationBar): ExpandBasicAdapter {
        if (!operation_bars.contains(operationBar)){
            if (operation_bars.size>5){
                operation_bars.removeAt(0)
            }
            if (operationBar.gravity==Gravity.LEFT){
                if (leftItemCount>=3) return this
                if (!canScrollToRight) canScrollToRight=true
                leftItemCount++
            }else if (operationBar.gravity==Gravity.RIGHT){
                if (rightItemCount>=3) return this
                if (!canScrollToLeft) canScrollToLeft=true
                rightItemCount++
            }
            operation_bars.add(operationBar)
        }

        return  this
    }


    /**
     * 通过顺序将operationbar 排序
     */
    private fun getOperationBar(groupPosition: Int,childPosition: Int):MutableList<OperationBar>{
        val list:MutableList<OperationBar> = mutableListOf()
        operation_bars.forEach {
            if ((it.groupId==Int.MAX_VALUE && it.childId==Int.MAX_VALUE) || (it.groupId==groupPosition && (it.childId==Int.MAX_VALUE|| it.childId==childPosition))){
                if (list.size>=it.position){
                    list.add(it.position, it)
                }else if (list.size>0 && list.last().position>=it.position){
                    list.add(list.size-2,it)
                }else{
                    list.add(it)
                }
            }
        }
        return list
    }


    /***
     * 左侧操作bar
     */
    private fun getLeftOperationBar(groupPosition: Int,childPosition: Int):MutableList<OperationBar>{
        val list:MutableList<OperationBar> = getOperationBar(groupPosition,childPosition)

        return  list.filter {
            it.gravity==Gravity.LEFT
        }.toMutableList()

    }


    /**
     * 右侧操作bar
     */
    private fun getRightOperationBar(groupPosition: Int,childPosition: Int):MutableList<OperationBar>{
        val list:MutableList<OperationBar> = getOperationBar(groupPosition,childPosition)
        return  list.filter {
            it.gravity==Gravity.RIGHT
        }.toMutableList()

    }


    private fun addOperationViews(parent:ViewGroup,operationBars:MutableList<OperationBar>,groupPosition: Int,childPosition: Int,itemView:View){
        for (x in 0 until operationBars.size){
            val operationBar=operationBars[x]
            if(operationBar.idRes== Int.MIN_VALUE){
                /**
                 * textView样式
                 */

                val textView=TextView(context)
                textView.text=operationBar.text
                textView.setTextColor(Color.parseColor("#ffffff"))        //这里可能需要替换
                textView.setBackgroundColor(Color.parseColor("#ff0000")) //背景色
                textView.gravity=Gravity.CENTER
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f)
                textView.layoutParams= ViewGroup.LayoutParams(dip2px(context,60f).toInt(),ViewGroup.LayoutParams.MATCH_PARENT)
                parent.addView(textView)
                textView.setOnClickListener{
                    log(message = "textview set onClick listener")
                    operationCallback?.onClick(it,itemView,operationBar,groupPosition,childPosition)
                }

            }else{
                /**
                 * imageview样式
                 */
                val imageView=ImageView(context)

                val padding=dip2px(context,3f).toInt()
                imageView.setBackgroundColor(Color.parseColor("#ff0000"))
                imageView.setPadding(padding,padding,padding,padding)
                imageView.setImageResource(operationBar.idRes)
                imageView.scaleType=ImageView.ScaleType.FIT_CENTER
                imageView.layoutParams= ViewGroup.LayoutParams(dip2px(context,60f).toInt(),ViewGroup.LayoutParams.MATCH_PARENT)

                parent.addView(imageView,0)
                imageView.setOnClickListener{
                    log(message = "image set onClick listener")
                    operationCallback?.onClick(it,itemView,operationBar,groupPosition,childPosition)
                }

            }

        }


    }






    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        var view:View
        var childViewHolder: ChildViewHolder?

        if (convertView==null){
            view=View.inflate(context,R.layout.child_item_parent,null)
            childViewHolder= ChildViewHolder(view)

            if (canScrollToRight){
                /**
                 * 左侧操作Bar
                 */
                val leftOperationBars=getLeftOperationBar(groupPosition,childPosition)
                if (leftOperationBars.size>0) {
//                    val leftOperationLayout =LinearLayout(context)
//                    leftOperationLayout.layoutParams= ViewGroup.LayoutParams(dip2px(context,60f).toInt(),WindowManager.LayoutParams.MATCH_PARENT)
//                    leftOperationLayout.gravity=Gravity.START
                    addOperationViews(childViewHolder.item_layout,leftOperationBars,groupPosition,childPosition,view)
//                    childViewHolder.item_layout.addView(leftOperationLayout,ViewGroup.LayoutParams(dip2px(context,60f).toInt(),WindowManager.LayoutParams.MATCH_PARENT))        //添加右侧操作栏父布局

                }
            }

            val itemView=addChildView(groupPosition,childPosition,isLastChild,convertView,childViewHolder.item_layout)
            childViewHolder.item_layout.addView(itemView)
            /**
             * 添加操作bar
             */
            if (canScrollToLeft){
                /**
                 * 右侧操作Bar
                 */
                val rightOperationBars=getRightOperationBar(groupPosition,childPosition)
                if (rightOperationBars.size>0) {
//                    val rightOperationLayout =LinearLayout(context)
//                    rightOperationLayout.layoutParams= ViewGroup.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.MATCH_PARENT)
//                    rightOperationLayout.gravity=Gravity.START
                    addOperationViews( childViewHolder.item_layout,rightOperationBars,groupPosition,childPosition,view)
//                    childViewHolder.operation_bar_layout.addView(rightOperationLayout)        //添加右侧操作栏父布局
                }
            }

            view.tag=childViewHolder
        }else{
            view=convertView
            childViewHolder= view.tag as ChildViewHolder?
        }


        setData(childViewHolder!!.item_layout,groupPosition,childPosition)

        if (canScrollToLeft || canScrollToRight) {
            view.item_layout.requestFocus()
            view.item_layout.isClickable=true
//            view.isClickable=true
            view.item_layout.setOnTouchListener(object : View.OnTouchListener {

                var startX = 0f
                var scrollX = 0
                var actionX = 0f

                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            /**
                             * 开始触摸
                             */
                            startX = event.x
                        }

                        MotionEvent.ACTION_MOVE -> {
                            /**
                             * 滑动过程
                             */
                            fling(event)
                        }

                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                            /**
                             * 结束滑动
                             */
                            fling(event)
                            scrollX = view.item_layout.scrollX
                            if ((scrollX > 0 && scrollX < itemWidth / 2f) || (scrollX<0 && scrollX>-itemWidth /2f)) {
                                scrollX = 0
                            } else if (scrollX >= itemWidth/ 2f) {
                                scrollX = itemWidth*rightItemCount
                            } else if (scrollX <=  -itemWidth/2f){
                                scrollX = -itemWidth*leftItemCount
                            }
                            if (scrollX!=0){
                                openOperation(view.item_layout)
                            }
                            view.item_layout.scrollTo(scrollX, 0)
                            startX = 0f
                        }


                    }
                    return false
                }


                fun fling(event: MotionEvent) {
                    /**
                     * 滑动过程
                     */
                    actionX = startX - event.x
                    scrollX = view.item_layout.scrollX + actionX.toInt()
//                    log(message = "scrollX:$scrollX,and canScrollToleft:$canScrollToLeft,canScrollToRight:$canScrollToRight")
                    if (!canScrollToRight) {
                        //不可向右滑动
                        if (scrollX < 0) {
                            scrollX = 0
                        }
                    } else {
                        //可以向右滑动
                        if (scrollX < -itemWidth*leftItemCount) {
                            scrollX = -itemWidth*leftItemCount
                        }
                    }


                    if (!canScrollToLeft) {
                        //不可向左滑
                        if (scrollX > 0) {
                            scrollX = 0
                        }
                    } else {
                        //可以向左滑
                        if (scrollX > itemWidth*rightItemCount) {
                            scrollX = itemWidth*rightItemCount
                        }

                    }
//                    log(message = "onfling scroll to scrollX :$scrollX")
                    view.item_layout.scrollTo(scrollX, 0)
                    startX = event.x
                }


            })


        }


       return view
    }










    abstract  fun setData(parent: ViewGroup?,groupPosition: Int,childPosition: Int)

    /**
     * 填充单个条目数据
     */
    abstract fun addChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?):View



    override fun getChildId(groupPosition: Int, childPosition: Int): Long =childPosition.toLong()
    override fun getGroupCount(): Int =groupList.size


    interface  OnOperationScrollListener{
        fun onScroll(groupPosition: Int,childPosition: Int)
    }
}