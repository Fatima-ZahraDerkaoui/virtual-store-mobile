package com.virtualstore.virtualstore.activities.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.textfield.TextInputLayout;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.database.DataManager;
import com.virtualstore.virtualstore.model.Categorie;
import com.virtualstore.virtualstore.model.Product;

import java.util.ArrayList;
import java.util.List;

public class AddEditProductActivity extends BaseAdminActivity {

    private EditText etProductName, etPrice, etDescription;
    private Spinner spinnerCategory;
    private Button btnSaveProduct, btnSelectImage;
    private ImageView ivProductPreview;
    private TextInputLayout tilProductName, tilPrice, tilDescription;

    private String productId = null;
    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<Intent> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    ivProductPreview.setImageURI(selectedImageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        etProductName = findViewById(R.id.etProductName);
        etPrice = findViewById(R.id.etPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        etDescription = findViewById(R.id.etDescription);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnSelectImage = findViewById(R.id.btnUploadImage);
        ivProductPreview = findViewById(R.id.ivProductPreview);

        tilProductName = findViewById(R.id.tilProductName);
        tilPrice = findViewById(R.id.tilPrice);
        tilDescription = findViewById(R.id.tilDescription);

        // Set up Spinner
        List<String> categoryNames = new ArrayList<>();
        for (Categorie cat : DataManager.categories) {
            categoryNames.add(cat.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categoryNames
        );
        spinnerCategory.setAdapter(adapter);

        // Check if editing
        productId = getIntent().getStringExtra("PRODUCT_ID");
        if (productId != null) {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("Edit Product");
            Product product = null;
            for (Product p : DataManager.products) {
                if (p.getId().equals(productId)) {
                    product = p;
                    break;
                }
            }

            if (product != null) {
                etProductName.setText(product.getName());
                etPrice.setText(String.valueOf(product.getPrice()));
                etDescription.setText(product.getDescription());
                if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                    ivProductPreview.setImageURI(Uri.parse(product.getImageUrl()));
                    selectedImageUri = Uri.parse(product.getImageUrl());
                }
                int categoryIndex = categoryNames.indexOf(product.getCategory());
                if (categoryIndex != -1) spinnerCategory.setSelection(categoryIndex);
            }
        } else {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("Add Product");
        }

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            selectImageLauncher.launch(intent);
        });

        btnSaveProduct.setOnClickListener(v -> {
            if (validateForm()) {
                saveProduct();
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        String name = etProductName.getText().toString();
        String priceStr = etPrice.getText().toString();
        String description = etDescription.getText().toString();

        if (name.isEmpty()) {
            tilProductName.setError("Product name is required");
            isValid = false;
        } else {
            tilProductName.setError(null);
        }

        Double price = null;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException ignored) {}

        if (priceStr.isEmpty()) {
            tilPrice.setError("Price is required");
            isValid = false;
        } else if (price == null || price <= 0) {
            tilPrice.setError("Enter a valid positive price");
            isValid = false;
        } else {
            tilPrice.setError(null);
        }

        if (description.isEmpty()) {
            tilDescription.setError("Description is required");
            isValid = false;
        } else {
            tilDescription.setError(null);
        }

        return isValid;
    }

    private void saveProduct() {
        String name = etProductName.getText().toString();
        double price = Double.parseDouble(etPrice.getText().toString());
        String category = spinnerCategory.getSelectedItem() != null ? spinnerCategory.getSelectedItem().toString() : "";
        String description = etDescription.getText().toString();
        String imageUrl = selectedImageUri != null ? selectedImageUri.toString() : "";

        if (productId == null) {
            Product newProduct = new Product(
                    "",
                    name,
                    description,
                    price,
                    category,
                    imageUrl,
                    0f,
                    0
            );
            DataManager.addProduct(newProduct);
            Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
        } else {
            Product product = null;
            for (Product p : DataManager.products) {
                if (p.getId().equals(productId)) {
                    product = p;
                    break;
                }
            }
            if (product != null) {
                product.setName(name);
                product.setPrice(price);
                product.setCategory(category);
                product.setDescription(description);
                product.setImageUrl(imageUrl);
                DataManager.updateProduct(product);
                Toast.makeText(this, "Product Updated", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}