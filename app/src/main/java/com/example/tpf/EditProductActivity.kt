package com.example.tpf

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.productapp.ProductDbHelper
import com.google.android.material.button.MaterialButton
import java.io.ByteArrayOutputStream

class EditProductActivity : AppCompatActivity() {

    private lateinit var editTextLabel: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var switchAvailability: SwitchCompat
    private lateinit var imageView: ImageView
    private lateinit var buttonChooseImage: MaterialButton
    private lateinit var buttonSave: MaterialButton

    private var product: Product? = null
    private var imageUri: Uri? = null
    private var imageBytes: ByteArray? = null

    // Create an activity result launcher for the image picker
    private val imagePickerLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Get the image uri from the result data
                imageUri = result.data?.data
                if (imageUri != null) {
                    // Decode the image uri to a bitmap and set it to the image view
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                contentResolver,
                                imageUri!!
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    }
                    imageView.setImageBitmap(bitmap)

                    // Convert the bitmap to a byte array and store it in the imageBytes variable
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    imageBytes = baos.toByteArray()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        setSupportActionBar(findViewById(R.id.edit_product_toolbar))

        // Enable the home button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize the views
        editTextLabel = findViewById(R.id.editTextLabel)
        editTextPrice = findViewById(R.id.editTextPrice)
        switchAvailability = findViewById(R.id.switchAvailability)
        imageView = findViewById(R.id.imageView)
        buttonChooseImage = findViewById(R.id.buttonChooseImage)
        buttonSave = findViewById(R.id.buttonSave)

        // Get the product data from the intent extra if it exists
        product = intent.getParcelableExtra(Product.TABLE_NAME)

        // Populate the views with the product data if it is not null
        if (product != null) {
            editTextLabel.setText(product!!.label)
            editTextPrice.setText(product!!.price)
            switchAvailability.isChecked = product!!.isAvailable
            if (product!!.photo != null) {
                // Decode the byte array to a bitmap and set it to the image view
                val bitmap = BitmapFactory.decodeByteArray(product!!.photo, 0, product!!.photo!!.size)
                imageView.setImageBitmap(bitmap)
                // Store the byte array in the imageBytes variable
                imageBytes = product!!.photo
            }
        }

        // Set the click listener for the choose image button
        buttonChooseImage.setOnClickListener {
            // Create an intent to pick an image from the device or take a photo with the camera
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            // Launch the image picker with the intent
            imagePickerLauncher.launch(intent)
        }

        // Set the click listener for the save button
        buttonSave.setOnClickListener {
            // Get the input values from the views
            val label = editTextLabel.text.toString().trim()
            val price = editTextPrice.text.toString().trim()
            val isAvailable = switchAvailability.isChecked

            // Validate the input values
            if (label.isEmpty()) {
                editTextLabel.error = "Label is required"
                return@setOnClickListener
            }
            if (price.isEmpty()) {
                editTextPrice.error = "Price is required"
                return@setOnClickListener
            }

            // Create a product object with the input values and the imageBytes
            val newProduct = Product(
                product?.id ?: 0,
                label,
                price,
                isAvailable,
                imageBytes
            )

            // Save the product to the database using the helper class
            val dbHelper = ProductDbHelper(this)
            val result = if (product == null) {
                // Insert a new product if the product is null
                dbHelper.insertProduct(newProduct)
            } else {
                // Update an existing product if the product is not null
                dbHelper.updateProduct(newProduct)
            }

            // Show a toast message based on the result
            if (result != null) {
                Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show()
                // Finish the activity and go back to the main activity
                finish()
            } else {
                Toast.makeText(this, "Error saving product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu from the menu_edit_product.xml file
        menuInflater.inflate(R.menu.menu_edit_product, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle the menu item selection
        return when (item.itemId) {
            R.id.action_delete -> {
                deleteProduct()
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    // A helper function to delete the product from the database
    private fun deleteProduct() {
        // Delete the product from the database if it is not null
        if (product != null) {
            val dbHelper = ProductDbHelper(this)
            val result = dbHelper.deleteProduct(product!!.id)
            if (result > 0) {
                Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
                // Finish the activity and go back to the main activity
                finish()
            } else {
                Toast.makeText(this, "Error deleting product", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
