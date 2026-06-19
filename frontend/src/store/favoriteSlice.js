// ## REDUX SLICE — Favoritos
// ##
// ## Maneja la lista de productos marcados como favoritos por el usuario.
// ## Si el usuario está logueado: sincroniza con la DB via API.
// ## Si no está logueado: guarda solo en memoria local (se pierde al recargar).
// ##
// ## Estado: { favoriteItems: [], loading: false, error: null }
// ## Endpoints que usa: GET/POST/DELETE /api/favoritos/{userId}/...

import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { favoriteApiService } from "../services/favoriteApiService";

// ── Thunks (acciones asincrónicas) ────────────────────────────────────────

// ## Carga los favoritos del usuario desde la DB al iniciar sesión
// ## Se dispara en App.jsx junto con fetchCart
export const fetchFavorites = createAsyncThunk(
  "favorite/fetchFavorites",
  async ({ userId }) => {
    const items = await favoriteApiService.getFavorites(userId);
    // ## El backend devuelve Favorite { id, product }
    // ## Extraemos solo el product para simplificar el estado
    return items.map((fav) => fav.product);
  }
);

// ## Alterna un producto entre favorito / no favorito
// ## isFav=true → lo quita | isFav=false → lo agrega
export const toggleFavoriteAsync = createAsyncThunk(
  "favorite/toggleFavoriteAsync",
  async ({ product, userId, isFav }) => {
    if (isFav) {
      await favoriteApiService.removeFavorite(userId, product.id);
      return { product, removed: true };
    } else {
      await favoriteApiService.addFavorite(userId, product.id);
      return { product, removed: false };
    }
  }
);

// ── Slice ─────────────────────────────────────────────────────────────────

const favoriteSlice = createSlice({
  name: "favorite",
  initialState: {
    favoriteItems: [], // ## array de productos favoritos
    loading:       false,
    error:         null,
  },

  reducers: {
    // ## Toggle local (sin backend): para usuarios no logueados
    // ## Agrega si no existe, elimina si ya está
    toggleFavorite(state, action) {
      const product = action.payload;
      const existe  = state.favoriteItems.find((item) => item.id === product.id);
      if (existe) {
        state.favoriteItems = state.favoriteItems.filter((item) => item.id !== product.id);
      } else {
        state.favoriteItems.push(product);
      }
    },
  },

  extraReducers: (builder) => {
    // ## fetchFavorites: carga inicial desde la DB
    builder
      .addCase(fetchFavorites.pending,   (state)         => { state.loading = true; })
      .addCase(fetchFavorites.fulfilled, (state, action) => {
        state.loading       = false;
        state.favoriteItems = action.payload;
      })
      .addCase(fetchFavorites.rejected,  (state, action) => {
        state.loading = false;
        state.error   = action.error.message;
      });

    // ## toggleFavoriteAsync: actualiza el estado local según lo que respondió la DB
    builder.addCase(toggleFavoriteAsync.fulfilled, (state, action) => {
      const { product, removed } = action.payload;
      if (removed) {
        // ## Lo eliminamos del array local
        state.favoriteItems = state.favoriteItems.filter((item) => item.id !== product.id);
      } else {
        // ## Lo agregamos solo si no estaba ya (evita duplicados)
        const existe = state.favoriteItems.find((i) => i.id === product.id);
        if (!existe) state.favoriteItems.push(product);
      }
    });
  },
});

export const { toggleFavorite } = favoriteSlice.actions;

// ── Selectores ────────────────────────────────────────────────────────────

// ## Devuelve todos los productos favoritos
export const selectFavoriteItems = (state) => state.favorite.favoriteItems;

// ## Devuelve true/false si un producto específico es favorito
// ## Uso: const esFav = useSelector(selectIsFavorite(product.id))
export const selectIsFavorite = (productId) => (state) =>
  state.favorite.favoriteItems.some((item) => item.id === productId);

export default favoriteSlice.reducer;
