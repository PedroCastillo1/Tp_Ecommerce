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
      throw new Error(msg || 'Usuario o contraseña incorrectos.');
    }

    // El backend devuelve el JWT como texto plano
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
      const payload = JSON.parse(atob(token.split('.')[1]));
      return { email: payload.sub || payload.email };
    } catch {
      return null;
    }
  },
};
