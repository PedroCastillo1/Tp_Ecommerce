import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './App.css'

// REDUX — Provider y store
// FavoriteProvider ya no es necesario, favoritos ahora vive en Redux
import { Provider } from 'react-redux'
import store from './store/store'

// Auth sigue en Context porque maneja llamadas async a la API (login/register)
import { AuthProvider } from './features/auth/context/AuthContext'

import LoginJWTContext from './components/LoginJWTContext'
import ProtectedRoute from './components/ProtectedRoute'
import Navbar from './components/Navbar'
import Home from './components/Home'
import ProductList from './components/ProductList'
import ProductDetail from './components/ProductDetail'
import Cart from './components/Cart'
import Checkout from './components/Checkout'
import Favorite from './components/Favorite'
import FormularioValidado from './formularios/FormularioValidado'
import FormularioPago from './formularios/FormularioPago'
import FormularioPagoReactForm from './formularios/FormularioPagoReactForm'

function App() {
  return (
    // Provider unico envuelve toda la app — da acceso al store a todos los componentes
    <Provider store={store}>
      {/* AuthProvider sigue en Context (maneja async JWT) */}
      <AuthProvider>
        <BrowserRouter>
          <Navbar />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/products" element={<ProductList />} />
            <Route path="/products/:id" element={<ProductDetail />} />
            <Route path="/cart" element={<Cart />} />
            <Route path="/login" element={<LoginJWTContext />} />
            <Route path="/checkout"
              element={
                <ProtectedRoute>
                  <Checkout />
                </ProtectedRoute>
              }
            />
            <Route path="/favorites"
              element={
                <ProtectedRoute>
                  <Favorite />
                </ProtectedRoute>
              }
            />
            <Route path="/formValido" element={<FormularioValidado />} />
            <Route path="/formPago" element={<FormularioPago />} />
            <Route path="/formReact" element={<FormularioPagoReactForm />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </Provider>
  )
}

export default App