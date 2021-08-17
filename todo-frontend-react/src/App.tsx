import React from 'react';
import './App.css';
import TodoList from "./components/TodoList";
import CreateTodo from "./components/CreateTodo";
import Header from "./components/Header";

const App = () => {
    return (
        <div className="App">
            <Header />
            <CreateTodo />
            <TodoList />
        </div>
    );
};

export default App;
