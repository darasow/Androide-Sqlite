package com.example.facebook.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.widget.Toast
import com.example.facebook.PosteAdaper
import com.example.facebook.data.Poste
import com.example.facebook.data.User
import java.io.ByteArrayOutputStream

class DatabaseFb(var mContext: Context) :
    SQLiteOpenHelper(mContext, dbName, null, dbVersion)
{
    override fun onCreate(db: SQLiteDatabase?)
    {
        // cration des tables
        val createTableUser = """
            CREATE TABLE "$UserTableName" (
            $UserId integer PRIMARY KEY,
            $UserName varchar(50),
            $UserEmail varchar(100) unique,
            $UserPassword varchar(20)
            )
        """.trimIndent()

        val createTablePoste = """
            CREATE TABLE "$PosteTableName" (
            $PosteId integer PRIMARY KEY,
            $PosteTitre varchar(50),
            $PosteDescription text,
            $PosteImage blob,
            $PosteJaime integer null
            )
        """.trimIndent()
       // db?.execSQL(createTableUser)
        db?.execSQL(createTablePoste)

    }

    // on Upgrade une base de donnee c'est lorsq'on change la structure de la base de donnee (modification de nom d'une table, type de colonne...)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        //db?.execSQL("DROP TABLE IF EXISTS $UserTableName")
        db?.execSQL("DROP TABLE IF EXISTS $PosteTableName")
        onCreate(db)
    }

    companion object
    {
        // DB
        private var dbName = "Facebook"
        private var dbVersion = 3

        // Users
        private var UserTableName = "Users"
        private var UserId = "id"
        private var UserName = "name"
        private var UserEmail = "email"
        private var UserPassword = "password"

        //Postes
        private var PosteTableName = "Postes"
        private var PosteId = "id"
        private var PosteTitre = "titre"
        private var PosteDescription = "description"
        private var PosteImage = "image"
        private var PosteJaime = "jaime"


    }

    fun adduser(user : User) : Boolean
    {
        var db = writableDatabase
        var values = ContentValues()
        values.put(UserName, user.name)
        values.put(UserEmail, user.email)
        values.put(UserPassword, user.password)

        val resultat = db.insert(UserTableName, null, values).toInt()

        db.close()
        return  resultat != -1
    }

    fun addPoste(poste: Poste): Boolean {
        var db = writableDatabase
        var values = ContentValues()
        values.put(PosteTitre, poste.titre)
        values.put(PosteDescription, poste.description)

        // Redimensionner l'image
        val resizedImageByteArray = poste.image?.let { resizeByteArray(it, 1080, 1080) }

        values.put(PosteImage, resizedImageByteArray)

        val resultat = db.insert(PosteTableName, null, values)
        db.close()
        return resultat != -1L
    }

    fun deletePoste(id : Int) : Boolean
    {
        var db = writableDatabase
       var resultat =  db.delete(PosteTableName, "id=?", arrayOf(id.toString()))
        db.close()
        return  resultat > 0
    }

    fun resizeByteArray(imageByteArray: ByteArray, maxWidth: Int, maxHeight: Int): ByteArray {
        val originalBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)

        val width = originalBitmap.width
        val height = originalBitmap.height

        if (width <= maxWidth && height <= maxHeight) {
            return imageByteArray // Pas besoin de redimensionner
        }

        val scaleWidth = maxWidth.toFloat() / width
        val scaleHeight = maxHeight.toFloat() / height

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        val resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, false)

        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()
    }


    @SuppressLint("SuspiciousIndentation")
    fun findUser(email : String, password : String) : User?
    {
        val db = this.readableDatabase

        var querySelect = """
            SELECT * FROM $UserTableName WHERE $UserEmail =? AND $UserPassword =?
        """.trimIndent()

        var selectionArgs = arrayOf(email, password)
        var user : User? = null
        var cursor = db.query(UserTableName, null, "$UserEmail =? AND $UserPassword =?", selectionArgs, null, null, null)
          if(cursor != null)
          {
              if(cursor.moveToFirst())
              {
                  val id = cursor.getInt(cursor.getColumnIndexOrThrow(UserId))
                  val name = cursor.getString(cursor.getColumnIndexOrThrow(UserName))
                  val email = cursor.getString(cursor.getColumnIndexOrThrow(UserEmail))
                  user = User(id, name, email, "")
                  return user

              }
          }
        db.close()
        return user

    }

    @SuppressLint("SuspiciousIndentation")
    fun findPostes() : ArrayList<Poste>
    {
        val db = this.readableDatabase
        val postes = ArrayList<Poste>()
        val queryPostes = "SELECT * FROM $PosteTableName"

        var cursor = db.rawQuery(queryPostes, null)
          if(cursor != null)
          {
              if(cursor.moveToFirst())
              {
                  do {
                       val id = cursor.getInt(cursor.getColumnIndexOrThrow(PosteId))
                       val titre = cursor.getString(cursor.getColumnIndexOrThrow(PosteTitre))
                       val description = cursor.getString(cursor.getColumnIndexOrThrow(
                           PosteDescription))
                       val image = cursor.getBlob(cursor.getColumnIndexOrThrow(PosteImage))
                       val jaime = cursor.getInt(cursor.getColumnIndexOrThrow(PosteJaime))
                      postes.add(Poste(id, titre, description, image, jaime))
                  }while (cursor.moveToNext())
              }
          }

    db.close()
    return postes
    }

//    fun incrementJaime(poste: Poste) {
//        var db = this.writableDatabase
//        var cptJaime = poste.jaime?.plus(1)
//
//        val value = ContentValues()
//        value.put(PosteJaime, cptJaime)
//
//        db.update(PosteTableName, value, "id=?", arrayOf("${poste.id}"))
//        db.close()
//    }


    fun incrementJaime(poste: Poste, posteAdapter: PosteAdaper) {
        var db = this.writableDatabase
        var cptJaime = poste.jaime?.plus(1)

        val value = ContentValues()
        value.put(PosteJaime, cptJaime)

        val rowsAffected = db.update(PosteTableName, value, "id=?", arrayOf("${poste.id}"))
        db.close()

        if (rowsAffected > 0) {
            // Mise à jour réussie dans la base de données
            poste.jaime = cptJaime // Mettez à jour l'objet Poste avec le nouveau nombre de "j'aime"

            // Mise à jour de l'interface utilisateur (nécessaire pour les ListView et les Adapters)
            posteAdapter.notifyDataSetChanged()
        }
    }


}