package cn.enjoytoday.expandrefresh

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import com.github.johnpersano.supertoasts.SuperToast

/**
 * log control,输出log信息,通过添加系统属性控制log信息输出
 */
fun Any.log(tag:String?=this.javaClass.simpleName,message:String){
    val TAG:String = tag ?: "test"
    if (BuildConfig.DEBUG){
        Log.e(TAG, message)
    }

}

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun Any.dip2px(context: Context, dpValue: Float): Float {
    val scale = context.resources.displayMetrics.density
    return dpValue * scale + 0.5f
}

/**
 * toast add.
 */
fun Activity.toast(message: CharSequence) {
    val superToast= SuperToast(this.applicationContext)
    superToast.animations = SuperToast.Animations.FLYIN
    superToast.duration = SuperToast.Duration.VERY_SHORT
    superToast.textColor = Color.parseColor("#ffffff")
    superToast.setTextSize(SuperToast.TextSize.SMALL)
    superToast.text=message
    superToast.background = android.R.color.transparent
    superToast.show()
}

/**
 * 格式化给定的日期,输出为字符串格式
 * @param 需要给是化的日期,默认为当前
 */
fun Any.formatDate(millils: Long = System.currentTimeMillis()):String{
    return java.text.DateFormat.getDateInstance().format(millils)
}