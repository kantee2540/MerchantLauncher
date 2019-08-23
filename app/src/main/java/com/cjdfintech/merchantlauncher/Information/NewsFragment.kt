package com.cjdfintech.merchantlauncher.Information

import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cjdfintech.merchantlauncher.BuildConfig
import com.cjdfintech.merchantlauncher.R
import com.google.firebase.database.*
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.fragment_news.view.*
import java.lang.ref.Reference

class NewsFragment : Fragment(){

    private lateinit var rootView: View
    lateinit var myRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_news, container, false)

        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("message")

        getMessage()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        getMessage()
    }

    private fun getMessage(){

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java)
                rootView.message_tv.text = value
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                rootView.message_tv.text = "Oops! Failed"
            }
        })

    }
}