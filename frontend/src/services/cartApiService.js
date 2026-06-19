// ## SERVICIO API — Carrito de Compras
// ##
// ## Encapsula todas las llamadas HTTP al backend para el carrito.
// ## Usa credentials: 'include' en todas las requests para enviar la cookie JWT
// ## automáticamente (no se necesita token en headers).
// ##
// ## Endpoints que usa:
// ##   GET    /api/carrito/{userId}                       → obtener carrito
// ##   POST   /api/carrito/{userId}/agregar/{productId}   → agregar item
// ##   DELETE /api/carrito/{userId}/vaciar                → vaciar carrito
// ##   POST   /api/carrito/{userId}/checkout              → confirmar compra

const BASE_URL = 'http://localhost:8080';

const headers = {
  'Content-Type': 'application/json',
};

export const cartApiService = {
  // ## Obtiene todos los items del carrito del usuario desde la DB
  // ## Devuelve: CartItem[] { id, product, quantity }
  getCart: async (userId) => {
    const res = await fetch(`${BASE_URL}/api/carrito/${userId}`, {
      headers,
      credentials: 'include',
    });
    if (!res.ok) throw new Error('Error al obtener el carrito');
    return res.json();
  },

  // ## Agrega un producto al carrito (cantidad inicial = 1)
  // ## Si el producto ya existe en el carrito, el backend suma la cantidad
  // ## Devuelve: el CartItem creado/actualizado
  addItem: async (userId, productId, cantidad) => {
    const res = await fetch(
      `${BASE_URL}/api/carrito/${userId}/agregar/${productId}?cantidad=${cantidad}`,
      { method: 'POST', headers, credentials: 'include' }
    );
    if (!res.ok) throw new Error('Error al agregar al carrito');
    return res.json();
  },

  // ## Elimina todos los items del carrito del usuario en la DB
  clearCart: async (userId) => {
    const res = await fetch(`${BASE_URL}/api/carrito/${userId}/vaciar`, {
      method: 'DELETE',
      headers,
      credentials: 'include',
    });
    if (!res.ok) throw new Error('Error al vaciar el carrito');
  },

  // ## Confirma la compra:
  // ##   1. Descuenta el stock de cada producto
  // ##   2. Guarda la orden en la tabla `ordenes`
  // ##   3. Vacía el carrito del usuario
  // ## Devuelve: total de la compra (número)
  checkout: async (userId) => {
    const res = await fetch(`${BASE_URL}/api/carrito/${userId}/checkout`, {
      method: 'POST',
      headers,
      credentials: 'include',
    });
    if (!res.ok) throw new Error('Error en el checkout');
    return res.json();
  },
};
