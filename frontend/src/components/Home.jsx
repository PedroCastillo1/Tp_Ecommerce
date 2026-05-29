import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import ProductCard from './ProductCard';

const API = 'http://localhost:8080';

const Section = ({ title, subtitle, children }) => (
  <section style={{ padding: '2rem 2rem 1rem' }}>
    <div style={{ marginBottom: '1.25rem' }}>
      <h2 style={{ margin: 0, fontSize: '1.5rem', color: '#1a1a2e' }}>{title}</h2>
      {subtitle && <p style={{ margin: '0.25rem 0 0', color: '#888', fontSize: '0.9rem' }}>{subtitle}</p>}
    </div>
    {children}
  </section>
);

const ProductGrid = ({ products, badge }) => (
  <div style={{
    display: 'grid',
    gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
    gap: '1.25rem',
  }}>
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
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  // Ofertas: los 4 productos con precio más bajo
  const ofertas = [...allProducts]
    .sort((a, b) => Number(a.price) - Number(b.price))
    .slice(0, 4);

  if (loading) return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '60vh', fontSize: '1.1rem', color: '#888' }}>
      Cargando tienda...
    </div>
  );

  if (error) return (
    <div style={{ padding: '2rem', color: '#c62828' }}>
      Error al cargar productos: {error}. ¿Está corriendo el backend en el puerto 8080?
    </div>
  );

  return (
    <div style={{ background: '#f8f9fa', minHeight: '100vh' }}>

      {/* Hero */}
      <div style={{
        background: 'linear-gradient(135deg, #1a1a2e 0%, #16213e 60%, #0f3460 100%)',
        color: '#fff',
        padding: '4rem 2rem',
        textAlign: 'center',
      }}>
        <h1 style={{ fontSize: '2.5rem', margin: '0 0 0.75rem', fontWeight: '800' }}>
          Bienvenido a TpEcommerce
        </h1>
        <p style={{ fontSize: '1.1rem', color: '#aaa', margin: '0 0 2rem' }}>
          Los mejores productos al mejor precio
        </p>
        <Link to="/products" style={{
          background: '#e94560',
          color: '#fff',
          padding: '0.75rem 2rem',
          borderRadius: '30px',
          textDecoration: 'none',
          fontWeight: '600',
          fontSize: '1rem',
        }}>
          Ver todos los productos
        </Link>
      </div>

      {/* Ofertas Destacadas */}
      {ofertas.length > 0 && (
        <Section title="🔥 Ofertas Destacadas" subtitle="Los mejores precios de hoy">
          <ProductGrid products={ofertas} badge="🔥 Oferta" />
        </Section>
      )}

      {/* Más Vendidos */}
      {topProducts.length > 0 && (
        <Section title="⭐ Productos Más Vendidos" subtitle="Lo que más elige nuestra comunidad">
          <ProductGrid products={topProducts} badge="⭐ Top" />
        </Section>
      )}

      {/* Todos los Productos */}
      <Section title="🛍️ Todos los Productos" subtitle={`${allProducts.length} productos disponibles`}>
        <ProductGrid products={allProducts} />
        <div style={{ textAlign: 'center', marginTop: '1.5rem', paddingBottom: '2rem' }}>
          <Link to="/products" style={{
            display: 'inline-block',
            border: '2px solid #1a1a2e',
            color: '#1a1a2e',
            padding: '0.6rem 2rem',
            borderRadius: '30px',
            textDecoration: 'none',
            fontWeight: '600',
          }}>
            Ver catálogo completo →
          </Link>
        </div>
      </Section>

    </div>
  );
};

export default Home;
