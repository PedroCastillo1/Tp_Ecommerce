const API = 'http://localhost:8080/api/auth';

export const authService = {
  login: async (credentials) => {
    const response = await fetch(`${API}/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: credentials.email, password: credentials.password }),
    });

    if (!response.ok) {
      const msg = await response.text();
      throw new Error(msg || 'Usuario o contrasena incorrectos.');
    }

    const token = await response.text();
    return { token };
  },

  register: async (data) => {
    const response = await fetch(`${API}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const msg = await response.text();
      throw new Error(msg || 'Error al registrarse.');
    }

    return await response.text();
  },

  getCurrentUser: async (token) => {
    if (!token) return null;
    try {
      // Decodificamos el payload del JWT (parte del medio, en base64)
      // El backend incluye: sub (email), userId (id del usuario)
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        id: payload.userId,          // ID del usuario para llamadas a la API
        email: payload.sub || payload.email,
      };
    } catch {
      return null;
    }
  },
};
