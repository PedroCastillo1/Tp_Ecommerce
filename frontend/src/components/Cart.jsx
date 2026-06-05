import React from 'react';
import { Link } from 'react-router-dom';

// REDUX: reemplazamos useCart (Context) por useSelector y useDispatch
// useSelector -> lee datos del store (como "leer" el estado)
// useDispatch -> envia acciones al store (como "escribir" el estado)
import { useSelector, useDispatch } from 'react-redux';
import {
  selectCartItems,
  selectCartTotal,
  selectCartItemCount,
  removeFromCart,
  updateQuantity,
  clearCart,
} from '../store/cartSlice';

import './Cart.css';

const DEFAULT_IMAGE = 'https://placehold.co/100x100?text=?';

const Cart = () => {
  // useDispatch nos da la funcion para despachar acciones al store
  const dispatch = useDispatch();

  // useSelector extrae partes del estado usando los selectores definidos en cartSlice
  const cartItems = useSelector(selectCartItems);
  const total     = useSelector(selectCartTotal);
  const itemCount = useSelector(selectCartItemCount);

  return (
    <div className="cart">
      <h1 className="cart__title">Carrito de Compras</h1>
      {itemCount > 0 && (
        <p className="cart__count">{itemCount} {itemCount === 1 ? 'producto' : 'productos'}</p>
      )}

      {cartItems.length === 0 ? (
        <div className="cart__empty">
          <p>Tu carrito esta vacio</p>
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
                    {/* dispatch(updateQuantity) -> envia la accion al cartSlice */}
                    <button className="cart__qty-btn" onClick={() => dispatch(updateQuantity({ id: item.id, quantity: item.quantity - 1 }))}>-</button>
                    <span className="cart__qty-val">{item.quantity}</span>
                    <button className="cart__qty-btn" onClick={() => dispatch(updateQuantity({ id: item.id, quantity: item.quantity + 1 }))}>+</button>
                  </div>
                </div>
                <div>
                  <p className="cart__item-total">${(Number(item.price) * item.quantity).toLocaleString('es-AR')}</p>
                  {/* dispatch(removeFromCart) -> elimina el producto del carrito */}
                  <button className="cart__btn-remove" onClick={() => dispatch(removeFromCart(item.id))}>Eliminar</button>
                </div>
              </div>
            ))}
            {/* dispatch(clearCart) -> vacia el array cartItems en el store */}
            <button className="cart__btn-clear" onClick={() => dispatch(clearCart())}>Vaciar carrito</button>
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
            <Link to="/products" className="cart__btn-continue">Seguir comprando</Link>
          </div>
        </div>
      )}
    </div>
  );
};

export default Cart;
