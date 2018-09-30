package br.ufpe.cin.if710.rss.db

import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import br.ufpe.cin.if710.rss.ItemRSS


class SQLiteRSSHelper private constructor(//alternativa
        internal var c: Context) : SQLiteOpenHelper(c, DATABASE_NAME, null, DB_VERSION) {
    val items: Cursor?
        @Throws(SQLException::class)
        get() = null

    override fun onCreate(db: SQLiteDatabase) {
        //Executa o comando de criação de tabela
        db.execSQL(CREATE_DB_COMMAND)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //estamos ignorando esta possibilidade no momento
        throw RuntimeException("nao se aplica")
    }

    //IMPLEMENTAR ABAIXO
    //Implemente a manipulação de dados nos métodos auxiliares para não ficar criando consultas manualmente
    fun insertItem(item: ItemRSS): Long {
        return insertItem(item.title, item.pubDate, item.description, item.link)
    }

    fun insertItem(title: String, pubDate: String, description: String, link: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ITEM_TITLE, title)
        values.put(ITEM_DATE, pubDate)
        values.put(ITEM_DESC, description)
        values.put(ITEM_LINK, link)
        values.put(ITEM_UNREAD, true)
        return db.insert(DATABASE_TABLE, null, values)
    }

    @Throws(SQLException::class)
    fun getItemRSS(link: String): ItemRSS? {
        var item: ItemRSS? = null
        val db = this.writableDatabase
        val query = "SELECT * FROM $DATABASE_TABLE WHERE $ITEM_LINK = $link AND $ITEM_UNREAD = 1"
        val cursor = db.rawQuery(query, null)
        if(cursor != null) {
            cursor.moveToFirst()
            while(cursor.moveToNext()) {
                val title = cursor.getString(cursor.getColumnIndex(ITEM_TITLE))
                val link = cursor.getString(cursor.getColumnIndex(ITEM_LINK))
                val pubDate = cursor.getString(cursor.getColumnIndex(ITEM_DATE))
                val description = cursor.getString(cursor.getColumnIndex(ITEM_DESC))
                item = ItemRSS(title, link, pubDate, description)
            }
        }
        return item
    }

    fun markAsUnread(link: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ITEM_UNREAD, true)
        return db.update(DATABASE_TABLE, values,"$ITEM_LINK='$link'", null) > 0
    }

    fun markAsRead(link: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ITEM_UNREAD, false)
        return db.update(DATABASE_TABLE, values,"$ITEM_LINK='$link'", null) > 0
    }

    companion object {
        //Nome do Banco de Dados
        private val DATABASE_NAME = "rss"
        //Nome da tabela do Banco a ser usada
        val DATABASE_TABLE = "items"
        //Versão atual do banco
        private val DB_VERSION = 1

        private var db: SQLiteRSSHelper? = null

        //Definindo Singleton
        fun getInstance(c: Context): SQLiteRSSHelper {
            if (db == null) {
                db = SQLiteRSSHelper(c.applicationContext)
            }
            return db as SQLiteRSSHelper
        }

        //Definindo constantes que representam os campos do banco de dados
        val ITEM_ROWID = RssProviderContract._ID
        val ITEM_TITLE = RssProviderContract.TITLE
        val ITEM_DATE = RssProviderContract.DATE
        val ITEM_DESC = RssProviderContract.DESCRIPTION
        val ITEM_LINK = RssProviderContract.LINK
        val ITEM_UNREAD = RssProviderContract.UNREAD

        //Definindo constante que representa um array com todos os campos
        val columns = arrayOf(ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD)

        //Definindo constante que representa o comando de criação da tabela no banco de dados
        private val CREATE_DB_COMMAND = "CREATE TABLE " + DATABASE_TABLE + " (" +
                ITEM_ROWID + " integer primary key autoincrement, " +
                ITEM_TITLE + " text not null, " +
                ITEM_DATE + " text not null, " +
                ITEM_DESC + " text not null, " +
                ITEM_LINK + " text not null, " +
                ITEM_UNREAD + " boolean not null);"
    }

}

