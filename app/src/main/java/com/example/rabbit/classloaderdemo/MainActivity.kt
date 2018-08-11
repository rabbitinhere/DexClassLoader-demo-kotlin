package com.example.rabbit.classloaderdemo

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import dalvik.system.DexClassLoader
import java.io.File

class MainActivity : AppCompatActivity() {
    private val TAG = "RA_Loader"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1)
    }

    fun dexClassLoader(view: View) {
        val optimizedDexOutputPath = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "1WORK" + File.separator + "addhelper-debug.apk")
        val dexOutputDir = this.getDir("dex", 0)
        val dexClassLoader = DexClassLoader(optimizedDexOutputPath.absolutePath, dexOutputDir.absolutePath, null, ClassLoader.getSystemClassLoader().parent)

        try {
            val libProviderClazz = dexClassLoader.loadClass("com.addhelper.rabbit.addhelper.Helper")
            val methods = libProviderClazz.declaredMethods
            for (i in methods.indices) {
                Log.i(TAG, " method>>" + methods[i].toString())
            }
            val method = libProviderClazz.getDeclaredMethod("Add", Int::class.java, Int::class.java)
            method.isAccessible = true
            val string = method.invoke(libProviderClazz.newInstance(), 3, 5).toString()
            Toast.makeText(this, string, Toast.LENGTH_LONG).show()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

    }
}
