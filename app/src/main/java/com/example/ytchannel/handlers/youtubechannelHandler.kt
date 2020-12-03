package com.example.ytchannel.handlers

import com.example.ytchannel.models.youtubechannel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class youtubechannelHandler {
     var database:FirebaseDatabase
    var youtubechannelRef:DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
         youtubechannelRef =database.getReference("youtube")

    }
    fun create(youtube:youtubechannel):Boolean{

        val id = youtubechannelRef.push().key
        youtube.id = id

        youtubechannelRef.child(id!!).setValue(youtube)

        return true
    }
    fun update(youtube: youtubechannel):Boolean{
        youtubechannelRef.child(youtube.id!!).setValue(youtube)
        return true

    }
    fun delete(youtube: youtubechannel):Boolean{
        youtubechannelRef.child(youtube.id!!).removeValue()
        return true
    }

}