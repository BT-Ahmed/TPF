package com.example.tpf

import android.provider.BaseColumns

object ProductContract {
    class ProductEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "product"
            const val COLUMN_ID = BaseColumns._ID
            const val COLUMN_LABEL = "label"
            const val COLUMN_PRICE = "price"
            const val COLUMN_IS_AVAILABLE = "is_available"
            const val COLUMN_PHOTO = "photo"
        }
    }
}
