package com.absan.firedo

import java.io.Serializable

data class ListItem(var id: Int, var content: String,var isComplete: Boolean = false):Serializable
//{
//    val itemId = id;
//    val itemContent = content;
//    var isComplete = false;
//
//    fun setComplete(){
//        isComplete = true;
//    }
//}
