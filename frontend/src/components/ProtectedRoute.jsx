// ## COMPONENTE — ProtectedRoute (Ruta Protegida)
// ##
// ## Wrapper que protege rutas que requieren autenticación.
// ## Si el usuario NO está logueado → redirige a /login con <Navigate>.
// ## Si SÍ está logueado → renderiza el componente hijo normalmente.
// ## Mientras se verifica la sesión → muestra "Verificando sesión..."
// ##
// ## Uso en App.jsx:
// ##   <Route path="/checkout" element={<ProtectedRoute><Checkout /></ProtectedRoute>} />
// ##
// ## Conceptos que demuestra:
// ##   - useContext (a través del hook personalizado useAuth)
// ##   - Renderizado condicional
// ##   - Navigate de React Router para redirección programática

import { Navigate } from 'react-router-dom';
import { useAuth } from '../features/auth/context/AuthContext';

const ProtectedRoute = ({ children }) => {
  // ## Trae isAuthenticated y loading del contexto global de autenticación
  const { isAuthenticated, loading } = useAuth();

  // ## Mientras el AuthProvider verifica la cookie JWT con /api/auth/me,
  // ## mostramos un spinner para no redirigir prematuramente
  if (loading) {
    return <div className="loading-state">Verificando sesión...</div>;
  }

  // ## Si está autenticado → renderiza los hijos (el componente protegido)
  // ## Si NO → redirige a /login (replace evita que quede en el historial del browser)
  return isAuthenticated
    ? children
    : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
