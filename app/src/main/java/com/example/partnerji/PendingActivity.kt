package com.example.partnerji

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.partnerji.databinding.ActivityPendingBinding
import com.example.partnerji.model.orderdetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PendingActivity : AppCompatActivity(),pendingAdapter.OnItemClicked {
    private var listofname: MutableList<String> = mutableListOf()
    private var listofaddress: MutableList<String> = mutableListOf()
    private var contactno: MutableList<String> = mutableListOf()
    private var email: MutableList<String> = mutableListOf()
    private var companyname:MutableList<String> = mutableListOf()
    private var companyimages:MutableList<String> = mutableListOf()
    private val listofdate:MutableList<String> = mutableListOf()
    private var listOrderItem: MutableList<orderdetail> = mutableListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderdetail: DatabaseReference
    private lateinit var binding: ActivityPendingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // intialize firebase
        database = FirebaseDatabase.getInstance()
        databaseOrderdetail = database.reference.child("Orderdetail").child(FirebaseAuth.getInstance().currentUser!!.uid)
        getOrderdetail()
        binding.backimage.setOnClickListener{
            finish()
        }
    }
    private fun getOrderdetail() {
        //retrive data rom firebase
        databaseOrderdetail.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetaill = orderSnapshot.getValue(orderdetail::class.java)
                    orderDetaill?.let {
                        listOrderItem.add(it)
                    }
                }
                addDatatolistofrecyclerview()
            }

            override fun onCancelled(error: DatabaseError) {


            }

        })

    }

    private fun addDatatolistofrecyclerview() {
        for(orderItem  in listOrderItem) {
            // add data
            orderItem.username?.let { listofname.add(it) }
            orderItem.useradrees?.let { listofaddress.add(it) }
            orderItem.selectdate?.let { listofdate.add(it) }
            orderItem.usercontactno?.let { contactno.add(it) }
            orderItem.companyname?.let { companyname.add(it) }
            orderItem.useremail?.let { email.add(it) }
            orderItem.companyimage?.let { companyimages.add(it) }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingRecyclerView.layoutManager= LinearLayoutManager(this)
        val adapter =pendingAdapter(this ,listofname,listofaddress,listofdate,contactno,companyname,email,companyimages,this)
        binding.pendingRecyclerView.adapter=adapter
    }

    override fun OnItemClickacceptListener(position: Int) {
// handler  item acceptence and update firebase
        val childitempushKey =listOrderItem[position].itemPushKey
        val clickitemOrdrreferencee =childitempushKey?.let {
            database.reference.child("Orderdetail").child(it)
        }
        clickitemOrdrreferencee?.child("orderAccepted")?.setValue(true)

//        updateOrderAcceptedvalue(position)
        val userIdofClickItem=listOrderItem[position].userUid
        val pushkeyofClikedItem=listOrderItem[position].itemPushKey
        val buyHistoryrefernce=database.reference.child("user").child(userIdofClickItem!!).child("BuyHistory").child(pushkeyofClikedItem!!)
        buyHistoryrefernce.child("orderAccepted").setValue(true)
        databaseOrderdetail.child(pushkeyofClikedItem).child("orderAccepted").setValue(true)
        val dispatchItemPushkey=listOrderItem[position].itemPushKey
        val dispatchitemorderreference=database.reference.child("CompletedOrder").child(dispatchItemPushkey!!)
        dispatchitemorderreference.setValue(listOrderItem[position]).addOnSuccessListener {
            deletethisitemfromorderdetail(dispatchItemPushkey)
        }
    }


    private fun deletethisitemfromorderdetail(dispatchItemPushkey: String) {
        val orderDetailItemreference=database.reference.child("Orderdetail").child(dispatchItemPushkey)
        orderDetailItemreference.removeValue().addOnSuccessListener {
            Toast.makeText(this,"Order Is Accepted" , Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{
                Toast.makeText(this,"Order Is Not Accepted" , Toast.LENGTH_SHORT).show()
            }
    }
}


