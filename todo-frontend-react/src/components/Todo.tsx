import React from "react";
import { ITodo } from "../models";
import { useDispatch, useSelector } from "react-redux";
import { Store } from "../state";
import { deleteTodoRest as deleteTodo} from "../state/action-creators";
import DeleteIcon from "@material-ui/icons/Delete";

const Todo = (props: ITodo = {text: "", category: ""}) => {
    const todo: ITodo = useSelector((state: Store) => state.todos.find(element => element.id === props.id)!);
    const dispatch = useDispatch();

    return (
        <div className="todo">
            <h1>{todo.text}</h1>
            <p>{todo.category}</p>
            <button onClick={() => dispatch(deleteTodo(todo.id!))}>
                <DeleteIcon />
            </button>
        </div>
    );
}

export default Todo;