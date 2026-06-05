// favoriteApiService.js
// Llamadas HTTP al backend para favoritos
// Endpoints: GET /api/favoritos/{userId}
//            POST /api/favoritos/{userId}/agregar/{productId}
//            DELETE /api/favoritos/{userId}/eliminar/{productId}

const BASE_URL = 'http://localhost:8080';

const headers = (token) => ({
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${token}`,
});

export const favoriteApiService = {
  // Obtiene los favoritos del usuario desde la DB
  getFavorites: async (userId, token) => {
    const res = await fetch(`${BASE_URL}/api/favoritos/${userId}`, {
      headers: headers(token),
    });
    if (!res.ok) throw new Error('Error al obtener favoritos');
    return res.json();
  },

  // Agrega un producto a favoritos en la DB
  addFavorite: async (userId, productId, token) => {
    const res = await fetch(
      `${BASE_URL}/api/favoritos/${userId}/agregar/${productId}`,
      { method: 'POST', headers: headers(token) }
    );
    if (!res.ok) throw new Error('Error al agregar favorito');
    return res.json();
  },

  // Elimina un producto de favoritos en la DB
  removeFavorite: async (userId, productId, token) => {
    const res = await fetch(
      `${BASE_URL}/api/favoritos/${userId}/eliminar/${productId}`,
      { method: 'DELETE', headers: headers(token) }
    );
    if (!res.ok) throw new Error('Error al eliminar favorito');
  },
};
