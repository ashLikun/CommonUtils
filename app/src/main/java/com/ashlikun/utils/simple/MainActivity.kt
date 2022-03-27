package com.ashlikun.utils.simple

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.location.*
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.ashlikun.utils.encryption.AESUtils
import com.ashlikun.utils.main.ProcessUtils
import com.ashlikun.utils.other.*
import com.ashlikun.utils.other.coroutines.taskAsync
import com.ashlikun.utils.other.coroutines.taskLaunchMain
import com.ashlikun.utils.other.spannable.XClickableSpan
import com.ashlikun.utils.other.store.StoreUtils
import com.ashlikun.utils.other.worker.WorkFlow
import com.ashlikun.utils.simple.databinding.MainViewgroupActivityBinding
import com.ashlikun.utils.ui.NotificationUtil
import com.ashlikun.utils.ui.ScreenUtils
import com.ashlikun.utils.ui.extend.dp
import com.ashlikun.utils.ui.extend.windowBrightness
import com.ashlikun.utils.ui.image.BitmapUtil
import com.ashlikun.utils.ui.image.DrawableUtils
import com.ashlikun.utils.ui.modal.SuperToast
import com.ashlikun.utils.ui.modal.ToastUtils
import com.ashlikun.utils.ui.resources.ResUtils
import com.ashlikun.utils.ui.status.StatusBarCompat
import com.ashlikun.utils.ui.text.FocusLinkMovementMethod
import com.ashlikun.utils.ui.text.SpannableUtils
import kotlinx.coroutines.delay
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    internal lateinit var statusBarCompat: StatusBarCompat
    val binding by lazy {
        MainViewgroupActivityBinding.inflate(layoutInflater)
    }

    var currentProgress = 0
    internal var statusColor = -0x10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        statusBarCompat = StatusBarCompat(this)
        statusBarCompat.setStatusBarColor(-0x1)
        binding.buttonJump.setOnClickListener {
            ToastUtils.show("22222222")
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)

        }
        val drawable = GradientDrawable()
        val size = DimensUtils.dip2px(this, 4f)
        drawable.setBounds(0, 0, size, size)
        drawable.setColor(-0x5a3b1)
        windowBrightness = 10f
        drawable.cornerRadius = size / 2f
        LogUtils.e("aaaaaaaaaaaaaa " + ProcessUtils.curProcessName);
        DeviceUtil.get()
//        textView.text = SpannableUtils.getBuilder("")
//                .append("").setResourceId(R.mipmap.main_icon_catalogue_icon).changImageSize()
//                .append("文案已复制图案已保存到相册图案已保存到相册图案已保存到相册图案已保存到相册图案已保存到相册图案已保存到相册\n")
//                .append("[space]").blockSpaceHeight(DimensUtils.dip2px(100f))
//                .append("\n")
//                .append("").setResourceId(R.mipmap.main_icon_catalogue_icon).setProportion(0.5f).changImageSize()
//                .append("图案已保存到相册").setProportion(0.5f).setForegroundColorRes(R.color.colorAccent)
//                .create()
        binding.text.text = SpannableUtils.getBuilder("")
            .append("相册\n").setForegroundColor(0xffff0000.toInt())
            .setBullet(20, resources.getColor(R.color.black), 30)
            .append("文案已复制图案已保存到相册图案已保存到相册图案已保存到相册图案已保存到相册图案已保存到相册图案已保存到相册\n")
            .setForegroundColor(0xffff0000.toInt())
            .setBullet(20, resources.getColor(R.color.black), 30)
            .append("文案已复制图案已保存到相册图案已保存到相册图案已保存到相册图案已保存到相册图案已保存到相册图案已保存到相册")
            .setBullet(20, resources.getColor(R.color.black), 30)
            .append("点击事件").setForegroundColorRes(R.color.black)
            .setClickSpan(object : XClickableSpan(ResUtils.getColor(R.color.colorPrimary)) {
                override fun onClick(widget: View) {
                    LogUtils.e("11111111111点击事件11")
                }

            })
            .create()
        binding.text.setMovementMethods(FocusLinkMovementMethod.instance)

//        binding.text.setOnClickListener {
//            LogUtils.e("222222222222点击事件")
//        }
        binding.textRoot.setOnClickListener {
            LogUtils.e("333333333333333点击事件")
        }
        binding.textRoot.setOnLongClickListener {
            LogUtils.e("4444444444点击事件")
            true
        }
        WorkFlow.Builder()
            .addWork {
                object : Thread() {
                    override fun run() {
                        super.run()
                        Thread.sleep(2000)
                        Log.e("aaaa", "第一个执行完")
                        it.onOk()
                    }
                }.start()
            }
            .addWork {
                object : Thread() {
                    override fun run() {
                        super.run()
                        Thread.sleep(3000)
                        Log.e("aaaa", "第二个执行完")
                        it.onOk()
                    }
                }.start()
            }
            .build()
            .start()

        binding.buttonJump.background = DrawableUtils.createGradientDrawable(0xffff0000.toInt())
            .roundRadiu(floatArrayOf(30f, 30f, 0f, 20f))
            .create()

//        StoreUtils.putString("testString", "testString")
//        StoreUtils.putInt("testInt", 22)
//        StoreUtils.putBoolean("testBoolean", true)
//        StoreUtils.putFloat("testFloat", 33f)
//        StoreUtils.putLong("testLong", 22332)
//        StoreUtils.putSet("testSet", setOf("aadd"))

        StoreUtils.getString("testString").logge()
        StoreUtils.getInt("testInt").logge()
        StoreUtils.getBoolean("testBoolean").logge()
        StoreUtils.getFloat("testFloat").logge()
        StoreUtils.getLong("testLong").logge()
        StoreUtils.getSet("testSet").logge()
        StoreUtils.getParcelable("testParcelable", TestParcelable())?.logge()
    }

    fun onView1Click(view: View) {
        SuperToast.get("aaaaaaa").info();

    }

    private inner class MyClickableSpan : XClickableSpan() {


        override fun onClick(widget: View) {
            SuperToast.get("aaaaaaa").info()
            SuperToast.get("bbbbbb").info()
            val intent = Intent(this@MainActivity, NotificationActivity::class.java)
            NotificationUtil.notification(
                123,
                R.mipmap.ic_launcher_round,
                "标题",
                "你收到通知啦",
                intent = intent
            )
        }
    }

    fun onView2Click(view: View) {
        statusColor = statusColor + 10
        statusBarCompat.setStatusBarColor(statusColor)
        //SuperToast.get("aaaaaaa").ok();
    }

    fun onView3Click(view: View) {
        // SuperToast.get("aaaaaaa").warn();
    }

    val ss =
        "{\"name\":\"\\u674e\\u6b23\\u6d0b\",\"paperstype\":\"1\",\"papersnumber\":\"222426199407031415\",\"mobile\":\"18506181482\"}"

    fun onView4Click(view: View) {
        Toast.makeText(this.application,"wwwwww",Toast.LENGTH_SHORT).show()
        ThreadUtils.execute {
//            ToastUtils.showLong("aaaaaaaaaaaaaaaaaa")
            LogUtils.e("aaaaaaaaaaaaaaaaaa    ")
        }
        val logo = BitmapUtil.decodeResource(R.mipmap.ic_launcher_round, 50.dp, 50.dp)
        //        imageView.setDrawingCacheEnabled(true);
        //        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        //        imageView.setDrawingCacheEnabled(false);
        val bitmap =
            BitmapUtil.decodeResource(R.mipmap.timg, ScreenUtils.width, ScreenUtils.height)
        binding.imageView.setImageBitmap(BitmapUtil.getWaterMaskImage(bitmap, logo))

        val aaa = AesAAUtil.encrypt(ss, "NeYHTiTLQp8J" + "\u0000\u0000\u0000\u0000")
        val aaaa = AESUtils.encrypt(ss, "NeYHTiTLQp8J")
        LogUtils.e("aaaa " + aaa ?: "")
        LogUtils.e("aaaaaaaa    " + aaaa)

        ToastUtils.show("1111111111")
    }

    var builder: NotificationCompat.Builder? = null
    fun onView5Click(view: View) {
        LogUtils.e(NotificationUtil.isNotificationEnabled())
        builder = NotificationUtil.createBuilder(
            R.mipmap.ic_launcher,
            "测试通知",
            "通知内容", defaults = NotificationCompat.DEFAULT_ALL
        )
        NotificationUtil.show(10, builder!!)

        taskLaunchMain {

            LogUtils.e(Thread.currentThread().name)
            val aa = taskAsync {
                delay(3000)
                LogUtils.e(Thread.currentThread().name)
                1
            }
            LogUtils.e(aa.await())
        }
//        var bb = "{\"nickname\":\"\\u5b59\\u8d5b-Simon\",\"mobile\":\"13285112318\"}"
//        var aa =
//            "8xxxSRkzLfjuzFkhbP4YYrijDYm5v5ZTgve79+C7ozXhE/d70RGlfyxI6PRBStX7XexXcbD/QlEerr5clbbDzQ=="
//        try {
//            var aaa = AESUtils.decryptHex(aa, "8d68a9777b8b7115364452c712837616")
//            LogUtils.e(aaa)
////            aadd = AESUtils.encrypt(bb, "8d68a9777b8b7115364452c712837616")
////            LogUtils.e(aadd)
////            aaa = AESUtils.encryptHex(bb, "8d68a9777b8b7115364452c712837616")
////            LogUtils.e(aaa)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    val runable = Runnable {
        builder?.setContentText("更新进度$currentProgress%");
        if (currentProgress >= 100) {
            builder?.setContentText("下载完成")
        }
        builder?.setProgress(100, currentProgress++, false)
        NotificationUtil.show(10, builder!!)
        startPost()
    }

    private fun startPost() {
        if (currentProgress <= 100) {
            MainHandle.postDelayed(runable, 1000)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        getLocation()
    }

    fun getLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1123
            )
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0f,
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Log.e("aaa", "location lat:" + location.latitude + ",lon:" + location.longitude)
                    val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                    var addresses: List<Address>? = null
                    try {
                        addresses = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude, 1
                        )
                    } catch (ioException: IOException) {

                        Log.e("aaa", "服务不可用！", ioException)

                    }

                    if (addresses == null || addresses.size == 0) {
                        Log.e("aaa", "没有找到相关地址!")
                    } else {
                        val address = addresses[0]
                        val addressFragments = ArrayList<String>()
                        var curAddr = ""
                        for (i in 0 until address.maxAddressLineIndex) {
                            addressFragments.add(address.getAddressLine(i))
                            curAddr = curAddr + address.getAddressLine(i)
                        }

                        if (!TextUtils.isEmpty(address.featureName)
                            && !addressFragments.isEmpty()
                            && addressFragments[addressFragments.size - 1] != address.featureName
                        ) {
                            addressFragments.add(address.featureName)
                            curAddr = curAddr + address.featureName
                        }
                        Log.e("", "详情地址已经找到,地址:$curAddr")
                    }

                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

                }

                override fun onProviderEnabled(provider: String) {

                }

                override fun onProviderDisabled(provider: String) {

                }
            })
    }
}
