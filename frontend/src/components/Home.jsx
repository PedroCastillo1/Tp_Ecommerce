import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import ProductCard from './ProductCard';
import './Home.css';

const API = 'http://localhost:8080';

const Section = ({ title, subtitle, children }) => (
  <section className="home__section">
    <div className="home__section-header">
      <h2>{title}</h2>
      {subtitle && <p>{subtitle}</p>}
    </div>
    {children}
  </section>
);

const ProductGrid = ({ products, badge }) => (
  <div className="home__grid">
    {products.map(p => (
      <ProductCard key={p.id} product={p} badge={badge} />
    ))}
  </div>
);

const Home = () => {
  const [allProducts, setAllProducts] = useState([]);
  const [topProducts, setTopProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const headers = { 'Accept': 'application/json', 'Content-Type': 'application/json' };
    const token = localStorage.getItem('token');
    if (token) headers['Authorization'] = token;

    Promise.all([
      fetch(`${API}/api/productos`, { headers }).then(r => r.json()),
      fetch(`${API}/api/productos?ordered=true`, { headers }).then(r => r.json()),
    ])
      .then(([all, ordered]) => {
        setAllProducts(all);
        setTopProducts(ordered.slice(0, 4));
        setLoading(false);
      })
      .catch(err => { setError(err.message); setLoading(false); });
  }, []);

  const ofertas = [...allProducts]
    .sort((a, b) => Number(a.price) - Number(b.price))
    .slice(0, 4);

  if (loading) return <div className="loading-state">Cargando tienda...</div>;
  if (error)   return <div className="error-state">Error: {error}. ¿Está corriendo el backend en el puerto 8080?</div>;

  return (
    <div className="home">
      <div className="home__hero">
        <h1>Bienvenido a TpEcommerce</h1>
        <p>Los mejores productos al mejor precio</p>
        <Link to="/products" className="btn-primary">Ver todos los productos</Link>
      </div>

      {ofertas.length > 0 && (
        <Section title="🔥 Ofertas Destacadas" subtitle="Los mejores precios de hoy">
          <ProductGrid products={ofertas} badge="🔥 Oferta" />
        </Section>
      )}

      {topProducts.length > 0 && (
        <Section title="⭐ Productos Más Vendidos" subtitle="Lo que más elige nuestra comunidad">
          <ProductGrid products={topProducts} badge="⭐ Top" />
        </Section>
      )}

      <Section title="🛍️ Todos los Productos" subtitle={`${allProducts.length} productos disponibles`}>
        <ProductGrid products={allProducts} />
        <div className="home__see-more">
          <Link to="/products" className="btn-outline">Ver catálogo completo →</Link>
        </div>
      </Section>
    </div>
  );
};

export default Home;
