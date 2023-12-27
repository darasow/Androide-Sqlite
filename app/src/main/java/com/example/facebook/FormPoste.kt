package com.example.facebook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.example.facebook.data.Poste
import com.example.facebook.db.DatabaseFb
import java.io.ByteArrayOutputStream

class FormPoste : AppCompatActivity() {
    lateinit var titrePoste : EditText
    lateinit var descriptionPoste : EditText
    lateinit var imagePoste : ImageView
    lateinit var posteCreate : Button
    lateinit var db : DatabaseFb
    var bitmap : Bitmap? = null
    private val PosteLauncherForImage: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                val inputStream = contentResolver.openInputStream(uri!!)
                 bitmap = BitmapFactory.decodeStream(inputStream)
                imagePoste.setImageBitmap(bitmap)
            }
        }

    @SuppressLint("MissingInflatedId", "ResourceAsColor")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_poste)
         db = DatabaseFb(this)
        val toolbar : Toolbar = findViewById(R.id.toolbarFormPoste) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(Color.WHITE)


        titrePoste = findViewById(R.id.titreFormPoste)
        descriptionPoste = findViewById(R.id.descriptionFormPoste)
        imagePoste = findViewById(R.id.imgPoste)
        posteCreate = findViewById(R.id.posteCreate)
        var errorFormPoste = findViewById<TextView>(R.id.errorFormPoste)

        imagePoste.setOnClickListener {
            val intentImg = Intent(Intent.ACTION_GET_CONTENT)
            intentImg.type = "image/*"
            PosteLauncherForImage.launch(intentImg)
        }
         posteCreate.setOnClickListener {

             val titrePosteText = titrePoste.text.toString().trim()
             val descriptionPosteText = descriptionPoste.text.toString().trim()
             var bitMapImg : ByteArray = getBYteImage(bitmap)
             if(descriptionPosteText.isEmpty() || titrePosteText.isEmpty() || bitMapImg.isEmpty())
             {
                 errorFormPoste.setBackgroundColor(R.color.red)
                 errorFormPoste.setText("Tout les champs sont obligatoire")
                 errorFormPoste.visibility = View.VISIBLE
             }else
             {
                 val poste = Poste(titrePosteText, descriptionPosteText, bitMapImg)
                 db.addPoste(poste)
                 db.close()

                 errorFormPoste.setBackgroundColor(R.color.red)
                 errorFormPoste.setText("Poste cree avec succes")
                 errorFormPoste.visibility = View.VISIBLE
                 titrePoste.setText("")
                 descriptionPoste.setText("")
                 imagePoste.setImageResource(R.drawable.addimg)
                 bitmap = null
                 finish()
             }

         }

    }// onCreate

    private fun getBYteImage(bitmap: Bitmap?): ByteArray {
             val stream = ByteArrayOutputStream()
             bitmap?.compress(Bitmap.CompressFormat.PNG, 0, stream)
             return stream.toByteArray()
    }




}