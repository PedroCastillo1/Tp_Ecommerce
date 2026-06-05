// favoriteSlice.js
// REDUX — Slice de Favoritos con integracion al backend
//
// Usa createAsyncThunk para sincronizar favoritos con la DB.
// Endpoints: GET/POST/DELETE /api/favoritos/{userId}/...

import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { favoriteApiService } from "../services/favoriteApiService";

// ── Async Thunks ──────────────────────────────────────────────────────────

// Carga los favoritos del usuario desde la DB
export const fetchFavorites = createAsyncThunk(
  "favorite/fetchFavorites",
  async ({ userId, token }) => {
    const items = await favoriteApiService.getFavorites(userId, token);
    // El backend devuelve Favorite con { id, product }
    // Extraemos el product para usar en los componentes
    return items.map((fav) => fav.product);
  }
);

// Agrega o quita un favorito en la DB (toggle)
export const toggleFavoriteAsync = createAsyncThunk(
  "favorite/toggleFavoriteAsync",
  async ({ product, userId, token, isFav }) => {
    if (isFav) {
      await favoriteApiService.removeFavorite(userId, product.id, token);
      return { product, removed: true };
    } else {
      await favoriteApiService.addFavorite(userId, product.id, token);
      return { product, removed: false };
    }
  }
);

// ── Slice ─────────────────────────────────────────────────────────────────

const favoriteSlice = createSlice({
  name: "favorite",
  initialState: {
    favoriteItems: [],
    loading: false,
    error: null,
  },

  reducers: {
    // Accion local de toggle (sin backend, por si no hay usuario)
    toggleFavorite(state, action) {
      const product = action.payload;
      const existe = state.favoriteItems.find((item) => item.id === product.id);
      if (existe) {
        state.favoriteItems = state.favoriteItems.filter(
          (item) => item.id !== product.id
        );
      } else {
        state.favoriteItems.push(product);
      }
    },
  },

  extraReducers: (builder) => {
    // fetchFavorites: carga favoritos desde la DB
    builder
      .addCase(fetchFavorites.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchFavorites.fulfilled, (state, action) => {
        state.loading = false;
        state.favoriteItems = action.payload;
      })
      .addCase(fetchFavorites.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      });

    // toggleFavoriteAsync: agrega o quita en DB y actualiza estado local
    builder.addCase(toggleFavoriteAsync.fulfilled, (state, action) => {
      const { product, removed } = action.payload;
      if (removed) {
        state.favoriteItems = state.favoriteItems.filter(
          (item) => item.id !== product.id
        );
      } else {
        const existe = state.favoriteItems.find((i) => i.id === product.id);
        if (!existe) state.favoriteItems.push(product);
      }
    });
  },
});

export const { toggleFavorite } = favoriteSlice.actions;

// ── Selectores ────────────────────────────────────────────────────────────
export const selectFavoriteItems = (state) => state.favorite.favoriteItems;
export const selectIsFavorite    = (productId) => (state) =>
  state.favorite.favoriteItems.some((item) => item.id === productId);

export default favoriteSlice.reducer;
