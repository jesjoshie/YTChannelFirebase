package com.example.ytchannel.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class youtubechannel(var id:String?= "",var title:String?="",var youtubeChannel:String?="",var reason:String? ="",var rank:String?="") {
    override fun toString(): String {
        return "$title with reason in $reason"
    }
}