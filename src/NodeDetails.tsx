import {useEffect, useMemo} from 'react'

import {useParams} from 'react-router-dom'
import {useStore} from "./Store";

interface Params {
    address: string
}

const NodeDetails = () => {
    const {nodes, setActiveNodeAddress} = useStore()
    const {address: encodedAddress} = useParams<Params>()
    const activeAddress = useMemo(() => decodeURIComponent(encodedAddress), [encodedAddress])
    const node = nodes.find(({address}) => address === activeAddress)

    useEffect(() => {
        if (node) {
            setActiveNodeAddress(activeAddress)
        } else {
            setActiveNodeAddress(undefined)
        }
    }, [node, setActiveNodeAddress, activeAddress]);

    if (node) {
        return (
            <div style={{
                position: 'absolute',
                width: '450px',
                zIndex: 2,
                background: 'rgb(252, 252, 252)',
                boxShadow: 'rgb(0 0 0 / 8%) 0px 0px 6px',
                borderRadius: '4px',
                top: '8px',
                left: '8px',
            }}>
                <div style={{
                    padding: '16px',
                }}>
                    <h1 style={{
                        fontSize: '12px',
                        color: 'rgb(50, 50, 50)',
                    }}>{node.name}</h1>
                    <h2 style={{
                        fontSize: '10px',
                        color: 'rgb(173, 173, 173)',
                    }}>{node.address}</h2>
                </div>
            </div>
        )
    } else {
        return (
            <></>
        )
    }
}

export default NodeDetails
