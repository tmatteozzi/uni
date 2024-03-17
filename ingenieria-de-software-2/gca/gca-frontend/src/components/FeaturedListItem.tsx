import { listDiv } from '../styleClassNames';
import type { Client } from '../types/client';

interface FeaturedListItemProps {
    text: string;
    client: Client;
}

export default function FeaturedListItem({
    text,
    client
}: FeaturedListItemProps) {
    const handleClick = () => {};

    return (
        <div className={listDiv} onClick={handleClick}>
            {client.name} - {text}
        </div>
    );
}
