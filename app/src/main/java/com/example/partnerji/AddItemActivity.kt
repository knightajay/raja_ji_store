package com.example.partnerji

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.partnerji.databinding.ActivityAddItemBinding
import com.example.partnerji.model.allmenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var companyprice: String
    private lateinit var CompanyAdress: String
    private lateinit var description: String
    private var imageUri: Uri? = null

    //firebass
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val storeList = arrayOf(
            "Band",
            "Singer",
            "Dancer",
            "Decoration",
            "Hotel",
            "Carpet",
            "Restaurant",
            "Cook",
            "Bar",
            "BMakeup",
            "Photographer",
            "GMakeup"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, storeList)
        val autoCompleteTextView = binding.listoflocation
        autoCompleteTextView.setAdapter(adapter)
        var selectedLocation: String? = null
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            selectedLocation = parent.getItemAtPosition(position) as String
        }

        // intialize Firease
        auth = FirebaseAuth.getInstance()
        //intailize firebase database
        database = FirebaseDatabase.getInstance()
        binding.additem.setOnClickListener {
            name = binding.entername.text.toString().trim()
            companyprice = binding.enterprice.text.toString().trim()
            CompanyAdress = binding.enteradress.text.toString().trim()
            description = binding.description.text.toString().trim()

            if (!(name.isBlank() || companyprice.isBlank() || CompanyAdress.isBlank() || description.isBlank())) {
                uploadData(selectedLocation)
                Toast.makeText(this, "Item Add Sucessfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill All Detail", Toast.LENGTH_SHORT).show()
            }
        }
        binding.selectimage.setOnClickListener {
            pickimage.launch("image/*")
        }

        binding.backbuttonn.setOnClickListener {
            finish()
        }

    }

    //
    private fun uploadData(selectedLocation: String?) {
        val MenuRef = FirebaseDatabase.getInstance().getReference(selectedLocation!!)
        val newItemKey = MenuRef.push().key

        if (imageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageref = storageRef.child("menu_image${newItemKey}.jpg")
            val uploadTask = imageref.putFile(imageUri!!)

            uploadTask.addOnSuccessListener { uploadTask ->
                imageref.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val newItem = allmenu(
                        companyname = name,
                        companyAdress = CompanyAdress,
                        Price = companyprice,
                        description = description,
                        adminid= auth.currentUser?.uid,
                        image = downloadUrl.toString()
                    )

                    newItemKey?.let { key ->
                        val menuRefKey = MenuRef.child(key)

                        // Set values in Firebase
                        menuRefKey.setValue(newItem).addOnSuccessListener {
                            selectedLocation?.let { location ->
                                menuRefKey.child("storelist").setValue(location)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Data uploaded successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }.addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "Storelist upload failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } ?: Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT)
                                .show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this, "Data upload failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickimage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            if (uri != null) {
                binding.selectedimage.setImageURI(uri)
                imageUri = uri
            }
        }

}