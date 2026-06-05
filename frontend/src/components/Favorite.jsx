// Favorite.jsx
// REDUX (useSelector + useDispatch) — favoritos sincronizados con la DB

import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { selectFavoriteItems, toggleFavoriteAsync, toggleFavorite } from "../store/favoriteSlice";
import { useAuth } from "../features/auth/context/AuthContext";
import ProductCard from "./ProductCard";
import "./Favorite.css";

const Favorite = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { user, token } = useAuth();
  const favoriteItems = useSelector(selectFavoriteItems);

  const handleRemove = (product) => {
    if (user && token) {
      dispatch(toggleFavoriteAsync({ product, userId: user.id, token, isFav: true }));
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
