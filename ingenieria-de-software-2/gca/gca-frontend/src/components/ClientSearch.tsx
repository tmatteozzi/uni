import { useState } from 'react';

import ClientList from './ClientList';
import { Client } from '../types/client';
import { clients } from '../testData';
import { GoSearch } from 'react-icons/go';
import { Link } from 'react-router-dom';
import { mainH2 } from '../styleClassNames';

export default function ClientSearch() {
    const [term, setTerm] = useState('');
    const [results, setResults] = useState<{ client: Client }[] | null>(
        clients
    );

    return (
        <div className="mx-auto flex flex-col">
            <div>
                <form className="flex items-center border border-gray-300 rounded-lg overflow-hidden">
                    <input
                        className="px-4 w-full outline-none"
                        id="term"
                        type="text"
                        placeholder="Buscar clientes"
                        value={term}
                        onChange={(e) => setTerm(e.target.value)}
                    />
                    <button className=" text-black px-4 py-2 hover:text-gray-500 focus:outline-none">
                        <GoSearch />
                    </button>
                </form>
            </div>
            <div className="mt-4">
                <h2 className={mainH2}>Resultados:</h2>
                {results && <ClientList clients={results} />}
            </div>
        </div>
    );
}
