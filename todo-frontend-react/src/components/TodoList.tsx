/* eslint-disable react-hooks/exhaustive-deps */
import React, { useEffect } from "react";
import Todo from "./Todo";
import { useDispatch, useSelector } from "react-redux";
import { Store } from "../state";
import { initialFetch } from "../state/action-creators";

const TodoList = () => {
    const todoList = useSelector((state: Store) => state.todos);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(initialFetch());
    }, []);

    return (
        <div>
            {todoList?.map(todo => <Todo {...todo} key={todo.id!} />)}
        </div>
    );
}

export default TodoList;