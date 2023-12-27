package com.example.facebook

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.facebook.data.Poste

class DetailPoste : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_poste)
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbarDetail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(Color.WHITE)

        var poste = intent.getParcelableExtra<Poste>("Poste") as Poste
        val titre: TextView = findViewById(R.id.titreDetail)
        val description: TextView = findViewById(R.id.descriptionDetail)
        val image: ImageView = findViewById(R.id.imageUserDetail)

        titre.text = poste.titre
        supportActionBar?.title = poste.titre
        description.text = poste.description
        var bitmat = getByteMap(poste.image)
        image.setImageBitmap(bitmat)

    }

    fun getByteMap(image: ByteArray?): Bitmap? {

        val bitmap = image?.let { BitmapFactory.decodeByteArray(image, 0, it.size) }
        return bitmap

    }

}