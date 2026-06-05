// ProtectedRoute.jsx
// Componente de Ruta Protegida
//
// ¿Qué hace?
//   Envuelve cualquier <Route> que requiera autenticación.
//   Si el usuario NO está logueado, lo redirige a /login usando <Navigate>.
//   Si SÍ está logueado, renderiza el componente hijo normalmente.
//
// Uso en App.jsx:
//   <Route path="/checkout" element={<ProtectedRoute><Checkout /></ProtectedRoute>} />
//
// Demuestra: useContext (useAuth), renderizado condicional, operador ternario, Navigate

import { Navigate } from 'react-router-dom';
import { useAuth } from '../features/auth/context/AuthContext';

const ProtectedRoute = ({ children }) => {
  // useContext a través del custom hook useAuth:
  // trae isAuthenticated y loading del AuthProvider global
  const { isAuthenticated, loading } = useAuth();

  // Renderizado condicional: mientras carga el estado de autenticación muestra spinner
  if (loading) {
    return <div className="loading-state">Verificando sesión...</div>;
  }

  // Operador ternario:
  //   - Si está autenticado → renderiza los hijos (el componente protegido)
  //   - Si NO está autenticado → redirige a /login con <Navigate>
  return isAuthenticated
    ? children
    : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
