import {Layer, Source} from "react-map-gl";
import * as React from "react";
import {useMemo} from "react";
import {useStore} from "../Store";

const SuperNodesLayer = () => {
    const { nodes } = useStore()
    const data: GeoJSON.FeatureCollection = useMemo(() => {
        return {
            type: 'FeatureCollection',
            features: nodes.filter(({superPeer}) => !superPeer).map((node) => ({
                type: 'Feature',
                geometry: {
                    type: 'Point',
                    coordinates: [node.location.longitude, node.location.latitude],
                },
                properties: {
                    address: node.address,
                    name: node.name,
                },
            })),
        }
    }, [nodes]);

    return (
        <Source id="SuperNodes-source" type="geojson" data={data} promoteId="address">
            <Layer
                id="SuperNodes-Layer"
                type="circle"
                paint={{
                    'circle-radius': [
                        'case',
                        ['boolean', ['feature-state', 'hover'], false],
                        6,
                        4,

                    ],
                    'circle-color': [
                        'case',
                        ['boolean', ['feature-state', 'active'], false],
                        '#ffffff',
                        '#e12403',
                    ],
                    'circle-stroke-color': '#e12403',
                    'circle-stroke-width': [
                        'case',
                        ['boolean', ['feature-state', 'active'], false],
                        8,
                        0,
                    ],
                }}
            />
        </Source>
    )
}

export default SuperNodesLayer
