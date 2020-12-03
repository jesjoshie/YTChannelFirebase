package com.example.ytchannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.ytchannel.handlers.youtubechannelHandler
import com.example.ytchannel.models.youtubechannel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var titleEditText: EditText
    lateinit var YcEditText: EditText
    lateinit var rankEditText: EditText
    lateinit var reasonEditText: EditText
    lateinit var addEditButton: Button
    lateinit var youtubechannelHandler: youtubechannelHandler
    lateinit var youtube: ArrayList<youtubechannel>
    lateinit var youtubelistview: ListView
    lateinit var  youtubesGettingEdited:youtubechannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        titleEditText = findViewById(R.id.title)
        YcEditText = findViewById(R.id.YTChannel)
        rankEditText = findViewById(R.id.rank)
        reasonEditText = findViewById(R.id.reason)
        addEditButton = findViewById(R.id.addbtn)
        youtubechannelHandler = youtubechannelHandler()

        youtube =ArrayList()
        youtubelistview =findViewById(R.id.ytListview)


        addEditButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val youtubeChannel = YcEditText.text.toString()
            val rank = rankEditText.text.toString()
            val reason = reasonEditText.text.toString()
            if(addEditButton.text.toString()=="Add"){
                val youtube = youtubechannel(
                    title = title,
                    youtubeChannel = youtubeChannel,
                    reason = reason,
                    rank = rank
                )
                if (youtubechannelHandler.create(youtube)) {
                    Toast.makeText(applicationContext, "Youtube Added", Toast.LENGTH_SHORT).show()
                    clearField()
                }

            }
            else if (addEditButton.text.toString() == "Update") {
                val youtubes = youtubechannel(id=youtubesGettingEdited.id,title =title,youtubeChannel =youtubeChannel, rank=rank,reason = reason)
                if(youtubechannelHandler.update(youtubes)){
                    Toast.makeText(applicationContext, "Youtube Added", Toast.LENGTH_SHORT).show()
                    clearField()
                }

            }

        }
        registerForContextMenu(youtubelistview)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.youtube_options,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as  AdapterView.AdapterContextMenuInfo
        return  when(item.itemId){
            R.id.edit_yt ->{
                youtubesGettingEdited =youtube[info.position]
                titleEditText.setText(youtubesGettingEdited.title)
                YcEditText.setText(youtubesGettingEdited.youtubeChannel)
                rankEditText.setText(youtubesGettingEdited.rank)
                reasonEditText.setText(youtubesGettingEdited.reason)
                addEditButton.setText("Update")
                true
            }
            R.id.delete_yt ->{

               if (youtubechannelHandler.delete(youtube[info.position])){
                   Toast.makeText(applicationContext, "Youtube Deleted", Toast.LENGTH_SHORT).show()
               }

                true
            }
            else -> super.onContextItemSelected(item)
        }

    }
    override fun onStart() {
        super.onStart()
        youtubechannelHandler.youtubechannelRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                youtube.clear()
                snapshot.children.forEach {
                    it->val youtubes =it.getValue(youtubechannel::class.java)
                    youtube.add(youtubes!!)
                }
                val adapter =ArrayAdapter<youtubechannel>(applicationContext,android.R.layout.simple_list_item_1,youtube)
                youtubelistview.adapter =adapter

            }

            override fun onCancelled(error: DatabaseError) {
                /*TODO("Not yet implemented")*/
            }
        })
    }
    fun clearField(){
     titleEditText.text.clear()
        YcEditText.text.clear()
        rankEditText.text.clear()
        reasonEditText.text.clear()
        addEditButton.setText("Add")

    }
}