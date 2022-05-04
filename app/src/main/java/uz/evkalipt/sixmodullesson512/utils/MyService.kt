package uz.evkalipt.sixmodullesson512.utils

import uz.evkalipt.sixmodullesson512.models.Rule

interface MyService {

    fun insert(rule: Rule)

    fun getOne(id:Int):Rule

    fun getAll():ArrayList<Rule>

    fun update(rule: Rule):Int

    fun delete(rule: Rule)

}