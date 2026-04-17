# Virtual Store Mobile App

Application Android de commerce électronique développée en Java avec SQLite.  
Elle permet de consulter des produits, gérer un panier, effectuer un paiement simulé et consulter l’historique des commandes.

---

## Fonctionnalités

### Utilisateur
- Consultation du catalogue de produits
- Recherche de produits
- Filtrage par catégories
- Ajout au panier avec gestion des quantités
- Paiement simulé par carte bancaire
- Consultation de l’historique des commandes
- Affichage des détails des commandes

### Administration
- Ajout, modification et suppression de produits
- Gestion des catégories
- Gestion des utilisateurs

---

## Design et interface
- Couleurs principales :
  - Bleu principal : #1877F2
  - Fond : #F0F2F5
  - Texte principal : #050505
- Interface basée sur RecyclerView et Toolbar
- Design simple et cohérent

---

## Architecture du projet

### Activities
- CatalogActivity
- CartActivity
- PaymentActivity
- HistoryActivity
- OrderDetailsActivity

### Fragments
- CatalogueFragment
- DetailProduitFragment

### Base de données SQLite
- DatabaseHelper
- ProductDAO
- CartDAO
- OrderDAO

### Adapters
- ProductAdapter
- CartAdapter
- OrderAdapter
- HistoryAdapter
- OrderLineAdapter

---

## Base de données

Tables utilisées :
- users
- products
- categories
- cart
- orders
- order_lines
- avis

---

## Installation

1. Cloner le projet
```bash
git clone https://github.com/Fatima-ZahraDerkaoui/virtual-store-mobile.git
