package com.example.productapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tpf.Product
import com.example.tpf.ProductContract.ProductEntry

class ProductDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        const val DATABASE_NAME = "product.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create the product table with the schema defined in the contract class
        val SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE ${ProductEntry.TABLE_NAME} (" +
                "${ProductEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${ProductEntry.COLUMN_LABEL} TEXT NOT NULL, " +
                "${ProductEntry.COLUMN_PRICE} TEXT NOT NULL, " +
                "${ProductEntry.COLUMN_IS_AVAILABLE} INTEGER NOT NULL, " +
                "${ProductEntry.COLUMN_PHOTO} BLOB)"
        db?.execSQL(SQL_CREATE_PRODUCT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop the product table if it exists and create a new one
        val SQL_DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS ${ProductEntry.TABLE_NAME}"
        db?.execSQL(SQL_DROP_PRODUCT_TABLE)
        onCreate(db)
    }

    // Insert a new product into the database and return the row id
    fun insertProduct(product: Product): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ProductEntry.COLUMN_LABEL, product.label)
            put(ProductEntry.COLUMN_PRICE, product.price)
            put(ProductEntry.COLUMN_IS_AVAILABLE, product.isAvailable)
            put(ProductEntry.COLUMN_PHOTO, product.photo)
        }
        return db.insert(ProductEntry.TABLE_NAME, null, values)
    }

    // Update an existing product in the database and return the number of affected rows
    fun updateProduct(product: Product): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ProductEntry.COLUMN_LABEL, product.label)
            put(ProductEntry.COLUMN_PRICE, product.price)
            put(ProductEntry.COLUMN_IS_AVAILABLE, product.isAvailable)
            put(ProductEntry.COLUMN_PHOTO, product.photo)
        }
        val selection = "${ProductEntry.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(product.id.toString())
        return db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs)
    }

    // Delete an existing product from the database and return the number of affected rows
    fun deleteProduct(id: Int): Int {
        val db = writableDatabase
        val selection = "${ProductEntry.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(ProductEntry.TABLE_NAME, selection, selectionArgs)
    }

    // Query all the products from the database and return a list of product objects
    fun queryProducts(): List<Product> {
        val db = readableDatabase
        val projection = arrayOf(
            ProductEntry.COLUMN_ID,
            ProductEntry.COLUMN_LABEL,
            ProductEntry.COLUMN_PRICE,
            ProductEntry.COLUMN_IS_AVAILABLE,
            ProductEntry.COLUMN_PHOTO
        )
        val cursor = db.query(
            ProductEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val products = mutableListOf<Product>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(ProductEntry.COLUMN_ID))
                val label = getString(getColumnIndexOrThrow(ProductEntry.COLUMN_LABEL))
                val price = getString(getColumnIndexOrThrow(ProductEntry.COLUMN_PRICE))
                val isAvailable = getInt(getColumnIndexOrThrow(ProductEntry.COLUMN_IS_AVAILABLE)) == 1
                val photo = getBlob(getColumnIndexOrThrow(ProductEntry.COLUMN_PHOTO))
                val product = Product(id, label, price, isAvailable, photo)
                products.add(product)
            }
        }
        cursor.close()
        return products
    }
}
