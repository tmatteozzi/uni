import { Client } from '../types/client';
import ClientListItem from './ClientListItem';

interface ClientListProps {
    clients: { client: Client }[];
}

export default function ClientList({ clients }: ClientListProps) {
    let renderedItems;
    if (clients) {
        renderedItems = clients.map((item) => {
            return <ClientListItem key={item.client.id} client={item.client} />;
        });
    }
    return <div>{renderedItems}</div>;
}
