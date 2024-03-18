import { useLoaderData } from 'react-router-dom';

import { ClientLoaderResults } from './clientLoader';
import PolizaList from '../../components/PolizaList';
import { containerDiv, mainH1, listDiv, listH1 } from '../../styleClassNames';

export default function ClientPage() {
    const { client } = useLoaderData() as ClientLoaderResults;

    const { name, lastName, address, birthDay, phone, country } = client;

    return (
        <div className={containerDiv}>
            <div>
                <h1 className={mainH1}>
                    {name} {lastName}
                </h1>
                <div className={listDiv}>
                    <h1 className={listH1}>Dirección:</h1>
                    <span className="font-medium">
                        {address || 'No disponible'}
                    </span>
                </div>
                <div className={listDiv}>
                    <h1 className={listH1}>Fecha de nacimiento:</h1>
                    <span className="font-medium">
                        {birthDay.toLocaleDateString() || 'No disponible'}
                    </span>
                </div>
                <div className={listDiv}>
                    <h1 className={listH1}>Teléfono:</h1>
                    <span className="font-medium">
                        {phone || 'No disponible'}
                    </span>
                </div>
                <div className={listDiv}>
                    <h1 className={listH1}>País:</h1>
                    <span className="font-medium">
                        {country || 'No disponible'}
                    </span>
                </div>
            </div>
            <div>
                <h1 className={`${mainH1} mt-4`}>Polizas:</h1>
                <PolizaList />
            </div>
        </div>
    );
}
