package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()

        for (i in 0..100) {
            val lon = i.toDouble()
            val lat = i.toDouble()

            val story = ListStoryItem(
                i.toString(),
                "createdAt + $i",
                "name $i",
                "description $i",
                lon,
                "id $i",
                lat
            )
            items.add(story)
        }
        return items
    }
}