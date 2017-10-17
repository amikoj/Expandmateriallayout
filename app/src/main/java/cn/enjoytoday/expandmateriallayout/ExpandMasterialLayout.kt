package cn.enjoytoday.expandmateriallayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.AbsListView
import android.widget.ExpandableListView
import cn.enjoytoday.expandmateriallayout.callbacks.RefreshCallback
import kotlinx.android.synthetic.main.header_view.view.*

/**
 * @date 17-10-16.
 * @className ExpandMasterialLayout
 * @serial 1.0.0
 *
 * 可拉伸、收缩.
 */
class ExpandMasterialLayout(context: Context, attributeset: AttributeSet?, defStyleAttr:Int): ExpandableListView(context,attributeset,defStyleAttr), AbsListView.OnScrollListener {

    init {
        requestFocus()
        isClickable=true

    }

    /**
     * 滑动到顶部
     */
    private var isScrollHeader=false


    /**
     * 滑动到底部
     */
    private  var isScrollFooter=false


    /**
     * 是否分页,默认分页开启上下滑动更新
     */
    private var isPaging=false


    /**
     * 是否停止滚动
     */
    private var scrollState: Int?=null


    /**
     * 顶部加载view
     */
    private var headView:View?=null

    /**
     * 底部加载view
     */
    private var footerView:View?=null


    private var refreshCallback:RefreshCallback?=null



    constructor(context: Context, attributeset: AttributeSet):this(context,attributeset,0)

    constructor(context: Context):this(context,null,0)


    fun setRefreshCallback(refreshCallback: RefreshCallback){
        this.refreshCallback=refreshCallback
    }


    fun setIsPaging(isPaging:Boolean){
        this.isPaging=isPaging

    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        setOnScrollListener(this)
        headView=View.inflate(context,R.layout.header_view,null)
        headView!!.tips.setText(R.string.header_tips)
        footerView=View.inflate(context,R.layout.header_view,null)
        footerView!!.tips.setText(R.string.footer_tips)
        headView!!.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT)

        addHeaderView(headView!!)
        headView!!.visibility= View.GONE


//
//        this.removeViewInLayout(headView)

//        addFooterView(footerView!!)
//        headView!!.visibility=View.GONE
//        headView!!.setPadding(0,-1400,0,0)
//
//        headView!!.invalidate()
//        footerView!!.visibility=View.GONE
//        footerView!!.setPadding(0,-1400,0,0)
//
//        footerView!!.invalidate()
//
//        headView!!.requestLayout()
    }







    /**
     * scrolling,正在滑动时触发
     */
    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
//        log(message = "ExpandMasterialLayout onScroll ,and firstVisibleItem:$firstVisibleItem,visibleItemCount:$visibleItemCount,totalItemCount:$totalItemCount")
        if (isPaging){
            /**
             * 支持分页
             */
            isScrollHeader=false
            isScrollFooter=false

            if (adapter==null || adapter.count==0){
                /**
                 * 未设置adapter 或者子项为空时不做处理
                 */
                return
            }
            if (firstVisibleItem==0){
                log(message = "scroll to head")
                /**
                 * 首个显示
                 */
                isScrollHeader=true
            } else if(firstVisibleItem+visibleItemCount==totalItemCount){
                log(message = "scroll to footer")
                isScrollFooter=true
            }




        }


    }

    /**
     * scroll state changed,滑动状态改变时触发
     */
    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
//        log(message = "ExpandMasterialLayout onScrollStateChanged and scrollState:$scrollState")
        this.scrollState=scrollState
    }


    var startX=0f
    var startY=0f
    var actionY=0f

    /**
     * 通过滑动事件处理加载view滑动状态
     */
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (isPaging && (isScrollFooter || isScrollHeader) && scrollState==OnScrollListener.SCROLL_STATE_IDLE){

            /**
             * 处理滑动
             */


            when(ev!!.action){
                MotionEvent.ACTION_DOWN ->{
                    /**
                     * 按下
                     */
                    startX=ev.x
                    startY=ev.y
                }

                MotionEvent.ACTION_MOVE ->{
                    /**
                     * 滑动
                     */
                    actionY=ev.y-startY

                    if (actionY>0){
                        /**
                         * 向下滑动,headview处理
                         */

                        if (isScrollFooter){
                            if (headView!=null && headView!!.visibility!=View.VISIBLE){
                                headView!!.visibility=View.VISIBLE
                                headView!!.setPadding(0,0,0,0)
                            }
                            /**
                             * 头部刷新
                             */



                        }



                    }else if (actionY<0){
                        /**
                         * 向上滑动,footerView处理
                         */

                        if (isScrollHeader){
                            /**
                             * 底部刷新
                             */

                            if (footerView!=null && footerView!!.visibility!=View.VISIBLE){
                                footerView!!.visibility=View.VISIBLE
                                footerView!!.setPadding(0,0,0,0)
                            }
                        }



                    }



                }


                MotionEvent.ACTION_UP ->{
                    /**
                     * 抬起
                     */


                }


            }




















        }else{

            /**
             * 不能滑动
             */
            if (headView!=null && headView!!.visibility== View.VISIBLE){
                headView!!.visibility= View.GONE
            }
            if (footerView!=null && footerView!!.visibility== View.VISIBLE){
                footerView!!.visibility= View.GONE
            }
        }






        return super.onTouchEvent(ev)
    }






}