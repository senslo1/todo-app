import { createStore, applyMiddleware } from "redux";
import thunk from "redux-thunk";
import todoReducer from "./reducers/todoReducer";
import { compose } from "redux";
import { ITodo } from "../models";

const composeEnhancers = (window as any).__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
export const store = createStore(todoReducer, composeEnhancers(applyMiddleware(thunk)));

export interface Store {
    todos: ITodo[];
    newTodo: ITodo;
}