import React, { useState } from 'react';
import { mainDiv, mainH1 } from '../styleClassNames';

interface LoginProps {
    onSubmit: (user: string, password: string) => Promise<void>;
}

export default function LoginPage({ onSubmit }: LoginProps) {
    const [user, setUser] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    };

    return (
        <div className={`${mainDiv} flex justify-center items-center h-screen`}>
            <div className="bg-white rounded-lg p-8 shadow-lg w-96">
                <h1 className={`${mainH1} text-2xl font-bold mb-4 text-center`}>
                    Iniciar Sesión
                </h1>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label
                            htmlFor="user"
                            className="block text-lg font-semibold"
                        >
                            Usuario:
                        </label>
                        <input
                            type="text"
                            id="user"
                            name="user"
                            value={user}
                            onChange={(e) => setUser(e.target.value)}
                            className="w-full border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:border-blue-500"
                        />
                    </div>
                    <div>
                        <label
                            htmlFor="password"
                            className="block text-lg font-semibold"
                        >
                            Contraseña:
                        </label>
                        <input
                            type="password"
                            id="password"
                            name="contrasena"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:border-blue-500"
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-blue-500 text-white font-semibold py-2 rounded-md hover:bg-blue-600 focus:outline-none focus:bg-blue-600"
                    >
                        Iniciar Sesión
                    </button>
                </form>
            </div>
        </div>
    );
}
