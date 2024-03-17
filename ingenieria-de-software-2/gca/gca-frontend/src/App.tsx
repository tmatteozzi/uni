import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Root from './pages/Root';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';

const router = createBrowserRouter([
    {
        path: '/',
        element: <Root />,
        children: [
            { path: '/', element: <HomePage /> },
            { path: '/login', element: <LoginPage /> }
        ]
    }
]);

export default function App() {
    return <RouterProvider router={router} />;
}
