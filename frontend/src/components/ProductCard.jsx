// ProductCard.jsx
// REDUX (useSelector + useDispatch para favoritos)

import React from 'react';
import { Link } from 'react-router-dom';

// REDUX: reemplazamos useFavorite (Context) por useSelector y useDispatch
import { useSelector, useDispatch } from 'react-redux';
import { toggleFavorite, selectIsFavorite } from '../store/favoriteSlice';

import './ProductCard.css';

const DEFAULT_IMAGE = 'https://placehold.co/300x200?text=Sin+imagen';

const ProductCard = ({ product, badge }) => {
  const dispatch = useDispatch();

  // selectIsFavorite(product.id) es un selector con parametro
  // devuelve true si el producto ya esta en favoritos
  const esFavorito = useSelector(selectIsFavorite(product.id));

  const badgeClass = badge === 'Oferta'
    ? 'product-card__badge product-card__badge--oferta'
    : 'product-card__badge product-card__badge--top';

  // Operador ternario: corazon lleno si es favorito, vacio si no
  const heartIcon = esFavorito ? 'liked' : 'like';

  const handleFavClick = (e) => {
    e.preventDefault();
    // dispatch(toggleFavorite(product)) -> envia la accion al favoriteSlice
    dispatch(toggleFavorite(product));
  };

  return (
    <Link to={`/products/${product.id}`} className="product-card__link">
      <div className="product-card">
        {badge && <span className={badgeClass}>{badge}</span>}

        {/* Boton de favorito — usa toggleFavorite del store Redux */}
        <button
          className="product-card__fav"
          onClick={handleFavClick}
          title={esFavorito ? 'Quitar de favoritos' : 'Agregar a favoritos'}
        >
          {esFavorito ? 'Fav' : 'No fav'}
        </button>

        <img
          className="product-card__img"
          src={product.imageUrl || DEFAULT_IMAGE}
          alt={product.name}
          onError={e => { e.target.src = DEFAULT_IMAGE; }}
        />

        <div className="product-card__body">
          <h3 className="product-card__name">{product.name}</h3>
          <p className="product-card__desc">
            {product.description?.length > 60
              ? product.description.substring(0, 60) + '...'
              : product.description}
          </p>
          <div className="product-card__footer">
            <span className="product-card__price">
              ${Number(product.price).toLocaleString('es-AR')}
            </span>
            <span className={`product-card__stock ${product.stock > 0 ? 'product-card__stock--ok' : 'product-card__stock--out'}`}>
              {product.stock > 0 ? `Stock: ${product.stock}` : 'Sin stock'}
            </span>
          </div>

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