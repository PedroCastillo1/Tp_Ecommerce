// ## COMPONENTE — Profile (Perfil del Usuario)
// ##
// ## Muestra el perfil del usuario autenticado con:
// ##   - Avatar (inicial del email) + nombre + email + botón de logout
// ##   - Lista de productos favoritos con imagen y precio
// ##   - Historial de compras (órdenes) con items, cantidades y precios
// ##
// ## Carga las órdenes al montar via fetchOrders (Redux async thunk).
// ## Los favoritos ya están en el store desde cuando el usuario se logueó.
// ##
// ## Ruta: /perfil (protegida — requiere login via ProtectedRoute)

import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../features/auth/context/AuthContext';
import { fetchOrders, selectOrders, selectOrdersLoading } from '../store/orderSlice';
import { selectFavoriteItems } from '../store/favoriteSlice';
import './Profile.css';

const Profile = () => {
  const dispatch  = useDispatch();
  const navigate  = useNavigate();
  const { user, logout, isAuthenticated } = useAuth();

  // ## Datos del store Redux
  const orders    = useSelector(selectOrders);
  const loading   = useSelector(selectOrdersLoading);
  const favorites = useSelector(selectFavoriteItems);

  // ## Al montar: si no está autenticado → redirige. Si sí → carga el historial.
  useEffect(() => {
    if (!isAuthenticated) { navigate('/login'); return; }
    if (user?.id) dispatch(fetchOrders({ userId: user.id }));
  }, [user, isAuthenticated, dispatch, navigate]);

  // ## Cierra sesión y vuelve a la home
  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  // ## Formatea fecha ISO a formato argentino "DD/MM/YYYY HH:MM"
  const formatFecha = (fecha) => {
    if (!fecha) return '';
    return new Date(fecha).toLocaleDateString('es-AR', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit',
    });
  };

  return (
    <div className="profile">
      {/* ## Header: avatar con inicial del email, nombre y botón de logout */}
      <div className="profile__header">
        <div className="profile__avatar">{user?.email?.[0]?.toUpperCase() ?? '?'}</div>
        <div>
          <h2 className="profile__name">{user?.username || user?.email}</h2>
          <p  className="profile__email">{user?.email}</p>
        </div>
        <button className="profile__logout" onClick={handleLogout}>Cerrar sesión</button>
      </div>

      {/* ## Sección de favoritos: links a cada producto */}
      <section className="profile__section">
        <h3 className="profile__section-title">❤️ Favoritos ({favorites.length})</h3>
        {favorites.length === 0 ? (
          <p className="profile__empty">No tenés productos favoritos aún.</p>
        ) : (
          <div className="profile__fav-list">
            {favorites.map(p => (
              <Link key={p.id} to={`/products/${p.id}`} className="profile__fav-item">
                <img
                  src={p.imageUrl || 'https://placehold.co/60x60?text=?'}
                  alt={p.name}
                  className="profile__fav-img"
                  onError={e => { e.target.src = 'https://placehold.co/60x60?text=?'; }}
                />
                <div>
                  <p className="profile__fav-name">{p.name}</p>
                  <p className="profile__fav-price">${Number(p.price).toLocaleString('es-AR')}</p>
                </div>
              </Link>
            ))}
          </div>
        )}
      </section>

      {/* ## Sección de historial de compras: una card por orden */}
      <section className="profile__section">
        <h3 className="profile__section-title">🛍️ Historial de compras</h3>
        {loading && <p className="profile__empty">Cargando...</p>}
        {!loading && orders.length === 0 && (
          <p className="profile__empty">Todavía no realizaste ninguna compra.</p>
        )}
        {orders.map(order => (
          <div key={order.id} className="profile__order">
            {/* ## Cabecera de la orden: ID, fecha y total */}
            <div className="profile__order-header">
              <span className="profile__order-id">Orden #{order.id}</span>
              <span className="profile__order-date">{formatFecha(order.fecha)}</span>
              <span className="profile__order-total">
                Total: ${Number(order.total).toLocaleString('es-AR')}
              </span>
            </div>
            {/* ## Items de la orden: imagen, nombre, cantidad y precio unitario */}
            <ul className="profile__order-items">
              {order.items?.map(item => (
                <li key={item.id} className="profile__order-item">
                  <img
                    src={item.product?.imageUrl || 'https://placehold.co/50x50?text=?'}
                    alt={item.product?.name}
                    className="profile__order-img"
                    onError={e => { e.target.src = 'https://placehold.co/50x50?text=?'; }}
                  />
                  <span className="profile__order-item-name">{item.product?.name}</span>
                  <span className="profile__order-item-qty">x{item.quantity}</span>
                  <span className="profile__order-item-price">
                    ${Number(item.precioUnitario).toLocaleString('es-AR')}
                  </span>
                </li>
              ))}
            </ul>
          </div>
        ))}
      </section>
    </div>
  );
};

export default Profile;
