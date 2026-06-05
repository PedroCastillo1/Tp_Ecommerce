// cartSlice.js
// REDUX — Slice del Carrito
//
// Un "slice" es un pedazo del estado global. Este slice maneja TODO lo
// relacionado al carrito: qué productos hay, cuántos, y el total.
//
// Reemplaza al CartContext que usábamos con useContext.
// La diferencia: ahora el estado vive en un lugar central (el store),
// no dentro de un Provider anidado en el árbol de componentes.

import { createSlice } from '@reduxjs/toolkit';

// createSlice recibe un objeto con:
//   name    → nombre del slice (se usa para los action types internamente)
//   initialState → el estado inicial, igual que en useState
//   reducers → las funciones que modifican el estado (como los "case" de un switch)
const cartSlice = createSlice({
  name: 'cart',

  // Estado inicial: el carrito empieza vacío
  initialState: {
    cartItems: [], // array de productos con su cantidad
  },

  reducers: {
    // ── addToCart ──────────────────────────────────────────────────────────
    // Recibe un producto. Si ya está en el carrito, suma 1 a la cantidad.
    // Si no está, lo agrega con quantity: 1.
    // "state" es el estado actual, "action.payload" es el dato que enviamos.
    addToCart(state, action) {
      const product = action.payload;
      const existing = state.cartItems.find(item => item.id === product.id);

      if (existing) {
        // Con RTK podemos mutar el estado directamente (usa Immer por debajo)
        existing.quantity += 1;
      } else {
        state.cartItems.push({ ...product, quantity: 1 });
      }
    },

    // ── removeFromCart ─────────────────────────────────────────────────────
    // Recibe el id del producto y lo elimina del carrito.
    removeFromCart(state, action) {
      const productId = action.payload;
      state.cartItems = state.cartItems.filter(item => item.id !== productId);
    },

    // ── updateQuantity ─────────────────────────────────────────────────────
    // Recibe { id, quantity }. Si quantity llega a 0 o menos, elimina el item.
    updateQuantity(state, action) {
      const { id, quantity } = action.payload;

      if (quantity <= 0) {
        // Cantidad inválida → eliminar el producto del carrito
        state.cartItems = state.cartItems.filter(item => item.id !== id);
      } else {
        const item = state.cartItems.find(item => item.id === id);
        if (item) item.quantity = quantity;
      }
    },

    // ── clearCart ──────────────────────────────────────────────────────────
    // Vacía el carrito completamente.
    clearCart(state) {
      state.cartItems = [];
    },
  },
});

// Exportamos las "acciones" → son las funciones que los componentes van a llamar
// con useDispatch para modificar el estado del carrito.
export const { addToCart, removeFromCart, updateQuantity, clearCart } = cartSlice.actions;

// ── Selectores ─────────────────────────────────────────────────────────────
// Un selector es una función que extrae datos del estado global.
// Los usamos con useSelector en los componentes.

// Devuelve todos los ítems del carrito
export const selectCartItems = (state) => state.cart.cartItems;

// Calcula el total sumando precio × cantidad de cada ítem
export const selectCartTotal = (state) =>
  state.cart.cartItems.reduce(
    (sum, item) => sum + Number(item.price) * item.quantity,
    0
  );

// Cuenta la cantidad total de unidades (no productos distintos, sino unidades)
export const selectCartItemCount = (state) =>
  state.cart.cartItems.reduce((sum, item) => sum + item.quantity, 0);

// Exportamos el reducer para registrarlo en el store
export default cartSlice.reducer;
