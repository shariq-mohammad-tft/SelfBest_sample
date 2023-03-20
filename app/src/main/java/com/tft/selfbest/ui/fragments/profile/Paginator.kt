package com.tft.selfbest.ui.fragments.profile

class Paginator {
    var TOTAL_NUM_ITEMS = 52
    var ITEMS_PER_PAGE = 4
    var ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE
    var LAST_PAGE = TOTAL_NUM_ITEMS/ITEMS_PER_PAGE

    fun generatePage(currentPage: Int): List<String>{
        var startItem = currentPage * ITEMS_PER_PAGE + 1
        var numOfData=ITEMS_PER_PAGE

        var pageData = mutableListOf<String>()

        if(currentPage==LAST_PAGE && ITEMS_REMAINING > 0){
            for(i in startItem..(startItem+ITEMS_REMAINING)){
                pageData.add("")
            }
        }else{
            for(i in startItem..(startItem+numOfData)){
                pageData.add("")
            }
        }

        return pageData
    }
}