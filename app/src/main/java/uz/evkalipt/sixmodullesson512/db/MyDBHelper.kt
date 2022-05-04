package uz.evkalipt.sixmodullesson512.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.channels.consumesAll
import uz.evkalipt.sixmodullesson512.models.Rule
import uz.evkalipt.sixmodullesson512.utils.Constant
import uz.evkalipt.sixmodullesson512.utils.MyService

class MyDBHelper(context: Context) :
    SQLiteOpenHelper(context, Constant.DB_NAME, null, Constant.DB_VERSION), MyService {
    override fun onCreate(db: SQLiteDatabase?) {
        var query =
            "create table ${Constant.TABLE_NAME}(${Constant.ID} integer not null primary key autoincrement unique, ${Constant.TITLE} text not null, ${Constant.DESCRIPTION} text not null, ${Constant.LOVE} integer not null, ${Constant.STR} text not null, ${Constant.IMAGE} text)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun insert(rule: Rule) {
        val database = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constant.TITLE, rule.title)
        contentValues.put(Constant.DESCRIPTION, rule.description)
        contentValues.put(Constant.LOVE, rule.love)
        contentValues.put(Constant.STR, rule.str)
        contentValues.put(Constant.IMAGE, rule.imagePath)
        database.insert(Constant.TABLE_NAME, null, contentValues)
        database.close()
    }

    override fun getOne(id: Int): Rule {
        val database = readableDatabase
        var cursor = database.query(
            Constant.TABLE_NAME,
            arrayOf(
                Constant.ID,
                Constant.TITLE,
                Constant.DESCRIPTION,
                Constant.LOVE,
                Constant.STR,
                Constant.IMAGE
            ),
            "${Constant.ID} = ?",
            arrayOf("$id"),
            null,
            null,
            null
        )
        cursor.moveToFirst()
        return Rule(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5))
    }

    override fun getAll(): ArrayList<Rule> {
        var list = ArrayList<Rule>()
        val database = readableDatabase
        var query = "select * from ${Constant.TABLE_NAME}"
        val cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()){
            do {
                var id = cursor.getInt(0)
                var title = cursor.getString(1)
                var description = cursor.getString(2)
                var love = cursor.getInt(3)
                var str = cursor.getString(4)
                var imagePath = cursor.getString(5)
                var rule = Rule(id, title, description, love, str, imagePath)
                list.add(rule)
            }while (cursor.moveToNext())
        }

        return list
    }

    override fun update(rule: Rule): Int {
        val database = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constant.ID, rule.id)
        contentValues.put(Constant.TITLE, rule.title)
        contentValues.put(Constant.DESCRIPTION, rule.description)
        contentValues.put(Constant.LOVE, rule.love)
        contentValues.put(Constant.STR, rule.str)
        contentValues.put(Constant.IMAGE, rule.imagePath)
        return database.update(Constant.TABLE_NAME, contentValues,"${Constant.ID} = ?", arrayOf("${rule.id}"))
    }

    override fun delete(rule: Rule) {
        val database = writableDatabase
        database.delete(Constant.TABLE_NAME, "${Constant.ID} = ?", arrayOf("${rule.id}"))
    }
}