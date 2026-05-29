import React from 'react';
import { Link } from 'react-router-dom';

const DEFAULT_IMAGE = 'https://placehold.co/300x200?text=Sin+imagen';

const ProductCard = ({ product, badge }) => {
  return (
    <Link
      to={`/products/${product.id}`}
      style={{ textDecoration: 'none', color: 'inherit' }}
    >
      <div style={{
        border: '1px solid #e0e0e0',
        borderRadius: '12px',
        overflow: 'hidden',
        background: '#fff',
        transition: 'transform 0.2s, box-shadow 0.2s',
        cursor: 'pointer',
        position: 'relative',
      }}
        onMouseEnter={e => {
          e.currentTarget.style.transform = 'translateY(-4px)';
          e.currentTarget.style.boxShadow = '0 8px 24px rgba(0,0,0,0.12)';
        }}
        onMouseLeave={e => {
          e.currentTarget.style.transform = 'translateY(0)';
          e.currentTarget.style.boxShadow = 'none';
        }}
      >
        {badge && (
          <span style={{
            position: 'absolute',
            top: '10px',
            left: '10px',
            background: badge === '🔥 Oferta' ? '#e94560' : '#1a1a2e',
            color: '#fff',
            padding: '3px 10px',
            borderRadius: '20px',
            fontSize: '0.75rem',
            fontWeight: '600',
            zIndex: 1,
          }}>
            {badge}
          </span>
        )}
        <img
          src={product.imageUrl || DEFAULT_IMAGE}
          alt={product.name}
          style={{
            width: '100%',
            height: '200px',
            objectFit: 'cover',
          }}
          onError={e => { e.target.src = DEFAULT_IMAGE; }}
        />
        <div style={{ padding: '1rem' }}>
          <h3 style={{ margin: '0 0 0.25rem', fontSize: '1rem', color: '#1a1a2e' }}>
            {product.name}
          </h3>
          <p style={{ margin: '0 0 0.5rem', fontSize: '0.85rem', color: '#777', minHeight: '36px' }}>
            {product.description?.length > 60
              ? product.description.substring(0, 60) + '...'
              : product.description}
          </p>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontSize: '1.2rem', fontWeight: '700', color: '#e94560' }}>
              ${Number(product.price).toLocaleString('es-AR')}
            </span>
            <span style={{
              fontSize: '0.8rem',
              color: product.stock > 0 ? '#2e7d32' : '#c62828',
              background: product.stock > 0 ? '#e8f5e9' : '#ffebee',
              padding: '2px 8px',
              borderRadius: '10px',
            }}>
              {product.stock > 0 ? `Stock: ${product.stock}` : 'Sin stock'}
            </span>
          </div>
          {product.categories?.length > 0 && (
            <div style={{ marginTop: '0.5rem', display: 'flex', flexWrap: 'wrap', gap: '4px' }}>
              {product.categories.map(cat => (
                <span key={cat.id} style={{
                  fontSize: '0.72rem',
                  background: '#f0f0f0',
                  color: '#555',
                  padding: '2px 8px',
                  borderRadius: '10px',
                }}>
                  {cat.name}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>
    </Link>
  );
};

export default ProductCard;
