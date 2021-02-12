package com.example.epicture.controller

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.epicture.model.model
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException


class uploadController(private val context: Context?): Callback
{
    private val GALLERY_REQUEST = 123
    private val CAMERA_REQUEST = 321
    private val AUTH_CAMERA_RQ = 100
    private val AUTH_GALLERY_RQ = 101
    var model = model()
    var b64file = ""

    public fun takeImage(cameraButton: ImageView) {
        cameraButton.setOnClickListener {
            this.openCamera()
        }
    }

    public fun addImage(imageView: ImageView) {
        imageView.setOnClickListener {
            this.openGallery()
        }
    }

    public fun openCamera() {
        if (!this.checkPicturePermission()) {
            this.requestPicturePermission()
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult((this.context as Activity), intent, CAMERA_REQUEST, null)
        }
    }

    public fun openGallery() {
        if (!this.checkReadPermission()) {
            this.requestReadPermission()
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult((this.context as Activity), intent, GALLERY_REQUEST, null)
        }
    }

    public fun checkReadPermission(): Boolean {
        var perm = this.context?.checkSelfPermission("READ_EXTERNAL_STORAGE")
        if (perm == PackageManager.PERMISSION_GRANTED)
            return (true)
        else
            return (false)
    }

    public fun requestReadPermission() {
        ActivityCompat.requestPermissions(
            this.context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            AUTH_GALLERY_RQ
        )
    }

    public fun checkPicturePermission(): Boolean {
        var perm = this.context?.checkSelfPermission("WRITE_EXTERNAL_STORAGE")
        var perm2 = this.context?.checkSelfPermission("CAMERA")
        if (perm == PackageManager.PERMISSION_GRANTED && perm2 == PackageManager.PERMISSION_GRANTED)
            return (true)
        else
            return (false)
    }

    public fun requestPicturePermission() {
        ActivityCompat.requestPermissions(
            (this.context as Activity), arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), AUTH_CAMERA_RQ
        )
    }

    public fun submit(
        uploadButton: Button,
        postTitle: EditText,
        description: EditText,
        sharedPreferences: SharedPreferences
    ) {
        uploadButton.setOnClickListener {
            var title = postTitle.text
            var tdescription = description.text
            if (this.b64file == "") {
                Toast.makeText(
                    this.context,
                    "You need to choose a file from gallery !",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                this.sendImageToImgur(b64file, title, tdescription, sharedPreferences)
            }
        }
    }

    public fun sendImageToImgur(
        file: String,
        title: Editable,
        description: Editable,
        sharedPreferences: SharedPreferences
    ) {
        val clientId = "Client-ID " + model.getClientId().toString()
        println(clientId)
        val client: OkHttpClient = OkHttpClient().newBuilder()
            .build()
        val mediaType: MediaType? = MediaType.parse("text/plain")
        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("image", file)
            .addFormDataPart("title", title.toString())
            .addFormDataPart("description", description.toString())
            .build()
        val request: Request = Request.Builder()
            .url("https://api.imgur.com/3/image")
            .method("POST", body)
            .addHeader(
                "authorization",
                "Bearer " + model.getAccessToken(sharedPreferences).toString()
            )
            .build()
        val response: Unit = client.newCall(request).enqueue(this)
    }

    override fun onFailure(call: Call, e: IOException) {
        println("RequestFailed check internet connexion")
    }

    override fun onResponse(call: Call, response: Response) {
        var data = response.body()?.string()
        Handler(Looper.getMainLooper()).post(Runnable {
            Toast.makeText(context, "Your picture have been uploaded", Toast.LENGTH_SHORT).show()
        })
    }

    public fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        imageView: ImageView
    ) {
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK)
        {
            val contentURI = data!!.data
            var bitmap = MediaStore.Images.Media.getBitmap(
                this.context?.contentResolver,
                contentURI
            );
            if (bitmap != null) {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                this.b64file = Base64.encodeToString(
                    byteArrayOutputStream.toByteArray(),
                    Base64.NO_WRAP
                )
                imageView.setImageURI(contentURI)
            } else {
                Toast.makeText(context, "Could not retrieve file", Toast.LENGTH_LONG).show()
                imageView.setImageURI(null)
            }

        } else if (requestCode == this.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            var photo = data?.extras?.get("data");
            if(photo != null) {
                imageView.setImageBitmap(photo as Bitmap?)
                val byteArrayOutputStream = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                this.b64file = Base64.encodeToString(byteArray, Base64.NO_WRAP)
            }
        }
    }

    public fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (permissions.size == 2 && requestCode == this.AUTH_CAMERA_RQ) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult((this.context as Activity), intent, CAMERA_REQUEST, null)
            } else {
                Toast.makeText(
                    context,
                    "You wont be able to use the camera if you refuse the permissions",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (permissions.size == 1 && requestCode == this.AUTH_GALLERY_RQ) {
            //TODO check if permissions have been granted or tell the user with a toast that he cannot use the app
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult((this.context as Activity), intent, GALLERY_REQUEST, null)
            } else {
                Toast.makeText(
                    context,
                    "You wont be able to use the gallery if you refuse the permissions",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}