import React, { useEffect, useState } from 'react';
import getAllTodos from "../httpClient";
import { ITodo } from "../interfaces";
import Todo from "./Todo";

const TodoList = () => {
    const [ todos, setTodos ] = useState<ITodo[]>([]);

    useEffect(() => {
        async function getTodos() {
            let response: ITodo[] = await getAllTodos();
            setTodos(response);
        }

        getTodos();
    }, []);

    return (
        <div>
            { todos?.map((todo, index) => <Todo { ...todo } key={ index }/>) }
        </div>
    );
}

export default TodoList;