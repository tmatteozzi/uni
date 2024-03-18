import { Policy } from '../types/policy';
import PolicyListItem from './PolicyListItem';

interface PolicyListProps {
    policies: { policy: Policy }[];
}

export default function PolicyList({ policies }: PolicyListProps) {
    let renderedItems;
    if (policies) {
        renderedItems = policies.map((item) => {
            return <PolicyListItem key={item.policy.id} policy={item.policy} />;
        });
    }
    return <div>{renderedItems}</div>;
}
