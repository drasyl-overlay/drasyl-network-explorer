import {useStore} from "./Store";
import {useEffect} from 'react';

const ControllerProvider = ({children}: {
    children: React.ReactNode
}) => {
    const {setNodes} = useStore()

    useEffect(() => {
        fetch('https://ping.drasyl.network/network-1.json')
            .then(resp => resp.json())
            .then(json => setNodes(json))
    }, [setNodes])

    return (
        <>{children}</>
    )
}

export default ControllerProvider;
