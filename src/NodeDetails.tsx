import React, {useEffect, useMemo} from 'react'
import {useParams} from 'react-router-dom'
import {useStore} from "./Store";
import Identicon from 'react-identicons';
import {Node} from "./api";
import "@fontsource/inconsolata/600.css";

const Children = ({children}: {
    children: Node[]
}) => {
    return (
        <div>
            <table className="min-w-max w-full table-auto">
                <thead>
                <tr className="bg-gray-200 text-gray-600 uppercase text-sm leading-normal">
                    <th className="py-3 px-3 text-left">Children</th>
                </tr>
                </thead>
                <tbody className="text-gray-600 text-sm font-light">
                {children.length > 0 ? children.map((node) => (
                    <tr className="border-b border-gray-200 hover:bg-gray-100">
                        <td className="py-3 px-3 text-left whitespace-nowrap">
                            <div className="flex items-center">
                                <div className="mr-2">
                                    <Identicon string={node.address} size={13}/>
                                </div>
                                <span className="text-gray-500 text-sm" style={{
                                    fontFamily: 'Inconsolata',
                                }} title={node.name}>{node.address}</span>
                            </div>
                        </td>
                    </tr>
                )) : (
                    <tr className="border-b border-gray-200 hover:bg-gray-100">
                        <td className="py-3 px-3 text-center text-xs">
                            None
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}

const SuperPeer = ({superPeer}: {
    superPeer: Node
}) => {
    return (
        <div>
            <table className="min-w-max w-full table-auto">
                <thead>
                <tr className="bg-gray-200 text-gray-600 uppercase text-sm leading-normal">
                    <th className="py-3 px-3 text-left">Super Peers</th>
                </tr>
                </thead>
                <tbody className="text-gray-600 text-sm font-light">
                <tr className="border-b border-gray-200 hover:bg-gray-100">
                    <td className="py-3 px-3 text-left whitespace-nowrap">
                        <div className="flex items-center">
                            <div className="mr-2">
                                <Identicon string={superPeer.address} size={13}/>
                            </div>
                            <span className="text-gray-500 text-sm" style={{
                                fontFamily: 'Inconsolata',
                            }} title={superPeer.name}>{superPeer.address}</span>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    )
}

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
            <div className="mt-3 p-5 shadow rounded-sm bg-white divide-y" style={{width: '550px'}}>
                <div className="pb-1 flex items-center space-x-3">
                    <div>
                        <Identicon string={node.address} size={30}/>
                    </div>
                    <div>
                        <p className="text-sm whitespace-nowrap overflow-ellipsis">{node.name}</p>
                        <p className="text-gray-500 text-sm" style={{
                            fontFamily: 'Inconsolata',
                        }}>{node.address}</p>
                    </div>
                </div>
                <div className="mt-1.5 pt-2">
                    {!node.superPeer ? (
                        <Children children={nodes.filter(({superPeer}) => node.address === superPeer)}/>
                    ) : (
                        <SuperPeer superPeer={nodes.find(({address}) => address === node.superPeer) as Node}/>
                    )}
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
