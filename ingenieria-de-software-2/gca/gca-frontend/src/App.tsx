import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Root from './pages/Root';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import ClientPage from './pages/ClientPage/ClientPage';
import { clientLoader } from './pages/ClientPage/clientLoader';
import ClientAddPage from './pages/ClientAddPage';

const router = createBrowserRouter([
    {
        path: '/',
        element: <Root />,
        children: [
            { path: '/', element: <HomePage /> },
            { path: '/login', element: <LoginPage /> },
            {
                path: '/client/:id',
                element: <ClientPage />,
                loader: clientLoader
            },
            { path: '/client-add', element: <ClientAddPage /> }
        ]
    }
]);

export default function App() {
    return <RouterProvider router={router} />;
}
