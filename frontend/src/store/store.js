// store.js
// REDUX — Store central
// El backend es la fuente de verdad cuando el usuario esta logueado.
// Al hacer login, AppContent dispara fetchCart y fetchFavorites para cargar desde la DB.

import { configureStore } from "@reduxjs/toolkit";
import cartReducer from "./cartSlice";
import favoriteReducer from "./favoriteSlice";

const store = configureStore({
  reducer: {
    cart: cartReducer,
    favorite: favoriteReducer,
  },
});

export default store;
