package com.example.appnamkc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnamkc.R;
import com.example.appnamkc.model.Product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onViewDetailsClick(Product product);
        void onAddToCartClick(Product product);
        void onFavoriteClick(Product product);
    }

    public ProductAdapter(ArrayList<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(ArrayList<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
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

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage, ivFavorite;
        private TextView tvProductName, tvProductDescription, tvProductPrice;
        private Button btnViewDetails, btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }

        public void bind(Product product) {
            tvProductName.setText(product.getName());
            tvProductDescription.setText(product.getDescription());
            
            // Format price
            NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            tvProductPrice.setText(formatter.format(product.getPrice()) + " VNĐ");

            // Set product image based on category
            int imageResId = getProductImage(product.getCategory(), product.getName());
            if (imageResId != 0) {
                ivProductImage.setImageResource(imageResId);
            }

            // Update favorite icon
            ivFavorite.setImageResource(product.isFavorite() 
                    ? R.drawable.ic_heart 
                    : R.drawable.ic_heart_outline);

            // Set click listeners
            ivFavorite.setOnClickListener(v -> {
                product.setFavorite(!product.isFavorite());
                ivFavorite.setImageResource(product.isFavorite() 
                        ? R.drawable.ic_heart 
                        : R.drawable.ic_heart_outline);
                if (listener != null) {
                    listener.onFavoriteClick(product);
                }
            });

            btnViewDetails.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewDetailsClick(product);
                }
            });

            btnAddToCart.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToCartClick(product);
                }
            });
        }
    }
}

