package com.virtualstore.virtualstore.model;

public class Avis {
    private int id;
    private String productId;
    private String auteur;
    private String commentaire;
    private float note;

    public Avis(int id, String productId, String auteur, String commentaire, float note) {
        this.id = id;
        this.productId = productId;
        this.auteur = auteur;
        this.commentaire = commentaire;
        this.note = note;
    }

    public int getId() { return id; }
    public String getProductId() { return productId; }
    public String getAuteur() { return auteur; }
    public String getCommentaire() { return commentaire; }
    public float getNote() { return note; }
}