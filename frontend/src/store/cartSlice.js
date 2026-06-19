// ## REDUX SLICE — Carrito de compras
// ##
// ## Maneja el estado global del carrito sincronizado con la base de datos.
// ## Cuando el usuario está logueado, cada acción se refleja en el backend.
// ## Cuando no está logueado, las acciones son solo locales (en memoria).
// ##
// ## Estado: { cartItems: [], loading: false, error: null }
// ## cartItems: array de productos con { ...product, quantity, cartItemId }

import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { cartApiService } from "../services/cartApiService";

// ── Thunks (acciones asincrónicas que llaman a la API) ────────────────────

// ## Carga el carrito del usuario desde la DB al iniciar sesión
// ## Se dispara en App.jsx cuando el usuario se autentica
export const fetchCart = createAsyncThunk(
  "cart/fetchCart",
  async ({ userId }) => {
    const items = await cartApiService.getCart(userId);
    // ## El backend devuelve CartItem { id, product, quantity }
    // ## Lo aplanamos para que los componentes accedan directamente a product.*
    return items.map((item) => ({
      ...item.product,
      quantity:   item.quantity,
      cartItemId: item.id,
    }));
  }
);

// ## Agrega un producto al carrito en la DB (cantidad = 1 por defecto)
export const addToCartAsync = createAsyncThunk(
  "cart/addToCartAsync",
  async ({ product, userId }) => {
    const item = await cartApiService.addItem(userId, product.id, 1);
    return {
      ...item.product,
      quantity:   item.quantity,
      cartItemId: item.id,
    };
  }
);

// ## Vacía el carrito en la DB (DELETE /api/carrito/{userId}/vaciar)
export const clearCartAsync = createAsyncThunk(
  "cart/clearCartAsync",
  async ({ userId }) => {
    await cartApiService.clearCart(userId);
  }
);

// ## Confirma la compra: descuenta stock, guarda historial y vacía el carrito
export const checkoutAsync = createAsyncThunk(
  "cart/checkoutAsync",
  async ({ userId }) => {
    const total = await cartApiService.checkout(userId);
    return total;
  }
);

// ── Slice ─────────────────────────────────────────────────────────────────

const cartSlice = createSlice({
  name: "cart",
  initialState: {
    cartItems: [], // ## array de productos en el carrito
    loading:   false,
    error:     null,
  },

  reducers: {
    // ## Actualiza la cantidad de un item localmente (sin llamar al backend)
    updateQuantity(state, action) {
      const { id, quantity } = action.payload;
      if (quantity <= 0) {
        // ## Si la cantidad es 0 o negativa, elimina el item del carrito
        state.cartItems = state.cartItems.filter((item) => item.id !== id);
      } else {
        const item = state.cartItems.find((item) => item.id === id);
        if (item) item.quantity = quantity;
      }
    },

    // ## Elimina un item del carrito localmente por su id
    removeFromCart(state, action) {
      state.cartItems = state.cartItems.filter(
        (item) => item.id !== action.payload
      );
    },

    // ## Vacía el carrito localmente (sin llamar al backend)
    clearCart(state) {
      state.cartItems = [];
    },
  },

  extraReducers: (builder) => {
    // ## fetchCart: mientras carga muestra loading, al terminar guarda los items
    builder
      .addCase(fetchCart.pending,   (state)         => { state.loading = true; })
      .addCase(fetchCart.fulfilled, (state, action) => {
        state.loading   = false;
        state.cartItems = action.payload;
      })
      .addCase(fetchCart.rejected,  (state, action) => {
        state.loading = false;
        state.error   = action.error.message;
      });

    // ## addToCartAsync: si el producto ya existe suma cantidad, si no lo agrega
    builder.addCase(addToCartAsync.fulfilled, (state, action) => {
      const newItem  = action.payload;
      const existing = state.cartItems.find((i) => i.id === newItem.id);
      if (existing) {
        existing.quantity += 1;
      } else {
        state.cartItems.push(newItem);
      }
    });

    // ## clearCartAsync y checkoutAsync: vacían el carrito en el estado local
    builder.addCase(clearCartAsync.fulfilled, (state) => { state.cartItems = []; });
    builder.addCase(checkoutAsync.fulfilled,  (state) => { state.cartItems = []; });
  },
});

export const { updateQuantity, removeFromCart, clearCart } = cartSlice.actions;

// ── Selectores ────────────────────────────────────────────────────────────
// ## Funciones que extraen datos del store para usar con useSelector()

export const selectCartItems     = (state) => state.cart.cartItems;
export const selectCartLoading   = (state) => state.cart.loading;

// ## Calcula el total sumando price * quantity de cada item
export const selectCartTotal = (state) =>
  state.cart.cartItems.reduce(
    (sum, item) => sum + Number(item.price) * item.quantity, 0
  );

// ## Cuenta el total de unidades en el carrito (para el badge del Navbar)
export const selectCartItemCount = (state) =>
  state.cart.cartItems.reduce((sum, item) => sum + item.quantity, 0);

export default cartSlice.reducer;
