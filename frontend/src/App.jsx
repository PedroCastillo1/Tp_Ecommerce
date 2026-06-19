// ## COMPONENTE RAÍZ — App
// ##
// ## Define la estructura global de la aplicación:
// ##   1. Redux Provider  → expone el store global a todos los componentes
// ##   2. AuthProvider    → expone el estado de autenticación a toda la app
// ##   3. BrowserRouter   → habilita el enrutamiento con React Router
// ##   4. AppContent      → componente interno que carga datos y define las rutas
// ##
// ## Jerarquía de providers:
// ##   <Provider store>          ← Redux
// ##     <AuthProvider>          ← Contexto de autenticación
// ##       <AppContent>          ← Router + carga de carrito/favoritos
// ##
// ## Rutas protegidas: /checkout, /favorites, /perfil
// ##   → redirigen a /login si el usuario no está autenticado (via ProtectedRoute)

import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useEffect } from "react";
import "./App.css";
import { Provider } from "react-redux";
import store from "./store/store";
import { AuthProvider, useAuth } from "./features/auth/context/AuthContext";
import { useDispatch } from "react-redux";
import { fetchCart }      from "./store/cartSlice";
import { fetchFavorites } from "./store/favoriteSlice";

// ## Importación de componentes y páginas
import LoginJWTContext from "./components/LoginJWTContext";
import ProtectedRoute  from "./components/ProtectedRoute";
import Navbar          from "./components/Navbar";
import Home            from "./components/Home";
import ProductList     from "./components/ProductList";
import ProductDetail   from "./components/ProductDetail";
import Cart            from "./components/Cart";
import Checkout        from "./components/Checkout";
import Favorite        from "./components/Favorite";
import Profile         from "./components/Profile";

// ## Formularios de ejemplo (para la materia)
import FormularioValidado        from "./formularios/FormularioValidado";
import FormularioPago            from "./formularios/FormularioPago";
import FormularioPagoReactForm   from "./formularios/FormularioPagoReactForm";

// ── AppContent ─────────────────────────────────────────────────────────────
// ## Componente interno separado de App para poder usar useAuth() y useDispatch()
// ## juntos (ambos necesitan sus respectivos Providers como ancestros)
function AppContent() {
  const dispatch       = useDispatch();
  const { user, loading } = useAuth();

  // ## Cuando el usuario se autentica, cargamos su carrito y favoritos desde la DB.
  // ## Esto corre al montar y cada vez que cambia el usuario (login/logout).
  useEffect(() => {
    if (user && !loading) {
      dispatch(fetchCart({ userId: user.id }));
      dispatch(fetchFavorites({ userId: user.id }));
    }
  }, [user, loading, dispatch]);

  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        {/* ## Rutas públicas — accesibles sin login */}
        <Route path="/"            element={<Home />} />
        <Route path="/products"    element={<ProductList />} />
        <Route path="/products/:id" element={<ProductDetail />} />
        <Route path="/cart"        element={<Cart />} />
        <Route path="/login"       element={<LoginJWTContext />} />

        {/* ## Rutas protegidas — redirigen a /login si no está autenticado */}
        <Route path="/checkout"  element={<ProtectedRoute><Checkout /></ProtectedRoute>} />
        <Route path="/favorites" element={<ProtectedRoute><Favorite /></ProtectedRoute>} />
        <Route path="/perfil"    element={<ProtectedRoute><Profile /></ProtectedRoute>} />

        {/* ## Formularios de ejemplo de la materia */}
        <Route path="/formValido" element={<FormularioValidado />} />
        <Route path="/formPago"   element={<FormularioPago />} />
        <Route path="/formReact"  element={<FormularioPagoReactForm />} />
      </Routes>
    </BrowserRouter>
  );
}

// ── App (raíz) ─────────────────────────────────────────────────────────────
function App() {
  return (
    // ## Redux Provider envuelve todo para que cualquier componente pueda usar useSelector/useDispatch
    <Provider store={store}>
      {/* ## AuthProvider adentro del Provider para que AppContent acceda a ambos */}
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </Provider>
  );
}

export default App;
