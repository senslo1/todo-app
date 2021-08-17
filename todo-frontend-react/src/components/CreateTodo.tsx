import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { Store } from "../state";
import { addTodo, setNewTodo } from "../state/action-creators";

const CreateTodo = () => {
    const newTodo = useSelector((state: Store) => state.newTodo);
    const dispatch = useDispatch();

    function changeNewTodo(event: React.FormEvent<HTMLInputElement>) {
        const { value, name } = event.currentTarget;
        dispatch(
            setNewTodo({
                ...newTodo,
                [name]: value
            })
        );
    }

    function submitTodo(event: any) {
        event.preventDefault();
        dispatch(addTodo(newTodo));
    }

    return (
        <form onSubmit={(event) => submitTodo(event)}>
            <label>What would you like to get done?</label><br />
            <input type="text"
                   name="text"
                   value={newTodo.text}
                   onChange={(event) => changeNewTodo(event)}
                   id="todoTextInput"
            /><br />
            <label>What is the category?</label><br />
            <input type="text"
                   name="category"
                   value={newTodo.category}
                   onChange={(event) => changeNewTodo(event)}
                   id="todoCategoryInput"
            /><br />
            <button type="submit">Create todo</button>
        </form>
    );
};

export default CreateTodo;