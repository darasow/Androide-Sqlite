package com.example.facebook

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.example.facebook.data.User
import com.example.facebook.db.DatabaseFb

class FormeUser : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    lateinit var db : DatabaseFb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forme_user)

        val toolbar : Toolbar = findViewById(R.id.toolbarFormUser) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(Color.WHITE)
         db = DatabaseFb(this)
        var userName = findViewById<TextView>(R.id.nameUser)
        var userEmail = findViewById<TextView>(R.id.emailUser)
        var userPassword = findViewById<TextView>(R.id.passwordUser)
        var userPasswordConfirme = findViewById<TextView>(R.id.confirmePassword)
        var creeCompte = findViewById<TextView>(R.id.userCreate)
        var errorFormeUser = findViewById<TextView>(R.id.errorFormUser)


        creeCompte.setOnClickListener {
         var userNameText = userName.text.toString().trim()
         var userPasswordText = userPassword.text.toString().trim()
         var userEmailText = userEmail.text.toString().trim()
         var userPasswordConfirmeText = userPasswordConfirme.text.toString().trim()

         if(userNameText == "" || userEmailText == "" || userPasswordText == "" || userPasswordConfirmeText == "")
         {
             errorFormeUser.setText("Tout les champs sont obligatoire")
             errorFormeUser.visibility = View.VISIBLE
         }else if(userPasswordText != userPasswordConfirmeText)
         {
             errorFormeUser.setText("Donner le meme mot de passe")
             errorFormeUser.visibility = View.VISIBLE

         }else
         {
             errorFormeUser.setText("")
             errorFormeUser.visibility = View.INVISIBLE
             var user : User = User(userNameText, userEmailText, userPasswordText)
             var result = db.adduser(user)
             if(result)
             {
                 errorFormeUser.setText("Enregistrement effectuer avec succes")
                 errorFormeUser.setBackgroundColor(R.color.purple_700)
                 errorFormeUser.visibility = View.VISIBLE
             }else
             {
                 errorFormeUser.setBackgroundColor(R.color.teal_700)
                 errorFormeUser.setText("L'email existe deja")
                 errorFormeUser.visibility = View.VISIBLE
             }
         }



        }





    }
}