import type { Client } from './types/client';

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

const client2: Client = {
    id: 2,
    name: 'Alice',
    lastName: 'Smith',
    address: '456 Elm St',
    birthDay: new Date('1985-05-15'),
    phone: '+1987654321',
    country: 'Canada',
    userId: 456
};

const client3: Client = {
    id: 3,
    name: 'Emily',
    lastName: 'Johnson',
    address: '789 Oak St',
    birthDay: new Date('1978-09-20'),
    phone: '+1122334455',
    country: 'UK',
    userId: 789
};

const client4: Client = {
    id: 4,
    name: 'Michael',
    lastName: 'Brown',
    address: '101 Pine St',
    birthDay: new Date('1982-03-10'),
    phone: '+4455667788',
    country: 'Australia',
    userId: 101
};

const client5: Client = {
    id: 5,
    name: 'Sophia',
    lastName: 'Martinez',
    address: '202 Maple St',
    birthDay: new Date('1995-07-25'),
    phone: '+6677889900',
    country: 'Mexico',
    userId: 202
};

export const clients: { client: Client }[] = [
    { client: client1 },
    { client: client2 },
    { client: client3 },
    { client: client4 },
    { client: client5 }
];
