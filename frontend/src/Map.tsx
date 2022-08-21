import ReactMapGL, {MapRef} from "react-map-gl";
import * as React from "react";
import {useCallback, useEffect, useRef} from "react";
import {useHistory} from "react-router-dom";
import SuperNodesLayer from "./Map/SuperNodesLayer";
import ChildrenNodesLayer from "./Map/ChildrenNodesLayer";
import {useStore} from "./Store";

const Map = () => {
    const {activeNode} = useStore()
    const history = useHistory()

    const [viewport, setViewport] = React.useState({
        longitude: 9.16,
        latitude: 51.3,
        zoom: 2,
        maxZoom: 17,
        minZoom: 2,
    });

    const onNodeClick = useCallback((address: string) => {
        history.replace(`/nodes/${encodeURIComponent(address)}`)
    }, [history])

    const onMapClick = useCallback(() => {
        // unselect active node
        history.replace('/')
    }, [history])

    const mapRef = useRef<MapRef>(null)
    const hoveredNodeAddressRef = useRef<string | null>(null)
    const activeNodeAddressRef = useRef<string | null>(null)

    const setNodeFeatureState = useCallback((address: string, state) => {
        const map = mapRef.current?.getMap()

        if (map && map.isStyleLoaded()) {
            map.setFeatureState({
                source: "SuperNodes-source",
                id: address,
            }, {
                ...state,
            })
            map.setFeatureState({
                source: "ChildrenNodes-source",
                id: address,
            }, {
                ...state,
            })
        } else if (map && !map.isStyleLoaded()) {
            // Defer the call for a while to wait for styles to load
            setTimeout(() => setNodeFeatureState(address, state), 100)
        }
    }, [])

    useEffect(() => {
        // Set node feature state to contain active status
        if (activeNode?.address) {
            if (activeNodeAddressRef.current !== activeNode.address) {
                if (activeNodeAddressRef.current) {
                    setNodeFeatureState(activeNodeAddressRef.current, {active: false})
                }
                activeNodeAddressRef.current = activeNode.address
            }

            setNodeFeatureState(activeNodeAddressRef.current, {active: true})
        } else if (activeNodeAddressRef.current != null) {
            setNodeFeatureState(activeNodeAddressRef.current, {active: false})
            activeNodeAddressRef.current = null
        }
    }, [activeNode, setNodeFeatureState])

    return (
        <div className="h-screen w-screen relative">
            <ReactMapGL
                {...viewport}
                width="100%"
                height="100%"
                mapStyle="mapbox://styles/mapbox/light-v9"
                ref={mapRef}
                interactiveLayerIds={['SuperNodes-Layer', 'ChildrenNodes-Layer']}
                touchRotate={true}
                dragRotate={false}
                onViewportChange={setViewport}
                getCursor={({isHovering, isDragging}: { isHovering: boolean; isDragging: boolean }) => {
                    if (isDragging) {
                        return 'all-scroll'
                    }

                    return isHovering ? 'pointer' : 'default'
                }}
                onClick={(e) => {
                    if (e.features && e.features.length > 0 && e.features[0].properties.address) {
                        const address = e.features[0].properties.address
                        if (address) {
                            onNodeClick(address)
                        }
                    } else {
                        onMapClick()
                    }
                }}
                onHover={(e) => {
                    if (e.features && e.features.length > 0 && e.features[0].properties.address) {
                        if (hoveredNodeAddressRef.current !== e.features[0].properties.address) {
                            if (hoveredNodeAddressRef.current) {
                                setNodeFeatureState(hoveredNodeAddressRef.current, {hover: false})
                            }
                            hoveredNodeAddressRef.current = e.features[0].properties.address
                        }

                        if (hoveredNodeAddressRef.current) {
                            setNodeFeatureState(hoveredNodeAddressRef.current, {hover: true})
                        }
                    } else {
                        if (hoveredNodeAddressRef.current) {
                            setNodeFeatureState(hoveredNodeAddressRef.current, {hover: false})
                        }
                        hoveredNodeAddressRef.current = null
                    }
                }}
                mapboxApiAccessToken="pk.eyJ1IjoiaGJvcm5ob2xkdCIsImEiOiJjanU5b2J6NjIyaTF6M3lzYXVhc3RrNmk4In0.zgOFFOmzLfBnuPgYjh31QA"
            >
                {/*<SuperNodeRegistrationsLayer/>*/}
                <ChildrenNodesLayer/>
                <SuperNodesLayer/>
            </ReactMapGL>
        </div>
    )
}

export default Map
