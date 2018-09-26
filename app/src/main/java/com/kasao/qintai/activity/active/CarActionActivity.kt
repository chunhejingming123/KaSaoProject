package com.kasao.qintai.activity.active

import android.Manifest
import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.dialoge.DialogeInvited
import com.kasao.qintai.dialoge.DialogeInvitedSucess
import com.kasao.qintai.dialoge.DialogeRequestPermisson
import com.kasao.qintai.model.Info
import com.kasao.qintai.model.RtnSuss
import com.kasao.qintai.popuwindow.TakePhotoPopuWindow
import com.kasao.qintai.util.*
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.base.MyApplication
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.LogUtil
import com.kasao.qintaiframework.until.ToastUtil
import kotlinx.android.synthetic.main.activity_car_action.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.*
import java.net.URI
import java.util.*
import kotlin.collections.HashMap

class CarActionActivity : BaseActivity(), View.OnClickListener {
    var ivPhoto: View? = null
    var imgName: String = "card.jpg"
    var ivShFen: ImageView? = null
    var viewPhoto: View? = null
    var viSumbit: View? = null
    override fun onLayoutLoad(): Int {
        return R.layout.activity_car_action
    }

    override fun findView() {
        ivPhoto = findViewById(R.id.ivPhoto)
        ivShFen = findViewById(R.id.ivshenfen)
        viewPhoto = findViewById(R.id.viewPhoto)
        viSumbit = findViewById(R.id.viSumbit)
        findViewById<TextView>(R.id.tv).text = Html.fromHtml("<font color=#ffffff>凡成功上传本人</font>"
                + "<font color=#fff18b>A1/A2驾驶证</font><font color=#ffffff>照片即可获</font><font color=#fff18b>10000元重卡购车金补贴。</font><br><br>"
                + "<font color=#ffffff>该补贴电子券自领取之日起</font><font color=#fff18b>两年</font>" +
                "<font color=#ffffff>内有效，当月到店还可享</font><font color=#fff18b>购车福利。</font><br><br>"
                + "<font color=#ffffff>本活动最终解释权归山西钦泰汽贸集团有限公司所有。</font>"
        )

        ivPhoto?.setOnClickListener(this)
        viSumbit?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.viSumbit -> sumbit()
            else -> showPopuwindow()
        }
    }

    var window: TakePhotoPopuWindow? = null

    private fun showPopuwindow() {
        window = TakePhotoPopuWindow(this, findViewById(R.id.main))
        window!!.showPopuWindow(object : TakePhotoPopuWindow.OnTakePhotoStyle {
            override fun takePhoto() {
                takePhotos()
            }

            override fun ChosePhotoWall() {
                FileUtils.choosePhoto(this@CarActionActivity, FileUtils.REQUEST_CODE_PICK_IMAGE, Info.PHOTO_DIR.toString(), imgName)
            }
        })
    }

    private fun takePhotos() {
        val isFlag = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            val isAllow: Boolean = SharedPreferencesHelper.getInstance(this).getSharedPreference(ParmarsValue.PETSION_CAMER, false) as Boolean

            if (isAllow && !isFlag) {
                val dialoge = DialogeRequestPermisson(this)
                dialoge.setmSetting(object : DialogeRequestPermisson.ISettings {
                    override fun toSetting() {
                        val `in` = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", getPackageName(), null)
                        `in`.data = uri
                        startActivityForResult(`in`, PermissionsUtils.REQUEST_PHOTE_PERSION)
                    }
                })
                dialoge.showDialoge(getString(R.string.permission_camare), getString(R.string.permission_request_camare))
            } else {
                SharedPreferencesHelper.getInstance(this).put(ParmarsValue.PETSION_CAMER, ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PermissionsUtils.REQUEST_PHOTE_PERSION)
            }
        } else {
            //有权限，直接拍照
            val intent = Intent(this, RectCameraActivity::class.java)
            intent.putExtra(ParmarsValue.KEY_KEYWORD, imgName)
            startActivityForResult(intent, FileUtils.TAKE_PIC)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionsUtils.REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileUtils.choosePhoto(this, FileUtils.REQUEST_CODE_PICK_IMAGE, Info.PHOTO_DIR.toString(), imgName)
                } else {
                    ToastUtil.showAlter("permission denied")
                }
            }

            PermissionsUtils.REQUEST_PHOTE_PERSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，可以拍照
                    takePhotos()
                } else {
                    ToastUtil.showAlter("CAMERA PERMISSION DENIED")
                }
            }
        }
    }

    var auxFile: File? = null
    private var mHandler: Handler? = Handler()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FileUtils.TAKE_PIC -> {
                if (resultCode == RESULT_OK) {
                    try {
                        if (data != null) {
                            val path = data.getStringExtra(ParmarsValue.KEY_KEYWORD)
                            disPlayView(path)
                        }
                    } catch (e: Exception) {
                        LogUtil.e("Tag", e.message)
                    }

                }
            }
            FileUtils.REQUEST_CODE_PICK_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    try {
                        /**
                         * 该uri是上一个Activity返回的
                         */
                        var url: String? = ""
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            url = handleImageOnKitKat(data!!)
                            auxFile = File(url!!)
                        } else {
                            val uri = data?.getData()
                            auxFile = File(URI(uri!!.toString()))
                        }
                        mHandler?.postDelayed({
                            disPlayView(auxFile!!.getPath())
                        }, 150)
                    } catch (e: Exception) {
                        LogUtil.e("Tag", e.message)
                    }
                }
            }
        }
        if (null != window) {
            window?.dismisPop()
        }
    }

    var btp: Bitmap? = null

    @TargetApi(19)
    private fun handleImageOnKitKat(data: Intent): String? {
        var imagePath: String? = null
        val uri = data.data
        if (DocumentsContract.isDocumentUri(this, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri!!.authority) {
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.provider.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            imagePath = uri.path
        }
        return imagePath
    }

    private fun getImagePath(uri: Uri, selection: String?): String? {
        var Path: String? = null
        val cursor = getContentResolver().query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                Path = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor!!.close()
        }
        return Path
    }


    private fun disPlayView(url: String) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        var `is`: InputStream? = null
        try {
            `is` = FileInputStream(url)
            val size = `is`.available() / 1024
            val opts = BitmapFactory.Options()
            opts.inTempStorage = ByteArray(1000 * 1024)
            opts.inPreferredConfig = Bitmap.Config.RGB_565
            opts.inPurgeable = true
            opts.inInputShareable = true
            opts.inSampleSize = 2
            if (size > 1000) {
                btp = BitmapFactory.decodeStream(`is`, null, opts)
            } else {
                btp = BitmapFactory.decodeStream(`is`, null, null)
            }
            if (null != btp) {
                if (!TextUtils.isEmpty(imgName)) {
                    ImageTools.savePhotoToSDCard(ImageTools.compressImage(btp, Info.ImageSize),
                            Info.PHOTO_DIR.toString(), imgName)
                }
                ivshenfen?.setImageBitmap(FileUtils.compressImage(btp))
                viewPhoto?.visibility = View.GONE
                ivshenfen?.setOnClickListener(this)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun sumbit() {
        var dialoge = DialogeInvitedSucess(this)
        dialoge.showDialoge()
        var strmap = HashMap<String, String>()
        strmap["nickname"] = "suochuminf"
        strmap["user_sex"] = "男"
        strmap["user_id"] = BaseKasaoApplication.getUser().user_id
        if (null == btp) {
            return
        }
        var map = HashMap<String, RequestBody>()
        val path1 = Info.PHOTO_DIR.toString() + "/" + imgName
        if (!TextUtils.isEmpty(path1)) {
            val file = File(path1)
            map.put("user_img\";filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
        }
        if (null == map) {
            return
        }
        ApiManager.getInstance.upLoadData(ApiInterface.MODEIFYMY, strmap, map, object : HttpRespnse {
            override fun _onComplete() {
            }

            override fun _onNext(t: ResponseBody) {
                var domai = GsonUtil.GsonToBean(t.string(), RtnSuss::class.java)
                var dialoge = DialogeInvited(this@CarActionActivity)
                dialoge.showDialoge()

            }

            override fun _onError(e: Throwable) {
                LogUtil.e("-----e=" + e.localizedMessage)
            }
        })
    }

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

}
