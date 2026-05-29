import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../hooks/useContext/CartContext';
import './Checkout.css';

const Checkout = () => {
  const { cartItems, total, itemCount, clearCart } = useCart();
  const [confirmed, setConfirmed] = useState(false);

  const handleConfirm = () => {
    setConfirmed(true);
    clearCart();
  };

  if (confirmed) {
    return (
      <div className="checkout__confirmed">
        <div className="checkout__confirmed__icon">✅</div>
        <h2>¡Compra confirmada!</h2>
        <p>Gracias por tu compra. Pronto recibirás más información.</p>
        <Link to="/" className="btn-primary">Volver a la tienda</Link>
      </div>
    );
  }

  return (
    <div className="checkout">
      <h1>Confirmar Compra</h1>

      {cartItems.length === 0 ? (
        <div className="checkout__empty">
          <p>No tenés productos en el carrito</p>
          <Link to="/products" className="btn-primary">Ir a la tienda</Link>
        </div>
      ) : (
        <div className="checkout__layout">
          <div className="checkout__items">
            <h3>Tu pedido ({itemCount} productos)</h3>
            {cartItems.map(item => (
              <div key={item.id} className="checkout__item">
                <div className="checkout__item-left">
                  <span className="checkout__item-qty">{item.quantity}x</span>
                  <span className="checkout__item-name">{item.name}</span>
                </div>
                <span className="checkout__item-subtotal">
                  ${(Number(item.price) * item.quantity).toLocaleString('es-AR')}
                </span>
              </div>
            ))}
          </div>

          <div className="checkout__panel">
            <h3>Total a pagar</h3>
            <div className="checkout__total">${total.toLocaleString('es-AR')}</div>
            <button className="checkout__btn-confirm" onClick={handleConfirm}>
              Confirmar compra
            </button>
            <Link to="/cart" className="checkout__btn-back">← Volver al carrito</Link>
          </div>
        </div>
      )}
    </div>
  );
};

export default Checkout;
