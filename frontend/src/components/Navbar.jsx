// ## COMPONENTE — Navbar (Barra de Navegación)
// ##
// ## Muestra los links de navegación con badges dinámicos para:
// ##   - Favoritos: cantidad de productos marcados como favoritos
// ##   - Carrito: cantidad total de unidades en el carrito
// ##
// ## Usa useSelector de Redux para leer el estado global (no necesita props).
// ## Resalta el link activo comparando la ruta actual con useLocation().

import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectCartItemCount } from '../store/cartSlice';
import { selectFavoriteItems  } from '../store/favoriteSlice';
import './Navbar.css';

const Navbar = () => {
  const location = useLocation();

  // ## Leemos el store global para mostrar los contadores en tiempo real
  const itemCount     = useSelector(selectCartItemCount); // ## total de unidades en el carrito
  const favoriteItems = useSelector(selectFavoriteItems); // ## array de productos favoritos

  // ## Devuelve la clase CSS base + clase activa si la ruta coincide
  const linkClass = (path) =>
    'navbar__link' + (location.pathname === path ? ' navbar__link--active' : '');

  return (
    <nav className="navbar">
      <Link to="/" className="navbar__logo">TpEcommerce</Link>

      <ul className="navbar__links">
        <li><Link to="/"         className={linkClass('/')}>Inicio</Link></li>
        <li><Link to="/products" className={linkClass('/products')}>Productos</Link></li>

        {/* ## Favoritos con badge que muestra la cantidad */}
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

        {/* ## Carrito con badge que muestra el total de unidades */}
        <li>
          <span className="navbar__cart-wrapper">
            <Link to="/cart" className={linkClass('/cart')}>
              Carrito
              {itemCount > 0 && <span className="navbar__badge">{itemCount}</span>}
            </Link>
          </span>
        </li>

        <li><Link to="/perfil" className={linkClass('/perfil')}>Mi perfil</Link></li>
        <li><Link to="/login"  className={linkClass('/login')}>Mi cuenta</Link></li>
      </ul>
    </nav>
  );
};

export default Navbar;
