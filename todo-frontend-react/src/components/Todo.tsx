import React from 'react';
import { ITodo } from "../models";

interface IProps extends ITodo {
    key: number;
}

const Todo = (props: IProps) => {
    return (
        <div>
            <h2>{props.text}</h2>
            <p>{props.category}</p>
        </div>
    );
}

export default Todo;