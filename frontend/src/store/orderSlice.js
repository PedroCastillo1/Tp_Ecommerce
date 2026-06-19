// ## REDUX SLICE — Historial de Compras
// ##
// ## Maneja el estado del historial de órdenes del usuario.
// ## Cada vez que el usuario hace checkout, el backend guarda la orden en la DB.
// ## Este slice la carga cuando el usuario visita /perfil.
// ##
// ## Estado: { orders: [], loading: false, error: null }
// ## Endpoint: GET /api/ordenes/{userId}

import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { orderApiService } from '../services/orderApiService';

// ── Thunks ────────────────────────────────────────────────────────────────

// ## Carga todas las órdenes del usuario desde la DB
// ## Se dispara al entrar a Profile.jsx
export const fetchOrders = createAsyncThunk(
  'orders/fetchOrders',
  async ({ userId }) => {
    return await orderApiService.getOrders(userId);
  }
);

// ── Slice ─────────────────────────────────────────────────────────────────

const orderSlice = createSlice({
  name: 'orders',
  initialState: {
    orders:  [], // ## array de Order { id, fecha, total, items: [{ product, quantity, precioUnitario }] }
    loading: false,
    error:   null,
  },
  reducers: {}, // ## sin acciones síncronas — el historial solo se lee, no se modifica desde el frontend
  extraReducers: (builder) => {
    builder
      .addCase(fetchOrders.pending,   (state) => { state.loading = true; })
      .addCase(fetchOrders.fulfilled, (state, action) => {
        state.loading = false;
        state.orders  = action.payload; // ## reemplaza el array con lo que devolvió la DB
      })
      .addCase(fetchOrders.rejected,  (state, action) => {
        state.loading = false;
        state.error   = action.error.message;
      });
  },
});

// ── Selectores ────────────────────────────────────────────────────────────

// ## Devuelve todas las órdenes para mostrar en Profile.jsx
export const selectOrders        = (state) => state.orders.orders;
export const selectOrdersLoading = (state) => state.orders.loading;

export default orderSlice.reducer;
