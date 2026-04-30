package com.virtualstore.virtualstore.activities.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.virtualstore.virtualstore.R;
import com.virtualstore.virtualstore.activities.panier_historique.CartActivity;
import com.virtualstore.virtualstore.adapter.AvisAdapter;
import com.virtualstore.virtualstore.database.AvisDAO;
import com.virtualstore.virtualstore.database.CartDAO;
import com.virtualstore.virtualstore.model.Avis;
import com.virtualstore.virtualstore.model.CartItem;
import com.virtualstore.virtualstore.model.Product;
import java.util.List;

public class DetailProduitFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_PRODUCT_NAME = "product_name";
    private static final String ARG_PRODUCT_DESC = "product_desc";
    private static final String ARG_PRODUCT_PRICE = "product_price";
    private static final String ARG_PRODUCT_RATING = "product_rating";
    private static final String ARG_PRODUCT_IMAGE = "product_image";

    public static DetailProduitFragment newInstance(Product product) {
        DetailProduitFragment fragment = new DetailProduitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, product.getId());
        args.putString(ARG_PRODUCT_NAME, product.getName());
        args.putString(ARG_PRODUCT_DESC, product.getDescription());
        args.putDouble(ARG_PRODUCT_PRICE, product.getPrice());
        args.putFloat(ARG_PRODUCT_RATING, product.getRating());
        args.putString(ARG_PRODUCT_IMAGE, product.getImageUrl());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_produit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Récupérer les données
        Bundle args = getArguments();
        String productId = args.getString(ARG_PRODUCT_ID);
        String name = args.getString(ARG_PRODUCT_NAME);
        String desc = args.getString(ARG_PRODUCT_DESC);
        double price = args.getDouble(ARG_PRODUCT_PRICE);
        float rating = args.getFloat(ARG_PRODUCT_RATING);
        String imageUrl = args.getString(ARG_PRODUCT_IMAGE);

        // Remplir les vues
        TextView tvName = view.findViewById(R.id.tvDetailName);
        TextView tvPrice = view.findViewById(R.id.tvDetailPrice);
        TextView tvDesc = view.findViewById(R.id.tvDetailDescription);
        RatingBar ratingBar = view.findViewById(R.id.ratingBarDetail);
        Button btnCart = view.findViewById(R.id.btnAddToCart);
        ImageView imgProduct = view.findViewById(R.id.imgDetailProduct);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        tvName.setText(name);
        tvPrice.setText(String.format("%.2f DH", price));
        tvDesc.setText(desc);
        ratingBar.setRating(rating);

        // Charger l'image avec Glide
        Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imgProduct);

        // Bouton retour via toolbar navigation icon
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> 
                requireActivity().getSupportFragmentManager().popBackStack()
            );
        }

        // Bouton Ajouter au panier
        CartDAO cartDAO = new CartDAO(requireContext());
        btnCart.setOnClickListener(v -> {
            cartDAO.addItem(new CartItem(productId, name, price, 1));
            Toast.makeText(requireContext(), name + " ajouté au panier !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireActivity(), CartActivity.class));
        });

        // Avis clients
        AvisDAO avisDAO = new AvisDAO(requireContext());
        List<Avis> avisList = avisDAO.getAvisByProduct(productId);
        RecyclerView recyclerAvis = view.findViewById(R.id.recyclerViewAvis);
        recyclerAvis.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerAvis.setAdapter(new AvisAdapter(avisList));
    }
}