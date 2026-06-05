// ProductCard.jsx
// Demuestra: useContext (useFavorite), renderizado condicional, operador ternario

import React from 'react';
import { Link } from 'react-router-dom';
import { useFavorite } from '../hooks/useContext/FavoriteContext';
import './ProductCard.css';

const DEFAULT_IMAGE = 'https://placehold.co/300x200?text=Sin+imagen';

const ProductCard = ({ product, badge }) => {
  // useContext a través del custom hook useFavorite
  const { toggleFavorite, isFavorite } = useFavorite();

  const badgeClass = badge === '🔥 Oferta'
    ? 'product-card__badge product-card__badge--oferta'
    : 'product-card__badge product-card__badge--top';

  // Operador ternario: corazón lleno si es favorito, vacío si no
  const heartIcon = isFavorite(product.id) ? '❤️' : '🤍';

  const handleFavClick = (e) => {
    // Evita que el click en el corazón navegue al detalle del producto
    e.preventDefault();
    toggleFavorite(product);
  };

  return (
    <Link to={`/products/${product.id}`} className="product-card__link">
      <div className="product-card">
        {/* Renderizado condicional: solo muestra badge si existe */}
        {badge && <span className={badgeClass}>{badge}</span>}

        {/* Botón de favorito — usa addToFavorite del contexto */}
        <button
          className="product-card__fav"
          onClick={handleFavClick}
          title={isFavorite(product.id) ? 'Quitar de favoritos' : 'Agregar a favoritos'}
        >
          {heartIcon}
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
            {/* Operador ternario para clase de stock */}
            <span className={`product-card__stock ${product.stock > 0 ? 'product-card__stock--ok' : 'product-card__stock--out'}`}>
              {product.stock > 0 ? `Stock: ${product.stock}` : 'Sin stock'}
            </span>
          </div>

          {/* Renderizado condicional con && */}
          {product.categories?.length > 0 && (
            <div className="product-card__categories">
              {/* .map sobre categorías */}
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
