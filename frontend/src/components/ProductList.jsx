// ## COMPONENTE — ProductList (Catalogo de Productos)
// ## Este archivo es el EJEMPLO MAS SIMPLE de varios temas de la materia:
// ## useState, useEffect, fetch con Promesas (.then/.catch) y renderizado condicional.

import React, { useState, useEffect } from 'react';
import ProductCard from './ProductCard';
import './ProductList.css';

const ProductList = () => {
  // ## ===== useState: la memoria del componente =====
  // ## Cada useState devuelve [valor, funcionParaCambiarlo].
  // ## Cambiar SIEMPRE con la funcion (setProducts, setLoading...) → React redibuja solo.
  const [products, setProducts] = useState([]);   // ## la lista de productos (arranca vacia)
  const [loading, setLoading] = useState(true);   // ## ¿estamos esperando la respuesta del servidor?
  const [error, setError] = useState(null);       // ## si algo fallo, aca se guarda el mensaje
  const [search, setSearch] = useState('');       // ## lo que el usuario escribe en el buscador

  // ## ===== useEffect: pedir datos a la API en el momento justo =====
  // ## Corre DESPUES de que el componente se dibuja.
  // ## El [] del final significa: ejecutate UNA sola vez, al montar (al entrar a la pantalla).
  // ## Si el fetch estuviera suelto en el cuerpo: fetch → cambia estado → redibuja → otro fetch = bucle infinito.
  useEffect(() => {
    const headers = { 'Accept': 'application/json', 'Content-Type': 'application/json' };
    const token = localStorage.getItem('token'); // ## legacy: hoy la cookie HttpOnly hace este trabajo
    if (token) headers['Authorization'] = token;

    // ## ===== fetch + Promesa (.then / .catch) =====
    // ## fetch dispara el pedido al endpoint y devuelve una PROMESA (un valor futuro).
    // ## credentials:'include' → el navegador adjunta solo la cookie con el JWT.
    fetch('http://localhost:8080/api/productos', { method: 'GET', headers, credentials: 'include', mode: 'cors' })
      .then(r => {
        // ## .then = "cuando la respuesta llegue bien (fulfilled), hace esto"
        if (!r.ok) throw new Error('Error al cargar los productos'); // ## respuesta con error → salta al .catch
        return r.json(); // ## convertir la respuesta a JSON (tambien devuelve una Promesa)
      })
      .then(data => { setProducts(data); setLoading(false); })       // ## guardar los productos y apagar el "cargando"
      .catch(err => { setError(err.message); setLoading(false); });  // ## .catch = "si fallo (rejected), guarda el error"
  }, []); // ## [] = dependencias vacias → una sola vez al montar

  // ## ===== Renderizado condicional: returns tempranos =====
  // ## La escalerita: ¿cargando? cartel. ¿error? cartel. ¿todo bien? el catalogo.
  // ## El componente corta aca y no dibuja el resto hasta que haya datos.
  if (loading) return <div className="loading-state">Cargando productos...</div>;
  if (error)   return <div className="error-state">Error: {error}</div>;

  // ## Filtro del buscador: usa el estado "search" (se recalcula en cada redibujo)
  const filtered = products.filter(p =>
    p.name?.toLowerCase().includes(search.toLowerCase()) ||
    p.description?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="product-list">
      <div className="product-list__header">
        <h1>Catálogo de Productos</h1>
        {/* ## Input CONTROLADO: value={search} + onChange → React siempre sabe que hay escrito.
            ## Cada letra llama a setSearch → redibuja → la lista se filtra al instante. */}
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
        {/* ## Reutilizacion de componentes: la misma ProductCard para cada producto.
            ## key={product.id} = el "DNI" de cada tarjeta, ayuda al diffing del Virtual DOM.
            ## product={product} = PROPS: el padre le pasa los datos al hijo. */}
        {filtered.map(product => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
};

export default ProductList;
