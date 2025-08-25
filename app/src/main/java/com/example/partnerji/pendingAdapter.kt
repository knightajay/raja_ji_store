package com.example.partnerji

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.partnerji.databinding.PendigitemBinding

class pendingAdapter (
    private val context: Context,
    private val listofname :MutableList<String>,
    private val listofAddress:MutableList<String>,
    private val listofDate:MutableList<String>,
    private val contactnumb :MutableList<String>,
    private val companyname:MutableList<String>,
    private val email:MutableList<String>,
    private val companyimage:MutableList<String>,
    private val itemClicked:OnItemClicked

): RecyclerView.Adapter<pendingAdapter.pendingOrderViewHolder>() {

    interface OnItemClicked{
        fun OnItemClickacceptListener(position: Int)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): pendingAdapter.pendingOrderViewHolder {
        val binding=PendigitemBinding.inflate(LayoutInflater.from(parent.context) ,parent,false)
        return pendingOrderViewHolder(binding)
    }



    override fun onBindViewHolder(holder: pendingAdapter.pendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount():Int= listofname.size
    inner class pendingOrderViewHolder(private val binding: PendigitemBinding): RecyclerView.ViewHolder(binding.root) {
        private var isAccepted =false
        fun bind(position: Int){
            binding.apply {
                username.text=listofname[position]
                companynames.text=companyname[position]
                useraddress.text=listofAddress[position]
                date.text=listofDate[position]
                contactnumber.text=contactnumb[position]
                emailforuser.text=email[position]
                val uriString =companyimage[position]
                val uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(foodImageView)
                Orderacceptedbutton.apply {
                    if(!isAccepted){
                        text="Accept"
                    }
                    else{
                        text="Dispatch"
                    }
                    setOnClickListener {
                        if(!isAccepted){
                            // text="Dispatch"
                            isAccepted=true
                            listofname.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            Toast.makeText(context,"Order is accepted" , Toast.LENGTH_SHORT).show()

                            itemClicked.OnItemClickacceptListener(position)
                        }
                        else{
                            Toast.makeText(context,"Order is not accepted" , Toast.LENGTH_SHORT).show()

                        }
                    }
                }

            }
        }
    }

}
