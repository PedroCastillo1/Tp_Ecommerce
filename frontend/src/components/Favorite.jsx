// Favorite.jsx
// REDUX (useSelector + useDispatch para favoritos)

import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

// REDUX: reemplazamos useFavorite (Context) por useSelector y useDispatch
import { useSelector, useDispatch } from 'react-redux';
import { selectFavoriteItems, removeFromFavorite } from '../store/favoriteSlice';

import ProductCard from './ProductCard';
import './Favorite.css';

const Favorite = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // Leemos los favoritos directamente del store Redux
  const favoriteItems = useSelector(selectFavoriteItems);

  const handleVerDetalle = (id) => {
    navigate(/products/${id});
  };

  return (
    <div className="favorite">
      <h1 className="favoritetitle">Mis Favoritos</h1>

      {favoriteItems.length > 0 && (
        <p className="favoritecount">
          {favoriteItems.length} {favoriteItems.length === 1 ? 'producto guardado' : 'productos guardados'}
        </p>
      )}

      {favoriteItems.length === 0 ? (
        <div className="favoriteempty">
          <p>Todavia no agregaste productos a favoritos</p>
          <Link to="/products" className="btn-primary">Explorar productos</Link>
        </div>
      ) : (
        <div className="favoritegrid">
          {favoriteItems.map(product => (
            <div key={product.id} className="favorite__item">
              {/* ProductCard ahora usa Redux internamente para favoritos y carrito */}
              <ProductCard product={product} />
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Favorite;