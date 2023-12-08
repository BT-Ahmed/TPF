package com.example.tpf

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productapp.ProductDbHelper
import com.google.android.material.button.MaterialButton

class ProductAdapter(
    private val context: Context,
    private val products: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textViewLabel: TextView = itemView.findViewById(R.id.textViewLabel)
        private val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        private val textViewAvailability: TextView = itemView.findViewById(R.id.textViewAvailability)
        private val buttonEdit: MaterialButton = itemView.findViewById(R.id.buttonEdit)
        private val buttonDelete: MaterialButton = itemView.findViewById(R.id.buttonDelete)

        fun bind(product: Product) {
            // Set the image view with the product photo or a default image
            if (product.photo != null) {
                val bitmap = BitmapFactory.decodeByteArray(product.photo, 0, product.photo.size)
                imageView.setImageBitmap(bitmap)
            } else {
                imageView.setImageResource(R.drawable.baseline_product_placeholder_image_24)
            }

            // Set the text views with the product properties
            textViewLabel.text = product.label
            textViewPrice.text = product.price
            textViewAvailability.text = if (product.isAvailable) "Available" else "Out of stock"

            // Get the text size and color preferences from the settings
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val textSize = sharedPreferences.getString("text_size", "16")?.toFloat() ?: 16f
            val textColor = sharedPreferences.getInt("text_color", ContextCompat.getColor(context, R.color.black))

            // Apply the text size and color to the text views
            textViewLabel.textSize = textSize
            textViewPrice.textSize = textSize
            textViewAvailability.textSize = textSize
            textViewLabel.setTextColor(textColor)
            textViewPrice.setTextColor(textColor)
            textViewAvailability.setTextColor(textColor)

            // Set the click listeners for the edit and delete buttons
            buttonEdit.setOnClickListener {
                // Start the EditProductActivity with the product data as an extra
                val intent = Intent(context, EditProductActivity::class.java)
                intent.putExtra(Product.TABLE_NAME, product)
                context.startActivity(intent)
            }

            buttonDelete.setOnClickListener {
                // Delete the product from the database and notify the adapter
                val dbHelper = ProductDbHelper(context)
                val result = dbHelper.deleteProduct(product.id)
                if (result > 0) {
                    Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show()
                    notifyItemRemoved(adapterPosition)
                } else {
                    Toast.makeText(context, "Error deleting product", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
