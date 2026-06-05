// ProductCard.jsx
// REDUX (useSelector + useDispatch) — favoritos sincronizados con la DB

import React from "react";
import { Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { toggleFavoriteAsync, toggleFavorite, selectIsFavorite } from "../store/favoriteSlice";
import { useAuth } from "../features/auth/context/AuthContext";
import "./ProductCard.css";

const DEFAULT_IMAGE = "https://placehold.co/300x200?text=Sin+imagen";

const ProductCard = ({ product, badge }) => {
  const dispatch = useDispatch();
  const { user, token } = useAuth();
  const esFavorito = useSelector(selectIsFavorite(product.id));

  const badgeClass =
    badge === "Oferta"
      ? "product-card__badge product-card__badge--oferta"
      : "product-card__badge product-card__badge--top";

  const heartIcon = esFavorito ? "liked" : "like";

  const handleFavClick = (e) => {
    e.preventDefault();
    if (user && token) {
      // Usuario logueado: sincroniza con la DB
      dispatch(toggleFavoriteAsync({ product, userId: user.id, token, isFav: esFavorito }));
    } else {
      // Sin login: solo actualiza el estado local
      dispatch(toggleFavorite(product));
    }
  };

  return (
    <Link to={`/products/${product.id}`} className="product-card__link">
      <div className="product-card">
        {badge && <span className={badgeClass}>{badge}</span>}

        <button
          className="product-card__fav"
          onClick={handleFavClick}
          title={esFavorito ? "Quitar de favoritos" : "Agregar a favoritos"}
        >
          {esFavorito ? "❤️" : "🤍"}
        </button>

        <img
          className="product-card__img"
          src={product.imageUrl || DEFAULT_IMAGE}
          alt={product.name}
          onError={(e) => { e.target.src = DEFAULT_IMAGE; }}
        />

        <div className="product-card__body">
          <h3 className="product-card__name">{product.name}</h3>
          <p className="product-card__desc">
            {product.description?.length > 60
              ? product.description.substring(0, 60) + "..."
              : product.description}
          </p>
          <div className="product-card__footer">
            <span className="product-card__price">
              ${Number(product.price).toLocaleString("es-AR")}
            </span>
            <span className={`product-card__stock ${product.stock > 0 ? "product-card__stock--ok" : "product-card__stock--out"}`}>
              {product.stock > 0 ? `Stock: ${product.stock}` : "Sin stock"}
            </span>
          </div>
          {product.categories?.length > 0 && (
            <div className="product-card__categories">
              {product.categories.map((cat) => (
                <span key={cat.id} className="product-card__cat">{cat.name}</span>
              ))}
            </div>
          )}
        </div>
      </div>
    </Link>
  );
};

export default ProductCard;
