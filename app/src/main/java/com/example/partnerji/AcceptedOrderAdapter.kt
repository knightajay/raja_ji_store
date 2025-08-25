package com.example.partnerji

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.partnerji.databinding.AcceptedorderitemBinding
import com.example.partnerji.databinding.ActivityAcceptedOrderBinding

class AcceptedOrderAdapter (
    private val context: Context,
    private  val customerName:MutableList<String>,private  val moneyStatus:MutableList<Boolean>
    ,private val companyAddress:MutableList<String>,private  val companyname:MutableList<String>,
    private val companyPrice:MutableList<String>,private val companyImage:MutableList<String>,
    private val customerAddress:MutableList<String>,
    private  val customerNumber:MutableList<String>,
    private val customeremail:MutableList<String>,
    private val Date:MutableList<String>,
    ) : RecyclerView.Adapter<AcceptedOrderAdapter.acceptedorderViewHolder>() {

    inner class acceptedorderViewHolder(private val binding: AcceptedorderitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                username.text = customerName[position]
                companynames.text = companyname[position]
                date.text = Date[position]
                useraddress.text = customerAddress[position]
                contactnumber.text = customerNumber[position]
                emailforuser.text = customeremail[position]
                val uri = companyImage[position]
                val Uri = Uri.parse(uri)
                Glide.with(context).load(Uri).into(foodImageView)
//if(moneyStatus[position]==true){
//    emailforuser.text="Recieved"
//}
//        else{
//            emailforuser.text="NotRecieved"
//        }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): acceptedorderViewHolder {
        val binding =
            AcceptedorderitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return acceptedorderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: acceptedorderViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerName.size


}