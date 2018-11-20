package com.project.gcssns.gcssns

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.gcssns.gcssns.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_members.*
import kotlinx.android.synthetic.main.useritem_userlist_row.view.*

/**
 * Created by i412 on 2018-10-29.
 */

class MembersFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_members, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = GroupAdapter<ViewHolder>()
        //adapter.add()
        recyclerview_member_list.adapter = adapter
        recyclerview_member_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        fetchUsers()
    }

    companion object {
        val USER_KEY  = "USER_KEY"
    }

    private fun fetchUsers(){ //데이터 베이스에서 유저들을 가져와 recycler로 보여준다
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            val adapter = GroupAdapter<ViewHolder>()
            override fun onDataChange(p0: DataSnapshot?) {
                p0!!.children.forEach{
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem

                    val intent = Intent(context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, item.user)
                    startActivity(intent)
                }
                recyclerview_member_list.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }
}

class UserItem(val user: User) : Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_useritem_username.text = user.userName
        viewHolder.itemView.textView_useritem_student_id.text = user.userStudentId
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_useritem_picture)
    }
    override fun getLayout(): Int {
        return R.layout.useritem_userlist_row
    }
}

/*
class memberAdapter :  RecyclerView.Adapter<RecyclerView.ViewHolder>{

}*/
