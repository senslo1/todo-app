import React, { FormEvent, useState } from "react";
import { ITodo } from "../interfaces";

// interface IProps {
//     createTodo: Function;
// }

const CreateTodo = () => {
    const emptyTodo: ITodo = { category: "", id: 0, text: "" };
    const [ todo, setTodo ] = useState<ITodo>(emptyTodo);

    function changeTodo(event: React.FormEvent<HTMLInputElement>) {
        const { value, name } = event.currentTarget;

        setTodo((previous: ITodo) => {
            return { ...previous, [name]: value };
        });
    }

    function createTodo(event: FormEvent<HTMLButtonElement>) {
        event.preventDefault();
        // props.createTodo(todo); // should dispatch an action
        setTodo(emptyTodo);
    }

    return (
        <form>
            <label>What would you like to get done?</label><br />
            <input type="text"
                   name="text"
                   value={ todo.text }
                   onChange={ changeTodo }
                   id="todoTextInput"
            /><br />
            <label>What is the category?</label><br />
            <input type="text"
                   name="category"
                   value={ todo.category }
                   onChange={ changeTodo }
                   id="todoCategoryInput"
            /><br />
            <button onSubmit={ createTodo }>Create todo</button>
        </form>
    );
};

export default CreateTodo;