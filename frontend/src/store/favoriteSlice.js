// favoriteSlice.js
// REDUX — Slice de Favoritos
//
// Igual que cartSlice pero para los productos favoritos.
// Reemplaza al FavoriteContext que usabamos con useContext.

import { createSlice } from '@reduxjs/toolkit';

const favoriteSlice = createSlice({
  name: 'favorite',

  // Estado inicial: lista de favoritos vacia
  initialState: {
    favoriteItems: [],
  },

  reducers: {
    // ── addToFavorite ──────────────────────────────────────────────────────
    // Agrega un producto a favoritos. Si ya existe, no lo duplica.
    addToFavorite(state, action) {
      const product = action.payload;
      const yaExiste = state.favoriteItems.find(item => item.id === product.id);
      if (!yaExiste) {
        state.favoriteItems.push(product);
      }
    },

    // ── removeFromFavorite ─────────────────────────────────────────────────
    // Elimina un producto de favoritos por su id.
    removeFromFavorite(state, action) {
      const productId = action.payload;
      state.favoriteItems = state.favoriteItems.filter(item => item.id !== productId);
    },

    // ── toggleFavorite ─────────────────────────────────────────────────────
    // Si ya es favorito lo quita, si no lo agrega.
    toggleFavorite(state, action) {
      const product = action.payload;
      const existe = state.favoriteItems.find(item => item.id === product.id);
      if (existe) {
        state.favoriteItems = state.favoriteItems.filter(item => item.id !== product.id);
      } else {
        state.favoriteItems.push(product);
      }
    },
  },
});

// Exportamos las acciones para usar con useDispatch
export const { addToFavorite, removeFromFavorite, toggleFavorite } = favoriteSlice.actions;

// ── Selectores ─────────────────────────────────────────────────────────────
// Devuelve todos los productos favoritos
export const selectFavoriteItems = (state) => state.favorite.favoriteItems;

// Devuelve true si el producto con ese id ya es favorito
export const selectIsFavorite = (productId) => (state) =>
  state.favorite.favoriteItems.some(item => item.id === productId);

export default favoriteSlice.reducer;