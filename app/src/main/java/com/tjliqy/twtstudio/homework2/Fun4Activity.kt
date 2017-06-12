package com.tjliqy.twtstudio.homework2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.View
import android.widget.Toast

/**
 * Created by tjliqy on 2017/6/12.
 */

/*
Activity的拓展函数，用于Activity之间的跳转
 */
inline public fun <reified T : Activity> Activity.navigate(bundle: Bundle? = null, requestCode: Int?) {//为了获得传递进来的Activity的class，使用inline reified 等关键字对泛型进行修饰
    //实例化Intent，第二个参数表示是.class类型
    val intent = Intent(this, T::class.java)

    //如果存在bundle参数，就添加
    if (bundle != null) {
        intent.putExtras(bundle)
    }

//    如果存在request参数，则用startActivityForResult()方法，否则直接调用intent方法
    if (requestCode != null) {
        startActivityForResult(intent, requestCode)
    } else {
        startActivity(intent)
    }
}

/*
传入String弹出tosat
 */
fun Activity.toast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

/*
跳转返回时，传回参数并finish当前activity
 */
fun Activity.navigateBack(bundle: Bundle? = null) {
    if (bundle != null){
        intent.putExtras(bundle)
    }
    setResult(Activity.RESULT_OK, intent)
    finish()
}

fun <T : View> Activity.bind(@IdRes res: Int): Lazy<T> {//这个方法是一个代理方法
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE) { findViewById(res) as T } //返回值是lazy表示这个方法不会立即执行，而是在该值第一次被使用的时候执行
}