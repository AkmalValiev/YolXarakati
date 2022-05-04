package uz.evkalipt.sixmodullesson512.models

class Rule {

    var id:Int? = null
    var title:String? = null
    var description:String? = null
    var love:Int? = null
    var str:String? = null
    var imagePath:String? = null

    constructor(
        title: String?,
        description: String?,
        love: Int?,
        str: String?,
        imagePath: String?
    ) {
        this.title = title
        this.description = description
        this.love = love
        this.str = str
        this.imagePath = imagePath
    }

    constructor(
        id: Int?,
        title: String?,
        description: String?,
        love: Int?,
        str: String?,
        imagePath: String?
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.love = love
        this.str = str
        this.imagePath = imagePath
    }


}