import { ITodo } from "../models";

const url = `http://localhost:5000/api/todo`;

const getAllTodos = async (): Promise<ITodo[]> => {
    let response = await fetch(url, {
        method: 'GET'
    });
    if (response.status === 204) {
        return [];
    } else if (response.ok) {
        return response.json();
    } else {
        throw response.status;
    }
};

const postTodo = async (data: ITodo): Promise<ITodo> => {
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    if (!response.ok) {
        throw response.status;
    }
    return response.json();
};

const deleteTodo = async (id: number) => {
    const response = await fetch(`${url}/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        throw response.status;
    }
};

export { getAllTodos, postTodo, deleteTodo };