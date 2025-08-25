package com.example.partnerji

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.partnerji.databinding.ActivityAcceptedOrderBinding
import com.example.partnerji.model.orderdetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AcceptedOrder : AppCompatActivity() {
    private val binding:ActivityAcceptedOrderBinding by lazy {
        ActivityAcceptedOrderBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private  var complleteOrderList:ArrayList<orderdetail> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.backimage.setOnClickListener {
            finish()
        }
        //retrive and display completed Order
        retriveCompleteOrderDetail()

    }

    private fun retriveCompleteOrderDetail() {
        //intialize firebase database
        database= FirebaseDatabase.getInstance()
        val CompleteOrderReference=database.reference.child("CompletedOrder")
            .orderByChild("selectdate")
        CompleteOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                complleteOrderList.clear()
                for(orderSnapshot in snapshot.children){
                    val completeorder=orderSnapshot.getValue(orderdetail::class.java)
                    completeorder?.let {
                        complleteOrderList.add(it)
                    }
                }
                //display the list to display latested first
               // complleteOrderList.reverse()
                setdataintorecyclerview()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setdataintorecyclerview() {
        //intialize list to hold customerdetail and payement status
        val customername= mutableListOf<String>()
        val customeraddress= mutableListOf<String>()
        val customernumber= mutableListOf<String>()
        val customeremail= mutableListOf<String>()
        val companyname= mutableListOf<String>()
        val companyprice= mutableListOf<String>()
        val companyimage= mutableListOf<String>()
        val companyaddress= mutableListOf<String>()
        val date = mutableListOf<String>()
        val moneyStatus= mutableListOf<Boolean>()
        for(order in complleteOrderList){
            order.username?.let {
                customername.add(it)
            }
            order.companyaddress?.let {
                companyaddress.add(it)
            }
            order.useradrees?.let {
                customeraddress.add(it)
            }
            order.usercontactno?.let {
                customernumber.add(it)
            }
            order.useremail?.let {
                customeremail.add(it)
            }
            order.companyname?.let {
                companyname.add(it)
            }
            order.companyprice?.let {
                companyprice.add(it)
            }
            order.selectdate?.let {
                date.add(it)
            }
            order.companyimage?.let {
                companyimage.add(it)
            }
//            moneyStatus.add(order.paymentrecieve)
        }
        val adapter=AcceptedOrderAdapter(this ,customername ,moneyStatus,companyaddress,companyname,companyprice,companyimage,customeraddress,customernumber,customeremail,date)
        binding.aceeptordeRecyclerView.adapter=adapter
        binding.aceeptordeRecyclerView.layoutManager= LinearLayoutManager(this)
    }

}