import { ITodo } from "../../models";
import { ActionTypes } from "../action-types";
import { Action } from "redux";
import { ThunkAction } from "redux-thunk";
import { Store } from "../index";
import getAllTodos from "../../httpClient";

export const addTodo = (todo: ITodo) => ({
    type: ActionTypes.ADD,
    payload: todo
});

export const initialFetchSuccess = (todos: ITodo[]) => ({
    type: ActionTypes.INITIAL_FETCH_SUCCESS,
    payload: todos
});

export const setNewTodo = (todo: ITodo) => ({
    type: ActionTypes.SET_NEWTODO,
    payload: todo
});

export const initialFetch = (): ThunkAction<void, Store, unknown, Action<string>> => async (dispatch) => {
    const allTodos: ITodo[] = await getAllTodos();
    dispatch(initialFetchSuccess(allTodos));
};