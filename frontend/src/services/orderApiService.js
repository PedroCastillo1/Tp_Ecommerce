// ## SERVICIO API — Historial de Compras
// ##
// ## Encapsula la llamada HTTP al backend para obtener el historial de órdenes.
// ## Usa credentials: 'include' para enviar la cookie JWT automáticamente.
// ##
// ## Endpoint: GET /api/ordenes/{userId}
// ## Devuelve:  Order[] { id, fecha, total, items: [{ product, quantity, precioUnitario }] }

const BASE_URL = 'http://localhost:8080';

export const orderApiService = {
  // ## Obtiene todas las compras realizadas por el usuario, ordenadas de más reciente a más antigua
  getOrders: async (userId) => {
    const res = await fetch(`${BASE_URL}/api/ordenes/${userId}`, {
      credentials: 'include',
    });
    if (!res.ok) throw new Error('Error al obtener el historial');
    return res.json();
  },
};
