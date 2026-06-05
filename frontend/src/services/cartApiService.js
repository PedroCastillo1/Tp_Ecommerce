// cartApiService.js
// Llamadas HTTP al backend para el carrito
// Endpoints: GET /api/carrito/{userId}, POST /api/carrito/{userId}/agregar/{productId}
//            DELETE /api/carrito/{userId}/vaciar, POST /api/carrito/{userId}/checkout

const BASE_URL = 'http://localhost:8080';

const headers = (token) => ({
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${token}`,
});

export const cartApiService = {
  // Obtiene el carrito del usuario desde la DB
  getCart: async (userId, token) => {
    const res = await fetch(`${BASE_URL}/api/carrito/${userId}`, {
      headers: headers(token),
    });
    if (!res.ok) throw new Error('Error al obtener el carrito');
    return res.json();
  },

  // Agrega un producto al carrito en la DB
  addItem: async (userId, productId, cantidad, token) => {
    const res = await fetch(
      `${BASE_URL}/api/carrito/${userId}/agregar/${productId}?cantidad=${cantidad}`,
      { method: 'POST', headers: headers(token) }
    );
    if (!res.ok) throw new Error('Error al agregar al carrito');
    return res.json();
  },

  // Vacia el carrito en la DB
  clearCart: async (userId, token) => {
    const res = await fetch(`${BASE_URL}/api/carrito/${userId}/vaciar`, {
      method: 'DELETE',
      headers: headers(token),
    });
    if (!res.ok) throw new Error('Error al vaciar el carrito');
  },

  // Confirma la compra en la DB (descuenta stock)
  checkout: async (userId, token) => {
    const res = await fetch(`${BASE_URL}/api/carrito/${userId}/checkout`, {
      method: 'POST',
      headers: headers(token),
    });
    if (!res.ok) throw new Error('Error en el checkout');
    return res.json();
  },
};
