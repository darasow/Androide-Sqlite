package com.example.facebook.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import java.io.ByteArrayOutputStream

data class Poste(
    var titre: String?,
    var description: String?,
    var image: ByteArray?,
    var jaime : Int? = 0,
) : Parcelable {
    constructor(parcel: Parcel?) : this(
        parcel?.readString(),
        parcel?.readString(),
        parcel?.createByteArray(),
        parcel?.readInt(),
    ) {
    }

    var id : Int = -1
    constructor(id : Int, titre: String?, description: String?, image: ByteArray?, jaime : Int?):this(titre, description, image, jaime)
    {
       this.id = id
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(titre)
        parcel.writeString(description)
        parcel.writeByteArray(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Poste> {
        override fun createFromParcel(parcel: Parcel): Poste {
            return Poste(parcel)
        }

        override fun newArray(size: Int): Array<Poste?> {
            return arrayOfNulls(size)
        }
    }
    fun resizeImage(image: ByteArray?, maxWidth: Int, maxHeight: Int): Bitmap? {
        image?.let {
            val originalBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)

            var width = 200
            var height = 200

            val aspectRatio: Float = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()

            if (width > height) {
                width = maxWidth
                height = (width / aspectRatio).toInt()
            } else {
                height = maxHeight
                width = (height * aspectRatio).toInt()
            }

            return Bitmap.createScaledBitmap(originalBitmap, width, height, true)
        }
        return null
    }







}
