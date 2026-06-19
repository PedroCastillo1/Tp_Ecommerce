// ## REDUX STORE — configuración central del estado global
// ## Combina los tres reducers de la app:
// ##   cart     → items del carrito del usuario
// ##   favorite → productos marcados como favoritos
// ##   orders   → historial de compras realizadas
// ##
// ## Todos los componentes acceden al estado via useSelector()
// ## y disparan acciones via useDispatch()

import { configureStore } from "@reduxjs/toolkit";
import cartReducer     from "./cartSlice";
import favoriteReducer from "./favoriteSlice";
import orderReducer    from "./orderSlice";

const store = configureStore({
  reducer: {
    cart:     cartReducer,
    favorite: favoriteReducer,
    orders:   orderReducer,
  },
});

export default store;
