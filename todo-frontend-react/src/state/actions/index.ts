import { ITodo } from "../../models";
import { ActionTypes } from "../action-types";

interface InitialFetchAction {
    type: ActionTypes.INITIAL_FETCH,
    payload: ITodo[]
}

interface AddAction {
    type: ActionTypes.ADD,
    payload: ITodo
}

interface SetTodosAction {
    type: ActionTypes.INITIAL_FETCH_SUCCESS,
    payload: ITodo[]
}

interface SetNewTodoAction {
    type: ActionTypes.SET_NEWTODO,
    payload: ITodo
}

export type Action = InitialFetchAction | AddAction | SetTodosAction | SetNewTodoAction;