package com.dyf.dyfkotlindemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dyf.dyfkotlindemo.bluetooth.BlueActivity
import com.dyf.dyfkotlindemo.ipcdemo.socket.TCPClientActivity
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    inner class UI : AnkoComponent<MainActivity> {
        override fun createView(ui: AnkoContext<MainActivity>): View {
            return with(ui){
                scrollView {
                    verticalLayout {
                        val textView=textView("我是一个TextView"){
                            textSize = sp(17).toFloat()
                            textColor=context.resources.getColor(R.color.red)
                        }.lparams{
                            margin=dip(10)
                            height= wrapContent
                            width= matchParent
                        }
//                        val name = editText("EditText")
//                        button("Button") {
//                            onClick { view ->
//                                toast("Hello, ${name.text}!")
//                                //
//                                alert ("我是Dialog"){
//                                    yesButton {
//                                        toast("yes")
//                                        doAsync {
//                                            //后台执行代码
//                                            uiThread {
//                                                //UI线程
//                                                toast("线程${Thread.currentThread().name}")
//
//                                            }
//                                        }
//                                    }
//                                    noButton {  toast("no")}
//                                }.show()
//                            }
//                        }.lparams{
//                            margin=dip(20)
//                            height= wrapContent
//                            width= matchParent
//                        }

                        button("Socket IPC ") {
                            onClick { view ->
                                navigate<TCPClientActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("utest") {
                            onClick { view ->
                                navigate<UActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("myTest") {
                            onClick { view ->
                                navigate<MyTestActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("DragView") {
                            onClick { view ->
                                navigate<DragViewActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("Clock") {
                            onClick { view ->
                                navigate<CloclActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("Nested") {
                            onClick { view ->
                                navigate<NestedActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("ImageCycle") {
                            onClick { view ->
                                navigate<RecyleActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("litepal") {
                            onClick { view ->
                                navigate<DataActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("weather demo") {
                            onClick { view ->
                                navigate<com.dyf.coolweather.android.MainActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }


                        button("wallpaper demo") {
                            onClick { view ->
                                navigate<com.dyf.dyfkotlindemo.wallpaper.MainActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("audio demo") {
                            onClick { view ->
                                navigate<com.dyf.dyfkotlindemo.audio.MainActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("bluetooth demo") {
                            onClick { view ->
                                navigate<com.dyf.dyfkotlindemo.bluetooth.blue.BlueActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }
                        button("nohttp demo") {
                            onClick { view ->
                                navigate<com.dyf.dyfkotlindemo.nohttp.MainActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }

                        button("saolei demo") {
                            onClick { view ->
                                navigate<com.dyf.dyfkotlindemo.saolei.MainActivity>()
                            }
                        }.lparams{
                            margin=dip(20)
                            height= wrapContent
                            width= matchParent
                        }
                    }
                }
            }
        }
    }


    inline public fun <reified T : Activity> Activity.navigate(bundle: Bundle? = null) {
        val intent = Intent(this, T::class.java)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }


    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UI().setContentView(this@MainActivity)
    }

    /**
     * Basics grammar
     */

    /**
     *
     */
    fun sum(a: Int, b: Int) = a + b;

    fun main(args: Array<String>) {
        println("sum of 19 and 23 is ${sum(19, 23)}")
    }
}
