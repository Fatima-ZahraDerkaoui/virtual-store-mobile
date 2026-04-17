package com.virtualstore.virtualstore.activities.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.activities.panier_historique.CartActivity;
import com.virtualstore.virtualstore.adapter.ProductAdapter;
import com.virtualstore.virtualstore.activities.catalog.CatalogActivity;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.database.CategorieDAO;
import com.virtualstore.virtualstore.database.ProductDAO;
import com.virtualstore.virtualstore.model.CartItem;
import com.virtualstore.virtualstore.model.Categorie;
import com.virtualstore.virtualstore.model.Product;
import java.util.ArrayList;
import java.util.List;

public class CatalogueFragment extends Fragment {

    private ProductAdapter adapter;
    private List<Product> allProducts;
    private ProductDAO productDAO;
    private CategorieDAO categorieDAO;
    private CartDAO cartDAO;
    private LinearLayout categoryLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalogue, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productDAO = new ProductDAO(requireContext());
        categorieDAO = new CategorieDAO(requireContext());
        cartDAO = new CartDAO(requireContext());
        
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewProducts);
        SearchView searchView = view.findViewById(R.id.searchView);
        categoryLayout = view.findViewById(R.id.categoryFilterLayout);

        // Initial load
        allProducts = productDAO.getAllProducts();

        // Setup RecyclerView
        adapter = new ProductAdapter(new ArrayList<>(allProducts),
                product -> {
                    DetailProduitFragment detailFragment = DetailProduitFragment.newInstance(product);
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit();
                },
                product -> {
                    cartDAO.addItem(new CartItem(product.getId(), product.getName(), product.getPrice(), 1));
                    if (requireActivity() instanceof CatalogActivity) {
                        ((CatalogActivity) requireActivity()).updateCartCount();
                    }
                    Toast.makeText(requireContext(), product.getName() + " ajouté au panier", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(requireActivity(), CartActivity.class);
                    startActivity(intent);
                }
        );

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);

        loadCategories();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    adapter.updateList(new ArrayList<>(allProducts));
                } else {
                    adapter.updateList(productDAO.searchProducts(newText));
                }
                return true;
            }
        });
    }

    private void loadCategories() {
        categoryLayout.removeAllViews();
        List<Categorie> categories = categorieDAO.getAllCategories();
        
        // Add "Tous" category
        addCategoryChip("Tous");
        
        for (Categorie cat : categories) {
            addCategoryChip(cat.getName());
        }
    }

    private void addCategoryChip(String name) {
        TextView chip = new TextView(requireContext());
        chip.setText(name);
        chip.setPadding(32, 16, 32, 16);
        chip.setTextColor(0xFF1A1A1A);
        chip.setBackgroundResource(R.drawable.chip_background);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 8, 8, 8);
        chip.setLayoutParams(params);
        chip.setOnClickListener(v -> filterByCategory(name));
        categoryLayout.addView(chip);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh products from DB whenever we return to the fragment
        allProducts = productDAO.getAllProducts();
        adapter.updateList(new ArrayList<>(allProducts));
    }

    private void filterByCategory(String category) {
        List<Product> filtered = new ArrayList<>();
        if (category.equals("Tous")) {
            filtered.addAll(allProducts);
        } else {
            for (Product p : allProducts) {
                if (p.getCategory().equalsIgnoreCase(category)) {
                    filtered.add(p);
                }
            }
        }
        adapter.updateList(filtered);
    }
}