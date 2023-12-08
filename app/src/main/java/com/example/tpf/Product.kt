package com.example.tpf

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val label: String,
    val price: String,
    val isAvailable: Boolean,
    val photo: ByteArray?
) : Parcelable {
    companion object {
        const val TABLE_NAME = "product"
        const val COLUMN_ID = "id"
        const val COLUMN_LABEL = "label"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IS_AVAILABLE = "is_available"
        const val COLUMN_PHOTO = "photo"
    }
}
