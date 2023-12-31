package com.example.tpf

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productapp.ProductDbHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var addProductFab: FloatingActionButton
    private lateinit var settingsFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_main_toollbar))

        // Enable the home button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize the recycler view and the adapter
        recyclerView = findViewById(R.id.all_products_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(this, queryProducts())
        recyclerView.adapter = productAdapter

        // Initialize the floating action button and set the click listener
        addProductFab = findViewById(R.id.add_product_fab)
        addProductFab.setOnClickListener {
            // Start the EditProductActivity with no extra data
            val intent = Intent(this, EditProductActivity::class.java)
            startActivity(intent)
        }

        settingsFab = findViewById(R.id.settings_fab)
        settingsFab.setOnClickListener {
            // Start the EditProductActivity with no extra data
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the product list and the adapter when the activity resumes
        productAdapter = ProductAdapter(this, queryProducts())
        recyclerView.adapter = productAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu from the menu_main.xml file
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle the menu item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                // Start the SettingsActivity
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Query all the products from the database using the helper class
    private fun queryProducts(): List<Product> {
        val dbHelper = ProductDbHelper(this)
        return dbHelper.queryProducts()
    }
}
