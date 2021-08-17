import React from "react";
import TodoList from "./components/TodoList";
import CreateTodo from "./components/CreateTodo";
import Header from "./components/Header";

const App = () => {
    return (
        <div>
            <Header />
            <CreateTodo />
            <TodoList />
        </div>
    );
};

export default App;
