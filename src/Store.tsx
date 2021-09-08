import React, {useContext, useMemo, useReducer, useCallback} from 'react'
import {Node} from "./api";

type ContextProps = {
    setNodes: (nodes: Node[]) => void
    nodes: Node[]
    setActiveNodeAddress: (activeNodeAddress?: string) => void
    activeNode: Node | undefined
}

const StoreContext = React.createContext<ContextProps | undefined>(undefined)

type Store = {
    nodes: Node[]
    activeNodeAddress: string | undefined
}

type Action =
    | { type: 'setNodes'; nodes: Node[] }
    | { type: 'setActiveNode'; activeNodeAddress: string | undefined }

const reducer = (state: Store, action: Action) => {
    switch (action.type) {
        case 'setNodes': {
            return {
                ...state,
                nodes: action.nodes
            }
        }
        case 'setActiveNode': {
            return {
                ...state,
                activeNodeAddress: action.activeNodeAddress,
            }
        }
    }

    return state
}

function useStoreContext() {
    const [store, dispatch] = useReducer(reducer, {
        nodes: [] as Node[]
    } as Store)

    const setNodes = useCallback(
        (nodes: Node[]) => {
            dispatch({
                type: 'setNodes',
                nodes: nodes
            })
        },
        [dispatch]
    )

    const setActiveNodeAddress = useCallback(
        (activeNodeAddress?: string) => {
            dispatch({
                type: 'setActiveNode',
                activeNodeAddress,
            })
        },
        [dispatch]
    )

    const {
        nodes,
        activeNodeAddress
    } = store

    const activeNode = useMemo(
        () => nodes.find(({address}) => activeNodeAddress === address),
        [nodes, activeNodeAddress]
    )

    return useMemo(
        () => ({
            setNodes,
            nodes,
            activeNode,
            setActiveNodeAddress,
        }),
        [
            setNodes,
            nodes,
            activeNode,
            setActiveNodeAddress,
        ]
    )
}

export const StoreProvider = ({children}: {
    children: React.ReactNode
}) => (
    <StoreContext.Provider value={useStoreContext()}>{children}</StoreContext.Provider>
)

export const useStore = () => {
    const context = useContext(StoreContext)

    if (!context) {
        throw new Error('useStore must be used inside <StoreProvider>')
    }

    return context
}
