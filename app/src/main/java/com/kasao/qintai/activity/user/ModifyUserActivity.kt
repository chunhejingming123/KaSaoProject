package com.kasao.qintai.activity.user

import android.Manifest
import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.kasao.qintai.R
import com.kasao.qintai.api.ApiInterface
import com.kasao.qintai.api.ApiManager
import com.kasao.qintai.base.BaseKasaoApplication
import com.kasao.qintai.dialoge.DialogeRequestPermisson
import com.kasao.qintai.model.Info
import com.kasao.qintai.model.domain.User
import com.kasao.qintai.model.domain.UserDomain
import com.kasao.qintai.popuwindow.TakePhotoPopuWindow
import com.kasao.qintai.util.*
import com.kasao.qintai.widget.CircleImageView
import com.kasao.qintaiframework.base.BaseActivity
import com.kasao.qintaiframework.http.HttpRespnse
import com.kasao.qintaiframework.until.GsonUtil
import com.kasao.qintaiframework.until.ToastUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class ModifyUserActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    var tv_cradphone: TextView? = null
    var username: EditText? = null
    var icon: CircleImageView? = null
    var sexstr = "男"
    var photoPath: String? = ""
    var photoName: String? = null
    val TAKE_PHOTO = 1//拍照
    val CHOOSE_PHOTO = 2//选择相册
    val PICTURE_CUT = 3//剪切图片
    var imageUri: Uri? = null//相机拍照图片保存地址
    var isClickCamera: Boolean = false//是否是拍照裁剪
    var rb_ginfo_r1: RadioButton? = null
    var rb_ginfo_r2: RadioButton? = null
    var outputUri: Uri? = null//裁剪万照片保存地址
    var imagePath: String? = null//打开相册选择照片的路径

    override fun onLayoutLoad(): Int {
        return R.layout.activity_modify_user
    }

    override fun findView() {
        val tv_title = findViewById<TextView>(R.id.tvTitle)
        tv_title.text = "个人信息"
        val ll_back = findViewById<View>(R.id.viewBack)
        ll_back.setOnClickListener(this)
        findViewById<ImageView>(R.id.ivBack).setImageResource(R.drawable.icon_return_black)
        username = findViewById(R.id.my_username)
        username?.setCursorVisible(false)
        tv_cradphone = findViewById(R.id.tv_cradphone)
        icon = findViewById(R.id.act_my_icon)

        rb_ginfo_r1 = findViewById(R.id.rb_ginfo_r1)
        rb_ginfo_r2 = findViewById(R.id.rb_ginfo_r2)

        rb_ginfo_r2?.setOnCheckedChangeListener(this)
        rb_ginfo_r1?.setOnCheckedChangeListener(this)
        username?.setOnClickListener(View.OnClickListener { username?.setCursorVisible(true) })
        icon?.setOnClickListener(this)
        SoftKeybordUtil.setSoftKeyboardListener(this, object : SoftKeybordUtil.OnSoftKeyboardChangeListener {
            override fun onSoftKeyBoardChange(softKeybardHeight: Int, isVisible: Boolean) {
                if (isVisible) {

                } else {
                    val user = BaseKasaoApplication.getUser()
                    if (user.nickname.equals(username?.getText().toString())) {
                    } else {
                       summbit()
                    }
                }
            }
        })
        rendView()
    }

    var user: User? = null

    override fun rendView() {
        user = BaseKasaoApplication.getUser()
        username?.setText(user?.nickname)
        GlideUtil.into(this, user?.user_img, icon, R.drawable.default_avater)
        if (TextUtils.isEmpty(user?.user_mobile) || "0" == user?.user_mobile) {
            tv_cradphone?.text = ("绑定手机")
            tv_cradphone?.setOnClickListener(this)
        } else {
            tv_cradphone?.text = (user?.user_mobile)
        }
        if (user!!.user_sex.endsWith("男")) {
            rb_ginfo_r1?.setChecked(true)
        } else if (user!!.user_sex.endsWith("女")) {
            rb_ginfo_r2?.setChecked(true)
        }
    }

    override fun onClick(view: View?) {
        when (view?.getId()) {
            R.id.act_my_icon -> addimage()
            R.id.viewBack -> finish()
//            R.id.tv_cradphone -> {
//                val bundle = Bundle()
//                bundle.putString(ParmarsValue.KEY_target, username?.getText().toString())
//                startActivity(BindCountActivity::class.java, bundle)
//            }
            else -> {
            }
        }
    }

    override fun onCheckedChanged(radio: CompoundButton?, p1: Boolean) {
        if (p1) {
            if (radio === rb_ginfo_r1) {
                sexstr = "男"
                summbit()
            }
            if (radio === rb_ginfo_r2) {
                sexstr = "女"
                summbit()
            }

        }
    }

    private fun summbit() {
        var path = ""
        if (!photoPath.isNullOrEmpty()) {
            //添加图片压缩功能   参照社区图片上传
            val bitmap = ImageTools.getimage(photoPath)
            path = ImageTools.saveBitmapToSDcard(this@ModifyUserActivity, bitmap)
        }
        var map = HashMap<String, RequestBody>()
        map["nickname"] = toRequestBody(username?.getText().toString())
        map["user_sex"] = toRequestBody(sexstr)
        map["user_id"] = toRequestBody(user?.user_id!!)
        if (!path.isNullOrEmpty()) {
            val file = File(path)
            map["user_img\";filename=\"" + file.name] = RequestBody.create(MediaType.parse("image/*"), file)
        }
        ApiManager.getInstance.upLoadData(ApiInterface.MODEIFYMY, map, object : HttpRespnse {
            override fun _onComplete() {

            }

            override fun _onNext(t: ResponseBody) {
                var useDomani = GsonUtil.GsonToBean(t.string(), UserDomain::class.java)
                if (null != useDomani && null != useDomani.data) {
                    BaseKasaoApplication.setUser(useDomani.data)
                    rendView()
                }

            }

            override fun _onError(e: Throwable) {

            }
        })

    }

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    fun addimage() {
        val window = TakePhotoPopuWindow(this, findViewById(R.id.main))
        window.showPopuWindow(object : TakePhotoPopuWindow.OnTakePhotoStyle {
            override fun takePhoto() {
                if (ImageTools.checkSDCardAvailable()) {
                    window.dismisPop()
                    val isFlag = ActivityCompat.shouldShowRequestPermissionRationale(this@ModifyUserActivity, Manifest.permission.CAMERA)
                    if (ContextCompat.checkSelfPermission(this@ModifyUserActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
                        var isAllow: Boolean = SharedPreferencesHelper.getInstance(this@ModifyUserActivity).getSharedPreference(ParmarsValue.PETSION_CAMER, false) as Boolean
                        if (isAllow && !isFlag) {
                            val dialoge = DialogeRequestPermisson(this@ModifyUserActivity)
                            dialoge.setmSetting(object : DialogeRequestPermisson.ISettings {
                                override fun toSetting() {
                                    val `in` = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", packageName, null)
                                    `in`.data = uri
                                    startActivityForResult(`in`, PermissionsUtils.REQUEST_PHOTE_PERSION)
                                }
                            })
                            dialoge.showDialoge(getString(R.string.permission_camare), getString(R.string.permission_request_camare))
                        } else {
                            SharedPreferencesHelper.getInstance(this@ModifyUserActivity).put(ParmarsValue.PETSION_CAMER, ActivityCompat.shouldShowRequestPermissionRationale(this@ModifyUserActivity, Manifest.permission.CAMERA))
                            ActivityCompat.requestPermissions(this@ModifyUserActivity,
                                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    PermissionsUtils.REQUEST_PHOTE_PERSION)
                        }
                    } else {
                        //有权限，直接拍照
                        openCamera()
                    }


                } else {
                    ToastUtil.showAlter("没有sd卡")
                }
            }

            override fun ChosePhotoWall() {
                //动态权限
                if (ContextCompat.checkSelfPermission(this@ModifyUserActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@ModifyUserActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                } else {
                    selectFromAlbum()//打开相册
                }
                window.dismisPop()
            }
        })
    }

    private fun selectFromAlbum() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        } else {
            openAlbum()
        }
    }

    private fun openAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, CHOOSE_PHOTO) // 打开相册
    }

    protected fun openCamera() {
        try {
            val outputImage = File(externalCacheDir, "output_image.jpg")
            try {
                if (outputImage.exists()) {
                    outputImage.delete()
                }
                outputImage.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (Build.VERSION.SDK_INT < 24) {
                imageUri = Uri.fromFile(outputImage)
            } else {
                //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
                //参数二:fileprovider绝对路径 com.dyb.testcamerademo：项目包名
                imageUri = FileProvider.getUriForFile(this, "com.qintai.meike.fileprovider", outputImage)
            }
            // 启动相机程序
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, TAKE_PHOTO)
        } catch (e: ActivityNotFoundException) {
            ToastUtil.showAlter("Photo NotFound")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            TAKE_PHOTO -> startPhotoZoom(imageUri)
            CHOOSE_PHOTO ->
                // 判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(data!!)
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(data!!)
                }
            PICTURE_CUT -> {
                isClickCamera = true
                var bitmap: Bitmap? = null
                if (null == data) {
                    return
                }
                try {
                    photoName = System.currentTimeMillis().toString() + ".jpg"
                    photoPath = Info.PHOTO_DIR.toString() + "/" + photoName
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M && data.data != null) {
                        bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data.data))
                    } else {
                        bitmap = data.getParcelableExtra("data")
                    }

                    if (null == bitmap) {
                        if (isClickCamera) {
                            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(outputUri))
                        } else {
                            bitmap = BitmapFactory.decodeFile(imagePath)
                        }
                    }
                    if (null == bitmap) {
                        return
                    }
                    icon?.setImageBitmap(bitmap)
                    ImageTools.savePhotoToSDCard(ImageTools.compressImage(bitmap, Info.ImageSize),
                            Info.PHOTO_DIR.toString(), photoName)
                      summbit()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            }
        }
    }

    // 4.4及以上系统使用这个方法处理图片 相册图片返回的不再是真实的Uri,而是分装过的Uri
    @TargetApi(19)
    private fun handleImageOnKitKat(data: Intent) {
        imagePath = null
        val uri = data.data
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri!!)
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1] // 解析出数字格式的id
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.path
        }
        startPhotoZoom(uri)
    }

    private fun getImagePath(uri: Uri?, selection: String?): String? {
        var path: String? = null
        // 通过Uri和selection来获取真实的图片路径
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    private fun handleImageBeforeKitKat(data: Intent) {
        val uri = data.data
        imagePath = getImagePath(uri, null)
        startPhotoZoom(uri)
    }

    private fun startPhotoZoom(uri: Uri?) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        val file = File(externalCacheDir, "crop_image.jpg")
        try {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        outputUri = Uri.fromFile(file)
        val intent = Intent("com.android.camera.action.CROP")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.setDataAndType(uri, "image/*")
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)
        intent.putExtra("return-data", true)
        intent.putExtra("noFaceDetection", true)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        try {
            startActivityForResult(intent, PICTURE_CUT)
        } catch (e: ActivityNotFoundException) {
            ToastUtil.showAlter("您的设备不支持照片剪裁")
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum()
            } else {
                ToastUtil.showAlter("You denied the permission")
            }
            PermissionsUtils.REQUEST_PHOTE_PERSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                openCamera()
            } else {
                ToastUtil.showAlter("CAMERA PERMISSION DENIED")
            }
        }
    }

}
