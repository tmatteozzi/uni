import type { Client } from '../types/client';
import FeaturedListItem from './FeaturedListItem';

interface FeaturedListProps {
    features: {
        text: string;
        client: Client;
    }[];
}

export default function FeaturedList({ features }: FeaturedListProps) {
    let renderedItems;
    if (features) {
        renderedItems = features.map((item) => {
            const { text, client } = item;

            return (
                <FeaturedListItem key={client.id} text={text} client={client} />
            );
        });
    }

    return <div>{renderedItems}</div>;
}
