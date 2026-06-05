import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { useDispatch } from "react-redux";
import { addToCartAsync } from "../store/cartSlice";
import { useAuth } from "../features/auth/context/AuthContext";
import "./ProductDetail.css";

const DEFAULT_IMAGE = "https://placehold.co/500x400?text=Sin+imagen";

const ProductDetail = () => {
  const dispatch = useDispatch();
  const { user, token } = useAuth();
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [added, setAdded] = useState(false);

  useEffect(() => {
    const hdrs = { Accept: "application/json", "Content-Type": "application/json" };
    const storedToken = localStorage.getItem("token");
    if (storedToken) hdrs["Authorization"] = storedToken;

    fetch(`http://localhost:8080/api/productos/${id}`, {
      method: "GET",
      headers: hdrs,
      credentials: "include",
      mode: "cors",
    })
      .then((r) => {
        if (!r.ok) throw new Error("Producto no encontrado");
        return r.json();
      })
      .then((data) => {
        setProduct(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [id]);

  const handleAddToCart = () => {
    if (user && token) {
      // Usuario logueado: sincroniza con la DB via async thunk
      dispatch(addToCartAsync({ product, userId: user.id, token }));
    }
    setAdded(true);
    setTimeout(() => setAdded(false), 2000);
  };

  if (loading) return <div className="loading-state">Cargando producto...</div>;
  if (error) return <div className="error-state">Error: {error}</div>;
  if (!product) return <div className="error-state">No se encontro el producto</div>;

  return (
    <div className="product-detail">
      <Link to="/products" className="product-detail__back">Volver al catalogo</Link>
      <div className="product-detail__grid">
        <img
          className="product-detail__img"
          src={product.imageUrl || DEFAULT_IMAGE}
          alt={product.name}
          onError={(e) => { e.target.src = DEFAULT_IMAGE; }}
        />
        <div>
          {product.categories?.length > 0 && (
            <div className="product-detail__categories">
              {product.categories.map((cat) => (
                <span key={cat.id} className="product-detail__cat">{cat.name}</span>
              ))}
            </div>
          )}
          <h1 className="product-detail__name">{product.name}</h1>
          <p className="product-detail__price">${Number(product.price).toLocaleString("es-AR")}</p>
          <p className="product-detail__desc">{product.description}</p>
          <span className={`product-detail__stock ${product.stock > 0 ? "product-detail__stock--ok" : "product-detail__stock--out"}`}>
            {product.stock > 0 ? `Stock disponible (${product.stock})` : "Sin stock"}
          </span>
          <div className="product-detail__actions">
            <button
              className={`product-detail__btn-cart${added ? " product-detail__btn-cart--added" : ""}`}
              onClick={handleAddToCart}
              disabled={product.stock === 0 || !user}
            >
              {!user ? "Debes iniciar sesion" : added ? "Agregado al carrito" : "Agregar al carrito"}
            </button>
            <Link to="/cart" className="product-detail__btn-view">Ver carrito</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;
