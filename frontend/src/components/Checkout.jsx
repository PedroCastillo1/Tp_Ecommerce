// ## COMPONENTE — Checkout (Confirmación de Compra)
// ##
// ## Pantalla final del proceso de compra.
// ## Muestra el resumen del pedido y un botón para confirmar.
// ##
// ## Al confirmar:
// ##   - Usuario logueado → llama a checkoutAsync (backend: descuenta stock, guarda Order, vacía carrito)
// ##   - Sin login        → solo limpia el carrito local con clearCart
// ##
// ## Ruta: /checkout (protegida — requiere login via ProtectedRoute)

import React, { useState } from "react";
import { Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import {
  selectCartItems, selectCartTotal, selectCartItemCount,
  checkoutAsync, clearCart,
} from "../store/cartSlice";
import { useAuth } from "../features/auth/context/AuthContext";
import "./Checkout.css";

const Checkout = () => {
  const dispatch  = useDispatch();
  const { user }  = useAuth();
  const cartItems = useSelector(selectCartItems);
  const total     = useSelector(selectCartTotal);
  const itemCount = useSelector(selectCartItemCount);

  // ## confirmed: alterna entre el formulario y la pantalla de "Compra confirmada"
  const [confirmed, setConfirmed] = useState(false);

  const handleConfirm = async () => {
    if (user) {
      // ## Llama al backend: descuenta stock, guarda historial, vacía carrito en DB
      await dispatch(checkoutAsync({ userId: user.id }));
    } else {
      // ## Sin login: solo limpia el estado local
      dispatch(clearCart());
    }
    setConfirmed(true);
  };

  // ## Pantalla de éxito después de confirmar
  if (confirmed) {
    return (
      <div className="checkout__confirmed">
        <div className="checkout__confirmed__icon">ok</div>
        <h2>Compra confirmada!</h2>
        <p>Gracias por tu compra. Pronto recibiras mas informacion.</p>
        <Link to="/" className="btn-primary">Volver a la tienda</Link>
      </div>
    );
  }

  return (
    <div className="checkout">
      <h1>Confirmar Compra</h1>

      {cartItems.length === 0 ? (
        <div className="checkout__empty">
          <p>No tenes productos en el carrito</p>
          <Link to="/products" className="btn-primary">Ir a la tienda</Link>
        </div>
      ) : (
        <div className="checkout__layout">
          {/* ## Lista de items del pedido */}
          <div className="checkout__items">
            <h3>Tu pedido ({itemCount} productos)</h3>
            {cartItems.map((item) => (
              <div key={item.id} className="checkout__item">
                <div className="checkout__item-left">
                  <span className="checkout__item-qty">{item.quantity}x</span>
                  <span className="checkout__item-name">{item.name}</span>
                </div>
                <span className="checkout__item-subtotal">
                  ${(Number(item.price) * item.quantity).toLocaleString("es-AR")}
                </span>
              </div>
            ))}
          </div>

          {/* ## Panel de total y botón de confirmación */}
          <div className="checkout__panel">
            <h3>Total a pagar</h3>
            <div className="checkout__total">${total.toLocaleString("es-AR")}</div>
            <button className="checkout__btn-confirm" onClick={handleConfirm}>
              Confirmar compra
            </button>
            <Link to="/cart" className="checkout__btn-back">Volver al carrito</Link>
          </div>
        </div>
      )}
    </div>
  );
};

export default Checkout;
