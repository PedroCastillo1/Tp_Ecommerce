// cartSlice.js
// REDUX — Slice del Carrito con integracion al backend
//
// Usa createAsyncThunk para hacer llamadas a la API del backend.
// Si el usuario esta logueado, el carrito se sincroniza con la DB.
// Cada accion tiene tres estados: pending, fulfilled, rejected.

import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { cartApiService } from "../services/cartApiService";

// ── Async Thunks (acciones que llaman a la API) ───────────────────────────

// Carga el carrito del usuario desde la DB al iniciar sesion
export const fetchCart = createAsyncThunk(
  "cart/fetchCart",
  async ({ userId, token }) => {
    const items = await cartApiService.getCart(userId, token);
    // El backend devuelve CartItem con { id, product, quantity }
    // Lo mapeamos al formato que usan nuestros componentes
    return items.map((item) => ({
      ...item.product,
      quantity: item.quantity,
      cartItemId: item.id,
    }));
  }
);

// Agrega un producto al carrito en la DB
export const addToCartAsync = createAsyncThunk(
  "cart/addToCartAsync",
  async ({ product, userId, token }) => {
    const item = await cartApiService.addItem(userId, product.id, 1, token);
    return {
      ...item.product,
      quantity: item.quantity,
      cartItemId: item.id,
    };
  }
);

// Vacia el carrito en la DB
export const clearCartAsync = createAsyncThunk(
  "cart/clearCartAsync",
  async ({ userId, token }) => {
    await cartApiService.clearCart(userId, token);
  }
);

// Confirma la compra en la DB
export const checkoutAsync = createAsyncThunk(
  "cart/checkoutAsync",
  async ({ userId, token }) => {
    const total = await cartApiService.checkout(userId, token);
    return total;
  }
);

// ── Slice ─────────────────────────────────────────────────────────────────

const cartSlice = createSlice({
  name: "cart",
  initialState: {
    cartItems: [],
    loading: false,
    error: null,
  },

  reducers: {
    // Accion local para update de cantidad (sin llamada al backend por ahora)
    updateQuantity(state, action) {
      const { id, quantity } = action.payload;
      if (quantity <= 0) {
        state.cartItems = state.cartItems.filter((item) => item.id !== id);
      } else {
        const item = state.cartItems.find((item) => item.id === id);
        if (item) item.quantity = quantity;
      }
    },
    // Elimina un item localmente
    removeFromCart(state, action) {
      state.cartItems = state.cartItems.filter(
        (item) => item.id !== action.payload
      );
    },
    // Vacia el carrito localmente
    clearCart(state) {
      state.cartItems = [];
    },
  },

  // extraReducers maneja los estados de los async thunks
  extraReducers: (builder) => {
    // fetchCart: carga el carrito desde la DB
    builder
      .addCase(fetchCart.pending, (state) => {
        state.loading = true;
      })
      .addCase(fetchCart.fulfilled, (state, action) => {
        state.loading = false;
        state.cartItems = action.payload;
      })
      .addCase(fetchCart.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message;
      });

    // addToCartAsync: agrega producto a la DB y al estado local
    builder
      .addCase(addToCartAsync.fulfilled, (state, action) => {
        const newItem = action.payload;
        const existing = state.cartItems.find((i) => i.id === newItem.id);
        if (existing) {
          existing.quantity += 1;
        } else {
          state.cartItems.push(newItem);
        }
      });

    // clearCartAsync: vacia el carrito en DB y en estado local
    builder
      .addCase(clearCartAsync.fulfilled, (state) => {
        state.cartItems = [];
      });

    // checkoutAsync: vacia el carrito despues de confirmar compra
    builder
      .addCase(checkoutAsync.fulfilled, (state) => {
        state.cartItems = [];
      });
  },
});

export const { updateQuantity, removeFromCart, clearCart } = cartSlice.actions;

// ── Selectores ────────────────────────────────────────────────────────────
export const selectCartItems     = (state) => state.cart.cartItems;
export const selectCartLoading   = (state) => state.cart.loading;
export const selectCartTotal     = (state) =>
  state.cart.cartItems.reduce(
    (sum, item) => sum + Number(item.price) * item.quantity,
    0
  );
export const selectCartItemCount = (state) =>
  state.cart.cartItems.reduce((sum, item) => sum + item.quantity, 0);

export default cartSlice.reducer;
