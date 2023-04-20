package com.example.myonlineorderingsystem

import Retrofitdatabase.DataXXXXXXXXX
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView

class OrdersProductAdapter(
    private val ordersListLiveData: LiveData<List<DataXXXXXXXXX>>
) : RecyclerView.Adapter<OrdersProductAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Oname: TextView = itemView.findViewById(R.id.name)
        val Oquantity: TextView = itemView.findViewById(R.id.quantity)
        val Oprice: TextView = itemView.findViewById(R.id.price)
        val Ototalprice: TextView = itemView.findViewById(R.id.totalprice)
        val Odate: TextView = itemView.findViewById(R.id.date)
        val Otime:TextView=itemView.findViewById(R.id.time)
        val Oimage: ShapeableImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OrdersProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.orders_product_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = ordersListLiveData.value?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrdersProductAdapter.ViewHolder, position: Int) {
        val order = ordersListLiveData.value?.get(position)
        if(order !=null) {
            holder.Oname.text = order.title
            holder.Oprice.text = "M.R.P: $${order.price}"
            holder.Oquantity.text ="Quantity: ${order.quantity.toString()}"
            holder.Oimage.load(order.imageUrl)
            holder.Ototalprice.text = "Total: $${order.productTotalAmount}"
            val date = order.createdAt.toString().substring(0, 10)
            holder.Odate.text = "Order Date: ${date}"
            val time = order.createdAt.toString().substring(12, 19)
            holder.Otime.text = "Order Time: ${time}"
        }

    }
}
