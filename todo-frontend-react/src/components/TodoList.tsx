import React, { useEffect } from 'react';
import Todo from "./Todo";
import { useDispatch, useSelector } from "react-redux";
import { Store } from "../state";
import { initialFetch } from "../state/action-creators";

const TodoList = () => {
    const todoList = useSelector((state: Store) => state.todos);
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(initialFetch());
    }, [dispatch]);

    return (
        <div>
            {todoList?.map((todo, index) => <Todo {...todo} key={index} />)}
        </div>
    );
}

export default TodoList;