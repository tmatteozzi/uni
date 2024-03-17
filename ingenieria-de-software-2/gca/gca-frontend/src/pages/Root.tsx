import { useContext } from 'react';
import { Outlet } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import LoginPage from './LoginPage';

export default function Root() {
    const { isLoggedIn } = useContext(AuthContext);

    return (
        <div className="min-h-screen bg-gray-100">
            <main className="container mx-auto p-4">
                {isLoggedIn ? <Outlet /> : <LoginPage />}
            </main>
        </div>
    );
}
