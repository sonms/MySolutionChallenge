package com.example.mysolutionchallenge.Navigation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mysolutionchallenge.databinding.FragmentCameraBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

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
    private lateinit var sendImage : Uri
    private lateinit var storage : FirebaseStorage

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
        storage = FirebaseStorage.getInstance()

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

        cameraBinding.sendImage.setOnClickListener {
            val storageRef = storage.reference.child("photo/1.png")
            //val rivesRef = storageRef.child("photo/1.png")
            //val uploadTask = rivesRef.putFile(sendImage)

            //이미지 리사이즈
            /*try {
                val inputSteam = requireActivity().contentResolver.openInputStream(sendImage)
                val imgBitmap = BitmapFactory.decodeStream(inputSteam)
                inputSteam!!.close()
                cameraBinding.cameraIV.setImageBitmap(imgBitmap)
            } catch (e : java.lang.Exception) {
                e.printStackTrace()
            }*/

            //여기서 생기는 오류는 익명으로 작성해서 올려서 그럼 그래서 로그인을 확인 해야함

            storageRef.putFile(sendImage!!).continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
                //storage에 업로드된 이미지의 downloadurl리턴
                return@continueWithTask storageRef.downloadUrl
            }.addOnSuccessListener {
                Toast.makeText(activity,"성공", Toast.LENGTH_SHORT).show()

                //FireStore에 데이터 저장
            }.addOnFailureListener {
                Toast.makeText(activity, "실패", Toast.LENGTH_SHORT).show()
            }
        }



        return cameraBinding.root
    }

    //카메라에서 사진 가져올 때
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCamera && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            sendImage = activity?.let { getImageUri(it, imageBitmap) }!!
            cameraBinding.cameraIV.setImageBitmap(imageBitmap)
        }
    }

    //bitmap to uri
    private fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
    //갤러리에서 사진 가져올 때
    fun setGallery(uri : Uri?) {
        cameraBinding.cameraIV.setImageURI(uri)
        sendImage = uri!!
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