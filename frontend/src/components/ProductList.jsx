import React, { useState, useEffect } from 'react';
import ProductCard from './ProductCard';
import './ProductList.css';

const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState('');

  useEffect(() => {
    const headers = { 'Accept': 'application/json', 'Content-Type': 'application/json' };
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

  if (loading) return <div className="loading-state">Cargando productos...</div>;
  if (error)   return <div className="error-state">Error: {error}</div>;

  const filtered = products.filter(p =>
    p.name?.toLowerCase().includes(search.toLowerCase()) ||
    p.description?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="product-list">
      <div className="product-list__header">
        <h1>Catálogo de Productos</h1>
        <input
          className="product-list__search"
          type="text"
          placeholder="Buscar productos..."
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
      </div>
      <p className="product-list__count">{filtered.length} productos encontrados</p>
      <div className="product-list__grid">
        {filtered.map(product => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
};

export default ProductList;
