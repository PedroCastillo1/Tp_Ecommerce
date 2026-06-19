// ## COMPONENTE — LoginJWTContext (Formulario de Login y Registro)
// ##
// ## Pantalla de "Mi cuenta" con dos tabs: Iniciar sesión / Registrarse.
// ##
// ## Usa AuthContext (useAuth) para todas las operaciones de auth:
// ##   - login(credentials)  → llama al backend, setea cookie JWT, actualiza contexto
// ##   - register(data)      → crea cuenta nueva (no inicia sesión automáticamente)
// ##   - logout()            → borra cookie en el servidor y limpia el estado
// ##
// ## Si el usuario ya está logueado, muestra la pantalla de bienvenida
// ## con opciones para ir a la tienda o cerrar sesión.
// ##
// ## Ruta: /login (pública)

import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../features/auth/context/AuthContext';
import './Login.css';

// ## Componente auxiliar para reutilizar campos de formulario
const Field = ({ label, type = 'text', name, value, onChange, disabled, required, max }) => (
  <div className="login-card__field">
    <label>{label}</label>
    <input
      type={type}
      name={name}
      value={value}
      onChange={onChange}
      disabled={disabled}
      required={required}
      max={max}
    />
  </div>
);

const LoginJWTContext = () => {
  const navigate = useNavigate();
  const { login, register, loading, error, isAuthenticated, logout, user } = useAuth();

  // ## tab: 'login' o 'register'
  const [tab,        setTab]        = useState('login');
  const [successMsg, setSuccessMsg] = useState('');

  // ## Estado de los formularios
  const [loginData, setLoginData] = useState({ email: '', password: '' });
  const [regData,   setRegData]   = useState({
    nombre: '', apellido: '', email: '', password: '',
    username: '', fechaNacimiento: '', sexo: '',
  });

  const handleLoginChange = (e) => setLoginData({ ...loginData, [e.target.name]: e.target.value });
  const handleRegChange   = (e) => setRegData({ ...regData,   [e.target.name]: e.target.value });

  // ## Login: si ok → navega a la home
  const handleLogin = async (e) => {
    e.preventDefault();
    const ok = await login(loginData);
    if (ok) navigate('/');
  };

  // ## Registro: si ok → muestra mensaje de éxito y cambia al tab de login
  const handleRegister = async (e) => {
    e.preventDefault();
    const ok = await register(regData);
    if (ok) {
      setSuccessMsg('¡Registro exitoso! Ya podés iniciar sesión.');
      setTab('login');
      setRegData({ nombre: '', apellido: '', email: '', password: '', username: '', fechaNacimiento: '', sexo: '' });
    }
  };

  const switchTab = (t) => { setTab(t); setSuccessMsg(''); };

  // ## Si ya está autenticado, muestra pantalla de bienvenida
  if (isAuthenticated) {
    return (
      <div className="login-card">
        <div className="login-card__logged">
          <h2>¡Bienvenido!</h2>
          <p>Sesión activa{user?.email ? ` como ${user.email}` : ''}</p>
          <div className="login-card__logged-actions">
            <Link to="/" className="login-card__btn-store">Ir a la tienda</Link>
            <button className="login-card__btn-logout" onClick={logout}>Cerrar sesión</button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="login-card">
      <h2 className="login-card__title">Mi cuenta</h2>

      {/* ## Tabs para alternar entre login y registro */}
      <div className="login-card__tabs">
        <button
          className={`login-card__tab${tab === 'login' ? ' login-card__tab--active' : ''}`}
          onClick={() => switchTab('login')}
        >
          Iniciar sesión
        </button>
        <button
          className={`login-card__tab${tab === 'register' ? ' login-card__tab--active' : ''}`}
          onClick={() => switchTab('register')}
        >
          Registrarse
        </button>
      </div>

      {/* ## Mensajes de error (del contexto) y éxito (registro exitoso) */}
      {error      && <div className="login-card__error">{error}</div>}
      {successMsg && <div className="login-card__success">{successMsg}</div>}

      {/* ## Formulario de Login */}
      {tab === 'login' && (
        <form className="login-card__form" onSubmit={handleLogin}>
          <Field label="Correo electrónico" type="email"    name="email"    value={loginData.email}    onChange={handleLoginChange} disabled={loading} required />
          <Field label="Contraseña"         type="password" name="password" value={loginData.password} onChange={handleLoginChange} disabled={loading} required />
          <button className="login-card__btn" type="submit" disabled={loading}>
            {loading ? 'Ingresando...' : 'Ingresar'}
          </button>
        </form>
      )}

      {/* ## Formulario de Registro */}
      {tab === 'register' && (
        <form className="login-card__form" onSubmit={handleRegister}>
          <div className="login-card__row">
            <Field label="Nombre"   name="nombre"   value={regData.nombre}   onChange={handleRegChange} disabled={loading} required />
            <Field label="Apellido" name="apellido" value={regData.apellido} onChange={handleRegChange} disabled={loading} required />
          </div>
          <Field label="Usuario"                       name="username"        value={regData.username}        onChange={handleRegChange} disabled={loading} required />
          <Field label="Correo electrónico" type="email"    name="email"      value={regData.email}           onChange={handleRegChange} disabled={loading} required />
          <Field label="Contraseña (mín. 3 caracteres)" type="password" name="password" value={regData.password} onChange={handleRegChange} disabled={loading} required />
          <Field label="Fecha de nacimiento" type="date"  name="fechaNacimiento" value={regData.fechaNacimiento} onChange={handleRegChange} disabled={loading} required max={new Date().toISOString().split('T')[0]} />
          <div className="login-card__field">
            <label>Sexo</label>
            <select name="sexo" value={regData.sexo} onChange={handleRegChange} required disabled={loading}>
              <option value="">Seleccionar...</option>
              <option value="MASCULINO">Masculino</option>
              <option value="FEMENINO">Femenino</option>
              <option value="OTRO">Otro</option>
            </select>
          </div>
          <button className="login-card__btn" type="submit" disabled={loading}>
            {loading ? 'Registrando...' : 'Crear cuenta'}
          </button>
        </form>
      )}
    </div>
  );
};

export default LoginJWTContext;
