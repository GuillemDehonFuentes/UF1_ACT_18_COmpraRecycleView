import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_act_18_comprarecycleview.R
import com.google.android.material.button.MaterialButton

data class Product(
    val name: String,
    val price: Double,
    val imageResId: Int,
    var quantity: Int = 0
)


class MainActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var totalPriceTextView: TextView

    private val products = mutableListOf(
        Product("Laptop", 1200.0, R.drawable.laptop),
        Product("Smartphone", 800.0, R.drawable.smartphone),
        Product("Headphones", 200.0, R.drawable.headphones),
        Product("Smartwatch", 150.0, R.drawable.smartwatch)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalPriceTextView = findViewById(R.id.total_price)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val checkoutButton: MaterialButton = findViewById(R.id.checkout_button)

        productAdapter = ProductAdapter(products) { updateTotalPrice() }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productAdapter

        updateTotalPrice()

        checkoutButton.setOnClickListener {
            // Acción para realizar el checkout (puedes implementar una nueva actividad)
        }
    }

    private fun updateTotalPrice() {
        val totalPrice = products.sumOf { it.price * it.quantity }
        totalPriceTextView.text = "Total: $%.2f".format(totalPrice)
    }
}

class ProductAdapter(
    private val products: List<Product>,
    private val onQuantityChange: () -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.product_name)
        private val priceTextView: TextView = itemView.findViewById(R.id.product_price)
        private val imageView: ImageView = itemView.findViewById(R.id.product_image)
        private val quantityTextView: TextView = itemView.findViewById(R.id.product_quantity)
        private val addButton: MaterialButton = itemView.findViewById(R.id.add_button)
        private val removeButton: MaterialButton = itemView.findViewById(R.id.remove_button)

        fun bind(product: Product) {
            nameTextView.text = product.name
            priceTextView.text = "$%.2f".format(product.price)
            imageView.setImageResource(product.imageResId)
            quantityTextView.text = product.quantity.toString()

            addButton.setOnClickListener {
                product.quantity++
                quantityTextView.text = product.quantity.toString()
                onQuantityChange()
            }

            removeButton.setOnClickListener {
                if (product.quantity > 0) {
                    product.quantity--
                    quantityTextView.text = product.quantity.toString()
                    onQuantityChange()
                }
            }
        }
    }
}
