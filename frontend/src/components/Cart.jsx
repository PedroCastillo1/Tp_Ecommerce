// ## COMPONENTE — Cart (Carrito de Compras)
// ##
// ## Muestra todos los items del carrito con controles de cantidad.
// ## Lee el estado del carrito desde Redux con useSelector.
// ##
// ## Acciones disponibles:
// ##   - Cambiar cantidad: updateQuantity (local, no llama al backend)
// ##   - Eliminar item:    removeFromCart (local)
// ##   - Vaciar carrito:   clearCartAsync (con DB si logueado) / clearCart (solo local)
// ## El checkout está en Checkout.jsx (ruta /checkout, protegida)

import React from "react";
import { Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import {
  selectCartItems, selectCartTotal, selectCartItemCount,
  removeFromCart, updateQuantity, clearCartAsync, clearCart,
} from "../store/cartSlice";
import { useAuth } from "../features/auth/context/AuthContext";
import "./Cart.css";

const DEFAULT_IMAGE = "https://placehold.co/100x100?text=?";

const Cart = () => {
  const dispatch  = useDispatch();
  const { user }  = useAuth();
  const cartItems = useSelector(selectCartItems);     // ## array de productos en el carrito
  const total     = useSelector(selectCartTotal);     // ## suma de price * quantity
  const itemCount = useSelector(selectCartItemCount); // ## total de unidades

  // ## Vacía el carrito: en DB si logueado, solo en Redux si no
  const handleClear = () => {
    if (user) {
      dispatch(clearCartAsync({ userId: user.id }));
    } else {
      dispatch(clearCart());
    }
  };

  return (
    <div className="cart">
      <h1 className="cart__title">Carrito de Compras</h1>

      {itemCount > 0 && (
        <p className="cart__count">{itemCount} {itemCount === 1 ? "producto" : "productos"}</p>
      )}

      {cartItems.length === 0 ? (
        <div className="cart__empty">
          <p>Tu carrito esta vacio</p>
          <Link to="/products" className="btn-primary">Ver productos</Link>
        </div>
      ) : (
        <div className="cart__layout">
          <div>
            {cartItems.map((item) => (
              <div key={item.id} className="cart__item">
                <img
                  className="cart__item-img"
                  src={item.imageUrl || DEFAULT_IMAGE}
                  alt={item.name}
                  onError={(e) => { e.target.src = DEFAULT_IMAGE; }}
                />
                <div>
                  <h3 className="cart__item-name">{item.name}</h3>
                  <p  className="cart__item-price">${Number(item.price).toLocaleString("es-AR")} c/u</p>

                  {/* ## Controles de cantidad: dispatcha updateQuantity con la nueva cantidad */}
                  <div className="cart__qty">
                    <button className="cart__qty-btn"
                      onClick={() => dispatch(updateQuantity({ id: item.id, quantity: item.quantity - 1 }))}>
                      -
                    </button>
                    <span className="cart__qty-val">{item.quantity}</span>
                    <button className="cart__qty-btn"
                      onClick={() => dispatch(updateQuantity({ id: item.id, quantity: item.quantity + 1 }))}>
                      +
                    </button>
                  </div>
                </div>
                <div>
                  <p  className="cart__item-total">
                    ${(Number(item.price) * item.quantity).toLocaleString("es-AR")}
                  </p>
                  {/* ## Elimina el item del store local (no persiste en DB hasta checkout) */}
                  <button className="cart__btn-remove"
                    onClick={() => dispatch(removeFromCart(item.id))}>
                    Eliminar
                  </button>
                </div>
              </div>
            ))}
            <button className="cart__btn-clear" onClick={handleClear}>Vaciar carrito</button>
          </div>

          {/* ## Resumen lateral con total y botón de checkout */}
          <div className="cart__summary">
            <h3>Resumen</h3>
            <div className="cart__summary-row">
              <span>Subtotal ({itemCount} productos)</span>
              <span>${total.toLocaleString("es-AR")}</span>
            </div>
            <div className="cart__summary-total">
              <span>Total</span>
              <span>${total.toLocaleString("es-AR")}</span>
            </div>
            <Link to="/checkout"  className="cart__btn-checkout">Finalizar compra</Link>
            <Link to="/products" className="cart__btn-continue">Seguir comprando</Link>
          </div>
        </div>
      )}
    </div>
  );
};

export default Cart;
