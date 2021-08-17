import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { Store } from "../state";
import { submitTodo as submitTodoToApi, setNewTodo } from "../state/action-creators";
import AddIcon from '@material-ui/icons/Add';
import Fab from '@material-ui/core/Fab';
import Zoom from '@material-ui/core/Zoom';

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
        dispatch(submitTodoToApi(newTodo));
    }

    return (
        <form className="create-todo" onSubmit={(event) => submitTodo(event)}>
            <input type="text"
                   name="text"
                   placeholder="What would you like to get done?"
                   value={newTodo.text}
                   onChange={(event) => changeNewTodo(event)}
                   id="todoTextInput"
            />
            <Zoom in={newTodo.text.length > 0}>
                <input type="text"
                       name="category"
                       placeholder="What is the category?"
                       value={newTodo.category}
                       onChange={(event) => changeNewTodo(event)}
                       id="todoCategoryInput"
                />
            </Zoom>
            <Zoom in={newTodo.text.length > 0}>
                <Fab type="submit">
                    <AddIcon />
                </Fab>
            </Zoom>
        </form>
    );
};

export default CreateTodo;