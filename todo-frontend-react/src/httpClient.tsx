import { ITodo } from "./models";

const getTodos = async (url: string): Promise<ITodo[]> => {
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

const getAllTodos = (): Promise<ITodo[]> => {
    const url = `http://localhost:5000/api/todo`;
    return getTodos(url);
};

export default getAllTodos;