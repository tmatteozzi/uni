import { listButton, listDiv, listH1 } from '../styleClassNames';
import type { Client } from '../types/client';

interface ClientListItemProps {
    client: Client;
}

export default function ClientListItem({ client }: ClientListItemProps) {
    return (
        <div className={listDiv}>
            <h1 className={listH1}>{client.name + ' ' + client.lastName}</h1>
            <button className={listButton}>Detalles</button>
        </div>
    );
}
