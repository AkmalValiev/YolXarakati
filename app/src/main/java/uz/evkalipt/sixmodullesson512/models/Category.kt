package uz.evkalipt.sixmodullesson512.models

class Category{

    var title:String? = null
    var ruleList:ArrayList<Rule>? = null

    constructor(title: String?, ruleList: ArrayList<Rule>?) {
        this.title = title
        this.ruleList = ruleList
    }
}