// ## COMPONENTE — Favorite (Lista de Favoritos)
// ##
// ## Muestra todos los productos marcados como favoritos por el usuario.
// ## Reutiliza ProductCard para consistencia visual con la grilla de productos.
// ##
// ## Permite quitar productos de favoritos con toggleFavoriteAsync (si logueado)
// ## o toggleFavorite (local, si no logueado).
// ##
// ## Ruta: /favorites (protegida — requiere login via ProtectedRoute)

import React from "react";
import { Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { selectFavoriteItems, toggleFavoriteAsync, toggleFavorite } from "../store/favoriteSlice";
import { useAuth } from "../features/auth/context/AuthContext";
import ProductCard from "./ProductCard";
import "./Favorite.css";

const Favorite = () => {
  const dispatch      = useDispatch();
  const { user }      = useAuth();
  const favoriteItems = useSelector(selectFavoriteItems); // ## array de productos favoritos desde Redux

  // ## Quita el producto de favoritos:
  // ##   isFav=true porque lo estamos removiendo
  const handleRemove = (product) => {
    if (user) {
      dispatch(toggleFavoriteAsync({ product, userId: user.id, isFav: true }));
    } else {
      dispatch(toggleFavorite(product));
    }
  };

  return (
    <div className="favorite">
      <h1 className="favorite__title">Mis Favoritos</h1>

      {favoriteItems.length > 0 && (
        <p className="favorite__count">
          {favoriteItems.length} {favoriteItems.length === 1 ? "producto guardado" : "productos guardados"}
        </p>
      )}

      {favoriteItems.length === 0 ? (
        <div className="favorite__empty">
          <p>Todavia no agregaste productos a favoritos</p>
          <Link to="/products" className="btn-primary">Explorar productos</Link>
        </div>
      ) : (
        // ## Grilla de cards — el corazón en ProductCard ya maneja el toggle
        <div className="favorite__grid">
          {favoriteItems.map((product) => (
            <div key={product.id} className="favorite__item">
              <ProductCard product={product} />
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Favorite;
