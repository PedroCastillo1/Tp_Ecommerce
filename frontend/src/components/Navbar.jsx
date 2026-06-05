// Navbar.jsx
// REDUX (useSelector) + useContext (FavoriteContext), renderizado condicional

import React from 'react';
import { Link, useLocation } from 'react-router-dom';

// REDUX: leemos el contador del carrito desde el store global
import { useSelector } from 'react-redux';
import { selectCartItemCount } from '../store/cartSlice';

// Favoritos sigue en Context - se migra a Redux en el Push 2
import { useFavorite } from '../hooks/useContext/FavoriteContext';
import './Navbar.css';

const Navbar = () => {
  const location = useLocation();

  // useSelector extrae itemCount del store Redux (no necesitamos un Provider local)
  const itemCount = useSelector(selectCartItemCount);

  // Favoritos todavia usa Context (se migra en Push 2)
  const { favoriteItems } = useFavorite();

  const linkClass = (path) =>
    'navbar__link' + (location.pathname === path ? ' navbar__link--active' : '');

  return (
    <nav className="navbar">
      <Link to="/" className="navbar__logo">TpEcommerce</Link>
      <ul className="navbar__links">
        <li><Link to="/" className={linkClass('/')}>Inicio</Link></li>
        <li><Link to="/products" className={linkClass('/products')}>Productos</Link></li>

        <li>
          <span className="navbar__cart-wrapper">
            <Link to="/favorites" className={linkClass('/favorites')}>
              Favoritos
              {favoriteItems.length > 0 && (
                <span className="navbar__badge">{favoriteItems.length}</span>
              )}
            </Link>
          </span>
        </li>

        <li>
          <span className="navbar__cart-wrapper">
            <Link to="/cart" className={linkClass('/cart')}>
              Carrito
              {/* useSelector nos da itemCount del store Redux */}
              {itemCount > 0 && <span className="navbar__badge">{itemCount}</span>}
            </Link>
          </span>
        </li>

        <li><Link to="/login" className={linkClass('/login')}>Mi cuenta</Link></li>
      </ul>
    </nav>
  );
};

export default Navbar;
