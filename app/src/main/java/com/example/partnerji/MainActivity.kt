package com.example.partnerji

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.partnerji.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.birthday, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner5, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner6, ScaleTypes.FIT))

        imageList.add(SlideModel(R.drawable.dhol, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        binding.AddMenu.setOnClickListener{
            val intent= Intent(this,AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.Seeall.setOnClickListener{
            val intent= Intent(this,AcceptedOrder::class.java)
            startActivity(intent)
        }
        binding.pendingorder.setOnClickListener {
            val intent= Intent(this ,PendingActivity::class.java)
            startActivity(intent)
        }
    }
}