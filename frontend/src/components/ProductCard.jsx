// ## COMPONENTE — ProductCard (Tarjeta de Producto)
// ##
// ## Muestra la información resumida de un producto en la grilla.
// ## Incluye un botón de favorito (❤️/🤍) en la esquina superior derecha.
// ##
// ## Comportamiento del corazón:
// ##   - Usuario logueado → sincroniza con la DB via toggleFavoriteAsync
// ##   - Sin login        → toggle solo en memoria local (Redux)
// ##   - e.preventDefault() evita navegar al detalle al hacer click en el corazón
// ##
// ## Props:
// ##   product → objeto producto { id, name, description, price, stock, imageUrl, categories }
// ##   badge   → texto del badge opcional ('- Oferta', '⭐ Top', etc.)

import React from 'react';
import { Link } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { toggleFavoriteAsync, toggleFavorite, selectIsFavorite } from '../store/favoriteSlice';
import { useAuth } from '../features/auth/context/AuthContext';
import './ProductCard.css';

const DEFAULT_IMAGE = 'https://placehold.co/300x200?text=Sin+imagen';

const ProductCard = ({ product, badge }) => {
  const dispatch   = useDispatch();
  const { user }   = useAuth();
  const esFavorito = useSelector(selectIsFavorite(product.id)); // ## true/false desde Redux

  // ## Clase CSS del badge: rojo para oferta, amarillo para top
  const badgeClass = badge === '- Oferta'
    ? 'product-card__badge product-card__badge--oferta'
    : 'product-card__badge product-card__badge--top';

  // ## Al hacer click en el corazón:
  // ##   1. Previene la navegación al detalle (el Link padre)
  // ##   2. Si logueado → llama al backend para persistir el cambio
  // ##   3. Si no logueado → solo modifica el estado local en Redux
  const handleFavClick = (e) => {
    e.preventDefault();
    if (user) {
      dispatch(toggleFavoriteAsync({ product, userId: user.id, isFav: esFavorito }));
    } else {
      dispatch(toggleFavorite(product));
    }
  };

  return (
    <Link to={`/products/${product.id}`} className="product-card__link">
      <div className="product-card">
        {badge && <span className={badgeClass}>{badge}</span>}

        {/* ## Botón de favorito superpuesto sobre la imagen */}
        <button
          className="product-card__fav"
          onClick={handleFavClick}
          title={esFavorito ? 'Quitar de favoritos' : 'Agregar a favoritos'}
        >
          {esFavorito ? '❤️' : '🤍'}
        </button>

        {/* ## Imagen con fallback si no carga */}
        <img
          className="product-card__img"
          src={product.imageUrl || DEFAULT_IMAGE}
          alt={product.name}
          onError={e => { e.target.src = DEFAULT_IMAGE; }}
        />

        <div className="product-card__body">
          <h3 className="product-card__name">{product.name}</h3>
          {/* ## Descripción truncada a 60 caracteres */}
          <p className="product-card__desc">
            {product.description?.length > 60
              ? product.description.substring(0, 60) + '...'
              : product.description}
          </p>
          <div className="product-card__footer">
            <span className="product-card__price">
              ${Number(product.price).toLocaleString('es-AR')}
            </span>
            {/* ## Color verde si hay stock, rojo si está agotado */}
            <span className={`product-card__stock ${product.stock > 0 ? 'product-card__stock--ok' : 'product-card__stock--out'}`}>
              {product.stock > 0 ? `Stock: ${product.stock}` : 'Sin stock'}
            </span>
          </div>

          {/* ## Chips de categorías (si el producto las tiene) */}
          {product.categories?.length > 0 && (
            <div className="product-card__categories">
              {product.categories.map(cat => (
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
