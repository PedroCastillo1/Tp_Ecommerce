import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Navbar = () => {
  const location = useLocation();

  const navStyle = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '0 2rem',
    height: '64px',
    background: '#1a1a2e',
    color: '#fff',
    boxShadow: '0 2px 8px rgba(0,0,0,0.3)',
    position: 'sticky',
    top: 0,
    zIndex: 100,
  };

  const logoStyle = {
    fontSize: '1.4rem',
    fontWeight: '700',
    color: '#e94560',
    textDecoration: 'none',
    letterSpacing: '1px',
  };

  const linksStyle = {
    display: 'flex',
    gap: '1.5rem',
    listStyle: 'none',
    margin: 0,
    padding: 0,
  };

  const linkStyle = (path) => ({
    color: location.pathname === path ? '#e94560' : '#ccc',
    textDecoration: 'none',
    fontWeight: location.pathname === path ? '600' : '400',
    fontSize: '0.95rem',
    transition: 'color 0.2s',
  });

  return (
    <nav style={navStyle}>
      <Link to="/" style={logoStyle}>🛒 TpEcommerce</Link>
      <ul style={linksStyle}>
        <li><Link to="/" style={linkStyle('/')}>Inicio</Link></li>
        <li><Link to="/products" style={linkStyle('/products')}>Productos</Link></li>
        <li><Link to="/cart" style={linkStyle('/cart')}>Carrito</Link></li>
        <li><Link to="/login" style={linkStyle('/login')}>Login</Link></li>
      </ul>
    </nav>
  );
};

export default Navbar;
