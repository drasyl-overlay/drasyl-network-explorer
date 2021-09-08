import {Node} from "../api";
import {Layer, Source} from "react-map-gl";
import * as React from "react";
import {useMemo} from "react";
import {useStore} from "../Store";

const SuperNodeRegistrationsLayer = () => {
    const { nodes } = useStore()
    const data: GeoJSON.FeatureCollection = useMemo(() => {
        return {
            type: 'FeatureCollection',
            features: nodes.filter(({superPeer}) => superPeer).map((node) => {
                const superPeer = (nodes.find(({address}) => address === node.superPeer) as Node)

                return ({
                    type: 'Feature',
                    geometry: {
                        type: 'LineString',
                        coordinates: [
                            [node.location.longitude, node.location.latitude],
                            [superPeer.location.longitude, superPeer.location.latitude]
                        ],
                    },
                    properties: {},
                })
            }),
        }
    }, [nodes]);

    return (
        <Source type="geojson" data={data}>
            <Layer
                type="line"
                layout={{
                    'line-join': 'round',
                    'line-cap': 'round',
                }}
                paint={{
                    'line-width': 1,
                    'line-color': '#888',
                    'line-opacity': 0.5,
                }}
            />
        </Source>
    )
}

export default SuperNodeRegistrationsLayer
