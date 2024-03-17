import type { Params } from 'react-router-dom';
import type { Client } from '../../types/client';

interface LoaderArgs {
    params: Params;
}

// test
const client1: Client = {
    id: 1,
    name: 'John',
    lastName: 'Doe',
    address: '123 Main St',
    birthDay: new Date('1990-01-01'),
    phone: '+1234567890',
    country: 'USA',
    userId: 123
};

export interface ClientLoaderResults {
    client: Client;
    // name: string;
}

export async function clientLoader({
    params
}: LoaderArgs): Promise<ClientLoaderResults> {
    const { id } = params;

    if (!id) {
        throw new Error('Name must be provided');
    }

    return { client: client1 };
}

// EN ESTE LOADER VOY A TENER QUE RECIBIR EL ID DEL CLIENTE POR PARAMETRO
// Y CON ESO LLAMAR GETUSER(ID) PARA QUE ME DEVUELVA EL OBJETO DEL CLIENTE
