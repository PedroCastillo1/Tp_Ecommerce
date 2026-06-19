// ## COMPONENTE — ProductDetail (Detalle de Producto)
// ##
// ## Muestra la información completa de un producto individual.
// ## Obtiene el :id de la URL con useParams() y hace fetch al backend.
// ##
// ## Funcionalidades:
// ##   - Botón "Agregar al carrito": si logueado → sincroniza con DB, si no → deshabilitado
// ##   - Botón de favorito (❤️/🤍): igual que en ProductCard
// ##   - Feedback visual "Agregado" por 2 segundos después de agregar al carrito

import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { addToCartAsync } from "../store/cartSlice";
import { toggleFavoriteAsync, toggleFavorite, selectIsFavorite } from "../store/favoriteSlice";
import { useAuth } from "../features/auth/context/AuthContext";
import "./ProductDetail.css";

const DEFAULT_IMAGE = "https://placehold.co/500x400?text=Sin+imagen";

const ProductDetail = () => {
  const dispatch   = useDispatch();
  const { user }   = useAuth();
  const { id }     = useParams(); // ## :id de la URL → /products/:id
  const esFavorito = useSelector(selectIsFavorite(Number(id)));

  // ## Estado local del componente
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error,   setError]   = useState(null);
  const [added,   setAdded]   = useState(false); // ## feedback visual al agregar al carrito

  // ## Toggle de favorito: sincroniza con DB si logueado, local si no
  const handleFavClick = () => {
    if (!product) return;
    if (user) {
      dispatch(toggleFavoriteAsync({ product, userId: user.id, isFav: esFavorito }));
    } else {
      dispatch(toggleFavorite(product));
    }
  };

  // ## Carga el producto del backend cuando cambia el :id en la URL
  useEffect(() => {
    fetch(`http://localhost:8080/api/productos/${id}`, {
      method:  "GET",
      headers: { Accept: "application/json", "Content-Type": "application/json" },
      credentials: "include",
      mode:    "cors",
    })
      .then((r) => {
        if (!r.ok) throw new Error("Producto no encontrado");
        return r.json();
      })
      .then((data) => { setProduct(data); setLoading(false); })
      .catch((err) => { setError(err.message); setLoading(false); });
  }, [id]);

  // ## Agrega al carrito y muestra feedback "Agregado" por 2 segundos
  const handleAddToCart = () => {
    if (user) {
      dispatch(addToCartAsync({ product, userId: user.id }));
    }
    setAdded(true);
    setTimeout(() => setAdded(false), 2000);
  };

  // ## Estados de carga y error
  if (loading) return <div className="loading-state">Cargando producto...</div>;
  if (error)   return <div className="error-state">Error: {error}</div>;
  if (!product) return <div className="error-state">No se encontro el producto</div>;

  return (
    <div className="product-detail">
      <Link to="/products" className="product-detail__back">Volver al catalogo</Link>

      <div className="product-detail__grid">
        {/* ## Imagen con fallback */}
        <img
          className="product-detail__img"
          src={product.imageUrl || DEFAULT_IMAGE}
          alt={product.name}
          onError={(e) => { e.target.src = DEFAULT_IMAGE; }}
        />

        <div>
          {/* ## Chips de categorías */}
          {product.categories?.length > 0 && (
            <div className="product-detail__categories">
              {product.categories.map((cat) => (
                <span key={cat.id} className="product-detail__cat">{cat.name}</span>
              ))}
            </div>
          )}

          <h1 className="product-detail__name">{product.name}</h1>
          <p  className="product-detail__price">${Number(product.price).toLocaleString("es-AR")}</p>
          <p  className="product-detail__desc">{product.description}</p>

          {/* ## Indicador de stock con color */}
          <span className={`product-detail__stock ${product.stock > 0 ? "product-detail__stock--ok" : "product-detail__stock--out"}`}>
            {product.stock > 0 ? `Stock disponible (${product.stock})` : "Sin stock"}
          </span>

          <div className="product-detail__actions">
            {/* ## Botón carrito: deshabilitado si sin stock o sin login */}
            <button
              className={`product-detail__btn-cart${added ? " product-detail__btn-cart--added" : ""}`}
              onClick={handleAddToCart}
              disabled={product.stock === 0 || !user}
            >
              {!user ? "Debes iniciar sesion" : added ? "Agregado al carrito" : "Agregar al carrito"}
            </button>

            {/* ## Botón de favorito */}
            <button
              className="product-detail__btn-fav"
              onClick={handleFavClick}
              title={esFavorito ? 'Quitar de favoritos' : 'Agregar a favoritos'}
            >
              {esFavorito ? '❤️ En favoritos' : '🤍 Favorito'}
            </button>

            <Link to="/cart" className="product-detail__btn-view">Ver carrito</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;
