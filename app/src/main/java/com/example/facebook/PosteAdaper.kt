package com.example.facebook

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.example.facebook.data.Poste
import com.example.facebook.db.DatabaseFb


class PosteAdaper(
    var mcontext: Context,
    var resource: Int,
    var value: ArrayList<Poste>
) : ArrayAdapter<Poste>(mcontext, resource, value)
{
    constructor(parcel: Parcel) : this(
        TODO("mcontext"),
        parcel.readInt(),
        TODO("value")
    ) {
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val poste = value[position]
        val convertView = LayoutInflater.from(this.mcontext).inflate(this.resource, parent, false)
        var titre = convertView.findViewById<TextView>(R.id.titre)
        var image = convertView.findViewById<ImageView>(R.id.imageUser)
        var description = convertView.findViewById<TextView>(R.id.description)
        var menuItem = convertView.findViewById<ImageView>(R.id.menuItem)
        var jaime = convertView.findViewById<TextView>(R.id.jaime)

        titre.text = poste.titre
        description.text = poste.description
        jaime.text = "${poste.jaime} j'aime"
        val resizedBitmap = getByteMap(poste.image)
        image.setImageBitmap(resizedBitmap)
        var db = DatabaseFb(mcontext)
        menuItem.setOnClickListener{
            val modal = PopupMenu(mcontext, it)
            modal.menuInflater.inflate(R.menu.modal_menu, modal.menu)
            modal.setOnMenuItemClickListener {
                when (it.itemId)
                {
                    R.id.modalItemDelete->{

                       var result =  db.deletePoste(poste.id)
                        if(result)
                        {
                            value.removeAt(position)
                            notifyDataSetChanged()
                            Toast.makeText(mcontext, "Suppression Effectuer avec succes", Toast.LENGTH_SHORT).show()
                        }

                    }
                    R.id.modalItemShow->{
                        Intent(mcontext, DetailPoste::class.java).also {item : Intent->
                            val poste : Poste = value[position]
                            item.putExtra("Poste", poste as Parcelable)
                            mcontext.startActivity(item)
                        }

                    }

                }
                true
            }
            modal.show()
        }
        jaime.setOnClickListener {
            db.incrementJaime(poste, this)
            jaime.text = "${poste.jaime} j'aime"
        }
        return convertView
    }

    fun getByteMap(image: ByteArray?): Bitmap? {
        return image?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }






}

