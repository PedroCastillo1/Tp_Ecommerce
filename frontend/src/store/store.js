// store.js
// REDUX — La "central" o "almacen" global del estado
//
// Ahora registramos dos slices:
//   cart     → estado del carrito (cartSlice)
//   favorite → estado de favoritos (favoriteSlice)

import { configureStore } from '@reduxjs/toolkit';
import cartReducer from './cartSlice';
import favoriteReducer from './favoriteSlice';

const store = configureStore({
  reducer: {
    // Cada clave es el nombre con el que accedemos al estado en los selectores
    cart: cartReducer,
    favorite: favoriteReducer,
  },
});

export default store;