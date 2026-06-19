// ## CONTEXTO — Autenticación Global
// ##
// ## Provee el estado de autenticación a toda la app via React Context.
// ## Todos los componentes que necesiten saber si el usuario está logueado
// ## usan el hook useAuth() en lugar de pasar props manualmente.
// ##
// ## Estado que expone:
// ##   isAuthenticated  → boolean: ¿hay sesión activa?
// ##   user             → { id, email, username } o null
// ##   loading          → boolean: ¿se está procesando alguna acción de auth?
// ##   error            → mensaje de error o null
// ##   login(creds)     → función: iniciar sesión
// ##   register(data)   → función: registrar cuenta
// ##   logout()         → función: cerrar sesión
// ##
// ## Flujo HTTP-Only cookie:
// ##   - Al hacer login, el backend devuelve una cookie JWT (no el token en JSON)
// ##   - El frontend nunca ve el token, solo manda 'credentials: include' en cada fetch
// ##   - Al recargar la página, /api/auth/me restaura la sesión leyendo la cookie

import React, { createContext, useState, useContext, useEffect } from 'react';
import { authService } from '../services/authService';

// ## Creamos el contexto con null como valor por defecto
// ## (si alguien usa useAuth() fuera de AuthProvider, tira error descriptivo)
const AuthContext = createContext(null);

// ── Provider ──────────────────────────────────────────────────────────────

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user,            setUser]            = useState(null);
  const [loading,         setLoading]         = useState(true);
  const [error,           setError]           = useState(null);

  // ## Al montar el Provider (una sola vez al cargar la app),
  // ## consultamos al backend si hay una cookie JWT válida.
  // ## Esto restaura la sesión después de recargar la página.
  useEffect(() => {
    authService.getCurrentUser()
      .then(userData => {
        if (userData) {
          setUser(userData);
          setIsAuthenticated(true);
        }
      })
      .finally(() => setLoading(false)); // ## loading=false aunque no haya sesión
  }, []);

  // ## Inicia sesión: llama al backend (que setea la cookie), luego obtiene el perfil
  const login = async (credentials) => {
    setLoading(true);
    setError(null);
    try {
      await authService.login(credentials);
      // ## El backend ya seteó la cookie — ahora pedimos los datos del usuario
      const userData = await authService.getCurrentUser();
      if (userData) {
        setUser(userData);
        setIsAuthenticated(true);
        return true;
      }
      return false;
    } catch (err) {
      setError(err.message || 'Error al iniciar sesión');
      return false;
    } finally {
      setLoading(false);
    }
  };

  // ## Registra una cuenta nueva (no inicia sesión automáticamente)
  // ## Después del registro, el usuario debe hacer login manualmente
  const register = async (data) => {
    setLoading(true);
    setError(null);
    try {
      await authService.register(data);
      return true;
    } catch (err) {
      setError(err.message || 'Error al registrarse');
      return false;
    } finally {
      setLoading(false);
    }
  };

  // ## Cierra sesión: le pide al servidor que borre la cookie JWT,
  // ## luego limpia el estado local
  const logout = async () => {
    await authService.logout();
    setIsAuthenticated(false);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, loading, error, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// ── Hook ──────────────────────────────────────────────────────────────────

// ## Hook personalizado para consumir el contexto de auth
// ## Uso: const { user, isAuthenticated, login, logout } = useAuth();
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  return context;
};
