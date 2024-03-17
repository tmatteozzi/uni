import React, { createContext, useState, PropsWithChildren } from 'react';

interface AuthContextType {
    isLoggedIn: boolean;
    username: string;
    login: (username: string) => void;
    logout: () => void;
}

const initialAuthContext: AuthContextType = {
    isLoggedIn: true,
    username: 'Toto',
    login: () => {},
    logout: () => {}
};

const AuthContext = createContext<AuthContextType>(initialAuthContext);

const AuthProvider: React.FC<PropsWithChildren<object>> = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);
    const [username, setUsername] = useState<string>('');

    const login = (username: string) => {
        setIsLoggedIn(true);
        setUsername(username);
    };

    const logout = () => {
        setIsLoggedIn(false);
        setUsername('');
    };

    return (
        <AuthContext.Provider value={{ isLoggedIn, username, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export { AuthProvider, AuthContext };
