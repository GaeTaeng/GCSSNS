package com.project.gcssns.gcssns.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.gcssns.gcssns.HomeCommentActivity
import com.project.gcssns.gcssns.HomeReadActivity
import com.project.gcssns.gcssns.R
import com.project.gcssns.gcssns.fragment.HomeFragment
import com.project.gcssns.gcssns.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_feed_row.view.*

class HomeFeedAdapter(context : Context?) : RecyclerView.Adapter<HomeFeedAdapter.ViewHolder>() {

        var m_homeFeedArray = ArrayList<HomeFeed>()
        var m_context = context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.home_feed_row, parent, false)
            return ViewHolder(v)
        }

        fun addItem(homeFeed : HomeFeed){
            m_homeFeedArray.add(homeFeed)
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return m_homeFeedArray.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val homeFeed = m_homeFeedArray.get(position)
            holder.username.text = homeFeed.user!!.userName
            holder.content.setText(homeFeed.text)
            val imageTarget = holder.image
            Picasso.get().load(homeFeed.user!!.profileImageUrl).into(imageTarget)
            holder.likeButton.setOnClickListener {
                Log.d("HomeFeedAdapter", "좋아요 버튼 클릭")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //holder.likeIcon.drawable.setTint(ContextCompat.getColor(m_context!!, R.color.colorButton1))
                    //holder.likeText.setTextColor(ContextCompat.getColor(m_context!!, R.color.colorButton1))
                }
            }
            holder.commentButton.setOnClickListener {
                Log.d("HomeFeedAdapter", "댓글 버튼 클릭!")
                openCommentActivity(homeFeed)
            }
            holder.sendButton.setOnClickListener {
                Log.d("HomeFeedAdapter", "보내기 버튼 클릭")
            }
            holder.username.setOnClickListener {
                openDetailActivity(homeFeed)
            }
            holder.content.setOnClickListener {
                openDetailActivity(homeFeed)
            }
            holder.image.setOnClickListener {
                openDetailActivity(homeFeed)
            }
        }

        fun openDetailActivity(homeFeed : HomeFeed){
            val intent = Intent(m_context, HomeReadActivity::class.java)
            intent.putExtra(HomeFragment.HOME_ITEM, homeFeed)
            m_context!!.startActivity(intent)
        }

        fun openCommentActivity(homeFeed : HomeFeed){
            val intent = Intent(m_context, HomeCommentActivity::class.java)
            intent.putExtra(HomeFragment.HOME_ITEM, homeFeed)
            m_context!!.startActivity(intent)
        }


        class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            val username = itemView.textView_home_feed_username
            val content = itemView.editText_home_feed_content
            val image = itemView.imageView_home_feed_user_image
            val likeButton = itemView.LinearLayout_home_feed_row_like
            val commentButton = itemView.LinearLayout_home_feed_row_comment
            val sendButton = itemView.LinearLayout_home_feed_row_send
            val likeText = itemView.textView_home_feed_row_like
            val commentText = itemView.textView_home_feed_row_comment
            val sendText = itemView.textView_home_feed_row_send
            val likeIcon = itemView.imageView_home_feed_row_like_icon
            val commentIcon = itemView.imageView_home_feed_row_comment_icon
            val sendIcon = itemView.imageView_home_feed_row_send_icon
        }


}