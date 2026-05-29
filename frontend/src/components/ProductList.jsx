import React, { useState, useEffect } from 'react';
import ProductCard from './ProductCard';

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState('');

  useEffect(() => {
    const headers = {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    };
    const token = localStorage.getItem('token');
    if (token) headers['Authorization'] = token;

    fetch('http://localhost:8080/api/productos', { method: 'GET', headers, credentials: 'include', mode: 'cors' })
      .then(r => {
        if (!r.ok) throw new Error('Error al cargar los productos');
        return r.json();
      })
      .then(data => { setProducts(data); setLoading(false); })
      .catch(err => { setError(err.message); setLoading(false); });
  }, []);

  if (loading) return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '60vh', color: '#888' }}>
      Cargando productos...
    </div>
  );

  if (error) return (
    <div style={{ padding: '2rem', color: '#c62828' }}>Error: {error}</div>
  );

  const filtered = products.filter(p =>
    p.name?.toLowerCase().includes(search.toLowerCase()) ||
    p.description?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div style={{ background: '#f8f9fa', minHeight: '100vh', padding: '2rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
        <h1 style={{ margin: 0, color: '#1a1a2e' }}>Catálogo de Productos</h1>
        <input
          type="text"
          placeholder="Buscar productos..."
          value={search}
          onChange={e => setSearch(e.target.value)}
          style={{
            padding: '0.5rem 1rem',
            borderRadius: '20px',
            border: '1px solid #ddd',
            fontSize: '0.95rem',
            width: '240px',
            outline: 'none',
          }}
        />
      </div>
      <p style={{ color: '#888', marginBottom: '1.5rem' }}>{filtered.length} productos encontrados</p>
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))',
        gap: '1.25rem',
      }}>
        {filtered.map(product => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
};

export default ProductList;
