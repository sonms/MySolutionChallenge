package com.example.mysolutionchallenge

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mysolutionchallenge.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {
    val requestCamera = 1
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var cameraBinding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(cameraBinding.root)

        cameraBinding.cameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)//.also {
                /*imageCaptureIntent -> imageCaptureIntent.resolveActivity(packageManager)?.also {
                    activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())  { result ->
                        print(result.resultCode)
                        if (result.resultCode == 1) {

                        }
                    }
                    }*/
            //}
            startActivityForResult(intent, requestCamera)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCamera && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            cameraBinding.cameraIV.setImageBitmap(imageBitmap)
        }
    }
}