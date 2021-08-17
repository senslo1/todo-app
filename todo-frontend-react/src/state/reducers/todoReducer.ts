import { Action } from "../actions";
import { ActionTypes } from "../action-types";
import { Store } from "../index";

const todoReducer = (
    state: Store = {
        todos: [],
        newTodo: {
            text: "",
            category: ""
        }
    },
    action: Action
): Store => {
    switch (action.type) {
        case ActionTypes.ADD:
            return {
                todos: [ ...state.todos, action.payload ],
                newTodo: { text: "", category: "" }
            };
        case ActionTypes.DELETE:
            return {
                ...state,
                todos: state.todos.filter(todo => todo.id !== action.payload)
            };
        case ActionTypes.INITIAL_FETCH_SUCCESS:
            return {
                ...state,
                todos: action.payload
            }
        case ActionTypes.SET_NEWTODO:
            return {
                todos: [ ...state.todos ],
                newTodo: action.payload
            };
        default:
            return state;
    }
}

export default todoReducer;