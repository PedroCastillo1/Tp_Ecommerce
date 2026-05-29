import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../hooks/useContext/CartContext';
import './Cart.css';

const DEFAULT_IMAGE = 'https://placehold.co/100x100?text=?';

const Cart = () => {
  const { cartItems, removeFromCart, updateQuantity, total, itemCount, clearCart } = useCart();

  return (
    <div className="cart">
      <h1 className="cart__title">🛒 Carrito de Compras</h1>
      {itemCount > 0 && (
        <p className="cart__count">{itemCount} {itemCount === 1 ? 'producto' : 'productos'}</p>
      )}

      {cartItems.length === 0 ? (
        <div className="cart__empty">
          <p>Tu carrito está vacío</p>
          <Link to="/products" className="btn-primary">Ver productos</Link>
        </div>
      ) : (
        <div className="cart__layout">
          <div>
            {cartItems.map(item => (
              <div key={item.id} className="cart__item">
                <img
                  className="cart__item-img"
                  src={item.imageUrl || DEFAULT_IMAGE}
                  alt={item.name}
                  onError={e => { e.target.src = DEFAULT_IMAGE; }}
                />
                <div>
                  <h3 className="cart__item-name">{item.name}</h3>
                  <p className="cart__item-price">${Number(item.price).toLocaleString('es-AR')} c/u</p>
                  <div className="cart__qty">
                    <button className="cart__qty-btn" onClick={() => updateQuantity(item.id, item.quantity - 1)}>−</button>
                    <span className="cart__qty-val">{item.quantity}</span>
                    <button className="cart__qty-btn" onClick={() => updateQuantity(item.id, item.quantity + 1)}>+</button>
                  </div>
                </div>
                <div>
                  <p className="cart__item-total">${(Number(item.price) * item.quantity).toLocaleString('es-AR')}</p>
                  <button className="cart__btn-remove" onClick={() => removeFromCart(item.id)}>Eliminar</button>
                </div>
              </div>
            ))}
            <button className="cart__btn-clear" onClick={clearCart}>Vaciar carrito</button>
          </div>

          <div className="cart__summary">
            <h3>Resumen</h3>
            <div className="cart__summary-row">
              <span>Subtotal ({itemCount} productos)</span>
              <span>${total.toLocaleString('es-AR')}</span>
            </div>
            <div className="cart__summary-total">
              <span>Total</span>
              <span>${total.toLocaleString('es-AR')}</span>
            </div>
            <Link to="/checkout" className="cart__btn-checkout">Finalizar compra</Link>
            <Link to="/products" className="cart__btn-continue">← Seguir comprando</Link>
          </div>
        </div>
      )}
    </div>
  );
};

export default Cart;
