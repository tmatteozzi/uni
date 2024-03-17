import FeaturedList from '../components/FeaturedList';
import ClientSearch from '../components/ClientSearch';
import { mainDiv } from '../styleClassNames';
import { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

export default function HomePage() {
    const { username } = useContext(AuthContext);

    return (
        <div className={mainDiv}>
            <div className="bg-white rounded-lg p-4">
                <h1 className="text-2xl font-bold">Featured</h1>
                <FeaturedList />
            </div>
            <div className="bg-white rounded-lg p-4 mt-4">
                <h1 className="text-2xl font-bold mb-4">
                    Clientes de {username}
                </h1>
                <ClientSearch />
            </div>
        </div>
    );
}
