// Navbar.jsx
// REDUX (useSelector para carrito Y favoritos), renderizado condicional

import React from 'react';
import { Link, useLocation } from 'react-router-dom';

// REDUX: leemos tanto el carrito como los favoritos del store global
import { useSelector } from 'react-redux';
import { selectCartItemCount } from '../store/cartSlice';
import { selectFavoriteItems } from '../store/favoriteSlice';

import './Navbar.css';

const Navbar = () => {
  const location = useLocation();

  // useSelector extrae los datos del store sin necesitar ningun Provider local
  const itemCount     = useSelector(selectCartItemCount);
  const favoriteItems = useSelector(selectFavoriteItems);

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
