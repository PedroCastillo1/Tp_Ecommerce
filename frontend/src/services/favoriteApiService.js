// ## SERVICIO API — Favoritos
// ##
// ## Encapsula todas las llamadas HTTP al backend para favoritos.
// ## Usa credentials: 'include' para enviar la cookie JWT automáticamente.
// ##
// ## Endpoints que usa:
// ##   GET    /api/favoritos/{userId}                          → obtener favoritos
// ##   POST   /api/favoritos/{userId}/agregar/{productId}      → agregar favorito
// ##   DELETE /api/favoritos/{userId}/eliminar/{productId}     → quitar favorito

const BASE_URL = 'http://localhost:8080';

const headers = {
  'Content-Type': 'application/json',
};

export const favoriteApiService = {
  // ## Obtiene todos los favoritos del usuario desde la DB
  // ## Devuelve: Favorite[] { id, product }
  getFavorites: async (userId) => {
    const res = await fetch(`${BASE_URL}/api/favoritos/${userId}`, {
      headers,
      credentials: 'include',
    });
    if (!res.ok) throw new Error('Error al obtener favoritos');
    return res.json();
  },

  // ## Agrega un producto a la lista de favoritos en la DB
  // ## Devuelve: el Favorite creado { id, product }
  addFavorite: async (userId, productId) => {
    const res = await fetch(
      `${BASE_URL}/api/favoritos/${userId}/agregar/${productId}`,
      { method: 'POST', headers, credentials: 'include' }
    );
    if (!res.ok) throw new Error('Error al agregar favorito');
    return res.json();
  },

  // ## Elimina un producto de la lista de favoritos en la DB
  removeFavorite: async (userId, productId) => {
    const res = await fetch(
      `${BASE_URL}/api/favoritos/${userId}/eliminar/${productId}`,
      { method: 'DELETE', headers, credentials: 'include' }
    );
    if (!res.ok) throw new Error('Error al eliminar favorito');
  },
};
