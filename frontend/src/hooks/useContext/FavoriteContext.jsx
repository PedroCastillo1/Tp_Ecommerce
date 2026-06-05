// FavoriteContext.jsx
// Demuestra: createContext, useContext, useState, custom hook

import React, { useState, useContext, createContext } from 'react';

// --- 1. CREACIÓN DEL CONTEXTO ---
const FavoriteContext = createContext();

// --- 2. CUSTOM HOOK para consumir el contexto ---
export function useFavorite() {
  const context = useContext(FavoriteContext);
  if (context === undefined) {
    throw new Error('useFavorite debe ser usado dentro de un FavoriteProvider');
  }
  return context;
}

// --- 3. PROVIDER ---
export function FavoriteProvider({ children }) {
  // favoriteItems: array de productos favoritos
  const [favoriteItems, setFavoriteItems] = useState([]);

  // Agrega un producto a favoritos. Si ya existe, lo ignora (no duplica).
  const addToFavorite = (product) => {
    setFavoriteItems(prev => {
      // Renderizado condicional: solo agrega si no existe ya
      const yaExiste = prev.find(item => item.id === product.id);
      return yaExiste ? prev : [...prev, product];
    });
  };

  // Elimina un producto de favoritos
  const removeFromFavorite = (productId) => {
    setFavoriteItems(prev => prev.filter(item => item.id !== productId));
  };

  // Verifica si un producto ya es favorito (útil para mostrar corazón lleno/vacío)
  const isFavorite = (productId) => {
    return favoriteItems.some(item => item.id === productId);
  };

  // Toggle: si ya es favorito lo quita, si no lo agrega
  const toggleFavorite = (product) => {
    isFavorite(product.id)
      ? removeFromFavorite(product.id)
      : addToFavorite(product);
  };

  const value = {
    favoriteItems,
    addToFavorite,
    removeFromFavorite,
    isFavorite,
    toggleFavorite,
  };

  return (
    <FavoriteContext.Provider value={value}>
      {children}
    </FavoriteContext.Provider>
  );
}
