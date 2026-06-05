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
    'navbarlink' + (location.pathname === path ? ' navbarlink--active' : '');

  return (
    <nav className="navbar">
      <Link to="/" className="navbarlogo">TpEcommerce</Link>
      <ul className="navbarlinks">
        <li><Link to="/" className={linkClass('/')}>Inicio</Link></li>
        <li><Link to="/products" className={linkClass('/products')}>Productos</Link></li>

        <li>
          <span className="navbarcart-wrapper">
            <Link to="/favorites" className={linkClass('/favorites')}>
              Favoritos
              {/* Badge de favoritos — dato del store Redux */}
              {favoriteItems.length > 0 && (
                <span className="navbarbadge">{favoriteItems.length}</span>
              )}
            </Link>
          </span>
        </li>

        <li>
          <span className="navbarcart-wrapper">
            <Link to="/cart" className={linkClass('/cart')}>
              Carrito
              {/* Badge del carrito — dato del store Redux */}
              {itemCount > 0 && <span className="navbarbadge">{itemCount}</span>}
            </Link>
          </span>
        </li>

        <li><Link to="/login" className={linkClass('/login')}>Mi cuenta</Link></li>
      </ul>
    </nav>
  );
};

export default Navbar;