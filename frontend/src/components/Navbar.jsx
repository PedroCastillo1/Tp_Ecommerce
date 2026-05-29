import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useCart } from '../hooks/useContext/CartContext';
import './Navbar.css';

const Navbar = () => {
  const location = useLocation();
  const { itemCount } = useCart();

  const linkClass = (path) =>
    `navbar__link${location.pathname === path ? ' navbar__link--active' : ''}`;

  return (
    <nav className="navbar">
      <Link to="/" className="navbar__logo">🛒 TpEcommerce</Link>
      <ul className="navbar__links">
        <li><Link to="/" className={linkClass('/')}>Inicio</Link></li>
        <li><Link to="/products" className={linkClass('/products')}>Productos</Link></li>
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
