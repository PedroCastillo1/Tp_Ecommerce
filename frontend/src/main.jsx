// ## Punto de entrada de la aplicación React
// ## Monta el componente raíz <App /> en el div#root definido en index.html
// ## StrictMode activa advertencias extra en desarrollo (doble render, deprecated APIs)

import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
