package com.example.facebook


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.facebook.data.Poste
import com.example.facebook.db.DatabaseFb

class Acceuil : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    lateinit var listeView : ListView
    lateinit var listePostes : ArrayList<Poste>
    lateinit var adapter : PosteAdaper
    lateinit var db : DatabaseFb


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceuil)

        val toolbar : Toolbar = findViewById(R.id.toolbarAcceuil) as Toolbar
        setSupportActionBar(toolbar)
        db = DatabaseFb(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(Color.WHITE)

        val emailExtra = intent.getStringExtra("email")
        val passwordExtra = intent.getStringExtra("password")

        listeView = findViewById(R.id.listeView) as ListView

        registerForContextMenu(listeView)


    }

    override fun onResume() {
        listePostes = db.findPostes()
        adapter = PosteAdaper(this, R.layout.item_poste, listePostes)
        listeView.adapter = adapter

        listeView.setOnItemClickListener( { adapterView, view,position, id ->
            val posteClicked = listePostes[position]
            Intent(applicationContext, DetailPoste::class.java).also {
                it.putExtra("Poste", posteClicked)
                startActivity(it)
            }
        })
        super.onResume()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_acceuil, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.ajouter -> {

                Intent(this, FormPoste::class.java).also {

                    startActivity(it)
                }
                return true
            }
            R.id.deleted ->{
                Toast.makeText(applicationContext, "Vous avec clicker sur delete", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.config->
            {
                Toast.makeText(applicationContext, "Vous avec clicker sur config", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.deconection ->
            {
                var confirmeFragment = ConfirmeDialoguePosteFragment("", "Quitter", applicationContext)
                confirmeFragment.listener = object : ConfirmeDialoguePosteFragment.ConfirmeDialogueListener{
                    override fun ondialoguePositiveClick() {
                        finish()
                    }
                    override fun ondialogueNegativeClick() {

                    }
                }
                confirmeFragment.show(supportFragmentManager, "ConfirmationDialogue")

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

//Ici c'est pour afficher un menu Contxtuel l'orsqu'on click longtemp sur un element
    // j'ai commenter son code xml pour creer un menu dans les trois point en haut a droit

//    override fun onCreateContextMenu(
//        menu: ContextMenu?,
//        v: View?,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        menuInflater.inflate(R.menu.context_menu, menu)
//        super.onCreateContextMenu(menu, v, menuInfo)
//    }

//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        val info : AdapterContextMenuInfo = item.menuInfo as AdapterContextMenuInfo
//        val position : Int = info.position
//        when(item.itemId)
//        {
//            R.id.ajouterContext -> {
//                Intent(applicationContext, DetailPoste::class.java).also {
//                    val poste : Poste = listePostes[position]
//                    it.putExtra("Poste", poste)
//                    startActivity(it)
//                }
//                return true
//            }
//            R.id.SupprimerContext ->{
//                listePostes.removeAt(position)
//                adapter.notifyDataSetChanged()
//                Toast.makeText(applicationContext, "Suppression effectuer avec succes context {$position}", Toast.LENGTH_SHORT).show()
//                return true
//            }
//        }
//        return super.onContextItemSelected(item)
//    }




}