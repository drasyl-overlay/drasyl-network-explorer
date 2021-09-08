export type Location = {
    latitude: number
    longitude: number
}

export type Node = {
    name: string,
    address: string,
    location: Location,
    superPeer?: string
}
