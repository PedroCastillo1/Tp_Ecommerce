import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useEffect } from "react";
import "./App.css";
import { Provider } from "react-redux";
import store from "./store/store";
import { AuthProvider, useAuth } from "./features/auth/context/AuthContext";
import { useDispatch } from "react-redux";
import { fetchCart } from "./store/cartSlice";
import { fetchFavorites } from "./store/favoriteSlice";
import LoginJWTContext from "./components/LoginJWTContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Navbar from "./components/Navbar";
import Home from "./components/Home";
import ProductList from "./components/ProductList";
import ProductDetail from "./components/ProductDetail";
import Cart from "./components/Cart";
import Checkout from "./components/Checkout";
import Favorite from "./components/Favorite";
import FormularioValidado from "./formularios/FormularioValidado";
import FormularioPago from "./formularios/FormularioPago";
import FormularioPagoReactForm from "./formularios/FormularioPagoReactForm";

// Componente interno que carga carrito y favoritos cuando el usuario se loguea
function AppContent() {
  const dispatch = useDispatch();
  const { user, token, loading } = useAuth();

  useEffect(() => {
    // Cuando el usuario esta logueado, cargamos carrito y favoritos desde la DB
    if (user && token && !loading) {
      dispatch(fetchCart({ userId: user.id, token }));
      dispatch(fetchFavorites({ userId: user.id, token }));
    }
  }, [user, token, loading, dispatch]);

  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/products" element={<ProductList />} />
        <Route path="/products/:id" element={<ProductDetail />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/login" element={<LoginJWTContext />} />
        <Route path="/checkout" element={<ProtectedRoute><Checkout /></ProtectedRoute>} />
        <Route path="/favorites" element={<ProtectedRoute><Favorite /></ProtectedRoute>} />
        <Route path="/formValido" element={<FormularioValidado />} />
        <Route path="/formPago" element={<FormularioPago />} />
        <Route path="/formReact" element={<FormularioPagoReactForm />} />
      </Routes>
    </BrowserRouter>
  );
}

function App() {
  return (
    // Provider envuelve todo — AuthProvider adentro para que AppContent acceda a ambos
    <Provider store={store}>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </Provider>
  );
}

export default App;
