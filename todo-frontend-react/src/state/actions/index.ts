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

interface DeleteAction {
    type: ActionTypes.DELETE,
    payload: number
}

interface SetTodosAction {
    type: ActionTypes.INITIAL_FETCH_SUCCESS,
    payload: ITodo[]
}

interface SetNewTodoAction {
    type: ActionTypes.SET_NEWTODO,
    payload: ITodo
}

export type Action = InitialFetchAction | AddAction | DeleteAction | SetTodosAction | SetNewTodoAction;