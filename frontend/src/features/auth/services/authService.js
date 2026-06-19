// ## SERVICIO — Autenticación
// ##
// ## Encapsula todas las llamadas HTTP al backend de auth.
// ## Usa credentials: 'include' para que el navegador envíe/reciba
// ## la cookie HTTP-Only con el JWT automáticamente.
// ##
// ## IMPORTANTE: el JWT nunca toca JavaScript — está en la cookie HttpOnly.
// ## El frontend no puede leer el token, solo enviarlo junto con cada request.
// ##
// ## Endpoints:
// ##   POST /api/auth/login    → autentica y setea la cookie JWT
// ##   POST /api/auth/register → crea cuenta nueva
// ##   POST /api/auth/logout   → borra la cookie JWT en el servidor
// ##   GET  /api/auth/me       → devuelve los datos del usuario autenticado

const API = 'http://localhost:8080/api/auth';

export const authService = {
  // ## Envía email + password al backend
  // ## Si son correctos, el servidor devuelve una cookie JWT HTTP-Only
  // ## No devuelve el token al frontend (por diseño — seguridad)
  login: async (credentials) => {
    const response = await fetch(`${API}/login`, {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include', // ## necesario para recibir la cookie del servidor
      body: JSON.stringify({ email: credentials.email, password: credentials.password }),
    });

    if (!response.ok) {
      const msg = await response.text();
      throw new Error(msg || 'Usuario o contraseña incorrectos.');
    }

    return true;
  },

  // ## Crea una cuenta nueva con los datos del formulario de registro
  register: async (data) => {
    const response = await fetch(`${API}/register`, {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const msg = await response.text();
      throw new Error(msg || 'Error al registrarse.');
    }

    return true;
  },

  // ## Le pide al servidor que invalide/borre la cookie JWT
  // ## Después de esto, todas las requests autenticadas fallarán con 401
  logout: async () => {
    await fetch(`${API}/logout`, {
      method:      'POST',
      credentials: 'include',
    });
  },

  // ## Consulta quién es el usuario actual según la cookie JWT activa
  // ## Devuelve: { id, email, username } si hay sesión, o null si no hay
  // ## Se usa al recargar la página para restaurar la sesión sin pedir login de nuevo
  getCurrentUser: async () => {
    try {
      const response = await fetch(`${API}/me`, {
        credentials: 'include',
      });
      if (!response.ok) return null; // ## 401 = no hay sesión activa, no es un error fatal
      return await response.json();
    } catch {
      return null; // ## si hay error de red, tratamos como "no autenticado"
    }
  },
};
