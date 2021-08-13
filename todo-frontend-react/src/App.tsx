import React from 'react';
import './App.css';
import TodoList from "./components/TodoList";
import CreateTodo from "./components/CreateTodo";

const App = () => {
    return (
        <div className="App">
            <CreateTodo />
            <TodoList />
        </div>
    );
};

export default App;
