package com.dyf.dyfkotlindemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
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
                        val name = editText("EditText")
                        button("Button") {
                            onClick { view ->
                                toast("Hello, ${name.text}!")
                                //
                                alert ("我是Dialog"){
                                    yesButton {
                                        toast("yes")
                                        doAsync {
                                            //后台执行代码
                                            uiThread {
                                                //UI线程
                                                toast("线程${Thread.currentThread().name}")

                                            }
                                        }
                                    }
                                    noButton {  toast("no")}
                                }.show()
                            }
                        }


                        button("Socket IPC ") {
                            onClick { view ->
                                navigate<TCPClientActivity>()
                            }
                        }
                        button("utest") {
                            onClick { view ->
                                navigate<UActivity>()
                            }
                        }
                        button("myTest") {
                            onClick { view ->
                                navigate<MyTestActivity>()
                            }
                        }
                        button("DragView") {
                            onClick { view ->
                                navigate<DragViewActivity>()
                            }
                        }
                        button("Clock") {
                            onClick { view ->
                                navigate<CloclActivity>()
                            }
                        }
                        button("Nested") {
                            onClick { view ->
                                navigate<NestedActivity>()
                            }
                        }
                        button("ImageCycle") {
                            onClick { view ->
                                navigate<RecyleActivity>()
                            }
                        }
                        button("litepal") {
                            onClick { view ->
                                navigate<DataActivity>()
                            }
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
