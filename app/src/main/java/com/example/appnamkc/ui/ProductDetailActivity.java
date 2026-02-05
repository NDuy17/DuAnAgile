package com.example.appnamkc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appnamkc.R;
import com.example.appnamkc.adapter.ProductAdapter;
import com.example.appnamkc.model.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage, ivBack;
    private TextView tvProductName, tvProductDescription, tvProductPrice;
    private TextView tvPurchases, tvLikes, tvViews;
    private EditText etQuantity;
    private ImageButton btnIncrease, btnDecrease;
    private Button btnAddToCart, btnCancel;
    
    private Product product;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get product from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            product = (Product) intent.getSerializableExtra("product");
        }

        if (product == null) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupViews();
        setupListeners();
    }

    private void initViews() {
        ivProductImage = findViewById(R.id.ivProductImage);
        ivBack = findViewById(R.id.ivBack);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvPurchases = findViewById(R.id.tvPurchases);
        tvLikes = findViewById(R.id.tvLikes);
        tvViews = findViewById(R.id.tvViews);
        etQuantity = findViewById(R.id.etQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupViews() {
        // Set product info
        tvProductName.setText(product.getName());
        tvProductDescription.setText(product.getDescription());
        
        // Format price
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        tvProductPrice.setText(formatter.format(product.getPrice()) + " VNĐ");

        // Set product image
        int imageResId = getProductImage(product.getCategory(), product.getName());
        if (imageResId != 0) {
            ivProductImage.setImageResource(imageResId);
        }

        // Set stats (sample data)
        tvPurchases.setText("Lượt mua: 220");
        tvLikes.setText("Yêu thích: 85");
        tvViews.setText("Lượt xem: 450");

        // Set quantity
        etQuantity.setText(String.valueOf(quantity));
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            etQuantity.setText(String.valueOf(quantity));
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                etQuantity.setText(String.valueOf(quantity));
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            // Add to cart logic here
            Toast.makeText(this, "Đã thêm " + quantity + "x " + product.getName() + " vào giỏ hàng", 
                    Toast.LENGTH_SHORT).show();
            // You can pass data back to HomeActivity if needed
            Intent resultIntent = new Intent();
            resultIntent.putExtra("product", product);
            resultIntent.putExtra("quantity", quantity);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private int getProductImage(String category, String name) {
        if (category == null) return 0;
        
        switch (category) {
            case "Burger":
                return R.drawable.ic_burger_product;
            case "Pizza":
                return R.drawable.ic_pizza;
            case "Cơm":
                return R.drawable.ic_rice;
            case "Đồ uống":
                return R.drawable.ic_drink;
            default:
                // Try to match by name
                if (name != null) {
                    String lowerName = name.toLowerCase();
                    if (lowerName.contains("burger")) {
                        return R.drawable.ic_burger_product;
                    } else if (lowerName.contains("pizza")) {
                        return R.drawable.ic_pizza;
                    } else if (lowerName.contains("cơm") || lowerName.contains("com")) {
                        return R.drawable.ic_rice;
                    } else if (lowerName.contains("nước") || lowerName.contains("nuoc") || 
                             lowerName.contains("drink") || lowerName.contains("cola")) {
                        return R.drawable.ic_drink;
                    }
                }
                return R.drawable.ic_burger_product; // Default
        }
    }
}

