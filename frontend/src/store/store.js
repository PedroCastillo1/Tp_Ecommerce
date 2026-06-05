// store.js
// REDUX — La "central" o "almacén" global del estado
//
// El store es el único lugar donde vive TODO el estado de Redux.
// Es como una base de datos en memoria para el frontend.
//
// configureStore recibe los "reducers" de cada slice.
// Cada slice maneja una parte del estado:
//   cart → todo lo del carrito (cartSlice)
//   (en el Push 2 agregaremos favorites → favoriteSlice)

import { configureStore } from '@reduxjs/toolkit';
import cartReducer from './cartSlice';

const store = configureStore({
  reducer: {
    // "cart" es el nombre con el que accedemos al estado del carrito
    // en los selectores: state.cart.cartItems
    cart: cartReducer,
  },
});

export default store;
