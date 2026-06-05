// Favorite.jsx
// Demuestra: useContext (useFavorite), useNavigate, .map,
//            renderizado condicional, operadores ternarios

import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useFavorite } from '../hooks/useContext/FavoriteContext';
import ProductCard from './ProductCard';
import './Favorite.css';

const Favorite = () => {
  // useNavigate: permite navegar programáticamente
  const navigate = useNavigate();

  // useContext a través del custom hook useFavorite
  const { favoriteItems, removeFromFavorite } = useFavorite();

  // useNavigate: al clickear "Ver detalle" navega al producto
  const handleVerDetalle = (id) => {
    navigate(`/products/${id}`);
  };

  return (
    <div className="favorite">
      <h1 className="favorite__title">❤️ Mis Favoritos</h1>

      {/* Renderizado condicional con && */}
      {favoriteItems.length > 0 && (
        <p className="favorite__count">
          {/* Operador ternario */}
          {favoriteItems.length} {favoriteItems.length === 1 ? 'producto guardado' : 'productos guardados'}
        </p>
      )}

      {/* Operador ternario: lista vacía vs grilla de productos */}
      {favoriteItems.length === 0 ? (
        <div className="favorite__empty">
          <p>Todavía no agregaste productos a favoritos</p>
          <Link to="/products" className="btn-primary">Explorar productos</Link>
        </div>
      ) : (
        <div className="favorite__grid">
          {/* .map sobre favoriteItems */}
          {favoriteItems.map(product => (
            <div key={product.id} className="favorite__item">
              {/* ProductCard usa useContext internamente (useFavorite, useCart) */}
              <ProductCard product={product} />
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Favorite;
