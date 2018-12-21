package com.project.gcssns.gcssns.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.gcssns.gcssns.R
import com.project.gcssns.gcssns.model.HomeFeed
import kotlinx.android.synthetic.main.email_send_row.view.*
import kotlinx.android.synthetic.main.home_feed_row.view.*

class EmailSendAdapter : RecyclerView.Adapter<EmailSendAdapter.ViewHolder>() {

    var m_emailArray = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.email_send_row, parent, false)
        return ViewHolder(v)
    }

    fun addItem(email : String){
        m_emailArray.add(email)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return m_emailArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var email = m_emailArray.get(position)
        holder.email.text = email
        holder.delete.setOnClickListener {
            m_emailArray.remove(m_emailArray.get(position));
            notifyDataSetChanged()
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val email = itemView.textView_email_send_row_email
        val delete = itemView.textView_email_send_row_delete
    }

}
