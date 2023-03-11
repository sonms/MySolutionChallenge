package com.example.mysolutionchallenge.Navigation

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mysolutionchallenge.R
import com.example.mysolutionchallenge.databinding.FragmentCameraBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var cameraBinding: FragmentCameraBinding
    private val requestCamera = 1
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it -> setGallery(uri = it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraBinding = FragmentCameraBinding.inflate(inflater, container, false)


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

        cameraBinding.galleryBtn.setOnClickListener {
            launcher.launch("image/*")
        }


        return cameraBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCamera && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            cameraBinding.cameraIV.setImageBitmap(imageBitmap)
        }
    }

    fun setGallery(uri : Uri?) {
        cameraBinding.cameraIV.setImageURI(uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CameraFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CameraFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}